/*
 * Copyright (c) 2016 Couchbase, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gittalent.service;

import com.gittalent.model.*;
import com.gittalent.repositories.*;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.OkUrlFactory;
import org.apache.log4j.Logger;
import org.kohsuke.github.*;
import org.kohsuke.github.extras.OkHttpConnector;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * Created by ldoguin on 17/08/16.
 */
@Service
public class GithubImportService {

    private static final Logger log = Logger.getLogger(GithubImportService.class);

    private final TicketRepository ticketRepository;

    private final DeveloperRepository developerRepository;

    private final OrganizationRepository organizationRepository;

    private final GitHub github;

    private final CountryList countryList;

    public GithubImportService(final TicketRepository ticketRepository, final DeveloperRepository developerRepository,
                               final OrganizationRepository organizationRepository) {
        this.ticketRepository = ticketRepository;
        this.developerRepository = developerRepository;
        this.organizationRepository = organizationRepository;
        this.github = initGithub();
        this.countryList = new CountryList();
    }

    public GitHub initGithub() {
        String tmpDirPath = System.getProperty("java.io.tmpdir");
        File cacheDirectoryParent = new File(tmpDirPath);
        File cacheDirectory = new File(cacheDirectoryParent, "okhttpCache");
        if (!cacheDirectory.exists()) {
            cacheDirectory.mkdir();
        }
        Cache cache = new Cache(cacheDirectory, 100 * 1024 * 1024);
        try {
            return GitHubBuilder.fromCredentials()
                    .withRateLimitHandler(RateLimitHandler.WAIT)
                    .withAbuseLimitHandler(AbuseLimitHandler.WAIT)
                    .withConnector(new OkHttpConnector(new OkUrlFactory(new OkHttpClient().setCache(cache))))
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void importDATeam() {
        try {
            importOneDeveloper("nraboy");
            importOneDeveloper("ldoguin");
            importOneDeveloper("arun-gupta");
            importOneDeveloper("mgroves");
            importOneDeveloper("HodGreeley");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void importDefaultGithubData() {
        try {
            importOneDeveloper("nraboy");
            importOneDeveloper("ldoguin");
            importOneDeveloper("arun-gupta");
            importOneDeveloper("mgroves");
            importOneDeveloper("HodGreeley");
            importOrganization("couchbase");
            importOrganization("couchbaselabs");
            importOrganization("github");
            importDeveloperByFollowers(">10000");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void importJavaOrgs() {
        github.searchUsers().type("org").language("java").list()
                .forEach(org -> {
                    importOrganization(org.getLogin());
                });
    }

    public void importOneDeveloper(String developerId) {
        try {
            GHUser user = github.getUser(developerId);
            List tickets = new ArrayList();
            github.searchIssues().mentions(user).list()
                    .forEach(ticket -> {
                        tickets.add(createTicket(ticket));
                        log.debug(String.format("wrote ticket: %s", ticket));
                    });
            createDeveloper(user, tickets);
        } catch (Exception e) {
            log.error(e);
            throw new RuntimeException(e);
        }
    }

    public void importOrganization(String orgName) {
        try {
            GHOrganization organization = github.getOrganization(orgName);
            PagedIterable<GHUser> members = organization.listPublicMembers();
            List<String> storedMembers = importGHUserList(members);
            createOrganization(organization, storedMembers);
        } catch (Exception e) {
            log.error(e);
            throw new RuntimeException(e);
        }
    }

    public void importDeveloperByFollowers(String followersQuery) {
        PagedSearchIterable<GHUser> followers = github.searchUsers().followers(followersQuery).list();
        importGHUserList(followers);
    }

    private List<String> importGHUserList(Iterable<GHUser> ghUsers) {
        List users = new ArrayList();
        ghUsers.forEach(
                ghUser -> {
                    List tickets = new ArrayList();
                    github.searchIssues().mentions(ghUser).list()
                            .forEach(ticket -> {
                                tickets.add(createTicket(ticket));
                                log.debug(String.format("wrote ticket: %s", ticket));
                            });
                    users.add(createDeveloper(ghUser, tickets));
                    log.debug(String.format("wrote user: %s", ghUser));
                });
        return users;
    }

    private String createDeveloper(GHUser ghUser, List<String> tickets) {
        String developerId = String.valueOf(ghUser.getId());
        Developer developer = new Developer();
        DeveloperInfo developerInfo = new DeveloperInfo();
        try {
            Date createdAt = ghUser.getCreatedAt();
            String company = ghUser.getCompany();
            if (company == null) company = "";
            String email = ghUser.getEmail();
            if (email == null) email = ghUser.getLogin() + "@random.com";
            String name = ghUser.getName();
            developer.setTicketCount(tickets.size());
            developer.setCompany(company);
            developer.setCreatedAt(createdAt.getTime());
            List<GithubRepository> repositories = new ArrayList<GithubRepository>();
            ghUser.getRepositories().forEach(new BiConsumer<String, GHRepository>() {
                @Override
                public void accept(String repoName, GHRepository ghRepository) {
                    try {
                        GithubRepository githubRepository = createGithubRepository(repoName, ghRepository);
                        if (githubRepository != null) {
                            repositories.add(githubRepository);
                        }
                    } catch (Exception e) {
                        log.error(e.getMessage());
                        throw new RuntimeException(e);
                    }
                }
            });
            developer.setRepositories(repositories);
            developerInfo.setEmail(email);
            if (name != null) {
                int idx = name.indexOf(' ');
                if (idx != -1) {
                    developerInfo.setLastName(name.substring(idx));
                    developerInfo.setFirstName(name.substring(0, idx));
                } else {
                    developerInfo.setFirstName(name);
                }
            }
            developerInfo.setAvatarURL(ghUser.getAvatarUrl());
            developerInfo.setFollowers(ghUser.getFollowersCount());
            developerInfo.setFollows(ghUser.getFollowingCount());
            developerInfo.setRepositoryCount(ghUser.getPublicRepoCount());
            developerInfo.setGistCount(ghUser.getPublicGistCount());
            developerInfo.setUsername(ghUser.getLogin());
            developer.setDeveloperInfo(developerInfo);
            Address address = new Address();
            String location = ghUser.getLocation();
            if (location == null) location = "";
            address.setStreet(location);
            address.setCountry(countryList.getRandomCountry());
            developer.setAddress(address);
            developer.setId(developerId);
            developer.setHistory(tickets);
            List<String> organizationIds = new ArrayList<>();
            List<String> organizations = new ArrayList<>();
            ghUser.getOrganizations().forEach(orga -> {
                organizationIds.add(String.valueOf(orga.getId()));
                organizations.add(orga.getLogin());
            });
            developer.setOrganizationIds(organizationIds);
            developer.setOrganizations(organizations);
            developerRepository.save(developer);
            return developer.getId();
        } catch (IOException e) {
            log.error(e);
            throw new RuntimeException(e);
        }
    }

    private GithubRepository createGithubRepository(String repoName, GHRepository ghRepository) {
        try {
            int size = ghRepository.getSize();
            int subscriberCount = ghRepository.getSubscribersCount();
            String fullName = ghRepository.getFullName();
            String description = ghRepository.getDescription();
            String mainLanguage = ghRepository.getLanguage();
            Map<String, Long> languages = ghRepository.listLanguages();
            Long createdAt = ghRepository.getCreatedAt().getTime();
            Long updatedAt = ghRepository.getUpdatedAt().getTime();
            return new GithubRepository(repoName, size, subscriberCount, fullName, description, mainLanguage, languages,
                    createdAt, updatedAt);
        } catch (IOException e) {
            log.error(e);
            // retur null to continue import
            return null;
        }
    }

    public void createOrganization(GHOrganization ghOrganization, List<String> publicMembers) throws IOException {
        String organizationId = String.valueOf(ghOrganization.getId());
        Organization organization = new Organization();
        DeveloperInfo developerInfo = new DeveloperInfo();
        try {
            organization.setMembersCount(publicMembers.size());
            organization.setMembers(publicMembers);
            Date createdAt = ghOrganization.getCreatedAt();
            String email = ghOrganization.getEmail();
            if (email == null) email = "";
            String name = ghOrganization.getName();
            String websiteURL = ghOrganization.getBlog();
            if (websiteURL == null) websiteURL = "";
            organization.setWebsiteURL(websiteURL);
            organization.setCreatedAt(createdAt.getTime());
            List<GithubRepository> repositories = new ArrayList<>();
            ghOrganization.getRepositories().forEach(new BiConsumer<String, GHRepository>() {
                @Override
                public void accept(String repoName, GHRepository ghRepository) {
                    try {
                        GithubRepository githubRepository = createGithubRepository(repoName, ghRepository);
                        if (githubRepository != null) {
                            repositories.add(githubRepository);
                        }
                    } catch (Exception e) {
                        log.error(e);
                        throw new RuntimeException(e);
                    }
                }
            });
            organization.setRepositories(repositories);
            developerInfo.setEmail(email);
            if (name != null) {
                int idx = name.indexOf(' ');
                if (idx != -1) {
                    developerInfo.setFirstName(name.substring(0, idx));
                    developerInfo.setLastName(name.substring(idx));
                } else {
                    developerInfo.setFirstName(name);
                }
            }
            String organizationName = ghOrganization.getCompany();
            if (organizationName == null) organizationName = "";
            developerInfo.setCompany(organizationName);
            developerInfo.setAvatarURL(ghOrganization.getAvatarUrl());
            developerInfo.setFollowers(ghOrganization.getFollowersCount());
            developerInfo.setFollows(ghOrganization.getFollowingCount());
            developerInfo.setRepositoryCount(ghOrganization.getPublicRepoCount());
            developerInfo.setGistCount(ghOrganization.getPublicGistCount());
            organization.setDeveloperInfo(developerInfo);
            developerInfo.setUsername(ghOrganization.getLogin());
            Address address = new Address();
            String location = ghOrganization.getLocation();
            if (location == null) location = "";
            address.setStreet(location);
            organization.setAddress(address);
            organization.setId(organizationId);
            organizationRepository.save(organization);
        } catch (IOException e) {
            log.error(e);
            throw new RuntimeException(e);
        }
    }

    public String createTicket(GHIssue ghIssue) {
        Ticket ticket = new Ticket();
        String ghIssueId = String.valueOf(ghIssue.getId());
        ticket.setId(ghIssueId);
        ticket.setStatus(ghIssue.getState().name());
        ticket.setBody(ghIssue.getBody());
        ticket.setTitle(ghIssue.getTitle());
        long closedAt = ghIssue.getClosedAt() == null ? 0 : ghIssue.getClosedAt().getTime();
        ticket.setClosedAt(closedAt);
        try {
            long updatedAt = ghIssue.getUpdatedAt() == null ? 0 : ghIssue.getUpdatedAt().getTime();
            ticket.setUpdatedAt(updatedAt);
            long createdAt = ghIssue.getCreatedAt() == null ? 0 : ghIssue.getCreatedAt().getTime();
            ticket.setCreatedAt(createdAt);
        } catch (IOException e) {
            log.error(e);
        }
        if (ghIssue.getClosedAt() != null) {
            ticket.setClosedAt(ghIssue.getClosedAt().getTime());
        }
        ticket.setOriginalURL(ghIssue.getHtmlUrl().toString());
        ticket.setCommentCount(ghIssue.getCommentsCount());
        ticket.setIssueNumber(ghIssue.getNumber());
        GHRepository repo = ghIssue.getRepository();
        if (repo != null) {
            ticket.setRepositoryName(repo.getName());
            ticket.setRepositoryOwner(repo.getOwnerName());
        }
        GHUser assignee = ghIssue.getAssignee();
        if (assignee != null) {
            ticket.setAssignedId(String.valueOf(assignee.getId()));
            ticket.setAssignedUsername(assignee.getLogin());
        }
        GHUser closingUser = ghIssue.getClosedBy();
        if (closingUser != null) {
            ticket.setClosedByUsername(closingUser.getLogin());
            ticket.setClosedById(String.valueOf(closingUser.getId()));
        }
        GHUser submittingUser = ghIssue.getUser();
        if (submittingUser != null) {
            ticket.setOpenedById(String.valueOf(submittingUser.getId()));
            ticket.setOpenedByUsername(submittingUser.getLogin());
        }
        ticket.setPullRequest(ghIssue.isPullRequest());
        ticketRepository.save(ticket);
        return ghIssueId;
    }

}