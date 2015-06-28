package com.petgang.service.upload;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.codehaus.jackson.node.ObjectNode;

import com.google.common.collect.Maps;
import com.petgang.commons.JsonUtil;
import com.petgang.commons.http.HttpUnit;
import com.petgang.commons.http.Result;

/**
 * Ï
 * 
 * @author bingying
 *
 */
public class FileUploadServer {

	private final String url = "";

	public Map<String, String> uploadMulti(Map<String, byte[]> fileMap) {

		Map<String, String> urlMap = Maps.newHashMap();
		if (MapUtils.isEmpty(fileMap)) {
			throw new IllegalArgumentException("arg fileMap is empty!!!");
		}
		try {
			Result result = null;
			ObjectNode node = null;
			for (Entry<String, byte[]> tmp : fileMap.entrySet()) {
				result = HttpUnit.post(url, tmp.getValue(), "file",
						RandomStringUtils.random(20, true, true), null);
				node = JsonUtil.convertObjectNode(result.getBody());
				urlMap.put(tmp.getKey(), node.get("url").asText());
			}
		} catch (Exception e) {
			throw new FileUploadException("File upload failed", e);
		}

		return urlMap;
	}

	public String getUrl() {
		return url;
	}

}
