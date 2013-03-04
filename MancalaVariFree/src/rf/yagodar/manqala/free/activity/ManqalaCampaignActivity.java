package rf.yagodar.manqala.free.activity;

import rf.yagodar.manqala.free.ManqalaMediaPlayer;
import rf.yagodar.manqala.free.R;
import rf.yagodar.manqala.free.database.ManqalaCharactersDBManager;
import rf.yagodar.manqala.free.logic.combat.ManqalaOpponentsSet.Opponent;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ManqalaCampaignActivity extends Activity {
	public void onClickHandler(View target) {
		Editor sharedPrefsEditor = getApplicationContext().getSharedPreferences(getString(R.string.shared_prefs_file_name), Context.MODE_PRIVATE).edit();
		
		switch(target.getId()) {
		case R.id.btn_c_back:
			onBackPressed();
			break;
		case R.id.btn_c_st_1:
			sharedPrefsEditor.putInt(getString(R.string.pref_key_company_state), ManqalaCharactersDBManager.getInstance().getMasterCompanyState());
			sharedPrefsEditor.putBoolean(getString(R.string.pref_key_start_new_combat), true);
			sharedPrefsEditor.putString(getString(R.string.pref_key_first_name), getString(R.string.air_monster_name));
			sharedPrefsEditor.putString(getString(R.string.pref_key_second_name), ManqalaCharactersDBManager.getInstance().getMasterName());
			sharedPrefsEditor.putInt(getString(R.string.pref_key_s_v_key), R.array.csv_air);
			sharedPrefsEditor.putInt(getString(R.string.pref_key_walketh), Opponent.FIRST.ordinal());
			sharedPrefsEditor.putBoolean(getString(R.string.pref_key_combat_paused), false);
			startActivity(new Intent(this, ManqalaCombatVariActivity.class));
			finish();
			break;
		case R.id.btn_c_st_2:
			sharedPrefsEditor.putInt(getString(R.string.pref_key_company_state), ManqalaCharactersDBManager.getInstance().getMasterCompanyState());
			sharedPrefsEditor.putBoolean(getString(R.string.pref_key_start_new_combat), true);
			sharedPrefsEditor.putString(getString(R.string.pref_key_first_name), getString(R.string.earth_monster_name));
			sharedPrefsEditor.putString(getString(R.string.pref_key_second_name), ManqalaCharactersDBManager.getInstance().getMasterName());
			sharedPrefsEditor.putInt(getString(R.string.pref_key_s_v_key), R.array.csv_earth);
			sharedPrefsEditor.putInt(getString(R.string.pref_key_walketh), Opponent.FIRST.ordinal());
			sharedPrefsEditor.putBoolean(getString(R.string.pref_key_combat_paused), false);
			startActivity(new Intent(this, ManqalaCombatVariActivity.class));
			finish();
			break;
		case R.id.btn_c_st_3:
			sharedPrefsEditor.putInt(getString(R.string.pref_key_company_state), ManqalaCharactersDBManager.getInstance().getMasterCompanyState());
			sharedPrefsEditor.putBoolean(getString(R.string.pref_key_start_new_combat), true);
			sharedPrefsEditor.putString(getString(R.string.pref_key_first_name), getString(R.string.fire_monster_name));
			sharedPrefsEditor.putString(getString(R.string.pref_key_second_name), ManqalaCharactersDBManager.getInstance().getMasterName());
			sharedPrefsEditor.putInt(getString(R.string.pref_key_s_v_key), R.array.csv_fire);
			sharedPrefsEditor.putInt(getString(R.string.pref_key_walketh), Opponent.FIRST.ordinal());
			sharedPrefsEditor.putBoolean(getString(R.string.pref_key_combat_paused), false);
			startActivity(new Intent(this, ManqalaCombatVariActivity.class));
			finish();
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

		int masterCompanyState = ManqalaCharactersDBManager.getInstance().getMasterCompanyState();
		
		if(masterCompanyState <= 0) {
			Button btnSt2 = (Button) findViewById(R.id.btn_c_st_2);
			btnSt2.setEnabled(false);
			
			Button btnSt3 = (Button) findViewById(R.id.btn_c_st_3);
			btnSt3.setEnabled(false);
		}
		else if(masterCompanyState > 0 && masterCompanyState < 2) {
			Button btnSt2 = (Button) findViewById(R.id.btn_c_st_2);
			btnSt2.setEnabled(true);
			
			Button btnSt3 = (Button) findViewById(R.id.btn_c_st_3);
			btnSt3.setEnabled(false);
		}
		else {
			Button btnSt3 = (Button) findViewById(R.id.btn_c_st_3);
			btnSt3.setEnabled(true);
		}
		
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
		setContentView(R.layout.campaign);
	}
}
