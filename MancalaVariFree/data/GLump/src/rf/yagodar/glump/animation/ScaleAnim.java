package rf.yagodar.glump.animation;

import rf.yagodar.glump.polygon.AbstractPolygon;
import rf.yagodar.glump.view.GLumpSV;

public class ScaleAnim extends AbstractAnim<Float> {
	public ScaleAnim(Float dest, long animationTime) {
		this(1.0f, dest, animationTime);
	}
	
	public ScaleAnim(Float start, Float dest, long animationTime) {
		super(start, dest, animationTime);
	}

	@Override
	protected boolean checkAttrs(Float start, Float dest) {
		if(start > 0 && dest > 0) {
			return true;
		}
		
		return false;
	}
	
	@Override
	protected Float[] calcSteps(Float start, Float dest, long animationTime) {
		Float[] steps = new Float[(int) Math.floor((double) animationTime / (double) GLumpSV.ANIMATION_STEP_MILISEC)];
		
		float stepPart = (dest - start) / (float) steps.length;
		for(int i = 0; i < steps.length; i++) {
			steps[i] = 1 + stepPart / (start + i * stepPart);
		}
				
		return steps;
	}

	@Override
	protected void applyStep(AbstractPolygon polygon, Float step) {
		if(getParentPolygon() != null && !getParentPolygon().equals(polygon)) {
			polygon.move((1 - step) * getParentPolygon().getCentralPoint().getX() + step * polygon.getCentralPoint().getX());
		}
		
		polygon.scale(step);
	}
}