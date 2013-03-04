package rf.yagodar.manqala.free.drawing.listener;

import rf.yagodar.glump.renderer.IGLumpSVRendererQueueListener;
import rf.yagodar.manqala.free.R;
import rf.yagodar.manqala.free.activity.ManqalaCampaignActivity;
import rf.yagodar.manqala.free.activity.ManqalaCombatVariActivity;
import rf.yagodar.manqala.free.activity.ManqalaMainMenuActivity;
import rf.yagodar.manqala.free.logic.combat.ManqalaCombatVari;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;

public class PlayerExitRendererQueueListener implements	IGLumpSVRendererQueueListener {
	public PlayerExitRendererQueueListener(ManqalaCombatVariActivity activity, ManqalaCombatVari manqalaCombatVari) {
		this.activity = activity;
		this.manqalaCombatVari = manqalaCombatVari;
	}
	
	@Override
	public void onRendered() {
		if(activity != null) {
			activity.finish();
			
			if(manqalaCombatVari != null && manqalaCombatVari.getCompanyState() != -1) {
				activity.startActivity(new Intent(activity, ManqalaCampaignActivity.class));	
			}
			else {
				activity.startActivity(new Intent(activity, ManqalaMainMenuActivity.class));
			}
			
			Editor sharedPrefsEditor = activity.getApplicationContext().getSharedPreferences(activity.getString(R.string.shared_prefs_file_name), Context.MODE_PRIVATE).edit();
			sharedPrefsEditor.putBoolean(activity.getString(R.string.pref_key_combat_paused), false);
			sharedPrefsEditor.commit();
		}
	}
	
	@Override
	public void onInterrupted() {}
	
	private ManqalaCombatVariActivity activity;
	private ManqalaCombatVari manqalaCombatVari;
}
