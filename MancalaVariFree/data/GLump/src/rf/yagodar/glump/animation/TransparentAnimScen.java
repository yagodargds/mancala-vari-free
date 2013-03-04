package rf.yagodar.glump.animation;

public class TransparentAnimScen extends AbstractHomogenAnimScen<Float> {
	public TransparentAnimScen(Float[] nodes, long animationStep, long animationTime) {
		super(nodes, animationStep, animationTime);
	}

	@Override
	protected float calcLenght(Float start, Float dest) {
		return Math.abs(start - dest);
	}

	@Override
	protected AbstractAnim<Float> createAnim(Float start, Float dest, long animatonStep, long animationTime) {
		return new TransparentAnim(start, dest, animatonStep, animationTime);
	}

}
