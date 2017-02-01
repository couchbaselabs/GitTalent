package com.gittalent;

import com.couchbase.client.java.bucket.BucketType;
import com.couchbase.client.java.cluster.DefaultBucketSettings;
import com.gittalent.testcontainers.CouchbaseContainer;
import com.gittalent.testcontainers.CouchbaseContainer;
import com.gittalent.testcontainers.LinkedContainer;
import org.junit.*;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.HttpWaitStrategy;

import java.io.File;
import java.time.Duration;

/**
 * Created by ldoguin on 12/26/16.
 */
@RunWith(SpringRunner.class)
public class GitTalentIntegrationTest {

    public static final String clusterUser = "Administrator";
    public static final String clusterPassword = "password";
    public static final String revId;

    static {
        if (System.getenv("revId") != null) {
            revId = System.getenv("revId");
        } else {
            revId = "latest";
        }
    }

    public static CouchbaseContainer  couchbaseContainer = new CouchbaseContainer()
            .withFTS(true)
            .withIndex(true)
            .withQuery(true)
            .withClusterUsername(clusterUser)
            .withClusterPassword(clusterPassword)
            .withNewBucket(DefaultBucketSettings.builder().enableFlush(true).name("default").quota(100).replicas(0).type(BucketType.COUCHBASE).build());

    static {
        couchbaseContainer.start();
    }

    public static GenericContainer gittalentBackend = new LinkedContainer("gittalent/backend:"+revId)
            .withLinkToContainer(couchbaseContainer, "couchbase")
            .withCommand("--spring.couchbase.bootstrap-hosts=couchbase", "--gittalent.cors.allowedOrigin=http://gittalentfrontend")
            .withExposedPorts(8080).waitingFor(new HttpWaitStrategy().forPath("/browser/index.html#/").forStatusCode(200).withStartupTimeout(Duration.ofSeconds(60)));

    static {
        gittalentBackend.start();
    }

    public static GenericContainer gittalentFrontend = new LinkedContainer("gittalent/frontend:" + revId).withLinkToContainer(gittalentBackend, "gittalentBackend").withExposedPorts(80);

    static {
        gittalentFrontend.start();
    }

    @ClassRule
    public static BrowserWebDriverContainer chrome = new BrowserWebDriverContainer()
            .withLinkToContainer(gittalentFrontend, "gittalentfrontend")
            .withLinkToContainer(gittalentBackend, "gittalentbackend")
            .withDesiredCapabilities(DesiredCapabilities.chrome())
            .withRecordingMode(BrowserWebDriverContainer.VncRecordingMode.RECORD_ALL, new File("target"));


    @Test
    public void testDeveloperTab() throws InterruptedException {
        RemoteWebDriver driver = chrome.getWebDriver();
        driver.get("http://gittalentbackend:8080/githubimport/developer/ldoguin");
        driver.get("http://gittalentbackend:8080/githubimport/status");
        while(driver.getPageSource().contains("false")) {
            Thread.sleep(1000);
            driver.navigate().refresh();
        };
        driver.get("http://gittalentfrontend/");
        WebElement myDynamicElement = (new WebDriverWait(driver, 20))
                    .until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/app-root/div/developers/div[1]/div[2]/div[1]/table/tbody/tr/td[2]")));
        Assert.assertTrue(myDynamicElement.getText().equals("ldoguin"));
    }
}

