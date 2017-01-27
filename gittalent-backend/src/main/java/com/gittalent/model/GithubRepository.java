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

import java.util.Map;

/**
 * Created by ldoguin on 27/09/16.
 */
public class GithubRepository {

    private String repoName;

    private int size;

    private int subscriberCount;

    private String fullName;

    private String description;

    private String mainLanguage;

    private Map<String, Long> languages;

    private Long createdAt;

    private Long updatedAt;

    public GithubRepository() {
    }

    public GithubRepository(String repoName, int size, int subscriberCount, String fullName, String description, String mainLanguage, Map<String, Long> languages, Long createdAt, Long updatedAt) {
        this.repoName = repoName;
        this.size = size;
        this.subscriberCount = subscriberCount;
        this.fullName = fullName;
        this.description = description;
        this.mainLanguage = mainLanguage;
        this.languages = languages;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSubscriberCount() {
        return subscriberCount;
    }

    public void setSubscriberCount(int subscriberCount) {
        this.subscriberCount = subscriberCount;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMainLanguage() {
        return mainLanguage;
    }

    public void setMainLanguage(String mainLanguage) {
        this.mainLanguage = mainLanguage;
    }

    public Map<String, Long> getLanguages() {
        return languages;
    }

    public void setLanguages(Map<String, Long> languages) {
        this.languages = languages;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getRepoName() {
        return repoName;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GithubRepository that = (GithubRepository) o;

        if (size != that.size) return false;
        if (subscriberCount != that.subscriberCount) return false;
        if (repoName != null ? !repoName.equals(that.repoName) : that.repoName != null) return false;
        if (fullName != null ? !fullName.equals(that.fullName) : that.fullName != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (mainLanguage != null ? !mainLanguage.equals(that.mainLanguage) : that.mainLanguage != null) return false;
        if (languages != null ? !languages.equals(that.languages) : that.languages != null) return false;
        if (createdAt != null ? !createdAt.equals(that.createdAt) : that.createdAt != null) return false;
        return updatedAt != null ? updatedAt.equals(that.updatedAt) : that.updatedAt == null;

    }

    @Override
    public int hashCode() {
        int result = repoName != null ? repoName.hashCode() : 0;
        result = 31 * result + size;
        result = 31 * result + subscriberCount;
        result = 31 * result + (fullName != null ? fullName.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (mainLanguage != null ? mainLanguage.hashCode() : 0);
        result = 31 * result + (languages != null ? languages.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (updatedAt != null ? updatedAt.hashCode() : 0);
        return result;
    }
}
