package com.pb.lunchandlearn.repository;

import com.pb.lunchandlearn.domain.CollectionId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by de007ra on 5/3/2016.
 */
@Repository
public interface IDRepository extends MongoRepository<CollectionId, String>{
	public CollectionId findByCollectionName(String collectionName);
}
