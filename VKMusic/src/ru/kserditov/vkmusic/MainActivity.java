package ru.kserditov.vkmusic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

public class MainActivity extends Activity {

	static final private int GET_AUTHENTICATION_PARAMETERS = 0;

	Utils utils = new Utils();

	public boolean isAuthenticated = false;
	public TextView txtInfo;
	public EditText txtSearch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		txtInfo = (TextView) findViewById(R.id.txtInfo);
		txtSearch = (EditText) findViewById(R.id.txtSearch);

		if (!checkAuthentication()) {
			doLogin();
		} else {

		}

		txtSearch.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				boolean handled = false;
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					txtInfo.setText(getResources()
							.getString(R.string.searching));
					Log.i("MAIN_ACTIVITY",
							"Searching "
									+ utils.searchAudioUrl(txtSearch.getText()
											.toString()));
					GetJson getJson = new GetJson();
					Log.i("MAIN_ACTIVITY", "Get JSON");
					getJson.execute(new String[] { utils
							.searchAudioUrl(txtSearch.getText().toString()) });

					handled = true;
				}
				return handled;
			}
		});
		
		ViewGroup relativeLayout = (ViewGroup) findViewById(R.id.relativeLayoutID);

		Button bt = new Button(this);
		bt.setText("A Button");
		//bt.setLayoutParams(new LayoutParams());
		relativeLayout.addView(bt);

	}

	private boolean checkAuthentication() {
		return isAuthenticated;
	}

	private void doLogin() {
		Intent loginIntent = new Intent(this, Login.class);
		startActivityForResult(loginIntent, GET_AUTHENTICATION_PARAMETERS);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == GET_AUTHENTICATION_PARAMETERS) {
			if (resultCode == RESULT_OK) {
				utils.setUserId(data.getStringExtra(Login.USER_ID));
				utils.setAccessToken(data.getStringExtra(Login.ACCESS_TOKEN));

				isAuthenticated = true;
				txtInfo.setText(getResources().getString(R.string.auth_success));
			}
		}
	}

}
