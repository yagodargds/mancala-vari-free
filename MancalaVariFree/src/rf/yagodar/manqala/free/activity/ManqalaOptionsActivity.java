package rf.yagodar.manqala.free.activity;

import rf.yagodar.manqala.free.ManqalaMediaPlayer;
import rf.yagodar.manqala.free.R;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.ToggleButton;

public class ManqalaOptionsActivity extends Activity {
	public void onClickHandler(View target) {
		switch(target.getId()) {
		case R.id.btn_o_back:
			onBackPressed();
			break;
		case R.id.btn_o_toogle_music:
			ToggleButton toogleBtnMusic = (ToggleButton) findViewById(R.id.btn_o_toogle_music);
			
			Editor sharedPrefsEditor = getApplicationContext().getSharedPreferences(getString(R.string.shared_prefs_file_name), Context.MODE_PRIVATE).edit();
			if(toogleBtnMusic.isChecked()) {
				sharedPrefsEditor.putBoolean(getString(R.string.pref_key_toogle_music), true);
				ManqalaMediaPlayer.getInstance().play(this);
			}
			else {
				sharedPrefsEditor.putBoolean(getString(R.string.pref_key_toogle_music), false);
				ManqalaMediaPlayer.getInstance().pause();
			}
			
			sharedPrefsEditor.commit();
			
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		ManqalaMediaPlayer.getInstance().pause();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		ToggleButton toogleBtnMusic = (ToggleButton) findViewById(R.id.btn_o_toogle_music);
		
		SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.shared_prefs_file_name), Context.MODE_PRIVATE);
		if(!sharedPref.getAll().isEmpty() && sharedPref.contains(getString(R.string.pref_key_toogle_music))) {
			if(sharedPref.getBoolean(getString(R.string.pref_key_toogle_music), true)) {
				ManqalaMediaPlayer.getInstance().resume();
				toogleBtnMusic.setChecked(true);
			}
			else {
				toogleBtnMusic.setChecked(false);
			}
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.options);
	}
}
