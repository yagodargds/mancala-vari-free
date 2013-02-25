package rf.yagodar.manqala.free.drawing.listener;

import rf.yagodar.glump.renderer.GLumpSVRendererQueue;
import rf.yagodar.glump.renderer.IGLumpSVRendererQueueListener;
import rf.yagodar.manqala.free.database.ManqalaCombatVariDBManager;
import rf.yagodar.manqala.free.drawing.view.ManqalaCombatVariSV;
import rf.yagodar.manqala.free.logic.combat.ManqalaCombatVari;

public class PlayerRestartRendererQueueListener implements IGLumpSVRendererQueueListener {
	public PlayerRestartRendererQueueListener(ManqalaCombatVariSV manqalaCombatVariSV, PlayerPauseRendererQueueListener playerPauseRendererQueueListener, ManqalaCombatVari manqalaCombatVari, PlayerRendererQueueListener playerRendererQueueListener, MonsterRendererQueueListener monsterRendererQueueListener) {
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

			rendererQueue.offerAllNodes(manqalaCombatVariSV.drawInitSV(manqalaCombatVari));
			
			rendererQueue.offerAllNodes(manqalaCombatVariSV.hidePausePlate());

			manqalaCombatVariSV.requestMainRender(rendererQueue);
		}
	}

	private ManqalaCombatVariSV manqalaCombatVariSV;
	private PlayerPauseRendererQueueListener playerPauseRendererQueueListener;
	private ManqalaCombatVari manqalaCombatVari;
	private PlayerRendererQueueListener playerRendererQueueListener;
	private MonsterRendererQueueListener monsterRendererQueueListener;
}
