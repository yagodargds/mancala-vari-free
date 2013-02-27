package rf.yagodar.manqala.free.drawing.listener;

import rf.yagodar.glump.renderer.IGLumpSVRendererQueueListener;
import rf.yagodar.manqala.free.activity.ManqalaCombatVariActivity;
import rf.yagodar.manqala.free.database.ManqalaCharactersDBManager;
import rf.yagodar.manqala.free.logic.combat.ManqalaCombatVari;

public class PlayerRendererQueueListener implements IGLumpSVRendererQueueListener {
	public PlayerRendererQueueListener(ManqalaCombatVariActivity activity, ManqalaCombatVari manqalaCombatVari) {
		setAllowTouchEvent(false);
		this.activity = activity;
		this.manqalaCombatVari = manqalaCombatVari;
	}
	
	@Override
	public void onRendered() {
		setAllowTouchEvent(true);
		
		if(manqalaCombatVari != null && !manqalaCombatVari.isCombating()) {
			if(manqalaCombatVari.getCompanyState() >= 0 && !manqalaCombatVari.isDraw() && manqalaCombatVari.getWinner().getCharName().equals(ManqalaCharactersDBManager.getInstance().getMasterName()) && ManqalaCharactersDBManager.getInstance().getMasterCompanyState() == manqalaCombatVari.getCompanyState()) {
				ManqalaCharactersDBManager.getInstance().incMasterCompanyState();
			}
			
			if(activity != null) {
				activity.showPausePlate();
			}
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

	private ManqalaCombatVariActivity activity;
	private boolean allowTouchEvent;
	private ManqalaCombatVari manqalaCombatVari;
}
