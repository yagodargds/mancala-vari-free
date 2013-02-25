package rf.yagodar.glump.animation;

public class TransparentAnimScen extends AbstractHomogenAnimScen<Float> {
	public TransparentAnimScen(Float[] nodes, long animationTime) {
		super(nodes, animationTime);
	}

	@Override
	protected float calcLenght(Float start, Float dest) {
		return Math.abs(start - dest);
	}

	@Override
	protected AbstractAnim<Float> createAnim(Float start, Float dest, long animationTime) {
		return new TransparentAnim(start, dest, animationTime);
	}

}
