package rf.yagodar.manqala.drawing.listener;

import rf.yagodar.glump.renderer.IGLumpSVRendererQueueListener;
import rf.yagodar.manqala.drawing.view.ManqalaCombatVariSV;

public class PlayerContinueRendererQueueListener implements IGLumpSVRendererQueueListener {
	public PlayerContinueRendererQueueListener(ManqalaCombatVariSV manqalaCombatVariSV) {
		this.manqalaCombatVariSV = manqalaCombatVariSV;
	}
	
	@Override
	public void onRendered() {
		if(manqalaCombatVariSV != null) {
			manqalaCombatVariSV.continueMainRender();
		}
	}

	private ManqalaCombatVariSV manqalaCombatVariSV;
}
