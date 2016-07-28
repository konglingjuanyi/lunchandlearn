package com.pb.lunchandlearn.repository;

import com.pb.lunchandlearn.domain.*;

import java.util.List;
import java.util.Map;

/**
 * Created by DE007RA on 6/6/2016.
 */
public interface CustomFeedbackRepository {

	FeedBack getRatings(Long trainingId);
}