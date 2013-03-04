package rf.yagodar.glump.animation;

import rf.yagodar.glump.polygon.AbstractPolygon;

public class ScaleAnimScen extends AbstractHomogenAnimScen<Float> {
	public ScaleAnimScen(Float[] nodes, long animatonStep, long animationTime) {
		this(nodes, null, animatonStep, animationTime);
	}
	
	public ScaleAnimScen(Float[] nodes, AbstractPolygon parentPolygon, long animatonStep, long animationTime) {
		super(nodes, parentPolygon, animatonStep, animationTime);
	}

	@Override
	protected float calcLenght(Float start, Float dest) {
		return Math.abs(start - dest);
	}

	@Override
	protected AbstractAnim<Float> createAnim(Float start, Float dest, long animatonStep, long animationTime) {
		return new ScaleAnim(start, dest, animatonStep, animationTime);
	}
}
