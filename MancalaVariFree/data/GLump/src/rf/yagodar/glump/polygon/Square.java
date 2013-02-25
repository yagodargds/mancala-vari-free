package rf.yagodar.glump.polygon;

import java.util.ArrayList;

import rf.yagodar.glump.point.Point2D;

public class Square extends Rectangle {
	public Square(Float levelZ, Float leftX, Float topY, Float sideLenght) {
		super(levelZ, new float[] { leftX, topY, sideLenght });
	}
	
	public Square(Float levelZ, Float alpha, Float leftX, Float topY, Float sideLenght) {
		super(levelZ, alpha, new float[] { leftX, topY, sideLenght });
	}
	
	public Square(Float levelZ, Float leftX, Float topY, Float sideLenght, Float redColor, Float greenColor, Float blueColor, Float alphaColor) {
		super(levelZ, new float[] { leftX, topY, sideLenght }, new float[] { redColor, greenColor, blueColor, alphaColor });
	}
	
	public Square(Float levelZ, Float alpha, Float leftX, Float topY, Float sideLenght, Float redColor, Float greenColor, Float blueColor, Float alphaColor) {
		super(levelZ, alpha, new float[] { leftX, topY, sideLenght }, new float[] { redColor, greenColor, blueColor, alphaColor });
	}

	public Square(Float levelZ, Integer bitmapId, Float leftX, Float topY, Float sideLenght) {		
		super(levelZ, bitmapId, new float[] { leftX, topY, sideLenght });
	}
	
	public Square(Float levelZ, Integer bitmapId, Float leftX, Float topY, Float sideLenght, Float leftS, Float topT, Float rightS, Float bottomT) {
		super(levelZ, bitmapId, new float[] { leftX, topY, sideLenght }, new float[] { leftS, topT, rightS, bottomT });
	}
	
	public Square(Float levelZ, Integer bitmapId, Float alpha, Float leftX, Float topY, Float sideLenght, Float leftS, Float topT, Float rightS, Float bottomT) {	
		super(levelZ, bitmapId, alpha, new float[] { leftX, topY, sideLenght }, new float[] { leftS, topT, rightS, bottomT });
	}

	@Override
	protected ArrayList<Point2D> calcPolygonPoints(float[] polygonPointsParams) {
		//leftX = polygonPointsParams[0]
		//topY = polygonPointsParams[1]
		//sideLenght = polygonPointsParams[2]
		return super.calcPolygonPoints(new float[] {	polygonPointsParams[0], 
														polygonPointsParams[1], 
														polygonPointsParams[0] + polygonPointsParams[2], 
														polygonPointsParams[1] - polygonPointsParams[2] });
	}
}