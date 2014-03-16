package ru.kserditov.vkmusic;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class Utils {

	public static final String redirectUrl = "http://oauth.vk.com/blank.html";
	public static final String authUrl = "http://oauth.vk.com/authorize";
	public static final int clientId = 4029909;
	public static final int scope = 8;

	private int userId;
	private String accessToken;

	public static String getAuthenticationUrl() {
		return authUrl + "?client_id=" + Integer.toString(clientId) + "&scope="
				+ Integer.toString(scope) + "&redirect_uri=" + redirectUrl
				+ "&response_type=token";
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = Integer.parseInt(userId);
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String searchAudioUrl(String request) {
		try {
			return "https://api.vk.com/method/audio.search?user_id=" + userId
					+ "&v=5.5&access_token=" + accessToken + "&q="
					+ URLEncoder.encode(request, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}

	public static String sanitizeFilename(String name) {
		return name.replaceAll("[:\\\\/*?|<>]", "_");
	}


}
