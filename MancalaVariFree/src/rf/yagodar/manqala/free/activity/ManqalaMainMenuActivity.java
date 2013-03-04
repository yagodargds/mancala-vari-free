package rf.yagodar.manqala.free.activity;

import rf.yagodar.glump.bitmap.ResBitmapManager;
import rf.yagodar.manqala.free.ManqalaMediaPlayer;
import rf.yagodar.manqala.free.R;
import rf.yagodar.manqala.free.database.DBProvider;
import rf.yagodar.manqala.free.database.ManqalaCharactersDBManager;
import rf.yagodar.manqala.free.database.ManqalaCombatVariDBManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.TypedArray;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ManqalaMainMenuActivity extends Activity {	
	public void onClickHandler(View target) {
		Editor sharedPrefsEditor = getApplicationContext().getSharedPreferences(getString(R.string.shared_prefs_file_name), Context.MODE_PRIVATE).edit();
		
		switch(target.getId()) {
		case R.id.btn_mm_continue:
			sharedPrefsEditor.putBoolean(getString(R.string.pref_key_start_new_combat), false);
			sharedPrefsEditor.putBoolean(getString(R.string.pref_key_combat_paused), false);
			startActivity(new Intent(this, ManqalaCombatVariActivity.class));
			break;
		case R.id.btn_mm_campaign:
			if(ManqalaCharactersDBManager.getInstance().getMaster() == null) {
				startActivity(new Intent(this, ManqalaCampaignWellcomeActivity.class));
			}
			else {
				startActivity(new Intent(this, ManqalaCampaignActivity.class));
			}
			break;
		case R.id.btn_mm_fastgame:
			sharedPrefsEditor.putInt(getString(R.string.pref_key_company_state), -1);
			sharedPrefsEditor.putBoolean(getString(R.string.pref_key_start_new_combat), true);
			sharedPrefsEditor.putString(getString(R.string.pref_key_first_name), getString(R.string.phone_monster_name));
			sharedPrefsEditor.putString(getString(R.string.pref_key_second_name), getString(R.string.master_player_name));
			sharedPrefsEditor.putInt(getString(R.string.pref_key_s_v_key), R.array.csv_air);
			sharedPrefsEditor.putInt(getString(R.string.pref_key_walketh), 0);
			sharedPrefsEditor.putBoolean(getString(R.string.pref_key_combat_paused), false);
			startActivity(new Intent(this, ManqalaCombatVariActivity.class));
			break;
		case R.id.btn_mm_multiplayer:
			startActivity(new Intent(this, ManqalaMultiplayerActivity.class));
			break;
		case R.id.btn_mm_tutorial:
			startActivity(new Intent(this, ManqalaTutorialActivity.class));
			break;
		case R.id.btn_mm_options:
			startActivity(new Intent(this, ManqalaOptionsActivity.class));
			break;
		case R.id.btn_mm_credits:
			startActivity(new Intent(this, ManqalaCreditsActivity.class));
			break;
		case R.id.btn_mm_exit:
			break;
		default:
			break;
		}
		
		sharedPrefsEditor.commit();
		
		onBackPressed();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		ManqalaMediaPlayer.getInstance().pause();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		Button btnContinue = (Button) findViewById(R.id.btn_mm_continue);
		if(ManqalaCombatVariDBManager.getInstance().isManqalaCombatVariSaved()) {
			btnContinue.setEnabled(true);
		}
		else {
			btnContinue.setEnabled(false);
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
		
		loadResources();
		
		setContentView(R.layout.main_menu);


		if(ManqalaActivityManager.getInstance().isCombatActivityExists()) {
			SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.shared_prefs_file_name), Context.MODE_PRIVATE);
			if(!sharedPref.getAll().isEmpty() && sharedPref.contains(getString(R.string.pref_key_combat_paused)) && sharedPref.getBoolean(getString(R.string.pref_key_combat_paused), false)) {
				onBackPressed();
			}
		}
		else {
			Editor sharedPrefsEditor = getApplicationContext().getSharedPreferences(getString(R.string.shared_prefs_file_name), Context.MODE_PRIVATE).edit();
			sharedPrefsEditor.putBoolean(getString(R.string.pref_key_combat_paused), false);
			sharedPrefsEditor.commit();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		DBProvider.getInstance().close();
	}
	
	@Override
	public void onBackPressed() {
		finish();
	}
	
	private void loadResources() {
		loadSoundtrack();
		
		DBProvider.createNewInstance(this);
		
		loadTextures();
	}
	
	private void loadTextures() {
		if(!ResBitmapManager.getInstance().isLoaded()) {
			TypedArray textures = getResources().obtainTypedArray(R.array.textures_to_load);
			int drawableResId;
			for(int i = 0; i < textures.length(); i++) {
				drawableResId = textures.getResourceId(i, 0);
				ResBitmapManager.getInstance().addToBitmapProvider(drawableResId, BitmapFactory.decodeResource(getResources(), drawableResId));
			}
			textures.recycle();
			ResBitmapManager.getInstance().setLoaded(true);
		}
	}
	
	private void loadSoundtrack() {
		SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.shared_prefs_file_name), Context.MODE_PRIVATE);
		if(!sharedPref.getAll().isEmpty() && sharedPref.contains(getString(R.string.pref_key_toogle_music))) {
			if(sharedPref.getBoolean(getString(R.string.pref_key_toogle_music), true)) {
				ManqalaMediaPlayer.getInstance().play(this);
			}
		}
		else {
			Editor sharedPrefsEditor = getApplicationContext().getSharedPreferences(getString(R.string.shared_prefs_file_name), Context.MODE_PRIVATE).edit();
			sharedPrefsEditor.putBoolean(getString(R.string.pref_key_toogle_music), true);
			sharedPrefsEditor.commit();
			
			ManqalaMediaPlayer.getInstance().play(this);
		}
	}
}
