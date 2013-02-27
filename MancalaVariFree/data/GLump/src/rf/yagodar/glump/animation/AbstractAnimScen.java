package rf.yagodar.glump.animation;

import java.util.ArrayList;

import rf.yagodar.glump.polygon.AbstractPolygon;
import android.util.Log;

public abstract class AbstractAnimScen implements Cloneable {
	public AbstractAnimScen(long animationTime) {
		this(null, animationTime);
	}
	
	public AbstractAnimScen(AbstractPolygon parentPolygon, long animationTime) {
		this(parentPolygon, animationTime, true);
	}
	
	protected AbstractAnimScen(AbstractPolygon parentPolygon, long animationTime, boolean writeAimations) {
		setAnimations(new ArrayList<AbstractAnim<?>>());
		animationIndx = 0;
		this.parentPolygon = parentPolygon;
		
		if(writeAimations) {
			if(!writeAnimations(animationTime)) {
				setAnimations(null);
				Log.e(LOG_TAG, "Error writing animations! [animationTime:(" + animationTime + ")]!");
			}
		}
	}
	
	@Override
	public AbstractAnimScen clone() {
		AbstractAnimScen clone = null;
		if(animations != null) {
			try {
				clone = (AbstractAnimScen) super.clone();

				ArrayList<AbstractAnim<?>> cloneAnimations = new ArrayList<AbstractAnim<?>>();
				for (AbstractAnim<?> animation : animations) {
					cloneAnimations.add(animation.clone());
				}
				clone.setAnimations(cloneAnimations);
			}
			catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}

		return clone;
	}
	
	public boolean applyNextStep(AbstractPolygon polygon) {
		if(animations != null && polygon != null) {
			try {
				if(animationIndx < animations.size()) {
					if(!animations.get(animationIndx).applyNextStep(polygon)) {
						animationIndx++;
					}

					return true;
				}
				else {
					animationIndx = 0;
					return false;
				}
			}
			catch(Exception e) {
				animationIndx = 0;
				return false;
			}
		}

		return false;
	}
	
	protected AbstractPolygon getParentPolygon() {
		return parentPolygon;
	}
	
	abstract protected boolean writeAnimations(long animationTime);
	
	protected void addAnimation(AbstractAnim<?> animation) {
		if(animations != null) {
			animation.setAnimationScenario(this);
			animations.add(animation);
		}
	}
	
	protected void setAnimations(ArrayList<AbstractAnim<?>> animations) {
		this.animations = animations;
	}
	
	private int animationIndx;
	private ArrayList<AbstractAnim<?>> animations;
	private AbstractPolygon parentPolygon;
	
	private static final String LOG_TAG = AbstractAnimScen.class.getSimpleName();
}
