package com.pb.lunchandlearn.service;

import com.pb.lunchandlearn.config.LikeType;
import com.pb.lunchandlearn.config.SecuredUser;
import com.pb.lunchandlearn.domain.*;
import com.pb.lunchandlearn.repository.TopicRepository;
import com.pb.lunchandlearn.service.es.ElasticSearchService;
import com.pb.lunchandlearn.service.mail.MailService;
import com.pb.lunchandlearn.utils.CommonUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.pb.lunchandlearn.config.SecurityConfig.getLoggedInUser;
import static com.pb.lunchandlearn.utils.CommonUtil.SORT_BY_LIKES;

/**
 * Created by de007ra on 5/1/2016.
 */
@Service
public class TopicServiceImpl implements TopicService {
	@Autowired
	private TopicRepository topicRepository;

	@Autowired
	private EmployeeServiceImpl employeeService;

	private static Pageable recentPageable;
	private static Pageable topByLikesPageable;

	@Autowired
	private ElasticSearchService elasticSearchService;

	@Autowired
	private MailService mailService;

	@Override
	@PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
	public JSONObject getAll(Pageable pageable, boolean contentOnly) {
		return getTopicsJSON(topicRepository.findAll(pageable), contentOnly);
	}
	static JSONObject getTopicsJSON(Page<Topic> topics, boolean contentOnly) {
		JSONObject jsonObject = new JSONObject();
		if (!contentOnly) {
			jsonObject = CommonUtil.setPaginationInfo(topics, jsonObject);
		}
		jsonObject.put("content", CommonUtil.getTopicsJsonBrief(topics.iterator()));
		return jsonObject;
	}

	@PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
	public static Pageable getRecentPageable() {
		return recentPageable;
	}

	@PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
	public static Pageable getTopByLikesPageable() {
		return topByLikesPageable;
	}

	@Override
	@PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
	public Long getCount() {
		return topicRepository.count();
	}

	@Override
	@PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
	public Topic getTopicByName(String topicName) {
		return topicRepository.findByName(topicName);
	}

	@Override
	@PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
	public Topic getTopicById(Long topicId) {
		return topicRepository.findById(topicId);
	}

	@Override
	@PreAuthorize("hasRole('ADMIN')")
	public void deleteTopic(String topicId) {
		topicRepository.delete(topicId);
	}

	@Override
	@PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
	public Topic add(Topic topic) {
		return topicRepository.insert(topic);
	}

	@Override
	@PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
	public Topic update(Topic topic) {
		return topicRepository.save(topic);
	}

	@Override
	@PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
	public List<Topic> getTrendingTopics() {
		return topicRepository.findAll();
	}

	@Override
	public JSONObject search(String term, Pageable pageable) {
		TextCriteria textCriteria = TextCriteria.forDefaultLanguage().matching(term);
		return getTopicsJSON(topicRepository.findAllBy(textCriteria, pageable), false);
	}

	@Override
	@PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
	public JSONObject updateLikes(Long topicId, LikeType type) {
		SecuredUser user = getLoggedInUser();
		JSONObject json = CommonUtil.getTopicJsonBrief(topicRepository.updateLikes(topicId, type, user.getUsername(), user.getGuid()));
		Topic topic = topicRepository.findNameById(topicId);
		employeeService.updateTopicInterestedIn(user.getGuid(), topicId, topic.getName());
		return json;
	}

	static {
		recentPageable = new Pageable() {
			@Override
			public int getPageNumber() {
				return 1;
			}

			@Override
			public int getPageSize() {
				return 4;
			}

			@Override
			public int getOffset() {
				return 0;
			}

			@Override
			public Sort getSort() {
				return CommonUtil.SORT_BY_DEFAULT;
			}

			@Override
			public Pageable next() {
				return null;
			}

			@Override
			public Pageable previousOrFirst() {
				return null;
			}

			@Override
			public Pageable first() {
				return null;
			}

			@Override
			public boolean hasPrevious() {
				return false;
			}
		};

		topByLikesPageable = new Pageable() {
			@Override
			public int getPageNumber() {
				return 1;
			}

			@Override
			public int getPageSize() {
				return 4;
			}

			@Override
			public int getOffset() {
				return 0;
			}

			@Override
			public Sort getSort() {
				return SORT_BY_LIKES;
			}

			@Override
			public Pageable next() {
				return null;
			}

			@Override
			public Pageable previousOrFirst() {
				return null;
			}

			@Override
			public Pageable first() {
				return null;
			}

			@Override
			public boolean hasPrevious() {
				return false;
			}
		};
	}

	@Override
	@PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
	public JSONObject getAllByIds(List<Long> topicIds) {
		JSONArray content = CommonUtil.getTopicsJsonBrief(topicRepository.getAllByIds(topicIds).iterator());
		JSONObject obj = new JSONObject();
		obj.put("content", content);
		return obj;
	}

	@Override
	@PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
	public boolean updateField(Long topicId, SimpleFieldEntry simpleFieldEntry) {
		if(topicRepository.updateByFieldName(topicId, simpleFieldEntry, getLoggedInUser())) {
			if(simpleFieldEntry.getName() == "name") {
				//update respective trainings and employees
				employeeService.updateTopics(topicId, simpleFieldEntry.getValue().toString());
			}
			return true;
		}
		return false;
	}

	@Override
	@PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
	public JSONArray getTrainings(Long topicId) {
		return getTrainings(topicId, null);
	}

	@Override
	@PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
	public JSONArray getTrainings(Long topicId, String type) {
		Topic topic;
		JSONArray array = new JSONArray();
		if("passed".equalsIgnoreCase(type)) {
			topic = topicRepository.findTrainingsByIdAndBeforeDate(topicId, new Date());
		}
		else if("upcoming".equalsIgnoreCase(type)) {
			topic = topicRepository.findTrainingsByIdAndAfterDate(topicId, new Date());
		}
		else {
			topic = topicRepository.findTrainingsById(topicId);
		}
		if(topic != null && !StringUtils.isEmpty(topic.getTrainings())) {
			for (MiniTrainingDetail training : topic.getTrainings()) {
				JSONObject obj = new JSONObject();
				obj.put("id", training.getId());
				obj.put("name", training.getName());
				obj.put("status", training.getStatus());
				array.add(obj);
			}
		}
		return array;
	}

	@Override
	@PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
	public void addTrainingTo(Map<Long, String> topics, Training training) {
		if(topics != null && topics.size() > 0) {
			for(Map.Entry<Long, String> topic : topics.entrySet()) {
				topicRepository.upsertTraining(topic.getKey(), training);
			}
		}
	}

	@Override
	@PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
	public void removeTrainingFrom(Map<Long, String> topics, Long trainingId) {
		if(topics != null && topics.size() > 0) {
			for(Map.Entry<Long, String> topic : topics.entrySet()) {
				topicRepository.removeTraining(topic.getKey(), trainingId);
			}
		}
	}

	@Override
	@PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
	public void removeEmployees(Map<Long, String> topics, String empGuid, String employeesStr) {
		if(topics != null && topics.size() > 0) {
			for(Map.Entry<Long, String> topic : topics.entrySet()) {
				topicRepository.removeEmployee(topic.getKey(), empGuid, employeesStr);
			}
		}
	}

	@Override
	@PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
	public void addEmployees(Map<Object, String> topics, Employee emp, String employeesStr) {
		if(topics != null && topics.size() > 0) {
			for(Map.Entry<Object, String> topic : topics.entrySet()) {
				Long topicId = Long.parseLong(topic.getKey().toString());
				topicRepository.addEmployee(topicId, emp, employeesStr);
			}
		}
	}

	@Override
	@PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
	public Integer getTrainingLikesCount(Long trainingId) {
		Topic topic = topicRepository.findLikesCountById(trainingId);
		if(topic != null) {
			return topic.getLikesCount();
		}
		return null;
	}

	@Override
	public JSONObject getSuggestedTopics(String topicName) {
		if(StringUtils.isEmpty(topicName)) {
			return null;
		}
		return elasticSearchService.searchTopics(topicName.trim());
	}

	@Scheduled(cron = "0 0 10 ? * MON") // triggers on every MON at 9 am
	public void sendTopicNotifications() {
		Calendar startDate = Calendar.getInstance();
		Calendar endDate = Calendar.getInstance();
		startDate.add(Calendar.DAY_OF_MONTH, -7);
		//new topics in last week
		List<Topic> topics = topicRepository.findAllByCreateDateTimeBetween(startDate.getTime(), endDate.getTime());
		if(!CollectionUtils.isEmpty(topics)) {
			mailService.sendMail(MailService.MailType.NEW_TOPICS_NOTIFICATION, topics);
		}
		topics = topicRepository.findAll(topByLikesPageable).getContent();
		if(!CollectionUtils.isEmpty(topics)) {
			topics = topics.stream().filter((topic) -> topic.getLikesCount() > 0).collect(Collectors.toList());
			if(!CollectionUtils.isEmpty(topics)) {
				mailService.sendMail(MailService.MailType.TOPIC_LIKES_NOTIFICATION, topics);
			}
		}
	}
}