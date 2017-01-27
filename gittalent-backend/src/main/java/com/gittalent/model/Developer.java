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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by ldoguin on 05/08/16.
 */
@Document
public class Developer {

    @Id
    private String key;

    private String id;

    private String type = "developer";

    private String company;

    @Valid
    private DeveloperInfo developerInfo;

    private Address address;

    private List<String> history;

    private List<String> contacts;

    private List<String> organizations;

    private List<String> organizationIds;

    private List<GithubRepository> repositories;

    private Long createdAt;

    private int ticketCount;

    public Developer() {
    }

    public Developer(String id, String type, String company, DeveloperInfo developerInfo, Address address, List<String> history, List<String> contacts, Long createdAt, List<GithubRepository> repositories, int ticketCount, List<String> organizations, List<String> organizationIds) {
        this.id = id;
        this.type = type;
        this.company = company;
        this.developerInfo = developerInfo;
        this.address = address;
        this.history = history;
        this.contacts = contacts;
        this.createdAt = createdAt;
        this.repositories = repositories;
        this.ticketCount = ticketCount;
        this.organizations = organizations;
        this.organizationIds = organizationIds;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.id = key;
        this.key = key;
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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public DeveloperInfo getDeveloperInfo() {
        return developerInfo;
    }

    public void setDeveloperInfo(DeveloperInfo developerInfo) {
        this.developerInfo = developerInfo;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<String> getHistory() {
        return history;
    }

    public void setHistory(List<String> history) {
        this.history = history;
    }

    public List<String> getContacts() {
        return contacts;
    }

    public void setContacts(List<String> contacts) {
        this.contacts = contacts;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public List<GithubRepository> getRepositories() {
        return repositories;
    }

    public void setRepositories(List<GithubRepository> repositories) {
        this.repositories = repositories;
    }

    public int getTicketCount() {
        return ticketCount;
    }

    public void setTicketCount(int ticketCount) {
        this.ticketCount = ticketCount;
    }

    public List<String> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(List<String> organizations) {
        this.organizations = organizations;
    }

    public List<String> getOrganizationIds() {
        return organizationIds;
    }

    public void setOrganizationIds(List<String> organizationIds) {
        this.organizationIds = organizationIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Developer developer = (Developer) o;

        if (ticketCount != developer.ticketCount) return false;
        if (key != null ? !key.equals(developer.key) : developer.key != null) return false;
        if (id != null ? !id.equals(developer.id) : developer.id != null) return false;
        if (type != null ? !type.equals(developer.type) : developer.type != null) return false;
        if (company != null ? !company.equals(developer.company) : developer.company != null) return false;
        if (developerInfo != null ? !developerInfo.equals(developer.developerInfo) : developer.developerInfo != null)
            return false;
        if (address != null ? !address.equals(developer.address) : developer.address != null) return false;
        if (history != null ? !history.equals(developer.history) : developer.history != null) return false;
        if (contacts != null ? !contacts.equals(developer.contacts) : developer.contacts != null) return false;
        if (organizations != null ? !organizations.equals(developer.organizations) : developer.organizations != null)
            return false;
        if (organizationIds != null ? !organizationIds.equals(developer.organizationIds) : developer.organizationIds != null)
            return false;
        if (repositories != null ? !repositories.equals(developer.repositories) : developer.repositories != null)
            return false;
        if (createdAt != null ? !createdAt.equals(developer.createdAt) : developer.createdAt != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (company != null ? company.hashCode() : 0);
        result = 31 * result + (developerInfo != null ? developerInfo.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (history != null ? history.hashCode() : 0);
        result = 31 * result + (contacts != null ? contacts.hashCode() : 0);
        result = 31 * result + (organizations != null ? organizations.hashCode() : 0);
        result = 31 * result + (organizationIds != null ? organizationIds.hashCode() : 0);
        result = 31 * result + (repositories != null ? repositories.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + ticketCount;
        return result;
    }
}
