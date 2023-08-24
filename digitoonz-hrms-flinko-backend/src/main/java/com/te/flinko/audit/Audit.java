package com.te.flinko.audit;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass	
@EntityListeners(AuditingEntityListener.class)
public class Audit {

	@CreatedBy
	@Column(name = "created_by", updatable = false)
	private Long createdBy;

	@CreatedDate
	@Column(name = "created_date", updatable = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime createdDate;

	@LastModifiedBy
	@Column(name = "modified_by")
	private Long lastModifiedBy;

	@LastModifiedDate
	@Column(name = "modified_date")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime lastModifiedDate;
	
	@Column(name="terminal_id", length=25)
	private String terminalId;

}
