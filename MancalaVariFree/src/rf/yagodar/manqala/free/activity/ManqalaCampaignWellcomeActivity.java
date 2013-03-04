package rf.yagodar.manqala.free.activity;

import rf.yagodar.manqala.free.ManqalaMediaPlayer;
import rf.yagodar.manqala.free.R;
import rf.yagodar.manqala.free.database.ManqalaCharactersDBManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ManqalaCampaignWellcomeActivity extends Activity {
	public void onClickHandler(View target) {
		switch(target.getId()) {
		case R.id.btn_cw_back:
			onBackPressed();
			break;
		case R.id.btn_cw_apply:
			EditText nameEditText = (EditText) findViewById(R.id.cw_enter_name_et);
			String newName = nameEditText.getText().toString();
			if(newName.length() == 0) {
				TextView nameErrTextView = (TextView) findViewById(R.id.cw_enter_name_err_tw);
				nameErrTextView.setText(R.string.enter_name_err_empty_label);
			}
			else if(ManqalaCharactersDBManager.getInstance().getCharacter(newName) != null) {
				TextView nameErrTextView = (TextView) findViewById(R.id.cw_enter_name_err_tw);
				nameErrTextView.setText(R.string.cw_enter_name_err_occupied_label);
			}
			else {
				ManqalaCharactersDBManager.getInstance().addCharacter(newName, false, true);
				startActivity(new Intent(this, ManqalaCampaignActivity.class));
				finish();
			}
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onBackPressed() {
		startActivity(new Intent(this, ManqalaMainMenuActivity.class));
		finish();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		ManqalaMediaPlayer.getInstance().pause();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.shared_prefs_file_name), Context.MODE_PRIVATE);
		if(!sharedPref.getAll().isEmpty() && sharedPref.contains(getString(R.string.pref_key_toogle_music))) {
			if(sharedPref.getBoolean(getString(R.string.pref_key_toogle_music), true)) {
				ManqalaMediaPlayer.getInstance().resume();
			}
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.campaign_welcome);
	}
}
