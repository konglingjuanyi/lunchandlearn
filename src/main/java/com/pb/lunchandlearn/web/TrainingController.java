package com.pb.lunchandlearn.web;

/**
 * Created by DE007RA on 4/27/2016.
 */

import com.pb.lunchandlearn.config.LikeType;
import com.pb.lunchandlearn.domain.*;
import com.pb.lunchandlearn.service.TrainingService;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/trainings")
public class TrainingController {
	@Autowired
	public TrainingService trainingService;

	@RequestMapping(value = "/ids", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject byIds(@RequestParam(value = "ids") List<Long> trainingIds) {
		return trainingService.getAllByIds(trainingIds);
	}

	@RequestMapping(value = "training/{id}/topics", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject topics(@PathVariable("id") Long trainingId) {
		return trainingService.getTopics(trainingId);
	}

	@RequestMapping(value = "training/{id}/comments", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public JSONArray comments(@PathVariable("id") Long trainingId) {
		return trainingService.getComments(trainingId);
	}

	@RequestMapping(value = "training/{id}/feedbacks", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject getFeedbacks(@PathVariable("id") Long trainingId) {
		return trainingService.getFeedBacks(trainingId);
	}

	@RequestMapping(value = "training/{id}/feedbacks/{feedbackId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public FeedBack getFeedback(@PathVariable("id") Long trainingId, @PathVariable("feedbackId") Long feedbackId) {
		return trainingService.getFeedBack(feedbackId);
	}

	@RequestMapping(value = "training/{id}/comments", method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Comment addComment(@PathVariable("id") Long trainingId, @RequestBody SimpleFieldEntry comment) {
		if (!StringUtils.isEmpty(comment.getValue())) {
			return trainingService.add(new Comment(comment.getValue().toString()), trainingId);
		}
		return null;
	}

	@RequestMapping(value = "training/{id}/comments/{commentId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public void removeComment(@PathVariable("id") Long trainingId, @PathVariable("commentId") Long commentId) throws IOException {
		trainingService.removeComment(trainingId, commentId);
	}

	@RequestMapping(value = "training/{id}/comments/{commentId}/replies/{replyCommentId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public void removeCommentReply(@PathVariable("id") Long trainingId, @PathVariable("commentId") Long commentId,
								   @PathVariable("replyCommentId") Long replyCommentId) throws IOException {
		trainingService.removeCommentReply(trainingId, commentId, replyCommentId);
	}

	@RequestMapping(value = "training/{id}/comments/{commentId}/reply", method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Comment addCommentReply(@PathVariable("id") Long trainingId, @PathVariable("commentId") Long commentParentId,
								   @RequestBody SimpleFieldEntry comment) {
		if (!StringUtils.isEmpty(comment.getValue())) {
			return trainingService.add(new Comment(comment.getValue().toString()), trainingId, commentParentId);
		}
		return null;
	}

	@RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject list(Pageable pageable, @RequestParam(value = "search", required = false) String searchTerm,
						   @RequestParam(value = "filterBy", required = false) String filterBy) {
		if (!StringUtils.isEmpty(searchTerm)) {
			return trainingService.search(searchTerm, pageable, filterBy);
		}
		return trainingService.getAll(pageable, false, filterBy);
	}

	@RequestMapping(value = "/{trainingStatus}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject byStatus(@PathVariable("trainingStatus")String status) {
		Pageable pageable = trainingService.getRecentPageable();
		return trainingService.getAll(pageable, true, status);
	}

	@RequestMapping(value = "/likes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject likes() {
		Pageable pageable = trainingService.getTopByLikesPageable();
		return trainingService.getAll(pageable, true, null);
	}

	@RequestMapping(value = "/count", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Long getCount() {
		return trainingService.getCount();
	}

	@RequestMapping(value = "/training/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Training getTraining(@PathVariable("id") Long trainingId) {
		return trainingService.getTrainingById(trainingId);
	}

	@RequestMapping(value = "/training/{id}/minimal", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject getMinimalTraining(@PathVariable("id") Long trainingId) {
		return trainingService.getTrainingMinimal(trainingId);
	}

	@RequestMapping(value = "/training/{id}/trainees", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, String> getTrainees(@PathVariable("id") Long trainingId) {
		return trainingService.getTraineesById(trainingId);
	}

	@RequestMapping(value = "/training", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Training addTraining(@RequestBody Training training, BindingResult result) {
		if (!result.hasErrors()) {
			return trainingService.add(training);
		}
		return null;
	}

	@RequestMapping(value = "/training/{id}/feedbacks", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public FeedBack addFeedback(@RequestBody FeedBack feedBack, BindingResult result) {
		if (!result.hasErrors()) {
			return trainingService.add(feedBack);
		}
		return null;
	}

	@RequestMapping(value = "/{id}/likes", method = RequestMethod.POST)
	public JSONObject likes(@PathVariable("id") Long trainingId) {
		return trainingService.updateLikes(trainingId, LikeType.LIKE);
	}

	@RequestMapping(value = "/training", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public void updateTraining(@RequestBody Training training) {
		trainingService.update(training);
	}

	@RequestMapping(value = "/training/{id}/field", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public void updateTrainingByField(@PathVariable("id") Long trainingId,
									  @RequestBody SimpleFieldEntry fieldEntry) throws ParseException {
		trainingService.updateField(trainingId, fieldEntry);
	}

	@RequestMapping(value = "/training/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void deleteTraining(@PathVariable("id") String trainingId) {
		trainingService.deleteTraining(trainingId);
	}

	@RequestMapping(value = "/trending", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Training> trendingTrainings() {
		return trainingService.getTrendingTrainings();
	}

	@RequestMapping(value = "training/{id}/attachment", method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public void attachFile(@PathVariable("id") Long trainingId, @RequestParam("file") MultipartFile file) throws IOException {
		InputStream in = null;
		try {
			in = file.getInputStream();
			trainingService.add(trainingId, file.getOriginalFilename(), in);
		} catch (IOException exp) {
			throw exp;
		} finally {
			IOUtils.closeQuietly(in);
		}
	}

	@RequestMapping(value = "training/{id}/attachments", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public JSONArray attachments(@PathVariable("id") Long trainingId) throws IOException {
		return trainingService.getAttachedFiles(trainingId);
	}

	@RequestMapping(value = "training/{id}/attachments/file", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public void removeAttachment(@PathVariable("id") Long trainingId, @RequestParam("name") String fileName) throws IOException {
		trainingService.removeAttachedFile(trainingId, fileName);
	}

	@RequestMapping(value = "training/{id}/attachments/file", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<InputStreamResource> getAttachmentFile(@PathVariable("id") Long trainingId, @RequestParam("name") String fileName) throws IOException {
		FileAttachmentInfo fileAttachmentInfo = trainingService.getAttachmentFileInfoWithFile(trainingId, fileName);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");
		headers.add("Content-Disposition", "attachment;filename=\"" + fileAttachmentInfo.getFileName() + "\"");
		return ResponseEntity.ok().contentLength(fileAttachmentInfo.getSize()).headers(headers)
				.contentType(MediaType.APPLICATION_OCTET_STREAM).body(new InputStreamResource(fileAttachmentInfo.getFile()));
	}
}