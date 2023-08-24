package com.te.flinko.util;

import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class LinkedHashMapToStringConverter implements AttributeConverter<LinkedHashMap<String, String>, String> {

	@Override
	public String convertToDatabaseColumn(LinkedHashMap<String, String> map) {
		return Objects.nonNull(map)
				? map.entrySet().stream().map(e -> e.getKey() + "=" + e.getValue()).collect(Collectors.joining(","))
				: "";
	}

	@Override
	public LinkedHashMap<String, String> convertToEntityAttribute(String joined) {
		if (joined == null)
			return new LinkedHashMap<>();
		return !joined.equals("")
				? Stream.of(joined.split(",")).map(word -> word.split("="))
						.collect(Collectors.toMap(word -> word[0], word -> word[1], (v1, v2) -> v1, LinkedHashMap::new))
				: new LinkedHashMap<>();
	}

}
