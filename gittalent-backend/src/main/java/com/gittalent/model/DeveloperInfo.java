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

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by nraboy on 09/22/16.
 */
public class DeveloperInfo {

    @Size(min = 1, max = 20)
    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private String avatarURL;

    private int followers;

    private int follows;

    private int repositoryCount;

    private int gistCount;

    private String company;

    @NotNull
    private String username;

    private SocialMedia socialMedia;

    public DeveloperInfo() {
    }

    public DeveloperInfo(String firstName, String lastName, String email, String phone, String avatarURL, int followers, int follows, int repositoryCount, int gistCount, String company, String username, SocialMedia socialMedia) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.avatarURL = avatarURL;
        this.followers = followers;
        this.follows = follows;
        this.repositoryCount = repositoryCount;
        this.gistCount = gistCount;
        this.company = company;
        this.username = username;
        this.socialMedia = socialMedia;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName.trim();
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone.trim();
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL.trim();
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getFollows() {
        return follows;
    }

    public void setFollows(int follows) {
        this.follows = follows;
    }

    public int getRepositoryCount() {
        return repositoryCount;
    }

    public void setRepositoryCount(int repositoryCount) {
        this.repositoryCount = repositoryCount;
    }

    public int getGistCount() {
        return gistCount;
    }

    public void setGistCount(int gistCount) {
        this.gistCount = gistCount;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username.trim();
    }

    public SocialMedia getSocialMedia() {
        return socialMedia;
    }

    public void setSocialMedia(SocialMedia socialMedia) {
        this.socialMedia = socialMedia;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeveloperInfo that = (DeveloperInfo) o;

        if (followers != that.followers) return false;
        if (follows != that.follows) return false;
        if (repositoryCount != that.repositoryCount) return false;
        if (gistCount != that.gistCount) return false;
        if (firstName != null ? !firstName.equals(that.firstName) : that.firstName != null) return false;
        if (lastName != null ? !lastName.equals(that.lastName) : that.lastName != null) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (phone != null ? !phone.equals(that.phone) : that.phone != null) return false;
        if (avatarURL != null ? !avatarURL.equals(that.avatarURL) : that.avatarURL != null) return false;
        if (company != null ? !company.equals(that.company) : that.company != null) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        if (socialMedia != null ? !socialMedia.equals(that.socialMedia) : that.socialMedia != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = firstName != null ? firstName.hashCode() : 0;
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (avatarURL != null ? avatarURL.hashCode() : 0);
        result = 31 * result + followers;
        result = 31 * result + follows;
        result = 31 * result + repositoryCount;
        result = 31 * result + gistCount;
        result = 31 * result + (company != null ? company.hashCode() : 0);
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (socialMedia != null ? socialMedia.hashCode() : 0);
        return result;
    }
}