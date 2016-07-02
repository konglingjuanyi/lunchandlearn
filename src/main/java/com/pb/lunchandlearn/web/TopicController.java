package com.pb.lunchandlearn.web;

/**
 * Created by DE007RA on 4/27/2016.
 */
import com.pb.lunchandlearn.config.LikeType;
import com.pb.lunchandlearn.domain.MiniTrainingDetail;
import com.pb.lunchandlearn.domain.SimpleFieldEntry;
import com.pb.lunchandlearn.domain.Topic;
import com.pb.lunchandlearn.service.TopicService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/topics")
public class TopicController {
	@Autowired
	public TopicService topicService;

	@RequestMapping(value = "/ids", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject byIds(@RequestParam(value = "ids") List<Long> topicIds) {
		return topicService.getAllByIds(topicIds);
	}

	@RequestMapping(value = "topic/{id}/trainings/{type}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<MiniTrainingDetail> trainings(@PathVariable("id") Long topicId, @PathVariable("type") String type) {
		return topicService.getTrainings(topicId, type);
	}

	@RequestMapping(value = "topic/{id}/trainings", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<MiniTrainingDetail> trainings(@PathVariable("id") Long topicId) {
		return topicService.getTrainings(topicId);
	}

	@RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject list(Pageable pageable, @RequestParam(value = "search", required = false) String searchTerm) {
		if(!StringUtils.isEmpty(searchTerm)) {
			return topicService.search(searchTerm, pageable);
		}
		return topicService.getAll(pageable, false);
	}

	@RequestMapping(value = "/recent", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject recent() {
		Pageable pageable = topicService.getRecentPageable();
		return topicService.getAll(pageable, true);
	}

	@RequestMapping(value = "/likes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject likes() {
		Pageable pageable = topicService.getTopByLikesPageable();
		return topicService.getAll(pageable, true);
	}

	@RequestMapping(value="/count", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Long getCount() {
		return topicService.getCount();
	}

	@RequestMapping(value="/topic/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Topic getTopic(@PathVariable("id") Long topicId) {
		return topicService.getTopicById(topicId);
	}

	@RequestMapping(value="/topic", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Topic addTopic(@RequestBody Topic topic, BindingResult result) {
		if(!result.hasErrors()) {
			return topicService.add(topic);
		}
		return null;
	}

	@RequestMapping(value="/{id}/likes", method = RequestMethod.POST)
	public JSONObject likes(@PathVariable("id") Long topicId) {
		return topicService.updateLikes(topicId, LikeType.LIKE);
	}

	@RequestMapping(value="/topic", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public void editTopic(@RequestBody Topic topic) {
		topicService.editTopic(topic);
	}

	@RequestMapping(value="/topic/{id}/field", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public void editTopicByField(@PathVariable("id") Long topicId,
								 @RequestBody SimpleFieldEntry fieldEntry) {
		topicService.editTopicField(topicId, fieldEntry);
	}

	@RequestMapping(value="/topic/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void deleteTopic(@PathVariable("id") String empId) {
		topicService.deleteTopic(empId);
	}

	@RequestMapping(value="/trending", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Topic> trendingTopics() {
		return topicService.getTrendingTopics();
	}
}