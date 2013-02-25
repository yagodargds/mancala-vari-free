package rf.yagodar.glump.animation;

import rf.yagodar.glump.polygon.AbstractPolygon;

public class HomogenAnimScenBuilder {
	public static AbstractHomogenAnimScen<?> generateScaleAnimScen(Float[] nodes, long animationTime) {
		return generateScaleAnimScen(nodes, null, animationTime);
	}
	
	public static AbstractHomogenAnimScen<?> generateScaleAnimScen(Float[] nodes, AbstractPolygon parentPolygon, long animationTime) {
		return new ScaleAnimScen(nodes, parentPolygon, animationTime);
	}
	
	public static AbstractHomogenAnimScen<?> generateTransparentAnimScen(Float[] nodes, long animationTime) {
		return new TransparentAnimScen(nodes, animationTime);
	}
}
