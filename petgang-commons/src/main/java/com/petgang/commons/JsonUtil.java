package com.petgang.commons;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.type.TypeReference;

public class JsonUtil {

	private static final ObjectMapper mapper = new ObjectMapper();

	public static ObjectNode objectNode() {
		return mapper.createObjectNode();
	}

	public static ArrayNode arrayNode() {
		return mapper.createArrayNode();
	}

	public static <T> List<T> convertList(String jsonStr, Class<T> element) {
		try {
			return mapper.readValue(jsonStr, new TypeReference<List<T>>() {
			});
		} catch (JsonParseException e) {
			throw new RuntimeException("反序列化失败 !" + e.getMessage(), e);
		} catch (JsonMappingException e) {
			throw new RuntimeException("反序列化失败 !" + e.getMessage(), e);
		} catch (IOException e) {
			throw new RuntimeException("反序列化失败 !" + e.getMessage(), e);
		}
	}

	public static <T> String convert(T t) {
		try {
			return mapper.writeValueAsString(t);
		} catch (JsonParseException e) {
			throw new RuntimeException("反序列化失败 !" + e.getMessage(), e);
		} catch (JsonMappingException e) {
			throw new RuntimeException("反序列化失败 !" + e.getMessage(), e);
		} catch (IOException e) {
			throw new RuntimeException("反序列化失败 !" + e.getMessage(), e);
		}
	}

	public static <T> ObjectNode convertObjectNode(T t) {
		if (t instanceof String) {
			return convert((String) t, ObjectNode.class);
		}
		return mapper.convertValue(t, ObjectNode.class);
	}

	public static <T> ArrayNode convertArrayNode(T t) {
		if (t instanceof String) {
			return convert((String) t, ArrayNode.class);
		}
		return mapper.convertValue(t, ArrayNode.class);
	}

	public static <T> T convert(String jsonStr, Class<T> t) {
		try {
			return mapper.readValue(jsonStr, t);
		} catch (JsonParseException e) {
			throw new RuntimeException("反序列化失败 !" + e.getMessage(), e);
		} catch (JsonMappingException e) {
			throw new RuntimeException("反序列化失败 !" + e.getMessage(), e);
		} catch (IOException e) {
			throw new RuntimeException("反序列化失败 !" + e.getMessage(), e);
		}
	}
}
