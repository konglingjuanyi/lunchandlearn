package com.pb.lunchandlearn.utils;

import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.pb.lunchandlearn.domain.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.util.CollectionUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by DE007RA on 6/7/2016.
 */
public final class CommonUtil {
	public static ISO8601DateFormat DF = new ISO8601DateFormat();
	public static  DateFormat DF_WEEK = new SimpleDateFormat("EEEEE");
	public static  DateFormat DF_YEAR = new SimpleDateFormat("YYYY");
	public static final Sort SORT_BY_DEFAULT = new Sort(Sort.Direction.DESC, "createDateTime");
	public static Sort SORT_BY_SCORE = new Sort(Sort.Direction.DESC, "score");

	public static Sort SORT_BY_LIKES = new Sort(Sort.Direction.DESC, "likesCount");
	public static Date parseDate(String isoDateStr) throws ParseException {
		return DF.parse(isoDateStr);
	}

	private static DateFormat dfDay = new SimpleDateFormat("d MMM");
	private static DateFormat dfTime = new SimpleDateFormat("h:mm a");

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
		objTraining.put("scheduledOn", training.getScheduledOn());
		objTraining.put("status", training.getStatus());
		objTraining.put("location", training.getLocation());
		objTraining.put("duration", training.getDuration());
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

	public static JSONObject getUserInfo(User user, String guid) {
		JSONObject objEmployee = new JSONObject();
		objEmployee.put("name", user.getName());
		objEmployee.put("guid", guid);
		objEmployee.put("emailId", user.getEmailId());
		objEmployee.put("roles", user.getRoles());
		return objEmployee;
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
		JSONObject objEmployee = getEmployeeGuidName(employee);
		objEmployee.put("emailId", employee.getEmailId());
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

	public static JSONObject getEmployeeGuidName(Employee employee) {
		JSONObject objEmployee = new JSONObject();
		objEmployee.put("guid", employee.getGuid());
		objEmployee.put("name", employee.getName());
		return objEmployee;
	}

	public static JSONArray getEmployeesGuidName(List<Employee> employees) {
		JSONArray jsonArray = new JSONArray();
		if(!CollectionUtils.isEmpty(employees)) {
			for (Employee employee : employees) {
				jsonArray.add(getEmployeeGuidName(employee));
			}
		}
		return jsonArray;
	}

	public static JSONObject getFeedback(FeedBack feedBack) {
		JSONObject objFeedback = new JSONObject();
		objFeedback.put("id", feedBack.getId());
		objFeedback.put("ratings", feedBack.getRatings());
		objFeedback.put("respondentGuid", feedBack.getRespondentGuid());
		objFeedback.put("respondentName", feedBack.getRespondentName());
		return objFeedback;
	}

	public static JSONArray getFeedbacks(List<FeedBack> feedBacks) {
		JSONArray jsonArray = new JSONArray();
		if(!CollectionUtils.isEmpty(feedBacks)) {
			for (FeedBack feedBack : feedBacks) {
				jsonArray.add(getFeedback(feedBack));
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

	public static String getDayMonthWithOrdinal(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		String ordinal = "th";
		switch (calendar.get(Calendar.DAY_OF_MONTH) % 10) {
			case 1: ordinal =  "st";
				break;
			case 2: ordinal  = "nd";
				break;
			case 3: ordinal = "rd";
				break;
		}
		String dateStr = dfDay.format(calendar.getTime());
		return dateStr.replace(" ", ordinal + " ");
	}

	public static String getWeekMonthYear(Date date) {
		StringBuffer sb = new StringBuffer(DF_WEEK.format(date.getTime()));
		sb.append(", ");
		sb.append(getDayMonthWithOrdinal(date));
		DF_YEAR.format(date.getTime());
		return sb.toString();
	}

	public static String getTime(Date date) {
		return dfTime.format(date.getTime());
	}

	public static JSONArray getTrainingRoomsJson(List<TrainingRoom> rooms) {
		JSONArray array = new JSONArray();
		for(TrainingRoom room : rooms) {
			array.add(getTrainingRoomJson(room));
		}
		return array;
	}
	public static JSONArray getTrainingRoomsJson(Iterator<TrainingRoom> trainingRoom) {
		JSONArray array = new JSONArray();
		while(trainingRoom.hasNext()) {
			array.add(getTrainingRoomJson(trainingRoom.next()));
		}
		return array;
	}

	public static JSONObject getTrainingRoomJson(TrainingRoom trainingRoom) {
		JSONObject objTopic = new JSONObject();
		objTopic.put("id", trainingRoom.getId());
		objTopic.put("name", trainingRoom.getName());
		objTopic.put("location", trainingRoom.getLocation());
		return objTopic;
	}
}