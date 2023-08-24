package com.te.flinko.util;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.AttributeConverter;

public class MapToStringConverter implements AttributeConverter<Map<String, String>, String> {

	@Override
	public String convertToDatabaseColumn(Map<String, String> map) {
		if (map == null)
			return "";
		return map.entrySet().stream().map(e -> e.getKey() + "=" + e.getValue()).collect(Collectors.joining(","));
	}

	@Override
	public Map<String, String> convertToEntityAttribute(String joined) {
		if (joined == null || joined=="")
			return Collections.emptyMap();
		return !joined.equals("") ? Stream.of(joined.split(",")).map(word -> word.split("="))
				.collect(Collectors.toMap(word -> word[0], word -> word[1])) : Collections.emptyMap();
	}
}


