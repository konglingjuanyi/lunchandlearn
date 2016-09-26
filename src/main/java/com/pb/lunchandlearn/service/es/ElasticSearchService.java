package com.pb.lunchandlearn.service.es;

import com.pb.lunchandlearn.domain.Topic;
import com.pb.lunchandlearn.domain.MiniTopic;
import com.pb.lunchandlearn.repository.TopicRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by de007ra on 9/22/2016.
 */
@Service("elasticSearchService")
public class ElasticSearchService {
	private static final short THREAD_POOL_SIZE = 5;
	private ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
	public static PageRequest DEFAULT_PAGE_REQUEST = new PageRequest(0, 5);

	private TopicRepository topicRepository;

	@Autowired
	private ApplicationContext applicationContext;

	public void addTopic(Topic topic) {
		ElasticSearchTask task = applicationContext.getBean(ElasticSearchTask.class);
		task.setType(ElasticSearchRequestType.ADD_TOPIC);
		task.setTopic(topic);
		executor.execute(task);
	}

	public JSONObject searchTopics(String searchString) {
		ElasticSearchTask task = applicationContext.getBean(ElasticSearchTask.class);
		task.setFullTextSearch(searchString);
		return task.getTopicsByFullText();//call sync
	}


	public void updateTopic(Topic topic) {
		ElasticSearchTask task = applicationContext.getBean(ElasticSearchTask.class);
		task.setType(ElasticSearchRequestType.UPDATE_TOPIC);
		task.setTopic(topic);
		executor.execute(task);
	}

	public void deleteTopic(Long topicId) {
		ElasticSearchTask task = applicationContext.getBean(ElasticSearchTask.class);
		task.setType(ElasticSearchRequestType.DELETE_TOPIC);
		task.setTopicId(topicId);
		executor.execute(task);
	}

	public void updateTopicById(Long topicId) {
		Topic topic = topicRepository.findById(topicId);
		updateTopic(topic);
	}

	public enum ElasticSearchRequestType {
		ADD_TOPIC, DELETE_TOPIC, UPDATE_TOPIC, GET_TOPICS
	}
}