package rf.yagodar.manqala.free.activity;

import rf.yagodar.manqala.free.ManqalaMediaPlayer;
import rf.yagodar.manqala.free.R;
import rf.yagodar.manqala.free.database.ManqalaCharactersDBManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class ManqalaMultiplayerActivity extends Activity {
	public void onClickHandler(View target) {
		Editor sharedPrefsEditor = getApplicationContext().getSharedPreferences(getString(R.string.shared_prefs_file_name), Context.MODE_PRIVATE).edit();
		
		switch(target.getId()) {
		case R.id.btn_mp_back:
			onBackPressed();
			break;
		case R.id.btn_mp_apply:
			EditText firstNameEditText = (EditText) findViewById(R.id.mp_enter_first_name_et);
			String firstName = firstNameEditText.getText().toString();
			
			EditText secondNameEditText = (EditText) findViewById(R.id.mp_enter_second_name_et);
			String secondName = secondNameEditText.getText().toString();
			
			if(firstName.length() == 0 || secondName.length() == 0) {
				if(firstName.length() == 0) {
					TextView firstNameErrTextView = (TextView) findViewById(R.id.mp_first_name_err);
					firstNameErrTextView.setText(R.string.err_label);
				}
				else {
					TextView firstNameErrTextView = (TextView) findViewById(R.id.mp_first_name_err);
					firstNameErrTextView.setText("");
				}

				if(secondName.length() == 0) {
					TextView secondNameErrTextView = (TextView) findViewById(R.id.mp_second_name_err);
					secondNameErrTextView.setText(R.string.err_label);
				}
				else {
					TextView secondNameErrTextView = (TextView) findViewById(R.id.mp_second_name_err);
					secondNameErrTextView.setText("");
				}
			}
			else {
				if(ManqalaCharactersDBManager.getInstance().getCharacter(firstName) == null) {
					ManqalaCharactersDBManager.getInstance().addCharacter(firstName, false, false);
				}
				
				if(ManqalaCharactersDBManager.getInstance().getCharacter(secondName) == null) {
					ManqalaCharactersDBManager.getInstance().addCharacter(secondName, false, false);
				}
				
				Spinner chooseGameboardSpinner = (Spinner) findViewById(R.id.mp_choose_gameboard_spinner);
				int selectedItemPos = chooseGameboardSpinner.getSelectedItemPosition();

				int sVKey;
				switch (selectedItemPos) {
				case 0:
				case AdapterView.INVALID_POSITION:
				default:
					sVKey = R.array.csv_air;
					break;
				case 1:
					sVKey = R.array.csv_earth;
					break;
				case 2:
					sVKey = R.array.csv_fire;
					break;
				}
				
				sharedPrefsEditor.putInt(getString(R.string.pref_key_company_state), -1);
				sharedPrefsEditor.putBoolean(getString(R.string.pref_key_start_new_combat), true);
				sharedPrefsEditor.putString(getString(R.string.pref_key_first_name), firstName);
				sharedPrefsEditor.putString(getString(R.string.pref_key_second_name), secondName);
				sharedPrefsEditor.putInt(getString(R.string.pref_key_s_v_key), sVKey);
				sharedPrefsEditor.putInt(getString(R.string.pref_key_walketh), 0);
				sharedPrefsEditor.putBoolean(getString(R.string.pref_key_combat_paused), false);
				startActivity(new Intent(this, ManqalaCombatVariActivity.class));
				finish();
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
		
		String[] data = null;
		int masterCompanyState = ManqalaCharactersDBManager.getInstance().getMasterCompanyState();
		if(masterCompanyState <= 0) {
			data = new String[] { getString(R.string.air_label) };
		}
		else if(masterCompanyState > 0 && masterCompanyState < 2) {
			data = new String[] { 	getString(R.string.air_label),
									getString(R.string.earth_label) };
		}
		else {
			data = new String[] { 	getString(R.string.air_label),
									getString(R.string.earth_label),
									getString(R.string.fire_label)	};
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		Spinner spinner = (Spinner) findViewById(R.id.mp_choose_gameboard_spinner);
		spinner.setAdapter(adapter);
		
		spinner.setSelection(0);
		
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
		setContentView(R.layout.multiplayer);
	}
}

