package rf.yagodar.manqala.free.activity;

import rf.yagodar.manqala.free.ManqalaMediaPlayer;
import rf.yagodar.manqala.free.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioGroup;

public class ManqalaOptionsActivity extends Activity {
	public void onClickHandler(View target) {
		Editor sharedPrefsEditor = getApplicationContext().getSharedPreferences(getString(R.string.shared_prefs_file_name), Context.MODE_PRIVATE).edit();
		
		switch(target.getId()) {
		case R.id.btn_o_back:
			onBackPressed();
			break;
		case R.id.chbx_o_music:
			CheckBox checkBoxMusic = (CheckBox) findViewById(R.id.chbx_o_music);
			if(checkBoxMusic != null && checkBoxMusic.isChecked()) {
				sharedPrefsEditor.putBoolean(getString(R.string.pref_key_toogle_music), true);
				ManqalaMediaPlayer.getInstance().play(this);
			}
			else {
				sharedPrefsEditor.putBoolean(getString(R.string.pref_key_toogle_music), false);
				ManqalaMediaPlayer.getInstance().pause();
			}
			break;
		case R.id.rb_o_anim_speed_slow:
		case R.id.rb_o_anim_speed_medium:
		case R.id.rb_o_anim_speed_fast:
			RadioGroup radioGroupAnimSpeed = (RadioGroup) findViewById(R.id.rg_o_anim_speed);
			if(radioGroupAnimSpeed != null) {
				sharedPrefsEditor.putInt(getString(R.string.pref_key_anim_speed_rg_rb_id), radioGroupAnimSpeed.getCheckedRadioButtonId());
			}
			break;
		default:
			break;
		}
		
		sharedPrefsEditor.commit();
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
		
		CheckBox checkBoxMusic = (CheckBox) findViewById(R.id.chbx_o_music);
		if(checkBoxMusic != null) {
			if(!sharedPref.getAll().isEmpty() && sharedPref.contains(getString(R.string.pref_key_toogle_music))) {
				if(sharedPref.getBoolean(getString(R.string.pref_key_toogle_music), true)) {
					ManqalaMediaPlayer.getInstance().resume();
					checkBoxMusic.setChecked(true);
				}
				else {
					checkBoxMusic.setChecked(false);
				}
			}
			else {
				checkBoxMusic.setChecked(false);
			}
		}
		
		RadioGroup radioGroupAnimSpeed = (RadioGroup) findViewById(R.id.rg_o_anim_speed);
		if(radioGroupAnimSpeed != null) {
			radioGroupAnimSpeed.clearCheck();
			if(!sharedPref.getAll().isEmpty() && sharedPref.contains(getString(R.string.pref_key_anim_speed_rg_rb_id))) {
				radioGroupAnimSpeed.check(sharedPref.getInt(getString(R.string.pref_key_anim_speed_rg_rb_id), R.id.rb_o_anim_speed_medium));
			}
			else {
				radioGroupAnimSpeed.check(R.id.rb_o_anim_speed_medium);
			}
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.options);
	}
}
