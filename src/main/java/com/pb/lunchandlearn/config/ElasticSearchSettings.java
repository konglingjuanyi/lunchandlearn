package com.pb.lunchandlearn.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by de007ra on 9/22/2016.
 */
@Configuration
@ConfigurationProperties(prefix = "elasticsearch")
public class ElasticSearchSettings {
	private String restUrl;
	private String indexName;
	private String topicName;

	@Override
	public String toString() {
		return "ElasticSearchSettings{" +
				"restUrl='" + restUrl + '\'' +
				", indexName='" + indexName + '\'' +
				", topicName='" + topicName + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ElasticSearchSettings)) return false;

		ElasticSearchSettings that = (ElasticSearchSettings) o;

		if (restUrl != null ? !restUrl.equals(that.restUrl) : that.restUrl != null) return false;
		if (indexName != null ? !indexName.equals(that.indexName) : that.indexName != null) return false;
		return topicName != null ? topicName.equals(that.topicName) : that.topicName == null;

	}

	@Override
	public int hashCode() {
		int result = restUrl != null ? restUrl.hashCode() : 0;
		result = 31 * result + (indexName != null ? indexName.hashCode() : 0);
		result = 31 * result + (topicName != null ? topicName.hashCode() : 0);
		return result;
	}

	public String getRestUrl() {
		return restUrl;
	}

	public void setRestUrl(String restUrl) {
		this.restUrl = restUrl;
	}

	public String getIndexName() {
		return indexName;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}
}
