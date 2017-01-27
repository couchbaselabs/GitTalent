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
package com.gittalent;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryResult;
import com.couchbase.client.java.query.N1qlQueryRow;
import com.gittalent.model.Developer;
import com.gittalent.model.DeveloperInfo;
import com.gittalent.repositories.DeveloperRepository;
import com.gittalent.testcontainers.AbstractSPDataTestConfig;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolationException;

public class GittalentBackendApplicationTests extends AbstractSPDataTestConfig {

	@Autowired
    private DeveloperRepository developerRepository;

	@Autowired
	private Bucket bucket;

	@Test
	public void testRepository() throws Exception {
		Developer developer = new Developer();
        DeveloperInfo developerInfo = new DeveloperInfo();
		developer.setId("alovelace");
        developerInfo.setFirstName("Ada");
        developerInfo.setLastName("Lovelace");
		developerInfo.setUsername("alovelace");
        developer.setDeveloperInfo(developerInfo);
		developerRepository.save(developer);

		JsonDocument doc = bucket.get("alovelace");
		Assert.assertNotNull(doc);
	}

	@Test(expected=ConstraintViolationException.class)
	public void testDevValidation() {
		Developer developer = new Developer();
		DeveloperInfo developerInfo = new DeveloperInfo();
		developer.setId("alovelace");
		developerInfo.setFirstName("Ada");
		developerInfo.setLastName("Lovelace");
		developer.setDeveloperInfo(developerInfo);
		developerRepository.save(developer);
	}

}
