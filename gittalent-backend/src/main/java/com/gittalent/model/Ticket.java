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

/**
 * Created by ldoguin on 05/08/16.
 */
@Document
public class Ticket {

    @Id
    private String key;

    private String id;

    private String type = "ticket";

    private String status;

    private String body;

    private Long updatedAt;

    private Long createdAt;

    private Long closedAt;

    private String closedById;

    private String closedByUsername;

    private String assignedUsername;

    private String assignedId;

    private String openedById;

    private String openedByUsername;

    private String title;

    private Integer issueNumber;

    private Integer commentCount;

    private String repositoryId;

    private String repositoryName;

    private String repositoryOwner;

    private String originalURL;

    private Boolean isPullRequest;

    public Ticket() {
    }

    public Ticket(String id, String status, String body, Long updatedAt, Long createdAt, Long closedAt, String closedById, String closedByUsername, String assignedUsername, String assignedId, String openedById, String openedByUsername, String title, Integer issueNumber, Integer commentCount, String repositoryId, String repositoryName, String repositoryOwner, String originalURL, Boolean isPullRequest) {
        this.id = id;
        this.status = status;
        this.body = body;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
        this.closedAt = closedAt;
        this.closedById = closedById;
        this.closedByUsername = closedByUsername;
        this.assignedUsername = assignedUsername;
        this.assignedId = assignedId;
        this.openedById = openedById;
        this.openedByUsername = openedByUsername;
        this.title = title;
        this.issueNumber = issueNumber;
        this.commentCount = commentCount;
        this.repositoryId = repositoryId;
        this.repositoryName = repositoryName;
        this.repositoryOwner = repositoryOwner;
        this.originalURL = originalURL;
        this.isPullRequest = isPullRequest;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
        this.id = key;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status.trim();
    }

    public String getAssignedId() {
        return assignedId;
    }

    public void setAssignedId(String assignedId) {
        this.assignedId = assignedId.trim();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        this.key = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(Long closedAt) {
        this.closedAt = closedAt;
    }

    public String getClosedByUsername() {
        return closedByUsername;
    }

    public void setClosedByUsername(String closedByUsername) {
        this.closedByUsername = closedByUsername;
    }

    public String getAssignedUsername() {
        return assignedUsername;
    }

    public void setAssignedUsername(String assignedUsername) {
        this.assignedUsername = assignedUsername;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getIssueNumber() {
        return issueNumber;
    }

    public void setIssueNumber(Integer issueNumber) {
        this.issueNumber = issueNumber;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public String getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public String getRepositoryOwner() {
        return repositoryOwner;
    }

    public void setRepositoryOwner(String repositoryOwner) {
        this.repositoryOwner = repositoryOwner;
    }

    public String getClosedById() {
        return closedById;
    }

    public void setClosedById(String closedById) {
        this.closedById = closedById;
    }

    public String getOpenedById() {
        return openedById;
    }

    public void setOpenedById(String openedById) {
        this.openedById = openedById;
    }

    public String getOpenedByUsername() {
        return openedByUsername;
    }

    public void setOpenedByUsername(String openedByUsername) {
        this.openedByUsername = openedByUsername.trim();
    }

    public String getOriginalURL() {
        return originalURL;
    }

    public void setOriginalURL(String originalURL) {
        this.originalURL = originalURL;
    }

    public Boolean getPullRequest() {
        return isPullRequest;
    }

    public void setPullRequest(Boolean pullRequest) {
        isPullRequest = pullRequest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ticket ticket = (Ticket) o;

        if (key != null ? !key.equals(ticket.key) : ticket.key != null) return false;
        if (id != null ? !id.equals(ticket.id) : ticket.id != null) return false;
        if (type != null ? !type.equals(ticket.type) : ticket.type != null) return false;
        if (status != null ? !status.equals(ticket.status) : ticket.status != null) return false;
        if (body != null ? !body.equals(ticket.body) : ticket.body != null) return false;
        if (updatedAt != null ? !updatedAt.equals(ticket.updatedAt) : ticket.updatedAt != null) return false;
        if (createdAt != null ? !createdAt.equals(ticket.createdAt) : ticket.createdAt != null) return false;
        if (closedAt != null ? !closedAt.equals(ticket.closedAt) : ticket.closedAt != null) return false;
        if (closedById != null ? !closedById.equals(ticket.closedById) : ticket.closedById != null) return false;
        if (closedByUsername != null ? !closedByUsername.equals(ticket.closedByUsername) : ticket.closedByUsername != null)
            return false;
        if (assignedUsername != null ? !assignedUsername.equals(ticket.assignedUsername) : ticket.assignedUsername != null)
            return false;
        if (assignedId != null ? !assignedId.equals(ticket.assignedId) : ticket.assignedId != null) return false;
        if (openedById != null ? !openedById.equals(ticket.openedById) : ticket.openedById != null) return false;
        if (openedByUsername != null ? !openedByUsername.equals(ticket.openedByUsername) : ticket.openedByUsername != null)
            return false;
        if (title != null ? !title.equals(ticket.title) : ticket.title != null) return false;
        if (issueNumber != null ? !issueNumber.equals(ticket.issueNumber) : ticket.issueNumber != null) return false;
        if (commentCount != null ? !commentCount.equals(ticket.commentCount) : ticket.commentCount != null)
            return false;
        if (repositoryId != null ? !repositoryId.equals(ticket.repositoryId) : ticket.repositoryId != null)
            return false;
        if (repositoryName != null ? !repositoryName.equals(ticket.repositoryName) : ticket.repositoryName != null)
            return false;
        if (repositoryOwner != null ? !repositoryOwner.equals(ticket.repositoryOwner) : ticket.repositoryOwner != null)
            return false;
        if (originalURL != null ? !originalURL.equals(ticket.originalURL) : ticket.originalURL != null) return false;
        return isPullRequest != null ? isPullRequest.equals(ticket.isPullRequest) : ticket.isPullRequest == null;

    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (body != null ? body.hashCode() : 0);
        result = 31 * result + (updatedAt != null ? updatedAt.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (closedAt != null ? closedAt.hashCode() : 0);
        result = 31 * result + (closedById != null ? closedById.hashCode() : 0);
        result = 31 * result + (closedByUsername != null ? closedByUsername.hashCode() : 0);
        result = 31 * result + (assignedUsername != null ? assignedUsername.hashCode() : 0);
        result = 31 * result + (assignedId != null ? assignedId.hashCode() : 0);
        result = 31 * result + (openedById != null ? openedById.hashCode() : 0);
        result = 31 * result + (openedByUsername != null ? openedByUsername.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (issueNumber != null ? issueNumber.hashCode() : 0);
        result = 31 * result + (commentCount != null ? commentCount.hashCode() : 0);
        result = 31 * result + (repositoryId != null ? repositoryId.hashCode() : 0);
        result = 31 * result + (repositoryName != null ? repositoryName.hashCode() : 0);
        result = 31 * result + (repositoryOwner != null ? repositoryOwner.hashCode() : 0);
        result = 31 * result + (originalURL != null ? originalURL.hashCode() : 0);
        result = 31 * result + (isPullRequest != null ? isPullRequest.hashCode() : 0);
        return result;
    }
}
