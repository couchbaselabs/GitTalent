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
package com.gittalent.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;

import java.util.List;

/**
 * Created by ldoguin on 05/08/16.
 */
@Document
public class Organization {

    @Id
    private String key;
    private String id;
    private String type = "organization";
    private String name;
    private String websiteURL;
    private Address address;
    private DeveloperInfo developerInfo;
    private Long createdAt;
    private List<GithubRepository> repositories;
    private int membersCount;
    private List<String> members;

    public Organization() {
    }

    public Organization(String id, String type, String name, String websiteURL, Address address, DeveloperInfo developerInfo, Long createdAt, List<GithubRepository> repositories, int membersCount, List<String> members) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.websiteURL = websiteURL;
        this.address = address;
        this.developerInfo = developerInfo;
        this.createdAt = createdAt;
        this.repositories = repositories;
        this.membersCount = membersCount;
        this.members = members;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
        this.id = key;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        this.key = id;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.trim();
    }

    public String getWebsiteURL() {
        return websiteURL;
    }

    public void setWebsiteURL(String websiteURL) {
        this.websiteURL = websiteURL.trim();
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public DeveloperInfo getDeveloperInfo() {
        return developerInfo;
    }

    public void setDeveloperInfo(DeveloperInfo developerInfo) {
        this.developerInfo = developerInfo;
    }

    public List<GithubRepository> getRepositories() {
        return repositories;
    }

    public void setRepositories(List<GithubRepository> repositories) {
        this.repositories = repositories;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public int getMembersCount() {
        return membersCount;
    }

    public void setMembersCount(int membersCount) {
        this.membersCount = membersCount;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Organization that = (Organization) o;

        if (membersCount != that.membersCount) return false;
        if (key != null ? !key.equals(that.key) : that.key != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (websiteURL != null ? !websiteURL.equals(that.websiteURL) : that.websiteURL != null) return false;
        if (address != null ? !address.equals(that.address) : that.address != null) return false;
        if (developerInfo != null ? !developerInfo.equals(that.developerInfo) : that.developerInfo != null)
            return false;
        if (createdAt != null ? !createdAt.equals(that.createdAt) : that.createdAt != null) return false;
        if (repositories != null ? !repositories.equals(that.repositories) : that.repositories != null) return false;
        return members != null ? members.equals(that.members) : that.members == null;

    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (websiteURL != null ? websiteURL.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (developerInfo != null ? developerInfo.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (repositories != null ? repositories.hashCode() : 0);
        result = 31 * result + membersCount;
        result = 31 * result + (members != null ? members.hashCode() : 0);
        return result;
    }
}
