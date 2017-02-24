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

import com.couchbase.client.core.message.kv.subdoc.multi.Lookup;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.subdoc.DocumentFragment;
import com.couchbase.client.java.util.rawQuerying.RawQueryExecutor;
import com.gittalent.service.GithubImportService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ldoguin on 17/08/16.
 */
@Controller
public class IndexController {

    private GithubImportService githubImportService;
    private Bucket bucket;
    private CouchbaseCluster couchbaseCluster;
    private RawQueryExecutor rawQueryExecutor;

    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private CompletableFuture future;

    public IndexController(final GithubImportService githubImportService, final Bucket bucket, final CouchbaseCluster couchbaseCluster, final RawQueryExecutor rawQueryExecutor) {
        this.githubImportService = githubImportService;
        this.bucket = bucket;
        this.couchbaseCluster = couchbaseCluster;
        this.rawQueryExecutor = rawQueryExecutor;
    }

    @ResponseBody
    @RequestMapping("/githubimport/")
    public String githubImport() throws Exception {
        if (future != null && !future.isDone()) {
            return "import already running";
        }
        future =
                CompletableFuture.runAsync(() -> githubImportService.importDefaultGithubData(), executor);
        return "Import started";
    }

    @ResponseBody
    @RequestMapping("/githubimport/dateam")
    public String githubImportDATeam() throws Exception {
        if (future != null && !future.isDone()) {
            return "import already running";
        }
        future =
                CompletableFuture.runAsync(() -> githubImportService.importDATeam(), executor);
        return "Import started";
    }

    @ResponseBody
    @RequestMapping("/githubimport/developer/{developerId}")
    public String githubImportDeveloper(final @PathVariable String developerId) throws Exception {
        if (future != null && !future.isDone()) {
            return "import already running";
        }
        future =
                CompletableFuture.runAsync(() -> githubImportService.importOneDeveloper(developerId), executor);
        return "Import started";
    }

    @ResponseBody
    @RequestMapping("/githubimport/developerByFollower/{followers}")
    public String githubImportDeveloperByFollower(final @PathVariable String followers) throws Exception {
        if (future != null && !future.isDone()) {
            return "import already running";
        }
        future =
                CompletableFuture.runAsync(() -> githubImportService.importDeveloperByFollowers(">" + followers), executor);
        return "Import started";
    }

    @ResponseBody
    @RequestMapping("/githubimport/organization/{orgaId}")
    public String githubImportOrganization(final @PathVariable String orgaId) throws Exception {
        if (future != null && !future.isDone()) {
            return "import already running";
        }
        future =
                CompletableFuture.runAsync(() -> githubImportService.importOrganization(orgaId), executor);
        return "Import started";
    }

    @ResponseBody
    @RequestMapping("/githubimport/javaOrgs/")
    public String importJavaOrgs() throws Exception {
        if (future != null && !future.isDone()) {
            return "import already running";
        }
        future =
                CompletableFuture.runAsync(() -> githubImportService.importJavaOrgs(), executor);
        return "Import started";
    }

    @ResponseBody
    @RequestMapping("/githubimport/status")
    public String githubImportStatus() throws Exception {
        return String.valueOf(future.isDone());
    }


    @ResponseBody
    @RequestMapping("/developer/fullcontact/{developerId}")
    public String developer(final @PathVariable String developerId) throws Exception {
        JsonArray params = JsonArray.create();
        params.add(developerId);
        N1qlQuery developerWithContacts = N1qlQuery.parameterized("SELECT customer.*, (SELECT contact.* FROM `" + bucket.name() + "` AS contact USE KEYS customer.contacts) AS contacts, (SELECT ticket.* FROM `" + bucket.name() + "` AS ticket USE KEYS customer.history) AS history FROM `" + bucket.name() + "` AS customer WHERE customer.type = 'developer' AND customer.id = $1", params);
        return rawQueryExecutor.n1qlToRawJson(developerWithContacts);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.PUT, path = "/developer/addContact/{developerId}")
    public String addContact(final @PathVariable String developerId, final @RequestBody String json) throws Exception {
        JsonObject jsonData = JsonObject.fromJson(json);
        bucket.mutateIn(developerId)
                .arrayAddUnique("contacts", jsonData.getString("contactId"), true)
                .execute();
        bucket.mutateIn(jsonData.getString("contactId"))
                .arrayAddUnique("contacts", developerId, true)
                .execute();
        return "{}";
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.PUT, path = "/developer/info/{developerId}")
    public String saveDeveloperInfo(final @PathVariable String developerId, final @RequestBody String json) throws Exception {
        JsonObject jsonData = JsonObject.fromJson(json);
        bucket.mutateIn(developerId)
                .replace("developerInfo", jsonData.getObject("developerInfo"))
                .replace("address", jsonData.getObject("address"))
                .execute();
        return "{}";
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.PUT, path = "/developer/removeContact/{developerId}")
    public String removeContact(final @PathVariable String developerId, final @RequestBody String json) throws Exception {
        JsonObject jsonData = JsonObject.fromJson(json);
        String contactId = jsonData.getString("contactId");

        DocumentFragment<Lookup> fragment = bucket.lookupIn(developerId).get("contacts").execute();
        JsonArray contactList = fragment.content("contacts", JsonArray.class);
        List tempList = contactList.toList();
        tempList.remove(contactId);
        contactList = JsonArray.from(tempList);
        bucket.mutateIn(developerId)
                .replace("contacts", contactList).execute();

        fragment = bucket.lookupIn(contactId).get("contacts").execute();
        contactList = fragment.content("contacts", JsonArray.class);
        tempList = contactList.toList();
        tempList.remove(developerId);
        contactList = JsonArray.from(tempList);
        bucket.mutateIn(contactId)
                .replace("contacts", contactList).execute();

        return "{}";
    }

    @ResponseBody
    @RequestMapping("/developer/contacts/{contactId}")
    public String getDeveloperContact(final @PathVariable String contactId) throws Exception {
        JsonArray params = JsonArray.create();
        params.add(contactId);
        N1qlQuery developerWithContacts = N1qlQuery.parameterized("SELECT " +
                "(SELECT contact.* FROM `" + bucket.name() + "` AS contact USE KEYS customer.contacts) AS contacts, " +
                "(SELECT ticket.* FROM `" + bucket.name() + "` AS ticket USE KEYS customer.history) AS tickets " +
                "FROM `" + bucket.name() + "` AS customer " +
                "WHERE customer.type = 'developer' AND customer.id = $1", params);
        return rawQueryExecutor.n1qlToRawJson(developerWithContacts);
    }

    @ResponseBody
    @RequestMapping("/developer/getByEmail")
    public String getCustomerByEmail(final @RequestParam("email") String email) throws Exception {
        JsonArray params = JsonArray.create();
        params.add(email);
        N1qlQuery developerByEmail = N1qlQuery.parameterized("SELECT customer.* " +
                "FROM `" + bucket.name() + "` AS customer " +
                "WHERE customer.type = 'developer' AND customer.developerInfo.email = $1", params);
        return rawQueryExecutor.n1qlToRawJson(developerByEmail);
    }

    @ResponseBody
    @RequestMapping("/developer/search")
    public String searchDevelopers(final @RequestParam("firstname") String firstName, final @RequestParam("lastname") String lastName) throws Exception {
        String statement = "SELECT developer.* FROM `" + bucket.name() + "` AS developer WHERE developer.type = 'developer'";
        JsonObject params = JsonObject.create();
        if (!firstName.equals("")) {
            params.put("firstName", firstName.toLowerCase() + "%");
            statement += " AND lower(developer.developerInfo.firstName) LIKE $firstName";
        }
        if (!lastName.equals("")) {
            params.put("lastName", lastName.toLowerCase() + "%");
            statement += " AND lower(developer.developerInfo.lastName) LIKE $lastName";
        }
        N1qlQuery developerByEmail = N1qlQuery.parameterized(statement, params);
        return rawQueryExecutor.n1qlToRawJson(developerByEmail);
    }

    @ResponseBody
    @RequestMapping("/developer/addTicket/{developerId}/{newTicketId}")
    public String addTicket(final @PathVariable String developerId, final @PathVariable String newTicketId) throws Exception {
        bucket.mutateIn(developerId)
                .arrayAddUnique("history", newTicketId, true)
                .execute();
        return "{}";
    }

    @ResponseBody
    @RequestMapping("/developer/tickets/{developerId}")
    public String getTicket(final @PathVariable String developerId) throws Exception {
        JsonArray params = JsonArray.create();
        params.add(developerId);
        N1qlQuery developerWithContacts = N1qlQuery.parameterized("SELECT (SELECT ticket.* FROM `" + bucket.name() + "` AS ticket USE KEYS customer.history) AS history FROM `" + bucket.name() + "` AS customer WHERE customer.type = 'developer' AND customer.id = $1", params);
        return rawQueryExecutor.n1qlToRawJson(developerWithContacts);
    }

    // tag::getPoolsNodesREST[]
    @ResponseBody
    @RequestMapping("/pools/nodes")
    public String getPoolsNodesREST() throws Exception {
        return couchbaseCluster.clusterManager("Administrator", "password").info().raw().toString();
    }
    // end::getPoolsNodesREST[]

}