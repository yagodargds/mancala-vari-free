package rf.yagodar.manqala.free.drawing.listener;

import rf.yagodar.glump.renderer.IGLumpSVRendererQueueListener;
import rf.yagodar.manqala.free.activity.ManqalaCombatVariActivity;

public class PlayerExitRendererQueueListener implements	IGLumpSVRendererQueueListener {
	public PlayerExitRendererQueueListener(ManqalaCombatVariActivity activity) {
		this.activity = activity;
	}
	
	@Override
	public void onRendered() {
		if(activity != null) {
			activity.finish();
		}
	}
	
	private ManqalaCombatVariActivity activity;
}
