package ru.kserditov.vkmusic;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;

public class Login extends Activity {

	WebView mWebView;
	public final static String USER_ID = "ru.kserditov.HelloWorld2.USER_ID";
	public final static String ACCESS_TOKEN = "ru.kserditov.HelloWorld2.ACCESS_TOKEN";

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		mWebView = (WebView) findViewById(R.id.WebView);
		mWebView.setWebViewClient(new HelloWebViewClient());
		mWebView.getSettings().setJavaScriptEnabled(true);
		Log.i("Login_activity",
				"Start loading url " + Utils.getAuthenticationUrl());
		mWebView.loadUrl(Utils.getAuthenticationUrl());
	}

	private class HelloWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			Log.i("Login_activity", "shouldOverrideUrlLoading: " + url);
			if (url.startsWith("http://oauth.vk.com/blank.html")) {
				if (!url.contains("error=")) {
					String[] auth;
					try {
						auth = parseRedirectUrl(url);
					} catch (Exception e) {
						return true;
					}
					Intent answer = new Intent();
					answer.putExtra(ACCESS_TOKEN, auth[0]);
					answer.putExtra(USER_ID, auth[1]);
					setResult(Activity.RESULT_OK, answer);
					finish();
				}
			}
			return false;
		}

		public String[] parseRedirectUrl(String url) throws Exception {
			String access_token = extractPattern(url, "access_token=(.*?)&");
			String user_id = extractPattern(url, "user_id=(\\d*)");
			if (user_id == null || user_id.length() == 0
					|| access_token == null || access_token.length() == 0)
				throw new Exception("Failed to parse redirect url " + url);
			return new String[] { access_token, user_id };
		}

		public String extractPattern(String string, String pattern) {
			Pattern p = Pattern.compile(pattern);
			Matcher m = p.matcher(string);
			if (!m.find())
				return null;
			return m.toMatchResult().group(1);
		}

	}

}
