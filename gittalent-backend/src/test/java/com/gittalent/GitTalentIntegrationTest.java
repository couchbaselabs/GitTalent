package com.gittalent;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryResult;
import com.couchbase.client.java.query.N1qlQueryRow;
import com.gittalent.service.GithubImportService;
import com.gittalent.testcontainers.AbstractSPDataTestConfig;
import com.gittalent.testcontainers.CouchbaseContainer;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.traits.LinkableContainer;

import java.io.File;

/**
 * Created by ldoguin on 12/26/16.
 */
public class GitTalentIntegrationTest extends AbstractSPDataTestConfig {

    @ClassRule
    public static GenericContainer gittalentFrontend = new GenericContainer("gittalent/frontend:latest").withExposedPorts(80);

    @ClassRule
    public static BrowserWebDriverContainer chrome = new BrowserWebDriverContainer().withLinkToContainer(gittalentFrontend, "gittalentFrontend")
            .withDesiredCapabilities(DesiredCapabilities.chrome())
            .withRecordingMode(BrowserWebDriverContainer.VncRecordingMode.RECORD_ALL, new File("target"));

    @Autowired
    private GithubImportService githubImportService;

    @Autowired
    private Bucket bucket;

    @Test
    public void testDeveloperTab(){
        RemoteWebDriver driver = chrome.getWebDriver();
        driver.get("http://gittalentFrontend/");

        WebElement navbar = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("navbar")));
        Assert.assertNotNull(navbar);
    }
}

