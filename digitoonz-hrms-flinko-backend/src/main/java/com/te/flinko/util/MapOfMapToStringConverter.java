package com.te.flinko.util;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.AttributeConverter;

public class MapOfMapToStringConverter implements AttributeConverter<Map<String, Map<String, String>>, String> {

	@Override
	public String convertToDatabaseColumn(Map<String, Map<String, String>> map) {

		String str = "";
		if (map == null || map.isEmpty())
			return "";
		Set<Entry<String, Map<String, String>>> entrySet = map.entrySet();
		int i = 0;
		for (Entry<String, Map<String, String>> entry : entrySet) {

			String strsub = "";
			Set<Entry<String, String>> entrySet2 = entry.getValue().entrySet();
			for (Entry<String, String> entry2 : entrySet2) {
				strsub = "" + entry2.getKey() + "$" + entry2.getValue();
			}
			if (i == 0) {
				str = str + entry.getKey() + "=" + strsub;
			} else {

				str = str + "#" + entry.getKey() + "=" + strsub;
			}
			i++;
		}
		return str;
	}

	@Override
	public Map<String, Map<String, String>> convertToEntityAttribute(String joined) {
		if (joined == null || joined.equals(""))
			return new LinkedHashMap<>();

		Map<String, Map<String, String>> linkedHashMap = new LinkedHashMap<>();
		String[] split = joined.split("\\#");
		for (String string : split) {
			String[] split2 = string.split("\\=");
			Map<String, String> collect = new LinkedHashMap<>();

			for (String string2 : split2) {
				if (string2.length() != 10) {
					collect = Stream.of(string2.split("\\,")).map(word -> word.split("\\$")).filter(y -> y.length > 1)
							.collect(Collectors.toMap(word -> word[0], word -> word[1]));
				}

			}
			linkedHashMap.put(split2[0], collect);
		}
		return linkedHashMap;
	}

}
