package com.pb.lunchandlearn.service;

import org.springframework.stereotype.Service;

/**
 * Created by DE007RA on 8/9/2016.
 */
public interface IDProviderService {
	Long getNextId(String collectionName);
}
