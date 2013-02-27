package rf.yagodar.manqala.free.drawing.listener;

import rf.yagodar.glump.renderer.GLumpSVRendererQueue;
import rf.yagodar.glump.renderer.IGLumpSVRendererQueueListener;
import rf.yagodar.manqala.free.R;
import rf.yagodar.manqala.free.activity.ManqalaCombatVariActivity;
import rf.yagodar.manqala.free.database.ManqalaCombatVariDBManager;
import rf.yagodar.manqala.free.drawing.view.ManqalaCombatVariSV;
import rf.yagodar.manqala.free.logic.combat.ManqalaCombatVari;
import android.content.Context;
import android.content.SharedPreferences;

public class PlayerRestartRendererQueueListener implements IGLumpSVRendererQueueListener {
	public PlayerRestartRendererQueueListener(ManqalaCombatVariActivity activity, ManqalaCombatVariSV manqalaCombatVariSV, PlayerPauseRendererQueueListener playerPauseRendererQueueListener, ManqalaCombatVari manqalaCombatVari, PlayerRendererQueueListener playerRendererQueueListener, MonsterRendererQueueListener monsterRendererQueueListener) {
		this.activity = activity;
		this.manqalaCombatVariSV = manqalaCombatVariSV;
		this.playerPauseRendererQueueListener = playerPauseRendererQueueListener;
		this.manqalaCombatVari = manqalaCombatVari;
		this.playerRendererQueueListener = playerRendererQueueListener;
		this.monsterRendererQueueListener = monsterRendererQueueListener;
	}
	
	@Override
	public void onRendered() {
		if(manqalaCombatVariSV != null && playerPauseRendererQueueListener != null && manqalaCombatVari != null && playerRendererQueueListener != null) {
			manqalaCombatVariSV.interruptMainRenderThread();
			
			if(activity != null) {
				SharedPreferences sharedPref = activity.getApplicationContext().getSharedPreferences(activity.getString(R.string.shared_prefs_file_name), Context.MODE_PRIVATE);
				if(!sharedPref.getAll().isEmpty() && sharedPref.contains(activity.getString(R.string.pref_key_start_new_combat))) {
					manqalaCombatVari.start(sharedPref.getInt(activity.getString(R.string.pref_key_walketh), 0));
				}
			}
			
			manqalaCombatVari.restart();
			
			ManqalaCombatVariDBManager.getInstance().saveManqalaCombatVari(manqalaCombatVari);
			
			GLumpSVRendererQueue rendererQueue = new GLumpSVRendererQueue();
			rendererQueue.addListener(playerPauseRendererQueueListener);

			if(manqalaCombatVari.getWalketh().isPlayer()) {
				rendererQueue.addListener(playerRendererQueueListener);
			}
			else if(monsterRendererQueueListener != null) {
				rendererQueue.addListener(monsterRendererQueueListener);
			}

			rendererQueue.offerAllNodes(manqalaCombatVariSV.hidePausePlate());
			
			rendererQueue.offerAllNodes(manqalaCombatVariSV.drawInitSV(manqalaCombatVari));

			manqalaCombatVariSV.requestMainRender(rendererQueue);
		}
	}
	
	@Override
	public void onInterrupted() {}

	private ManqalaCombatVariSV manqalaCombatVariSV;
	private PlayerPauseRendererQueueListener playerPauseRendererQueueListener;
	private ManqalaCombatVari manqalaCombatVari;
	private PlayerRendererQueueListener playerRendererQueueListener;
	private MonsterRendererQueueListener monsterRendererQueueListener;
	private ManqalaCombatVariActivity activity;
}
