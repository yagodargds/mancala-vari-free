package rf.yagodar.glump.polygon;

import java.util.ArrayList;

import rf.yagodar.glump.point.Point2D;

public class Rectangle extends Quadrangle {
	public Rectangle(Float levelZ, Float leftX, Float topY, Float rightX, Float bottomY) {
		this(levelZ, new float[] { leftX, topY, rightX, bottomY });
	}
	
	public Rectangle(Float levelZ, Float alpha, Float leftX, Float topY, Float rightX, Float bottomY) {
		this(levelZ, alpha, new float[] { leftX, topY, rightX, bottomY });
	}
	
	public Rectangle(Float levelZ, Float leftX, Float topY, Float rightX, Float bottomY, Float redColor, Float greenColor, Float blueColor, Float alphaColor) {
		this(levelZ, new float[] { leftX, topY, rightX, bottomY }, new float[] { redColor, greenColor, blueColor, alphaColor });
	}
	
	public Rectangle(Float levelZ, Float alpha, Float leftX, Float topY, Float rightX, Float bottomY, Float redColor, Float greenColor, Float blueColor, Float alphaColor) {
		this(levelZ, alpha, new float[] { leftX, topY, rightX, bottomY }, new float[] { redColor, greenColor, blueColor, alphaColor });
	}

	public Rectangle(Float levelZ, Integer bitmapId, Float leftX, Float topY, Float rightX, Float bottomY) {		
		this(levelZ, bitmapId, new float[] { leftX, topY, rightX, bottomY });
	}
	
	public Rectangle(Float levelZ, Integer bitmapId, Float leftX, Float topY, Float rightX, Float bottomY, Float leftS, Float topT, Float rightS, Float bottomT) {
		this(levelZ, bitmapId, new float[] { leftX, topY, rightX, bottomY }, new float[] { leftS, topT, rightS, bottomT });
	}
	
	public Rectangle(Float levelZ, Integer bitmapId, Float alpha, Float leftX, Float topY, Float rightX, Float bottomY, Float leftS, Float topT, Float rightS, Float bottomT) {	
		this(levelZ, bitmapId, alpha, new float[] { leftX, topY, rightX, bottomY }, new float[] { leftS, topT, rightS, bottomT });
	}
		
	public float getLeftX() {
		Point2D point2D = getPolygonPoints().get(0);
		if(point2D != null) {
			return point2D.getX();
		}
		
		return 0.0f;
	}
	
	public float getTopY() {
		Point2D point2D = getPolygonPoints().get(0);
		if(point2D != null) {
			return point2D.getY();
		}
		
		return 0.0f;
	}
	
	public float getRightX() {
		Point2D point2D = getPolygonPoints().get(2);
		if(point2D != null) {
			return point2D.getX();
		}
		
		return 0.0f;
	}	
	
	public float getBottomY() {
		Point2D point2D = getPolygonPoints().get(2);
		if(point2D != null) {
			return point2D.getY();
		}
		
		return 0.0f;
	}
	
	public float getWidth() {
		return getRightX() - getLeftX();
	}
	
	public float getHeight() {
		return getTopY() - getBottomY();
	}
		
	@Override
	public boolean isContainsPoint(Point2D point2D) {		
		return getPolygonPoints().get(0).getX() <= point2D.getX() && getPolygonPoints().get(0).getY() >= point2D.getY() && getPolygonPoints().get(2).getX() >= point2D.getX() && getPolygonPoints().get(2).getY() <= point2D.getY();
	}
	
	@Override
	public Point2D getCentralPoint() {
		return new Point2D(getPolygonPoints().get(0).getX() + (getPolygonPoints().get(2).getX() - getPolygonPoints().get(0).getX()) / 2.0f, getPolygonPoints().get(0).getY() - (getPolygonPoints().get(0).getY() - getPolygonPoints().get(2).getY()) / 2.0f);
	}
	
	@Override
	public void move(float destX) {
		float halfWidth = getWidth() / 2.0f;
		
		setLeftX(destX - halfWidth);
		setRightX(destX + halfWidth);
	}
	
	@Override
	public void scale(float mult) {
		float scaledHalfWidth = (getWidth() / 2.0f) * mult;
		float scaledHalfHeight = (getHeight() / 2.0f) * mult;
		Point2D centralPoint = getCentralPoint();
		
		setLeftX(centralPoint.getX() - scaledHalfWidth);
		setTopY(centralPoint.getY() + scaledHalfHeight);
		setRightX(centralPoint.getX() + scaledHalfWidth);
		setBottomY(centralPoint.getY() - scaledHalfHeight);
	}
	
	protected Rectangle(float levelZ, float[] polygonPointsParams) {
		super(levelZ, polygonPointsParams);
	}
	
	protected Rectangle(float levelZ, float alpha, float[] polygonPointsParams) {
		super(levelZ, alpha, polygonPointsParams, null);
	}
	
	protected Rectangle(float levelZ, float[] polygonPointsParams, float[] polygonColorParams) {
		super(levelZ, polygonPointsParams, polygonColorParams);
	}
	
	protected Rectangle(float levelZ, float alpha, float[] polygonPointsParams, float[] polygonColorParams) {
		super(levelZ, alpha, polygonPointsParams, polygonColorParams);
	}
	
	protected Rectangle(float levelZ, int bitmapId, float[] polygonPointsParams) {		
		super(levelZ, bitmapId, polygonPointsParams);
	}
	
	protected Rectangle(float levelZ, int bitmapId, float[] polygonPointsParams, float[] texturePointsParams) {
		super(levelZ, bitmapId, polygonPointsParams, texturePointsParams);
	}
	
	protected Rectangle(float levelZ, int bitmapId, float alpha, float[] polygonPointsParams, float[] texturePointsParams) {	
		super(levelZ, bitmapId, alpha, polygonPointsParams, texturePointsParams);
	}
	
	@Override
	protected ArrayList<Point2D> calcPolygonPoints(float[] polygonPointsParams) {
		//leftX polygonPointsParams[0]
		//topY polygonPointsParams[1]
		//rightX polygonPointsParams[2]
		//bottomY polygonPointsParams[3]
		return super.calcPolygonPoints(new float[] {	polygonPointsParams[0], polygonPointsParams[1], 
														polygonPointsParams[2], polygonPointsParams[1], 
														polygonPointsParams[2], polygonPointsParams[3], 
														polygonPointsParams[0], polygonPointsParams[3] });
	}

	@Override
	protected ArrayList<Point2D> calcTexturePoints(float[] texturePointsParams) {
		if(texturePointsParams.length == 0) {
			return super.calcTexturePoints(texturePointsParams);
		}
		else {
			return super.calcTexturePoints(new float[] {	texturePointsParams[0], texturePointsParams[1], 
															texturePointsParams[2], texturePointsParams[1], 
															texturePointsParams[2], texturePointsParams[3], 
															texturePointsParams[0], texturePointsParams[3] });
		}
	}
	
	private void setLeftX(float newX) {
		getPolygonPoints().get(0).setX(newX);
		getPolygonPoints().get(3).setX(newX);
	}
	
	private void setTopY(float newY) {
		getPolygonPoints().get(0).setY(newY);
		getPolygonPoints().get(1).setY(newY);
	}
	
	private void setRightX(float newX) {
		getPolygonPoints().get(1).setX(newX);
		getPolygonPoints().get(2).setX(newX);
	}	
	
	private void setBottomY(float newY) {
		getPolygonPoints().get(2).setY(newY);
		getPolygonPoints().get(3).setY(newY);
	}
}
