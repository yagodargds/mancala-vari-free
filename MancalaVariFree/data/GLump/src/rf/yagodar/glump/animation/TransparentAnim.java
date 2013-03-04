package rf.yagodar.glump.animation;

import rf.yagodar.glump.polygon.AbstractPolygon;

public class TransparentAnim extends AbstractAnim<Float> {
	public TransparentAnim(Float start, Float dest, long animatonStep, long animationTime) {
		super(start, dest, animatonStep, animationTime);
	}

	@Override
	protected boolean checkAttrs(Float start, Float dest) {
		if(start >= 0.0f && start <= 1.0f && dest >= 0.0f && start <= 1.0f) {
			return true;
		}
		
		return false;
	}

	@Override
	protected Float[] calcSteps(Float start, Float dest, long animatonStep, long animationTime) {
		Float[] steps = new Float[(int) Math.floor((double) animationTime / (double) animatonStep)];
		
		float stepPart = (dest - start) / (float) steps.length;
		for(int i = 0; i < steps.length; i++) {
			steps[i] = start + (i + 1) * stepPart;
		}
				
		return steps;
	}

	@Override
	protected void applyStep(AbstractPolygon polygon, Float step) {
		polygon.setAlpha(step);
	}
}
