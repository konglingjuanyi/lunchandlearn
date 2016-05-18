package com.pb.lunchandlearn.service;

import com.pb.lunchandlearn.domain.CollectionId;
import com.pb.lunchandlearn.repository.IDRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by de007ra on 5/3/2016.
 */
@Service
public class IDProviderService {
	@Autowired
	private IDRepository idRepository;

	public Long getNextId(String collectionName) {
		CollectionId collectionId = idRepository.findByCollectionName(collectionName);
		Long id = collectionId.getLastId();
		collectionId.setLastId(++id);
		idRepository.save(collectionId);
		return id;
	}
}
