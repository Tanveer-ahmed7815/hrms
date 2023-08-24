package com.te.flinko.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.AttributeConverter;

import org.apache.commons.lang3.StringUtils;


public class ListToStringConverter implements AttributeConverter<List<String>, String>{

	/**
	 * This method converts the value stored in the entity attribute into the data
	 * representation to be stored in the database.
	 * 
	 * @param attribute = data to be converted to string
	 */

	@Override
	public String convertToDatabaseColumn(List<String> attribute) {
		if (attribute == null || attribute.isEmpty()) {
			return "";
		}
		return StringUtils.join(attribute.toArray(), ',');
	}// End of the convertToDatabaseColumn()

	/**
	 * This method converts the data stored in the database column into the value to
	 * be stored in the entity attribute
	 * 
	 * @param dbData = data converted back to array of strings
	 */

	@Override
	public List<String> convertToEntityAttribute(String dbData) {
		if (dbData == null || dbData.trim().length() == 0) {
			return new ArrayList<>();
		}

		String[] data = dbData.split(",");
		return Arrays.asList(data);
	}// End of the convertToEntityAttribute()


}
