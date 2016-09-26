package com.pb.lunchandlearn.service.es;

import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pb.lunchandlearn.config.ElasticSearchSettings;
import com.pb.lunchandlearn.domain.MiniTopic;
import com.pb.lunchandlearn.domain.Topic;
import org.apache.commons.lang3.ArrayUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by de007ra on 9/22/2016.
 */
public class ElasticSearchTask implements Runnable {
	private Topic topic;
	private String fullTextSearch;
	private Long topicId;

	@Autowired
	private ElasticSearchSettings elasticSearchSettings;

	private boolean addTopic() {
		MiniTopic miniTopic = new MiniTopic(topic.getId(), topic.getName(), topic.getDesc());
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.postForLocation(getTopicRestUrl(topic.getId()), miniTopic);
		return true;
	}

	@Override
	public String toString() {
		return "ElasticSearchTask{" +
				"type=" + type +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ElasticSearchTask)) return false;

		ElasticSearchTask that = (ElasticSearchTask) o;

		return type == that.type;

	}

	@Override
	public int hashCode() {
		return type != null ? type.hashCode() : 0;
	}

	public void setType(ElasticSearchService.ElasticSearchRequestType type) {
		this.type = type;
	}

	private ElasticSearchService.ElasticSearchRequestType type;

	@Override
	public void run() {
		switch (type) {
			case ADD_TOPIC:
				addTopic();
				break;
			case UPDATE_TOPIC:
				updateTopic();
				break;
			case DELETE_TOPIC:
				deleteTopic();
				break;
		}
	}

	private String getTopicRestUrl(Long topicId) {
		return elasticSearchSettings.getRestUrl() + "/" + elasticSearchSettings.getIndexName() +
				"/" + elasticSearchSettings.getTopicName() + "/" + (topicId != null ? topicId : "");
	}

	private void deleteTopic() {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.delete(getTopicRestUrl(topicId));
	}

	private void updateTopic() {
		MiniTopic miniTopic = new MiniTopic(topic.getId(), topic.getName(), topic.getDesc());
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.postForLocation(getTopicRestUrl(topicId), miniTopic);
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}

	public void setFullTextSearch(String fullTextSearch) {
		this.fullTextSearch = fullTextSearch;
	}

	public void setTopicId(Long topicId) {
		this.topicId = topicId;
	}

	public JSONObject getTopicsByFullText() {
		RestTemplate restTemplate = new RestTemplate();
		String url = getTopicRestUrl(topicId) + "_search?fields=name,id";
		JSONObject jsonObject = getJsonSearchQry();

		String response = restTemplate.postForObject(url, jsonObject, String.class);
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode jsonNode = mapper.readTree(response);
			return getMiniTopics(jsonNode);
		} catch (Exception exp) {
			//TODO: logging
		}
		return null;
	}

	private JSONObject getJsonSearchQry() {
		JSONObject jsonObject = new JSONObject();
		JSONObject all = new JSONObject();
		all.put("_all", fullTextSearch);
		JSONObject match = new JSONObject();
		match.put("match", all);
		jsonObject.put("query", match);
		return jsonObject;
	}

	private JSONObject getMiniTopics(JsonNode jsonNode) {
		JsonNode treeNode = jsonNode.path("hits");
		if (treeNode.isMissingNode()) {
			return null;
		}
		int totalHits = treeNode.path("total").asInt();
		if (totalHits > 0) {
			JsonNode jsonNodes = treeNode.path("hits");
			if (jsonNodes.isArray()) {
				JSONObject miniTopics = new JSONObject();
				for (JsonNode node : jsonNodes) {
					String id = node.path("_id").asText();
					JsonNode fieldsJson = node.path("fields");
					JsonNode names = fieldsJson.path("name");
					if (names.isArray()) {
						String name = names.get(0).asText();
						miniTopics.put(id, name);
					}
				}
				return miniTopics;
			}
		}
		return null;
	}
}