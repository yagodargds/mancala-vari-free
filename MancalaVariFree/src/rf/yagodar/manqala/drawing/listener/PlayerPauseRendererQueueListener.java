package rf.yagodar.manqala.drawing.listener;

import rf.yagodar.glump.renderer.IGLumpSVRendererQueueListener;

public class PlayerPauseRendererQueueListener implements IGLumpSVRendererQueueListener {
	public PlayerPauseRendererQueueListener() {
		setAllowTouchEvent(false);
	}
	
	@Override
	public void onRendered() {
		setAllowTouchEvent(true);
	}
	
	public void setAllowTouchEvent(boolean allowTouchEvent) {
		this.allowTouchEvent = allowTouchEvent;
	}
	
	public boolean isAllowTouchEvent() {
		return allowTouchEvent;
	}

	private boolean allowTouchEvent;
}
