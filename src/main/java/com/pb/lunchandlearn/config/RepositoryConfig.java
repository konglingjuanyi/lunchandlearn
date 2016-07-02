package com.pb.lunchandlearn.config;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Created by DE007RA on 6/4/2016.
 */
@Configuration
@EnableMongoRepositories("com.pb.lunchandlearn.repository")
public class RepositoryConfig extends AbstractMongoConfiguration {
	@Override
	protected String getDatabaseName() {
		return "lunchandlearn";
	}
	@Override
	public Mongo mongo() throws Exception {
		return new MongoClient();
	}
	@Override
	protected String getMappingBasePackage() {
		return "com.pb.lunchandlearn.repository";
	}
}