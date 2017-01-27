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
import com.couchbase.client.java.util.rawQuerying.RawQueryExecutor;
import com.gittalent.model.Developer;
import com.gittalent.repositories.DeveloperRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.couchbase.core.CouchbaseTemplate;
import org.springframework.data.couchbase.core.mapping.event.ValidatingCouchbaseEventListener;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@SpringBootApplication
public class GittalentBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(GittalentBackendApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(final DeveloperRepository developerRepository) throws Exception {
		return args -> {
            Developer developer = new Developer();
            developer.setId("id");
            developer.setKey("id");
            developerRepository.save(developer);
            developer = developerRepository.findOne("id");
		};
	}

	@Bean
	public LocalValidatorFactoryBean validator() {
		return new LocalValidatorFactoryBean();
	}

	@Bean
	public RawQueryExecutor rawQueryExecutor(final Bucket bucket) {
		return new RawQueryExecutor(bucket.name(), "", bucket.core(), bucket.environment());
	}

}
