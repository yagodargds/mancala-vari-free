package rf.yagodar.glump.animation;

import rf.yagodar.glump.polygon.AbstractPolygon;

public class ScaleAnim extends AbstractAnim<Float> {
	public ScaleAnim(Float dest, long animatonStep, long animationTime) {
		this(1.0f, dest, animatonStep, animationTime);
	}
	
	public ScaleAnim(Float start, Float dest, long animatonStep, long animationTime) {
		super(start, dest, animatonStep, animationTime);
	}

	@Override
	protected boolean checkAttrs(Float start, Float dest) {
		if(start > 0 && dest > 0) {
			return true;
		}
		
		return false;
	}
	
	@Override
	protected Float[] calcSteps(Float start, Float dest, long animatonStep, long animationTime) {
		Float[] steps = new Float[(int) Math.floor((double) animationTime / (double) animatonStep)];
		
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