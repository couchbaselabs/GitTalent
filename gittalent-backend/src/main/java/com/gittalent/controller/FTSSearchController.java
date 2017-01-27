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
package com.gittalent.controller;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.search.HighlightStyle;
import com.couchbase.client.java.search.SearchQuery;
import com.couchbase.client.java.search.facet.SearchFacet;
import com.couchbase.client.java.search.queries.QueryStringQuery;
import com.couchbase.client.java.util.rawQuerying.RawQueryExecutor;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;

/**
 * Created by ldoguin on 17/08/16.
 */
@Controller
public class FTSSearchController {

    private Bucket bucket;
    private RawQueryExecutor rawQueryExecutor;

    public FTSSearchController(final Bucket bucket, final RawQueryExecutor rawQueryExecutor) {
        this.bucket = bucket;
        this.rawQueryExecutor = rawQueryExecutor;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, path = "/fulltext/")
    public String search(@RequestBody SearchForm form) {
        return executeRawQuerySearch(form);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, path = "/fulltext/ticket")
    public String searchTicket(@RequestBody TicketSearchForm form) {
        QueryStringQuery queryString = SearchQuery.queryString(form.getQueryString());
        SearchQuery query = new SearchQuery("all", queryString);
        query.fields("type", "id", "assignedId", "title", "createdAt", "status");
        query.addFacet("status", SearchFacet.term("status", 3));
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MONTH, -1);
        double monthOld = now.getTimeInMillis();
        now.add(Calendar.MONTH, -2);
        double threeMonthOld = now.getTimeInMillis();
        now.add(Calendar.MONTH, -3);
        double sixMonthOld = now.getTimeInMillis();
        now.add(Calendar.MONTH, -6);
        double yearOld = now.getTimeInMillis();
        now.add(Calendar.YEAR, -20);
        double tooOld = now.getTimeInMillis();
        query.addFacet("createdAt", SearchFacet.numeric("createdAt", 5)
                .addRange("1 month old", monthOld, (double) Calendar.getInstance().getTimeInMillis())
                .addRange("1 to 3 month old", threeMonthOld, monthOld)
                .addRange("3 to 6 month old", sixMonthOld, threeMonthOld)
                .addRange("6 to 12 month old", yearOld, sixMonthOld)
                .addRange("more than a year", tooOld, yearOld));
        query.highlight(HighlightStyle.HTML, "title");
        query.limit(form.getPageSize());
        query.skip(form.getPageSize() * form.getPage());
        return rawQueryExecutor.ftsToRawJson(query);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, path = "/fulltext/developer")
    public String searchDeveloper(@RequestBody DeveloperSearchForm form) {
        return searchDevOrOrg(form);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, path = "/fulltext/organization")
    public String searchOrganization(@RequestBody DeveloperSearchForm form) {
        return searchDevOrOrg(form);
    }

    public String searchDevOrOrg(DeveloperSearchForm form) {
        QueryStringQuery queryString = SearchQuery.queryString(form.getQueryString());
        SearchQuery query = new SearchQuery("all", queryString);
        query.fields("type", "id", "repositories.mainLanguage", "repositories.fullName", "repositories.repoName", "developerInfo.email");
        query.addFacet("Main Language", SearchFacet.term("repositories.mainLanguage", 10));
        query.highlight(HighlightStyle.HTML, "repositories.mainLanguage", "repositories.fullName", "repositories.repoName");
        query.limit(form.getPageSize());
        query.skip(form.getPageSize() * form.getPage());
        return rawQueryExecutor.ftsToRawJson(query);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, path = "/fulltext/{queryString}")
    public String searchAllIndex(final @PathVariable String queryString) {
        SearchForm searchForm = new SearchForm();
        searchForm.setTerm(queryString);
        return executeRawQuerySearch(searchForm);
    }

    public String executeRawQuerySearch(SearchForm form) {
        QueryStringQuery fts = SearchQuery.queryString(form.getQueryString());
        SearchQuery query = new SearchQuery("all", fts);
        query.fields("type", "id", "assignedId", "developerInfo.lastName", "developerInfo.email", "developerInfo.username", "developerInfo.firstName");
        query.addFacet("type", SearchFacet.term("type", 4));
        query.highlight(HighlightStyle.HTML, "title", "repositories.mainLanguage", "repositories.fullName", "repositories.repoName");
        query.limit(form.getPageSize());
        query.skip(form.getPageSize() * form.getPage());
        return rawQueryExecutor.ftsToRawJson(query);
    }
}

class SearchForm {

    private String term;
    private String[] types;
    private int page = 0;
    private int pageSize = 100;

    public SearchForm() {
    }

    public SearchForm(String term, String[] types, int page, int pageSize) {
        this.term = term;
        this.types = types;
        this.page = page;
        this.pageSize = pageSize;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getTermsQueryPart() {
        if (term == null) {
            return "";
        }
        return String.format(" +%s ", term);
    }

    public String[] getTypes() {
        return types;
    }

    public void setTypes(String[] types) {
        this.types = types;
    }

    public String getTypeQueryPart() {
        if (types == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String s : types) {
            sb.append(String.format(" +type:%s ", s));
        }
        return sb.toString();
    }

    public String getQueryString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getTermsQueryPart());
        sb.append(getTypeQueryPart());
        return sb.toString();
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}

class TicketSearchForm extends SearchForm {
    private long dateLowerBound;

    private long dateHigherBound;

    private String status;

    public TicketSearchForm() {
        super();
    }

    public TicketSearchForm(long dateLowerBound, long dateHigherBound, String status, String[] types, String term, int page, int pageSize) {
        super(term, types, page, pageSize);
        this.dateLowerBound = dateLowerBound;
        this.dateHigherBound = dateHigherBound;
        this.status = status;
    }

    public long getDateLowerBound() {
        return dateLowerBound;
    }

    public void setDateLowerBound(long dateLowerBound) {
        this.dateLowerBound = dateLowerBound;
    }

    public String getDateLowerBoundQueryPart() {
        if (dateLowerBound == 0) {
            return "";
        }
        return String.format(" +createdAt:>%d ", dateLowerBound);
    }

    public long getDateHigherBound() {
        return dateHigherBound;
    }

    public void setDateHigherBound(long dateHigherBound) {
        this.dateHigherBound = dateHigherBound;
    }

    public String getDateHigherBoundQueryPart() {
        if (dateHigherBound == 0) {
            return "";
        }
        return String.format(" +createdAt:<%d ", dateHigherBound);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusQueryPart() {
        if (status == null) {
            return "";
        }
        return String.format(" +status:%s ", status);
    }

    public String getQueryString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getTypeQueryPart());
        sb.append(getDateHigherBoundQueryPart());
        sb.append(getDateLowerBoundQueryPart());
        sb.append(getStatusQueryPart());
        sb.append(getTermsQueryPart());
        return sb.toString();
    }

}

class DeveloperSearchForm extends SearchForm {

    @JsonProperty("repositories.mainLanguage")
    private String mainLanguage;

    public DeveloperSearchForm() {
        super();
    }

    public String getMainLanguage() {
        return mainLanguage;
    }

    public void setMainLanguage(String mainLanguage) {
        this.mainLanguage = mainLanguage;
    }

    public String getMainLanguagesQueryPart() {
        if (mainLanguage == null) {
            return "";
        }
        return String.format(" +repositories.mainLanguage:%s ", mainLanguage);
    }

    public String getQueryString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getTypeQueryPart());
        sb.append(getMainLanguagesQueryPart());
        sb.append(getTermsQueryPart());
        return sb.toString();
    }

}