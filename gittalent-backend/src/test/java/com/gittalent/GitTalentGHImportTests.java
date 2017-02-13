package com.gittalent;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryResult;
import com.couchbase.client.java.query.N1qlQueryRow;
import com.gittalent.service.GithubImportService;
import com.gittalent.testcontainers.CouchbaseTestConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by ldoguin on 12/26/16.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {GittalentBackendApplication.class, CouchbaseTestConfig.class})
public class GitTalentGHImportTests {

    @Autowired
    private GithubImportService githubImportService;

    @Autowired
    private Bucket bucket;

    @Test
    public void importDevAdvocateTeam(){
        githubImportService.importOneDeveloper("ldoguin");
        N1qlQueryResult result = bucket.query(N1qlQuery.simple("CREATE PRIMARY INDEX ON default"));
        N1qlQuery query = N1qlQuery.simple("SELECT * FROM default WHERE developerInfo.username = 'ldoguin'");
        result = bucket.query(query);
        N1qlQueryRow row = result.rows().next();
        Assert.assertNotNull(row);
        System.out.println(row.value());
    }
}
