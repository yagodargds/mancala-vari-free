package rf.yagodar.glump.polygon;

import rf.yagodar.glump.point.Point2D;


public class PolygonInfo implements Cloneable {
	//TODO optimize
	public PolygonInfo(Point2D centralPoint, float halfAvailableWidth, float halfAvailableHeight) {
		this.centralPoint = centralPoint;
		leftX = centralPoint.getX() - halfAvailableWidth;
		topY = centralPoint.getY() + halfAvailableHeight;
		rightX = centralPoint.getX() + halfAvailableWidth;
		bottomY = centralPoint.getY() - halfAvailableHeight;
		width = 2.0f * halfAvailableWidth;
		height = 2.0f * halfAvailableHeight;
	}

	public PolygonInfo(boolean alignLeft, float leftOrRightX, float topY, float width, float height) {	
		this(alignLeft, true, leftOrRightX, topY, width, height);
	}
	
	public PolygonInfo(boolean alignLeft, boolean alignTop, float leftOrRightX, float topOrBottomY, float width, float height) {		
		if(alignLeft) {			
			leftX = leftOrRightX;
			rightX = leftOrRightX + width;
		}
		else {
			leftX = leftOrRightX - width;
			rightX = leftOrRightX;
		}
		
		if(alignTop) {
			topY = topOrBottomY;
			bottomY = topY - height;
		}
		else {
			bottomY = topOrBottomY;
			topY = bottomY + height;
		}
		
		centralPoint = new Point2D(leftX + width / 2.0f, topY - height / 2.0f);
		
		this.width = width;
		this.height = height;
	}
	
	public Point2D getCentralPoint() {
		//TODO а нужно ли
		return centralPoint;
	}
	
	public float getLeftX() {
		return leftX;
	}

	public float getTopY() {
		return topY;
	}

	public float getRightX() {
		return rightX;
	}

	public float getBottomY() {
		return bottomY;
	}
	
	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}
	
	public void moveOn(float x, float y) {
		moveXOn(x);
		moveYOn(y);
	}
	
	public void moveXOn(float x) {
		leftX += x;
		rightX += x;
	}
	
	public void moveYOn(float y) {
		topY += y;
		bottomY += y;
	}
	
	@Override
	public PolygonInfo clone() throws CloneNotSupportedException {
		PolygonInfo polygonInfoClone = (PolygonInfo) super.clone();
		Point2D centralPointClone = getCentralPoint().clone();		
		polygonInfoClone.setCentralPoint(centralPointClone);		
		return polygonInfoClone;
	}
	
	@Override
	public String toString() {
		return PolygonInfo.class.getSimpleName() + ": leftX[" + leftX + "], topY[" + topY + "], rightX[" + rightX + "], bottomY[" + bottomY + "], width[" + width + "], height[" + height + "], centralPoint[" + centralPoint + "]";
	}
	
	protected void setCentralPoint(Point2D centralPoint) {
		//TODO а нужно ли
		this.centralPoint = centralPoint;
	}
	
	private Point2D centralPoint;
	private float leftX;
	private float topY;
	private float rightX;
	private float bottomY;
	private final float width;
	private final float height;
}
