package rf.yagodar.glump.animation;

import rf.yagodar.glump.polygon.AbstractPolygon;

public class ScaleAnimScen extends AbstractHomogenAnimScen<Float> {
	public ScaleAnimScen(Float[] nodes, long animationTime) {
		this(nodes, null, animationTime);
	}
	
	public ScaleAnimScen(Float[] nodes, AbstractPolygon parentPolygon, long animationTime) {
		super(nodes, parentPolygon, animationTime);
	}

	@Override
	protected float calcLenght(Float start, Float dest) {
		return Math.abs(start - dest);
	}

	@Override
	protected AbstractAnim<Float> createAnim(Float start, Float dest, long animationTime) {
		return new ScaleAnim(start, dest, animationTime);
	}
}
