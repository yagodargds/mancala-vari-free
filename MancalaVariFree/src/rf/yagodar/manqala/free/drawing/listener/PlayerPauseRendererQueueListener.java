package rf.yagodar.manqala.free.drawing.listener;

import rf.yagodar.glump.renderer.IGLumpSVRendererQueueListener;

public class PlayerPauseRendererQueueListener implements IGLumpSVRendererQueueListener {
	public PlayerPauseRendererQueueListener() {
		setAllowTouchEvent(false);
	}
	
	@Override
	public void onRendered() {
		setAllowTouchEvent(true);
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
}
