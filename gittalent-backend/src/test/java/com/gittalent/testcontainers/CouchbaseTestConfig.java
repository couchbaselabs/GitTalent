package com.gittalent.testcontainers;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.bucket.BucketType;
import com.couchbase.client.java.cluster.BucketSettings;
import com.couchbase.client.java.cluster.ClusterInfo;
import com.couchbase.client.java.cluster.ClusterManager;
import com.couchbase.client.java.cluster.DefaultBucketSettings;
import com.couchbase.client.java.env.CouchbaseEnvironment;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.CouchbaseConfigurer;

import javax.annotation.PostConstruct;

/**
 * Created by ldoguin on 2/13/17.
 */
@Configuration
public class CouchbaseTestConfig  implements CouchbaseConfigurer {

    public static final String clusterUser = "Administrator";
    public static final String clusterPassword = "password";


    public static CouchbaseContainer couchbaseContainer = new CouchbaseContainer()
            .withFTS(true)
            .withIndex(true)
            .withQuery(true)
            .withBeerSample(true)
            .withClusterUsername(clusterUser)
            .withClusterPassword(clusterPassword);

    static {
        couchbaseContainer.start();
    }

    @PostConstruct
    public void init() throws Exception {
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

    @After
    public void tearDown() {
        couchbaseContainer.stop();
    }

}
