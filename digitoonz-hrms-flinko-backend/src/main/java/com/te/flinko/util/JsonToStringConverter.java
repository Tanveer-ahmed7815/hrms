package com.te.flinko.util;

import javax.persistence.AttributeConverter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.te.flinko.exception.DataNotFoundException;

public class JsonToStringConverter implements AttributeConverter<Object, String> {

	/**
	 * This method converts the value stored in the entity attribute into the data
	 * representation to be stored in the database.
	 * 
	 * @param attribute = data to be converted to string
	 */
	ObjectMapper mapper = new ObjectMapper();

	@Override
	public String convertToDatabaseColumn(Object json) {
		if (json == null)
			return "";
		try {
			return mapper.writeValueAsString(json);
		} catch (JsonProcessingException e) {
			throw new DataNotFoundException(e.getMessage());
		}
	}// End of the convertToDatabaseColumn()

	/**
	 * This method converts the data stored in the database column into the value to
	 * be stored in the entity attribute
	 * 
	 * @param dbData = data converted back to array of strings
	 */

	@Override
	public Object convertToEntityAttribute(String dbData) {
		if (dbData == null || dbData.trim().length() == 0)
			return "{}";
		try {
			return mapper.readValue(dbData, Object.class);
		} catch (JsonProcessingException e) {
			throw new DataNotFoundException(e.getMessage());
		}
	}// End of the convertToEntityAttribute()

}
