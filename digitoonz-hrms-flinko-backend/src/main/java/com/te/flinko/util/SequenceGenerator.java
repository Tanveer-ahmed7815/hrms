package com.te.flinko.util;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection  = "db_sequence_generator")
@JsonInclude(value = Include.NON_DEFAULT)
public class SequenceGenerator implements Serializable{
	@Id
    private String sequenceName;
    private long sequenceId;
}
