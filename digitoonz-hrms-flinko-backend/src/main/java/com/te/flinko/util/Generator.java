package com.te.flinko.util;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class Generator {

	@Autowired
	private MongoTemplate template;

	public Long generateSequence(String seqName) {
		Query query = new Query();
		query.addCriteria(new Criteria().andOperator(Criteria.where("_id").is(seqName)));
		SequenceGenerator sequenceGenerator = template.findAndModify(query, new Update().inc("sequenceId", 1),
				options().returnNew(true).upsert(true), SequenceGenerator.class);
		return !Objects.isNull(sequenceGenerator) ? sequenceGenerator.getSequenceId() : 1;
	}
}
