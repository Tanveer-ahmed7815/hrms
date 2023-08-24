package com.te.flinko.beancopy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class BeanCopy {

	private static ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

	private static Gson gson;
	static {
		gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
				.registerTypeAdapter(LocalTime.class, new LocalTimeDeserializer())
				.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer()).setPrettyPrinting().create();
	}

	public static <T> T jsonProperties(String json, Class<T> valueType) {
		try {
			return gson.fromJson(json, valueType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T> T jsonProperties(String json, TypeReference<T> valueTypeRef) {
		try {
			return gson.fromJson(json, valueTypeRef.getType());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T, E> T objectProperties(E value, Class<T> valueType) {
		try {
			return gson.fromJson(mapper.writeValueAsString(value), valueType);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T, E> T objectProperties(E value, TypeReference<T> valueTypeRef) {
		try {
			return gson.fromJson(mapper.writeValueAsString(value), valueTypeRef.getType());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

	private BeanCopy() {
	}
}
