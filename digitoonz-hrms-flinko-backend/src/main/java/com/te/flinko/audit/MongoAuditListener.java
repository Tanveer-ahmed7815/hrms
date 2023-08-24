package com.te.flinko.audit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@EnableMongoAuditing(auditorAwareRef = "mongoAuditorAware")
@Configuration
public class MongoAuditListener {
	
	@Bean("mongoAuditorAware")
    public AuditorAware<Long> myAuditorProvider() {
        return new EntityAuditorAware();
    }

}
