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
package com.gittalent.testcontainers;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.bucket.BucketType;
import com.couchbase.client.java.cluster.BucketSettings;
import com.couchbase.client.java.cluster.ClusterInfo;
import com.couchbase.client.java.cluster.ClusterManager;
import com.couchbase.client.java.cluster.DefaultBucketSettings;
import com.couchbase.client.java.env.CouchbaseEnvironment;
import com.couchbase.client.java.query.N1qlQuery;
import com.gittalent.GittalentBackendApplication;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.CouchbaseConfigurer;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;

/**
 * Created by ldoguin on 12/13/16.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {GittalentBackendApplication.class, AbstractSPDataTestConfig.CouchbaseTestConfig.class})
public abstract class AbstractSPDataTestConfig {

    public static final String clusterUser = "Administrator";
    public static final String clusterPassword = "password";

    @ClassRule
    public static CouchbaseContainer couchbaseContainer = new CouchbaseContainer()
            .withFTS(true)
            .withIndex(true)
            .withQuery(true)
            .withBeerSample(true)
            .withClusterUsername(clusterUser)
            .withClusterPassword(clusterPassword);

    @Configuration
    static class CouchbaseTestConfig implements CouchbaseConfigurer {

        private CouchbaseContainer couchbaseContainer;

        @PostConstruct
        public void init() throws Exception {
            couchbaseContainer = AbstractSPDataTestConfig.couchbaseContainer;
            BucketSettings settings = DefaultBucketSettings.builder()
                    .enableFlush(true).name("default").quota(100).replicas(0).type(BucketType.COUCHBASE).build();
            settings =  couchbaseCluster().clusterManager(clusterUser, clusterPassword).insertBucket(settings);
            couchbaseContainer.callCouchbaseRestAPI("/settings/indexes", "indexerThreads=0&logLevel=info&maxRollbackPoints=5&storageMode=memory_optimized", "Administrator", "password");
            waitForContainer();
        }

        public void waitForContainer(){
            CouchbaseWaitStrategy s = new CouchbaseWaitStrategy();
            s.withBasicCredentials(clusterUser, clusterPassword);
            s.waitUntilReady(couchbaseContainer);
        }

        @Override
        @Bean
        public CouchbaseEnvironment couchbaseEnvironment() {
            return couchbaseContainer.getCouchbaseEnvironnement();
        }

        @Override
        @Bean
        public Cluster couchbaseCluster() throws Exception {
            return couchbaseContainer.geCouchbaseCluster();
        }

        @Override
        @Bean
        public ClusterInfo couchbaseClusterInfo() throws Exception {
            Cluster cc = couchbaseCluster();
            ClusterManager manager = cc.clusterManager(clusterUser, clusterPassword);
            return manager.info();
        }

        @Override
        @Bean
        public Bucket couchbaseClient() throws Exception {
            return couchbaseContainer.geCouchbaseCluster().openBucket("default");
        }

    }
}
