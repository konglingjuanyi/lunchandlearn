package com.pb.lunchandlearn.utils;

import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.pb.lunchandlearn.domain.*;
import com.pb.lunchandlearn.exception.ResourceNotFoundException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.util.CollectionUtils;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.*;

/**
 * Created by DE007RA on 6/7/2016.
 */
public final class CommonUtil {
	private static ISO8601DateFormat DF = new ISO8601DateFormat();
	public static final ImmutableList<Comment> EMPTY_COMMENT_LIST = new ImmutableList.Builder<Comment>().build();
	public static final Sort SORT_BY_DEFAULT = new Sort(Sort.Direction.DESC, "createDateTime");
	public static Sort SORT_BY_SCORE = new Sort(Sort.Direction.DESC, "score");

	public static Sort SORT_BY_LIKES = new Sort(Sort.Direction.DESC, "likesCount");
	public static Date parseDate(String isoDateStr) throws ParseException {
		return DF.parse(isoDateStr);
	}

	public static JSONObject getTopicJsonBrief(Topic topic) {
		JSONObject objTopic = new JSONObject();
		objTopic.put("name", topic.getName());
		objTopic.put("id", topic.getId());
		objTopic.put("likesCount", topic.getLikesCount());
		return objTopic;
	}

	public static JSONObject getTrainingJsonBrief(Training training) {
		JSONObject objTraining = new JSONObject();
		objTraining.put("name", training.getName());
		objTraining.put("id", training.getId());
		objTraining.put("likesCount", training.getLikesCount());
		return objTraining;
	}

	public static JSONObject getComment(Comment comment) {
		JSONObject objComment = new JSONObject();
		objComment.put("ownerName", comment.getOwnerName());
		objComment.put("ownerGuid", comment.getOwnerGuid());
		objComment.put("id", comment.getId());
		objComment.put("text", comment.getText());
		objComment.put("replies", getComments(comment.getReplies()));
		return objComment;
	}

	public static JSONArray getComments(List<Comment> comments) {
		JSONArray jsonArray = new JSONArray();
		if(!CollectionUtils.isEmpty(comments)) {
			for (Comment comment : comments) {
				jsonArray.add(getComment(comment));
			}
		}
		return jsonArray;
	}

	public static JSONObject getEmployeeMinimal(Employee employee) {
		JSONObject objEmployee = new JSONObject();
		objEmployee.put("guid", employee.getGuid());
		objEmployee.put("emailId", employee.getEmailId());
		objEmployee.put("name", employee.getName());
		return objEmployee;
	}

	public static JSONArray getEmployeesMinimal(List<Employee> employees) {
		JSONArray jsonArray = new JSONArray();
		if(!CollectionUtils.isEmpty(employees)) {
			for (Employee employee : employees) {
				jsonArray.add(getEmployeeMinimal(employee));
			}
		}
		return jsonArray;
	}

	public static JSONObject getFileAttachmentInfoBrief(FileAttachmentInfo fileAttachmentInfo) {
		JSONObject objComment = new JSONObject();
		objComment.put("fileName", fileAttachmentInfo.getFileName());
		objComment.put("size", fileAttachmentInfo.getSize());
		return objComment;
	}

	public static JSONArray getFileAttachmentInfosBrief(List<FileAttachmentInfo> fileAttachmentInfos) {
		JSONArray jsonArray = new JSONArray();
		if(!CollectionUtils.isEmpty(fileAttachmentInfos)) {
			for (FileAttachmentInfo comment : fileAttachmentInfos) {
				jsonArray.add(getFileAttachmentInfoBrief(comment));
			}
		}
		return jsonArray;
	}

	public static JSONArray getTopicsJsonBrief(Iterator<Topic> iterator) {
		JSONArray jsonContent = new JSONArray();
		while(iterator.hasNext()) {
			jsonContent.add(CommonUtil.getTopicJsonBrief(iterator.next()));
		}
		return jsonContent;
	}

	public static JSONArray getTrainingsJsonBrief(Iterator<Training> iterator) {
		JSONArray jsonContent = new JSONArray();
		while(iterator.hasNext()) {
			jsonContent.add(CommonUtil.getTrainingJsonBrief(iterator.next()));
		}
		return jsonContent;
	}

	public static JSONObject setPaginationInfo(Page page, JSONObject jsonObject) {
		jsonObject.put("totalElements", page.getTotalElements());
		jsonObject.put("totalPages", page.getTotalPages());
		jsonObject.put("number", page.getNumber());
		jsonObject.put("numberOfElements", page.getNumberOfElements());
		jsonObject.put("size", page.getSize());
		return jsonObject;
	}

	public static Map<Object, Object> updateOldNewMapValues(Map<Object, Object> oldEntries, Map<Object, Object> newEntries,
											 Map<Object, Object> addedEntries) {
		if(oldEntries == null || oldEntries.size() == 0) {
			addedEntries = newEntries;
		}
		else if(newEntries != null || newEntries.size() > 0) {
			addedEntries = new HashMap<>(newEntries.size());
			for (Map.Entry<Object, Object> entry : newEntries.entrySet()) {
				Object matching = oldEntries.remove(entry.getKey());
				if (matching == null) {
					addedEntries.put(entry.getKey(), entry.getValue());
				}
			}
		}
		return addedEntries;
	}
}