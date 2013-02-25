package rf.yagodar.glump.animation;

import rf.yagodar.glump.polygon.AbstractPolygon;
import rf.yagodar.glump.view.GLumpSV;
import android.util.Log;

public abstract class AbstractAnim<T> implements Cloneable {
	public AbstractAnim(T start, T dest, long animationTime) {
		if(start != null && dest != null && checkAttrs(start, dest)) {
			stepIndx = 0;
			steps = calcSteps(start, dest, animationTime < GLumpSV.ANIMATION_STEP_MILISEC ? GLumpSV.ANIMATION_STEP_MILISEC : animationTime);
		}
		else {
			steps = null;
			Log.e(LOG_TAG, "Illegal init attr! [start:(" + start + "); dest:(" + dest + ")]!");
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public AbstractAnim<T> clone() throws CloneNotSupportedException {
		return (AbstractAnim<T>) super.clone();
	}
	
	protected void setAnimationScenario(AbstractAnimScen animationScenario) {
		this.animationScenario = animationScenario;
	}
	
	protected AbstractAnimScen getAnimationScenario() {
		return animationScenario;
	}
	
	protected AbstractPolygon getParentPolygon() {
		if(getAnimationScenario() != null) {
			return getAnimationScenario().getParentPolygon();
		}
		
		return null;
	}
	
	protected boolean applyNextStep(AbstractPolygon polygon) {
		if(steps != null) {
			if(stepIndx < steps.length) {
				applyStep(polygon, steps[stepIndx]);
				stepIndx++;
				return true;
			}
			else {
				stepIndx = 0;
				return false;
			}
		}

		return false;
	}
	
	abstract protected boolean checkAttrs(T start, T dest);
	
	abstract protected T[] calcSteps(T start, T dest, long animationTime);
	
	abstract protected void applyStep(AbstractPolygon polygon, T step);
	
	private int stepIndx;
	private final T[] steps;
	private AbstractAnimScen animationScenario;
	
	private static final String LOG_TAG = AbstractAnim.class.getSimpleName();
}