package rf.yagodar.glump.animation;

import rf.yagodar.glump.polygon.AbstractPolygon;

public class HomogenAnimScenBuilder {
	public static AbstractHomogenAnimScen<?> generateScaleAnimScen(Float[] nodes, long animationStep, long animationTime) {
		return generateScaleAnimScen(nodes, null, animationStep, animationTime);
	}
	
	public static AbstractHomogenAnimScen<?> generateScaleAnimScen(Float[] nodes, AbstractPolygon parentPolygon, long animationStep, long animationTime) {
		return new ScaleAnimScen(nodes, parentPolygon, animationStep, animationTime);
	}
	
	public static AbstractHomogenAnimScen<?> generateTransparentAnimScen(Float[] nodes, long animationStep, long animationTime) {
		return new TransparentAnimScen(nodes, animationStep, animationTime);
	}
}
