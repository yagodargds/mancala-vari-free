package rf.yagodar.manqala.free.activity;

import rf.yagodar.manqala.free.ManqalaMediaPlayer;
import rf.yagodar.manqala.free.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class ManqalaCreditsActivity extends Activity {
	public void onClickHandler(View target) {
		switch(target.getId()) {
		case R.id.btn_cr_back:
			onBackPressed();
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
		setContentView(R.layout.credits);
	}
}
