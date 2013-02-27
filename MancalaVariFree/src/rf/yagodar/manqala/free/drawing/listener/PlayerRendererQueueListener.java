package rf.yagodar.manqala.free.drawing.listener;

import rf.yagodar.glump.renderer.GLumpSVRendererQueue;
import rf.yagodar.glump.renderer.IGLumpSVRendererQueueListener;
import rf.yagodar.manqala.free.database.ManqalaCharactersDBManager;
import rf.yagodar.manqala.free.drawing.view.ManqalaCombatVariSV;
import rf.yagodar.manqala.free.logic.combat.ManqalaCombatVari;

public class PlayerRendererQueueListener implements IGLumpSVRendererQueueListener {
	public PlayerRendererQueueListener(ManqalaCombatVari manqalaCombatVari, PlayerPauseRendererQueueListener playerPauseRendererQueueListener, ManqalaCombatVariSV manqalaCombatVariSV) {
		setAllowTouchEvent(false);
		this.manqalaCombatVari = manqalaCombatVari;
		this.playerPauseRendererQueueListener = playerPauseRendererQueueListener;
		this.manqalaCombatVariSV = manqalaCombatVariSV;
	}
	
	@Override
	public void onRendered() {
		setAllowTouchEvent(true);
		
		if(manqalaCombatVari != null && !manqalaCombatVari.isCombating()) {
			if(manqalaCombatVari.getCompanyState() >= 0 && !manqalaCombatVari.isDraw() && manqalaCombatVari.getWinner().getCharName().equals(ManqalaCharactersDBManager.getInstance().getMasterName()) && ManqalaCharactersDBManager.getInstance().getMasterCompanyState() == manqalaCombatVari.getCompanyState()) {
				ManqalaCharactersDBManager.getInstance().incMasterCompanyState();
			}
			
			playerPauseRendererQueueListener.setAllowTouchEvent(false);

			manqalaCombatVariSV.pauseMainRender();

			GLumpSVRendererQueue rendererQueue = new GLumpSVRendererQueue();

			rendererQueue.addListener(playerPauseRendererQueueListener);
			rendererQueue.offerAllNodes(manqalaCombatVariSV.showGameOverPlate(manqalaCombatVari));

			manqalaCombatVariSV.requestAdditionalRender(rendererQueue);
		}
	}
	
	@Override
	public void onInterrupted() {}
	
	public void setAllowTouchEvent(boolean allowTouchEvent) {
		this.allowTouchEvent = allowTouchEvent;
	}
	
	public boolean isAllowTouchEvent() {
		return allowTouchEvent;
	}

	private boolean allowTouchEvent;
	private ManqalaCombatVari manqalaCombatVari;
	private PlayerPauseRendererQueueListener playerPauseRendererQueueListener;
	private ManqalaCombatVariSV manqalaCombatVariSV;
}
