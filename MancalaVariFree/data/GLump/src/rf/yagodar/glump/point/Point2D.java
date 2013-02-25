package rf.yagodar.glump.point;
//TODO DOC
public class Point2D implements Cloneable {
	public Point2D(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public float getX() {
		return x;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public float getY() {
		return y;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	@Override
	public Point2D clone() throws CloneNotSupportedException {
		return (Point2D) super.clone();
	}
	
	@Override
	public String toString() {		
		return getClass().getSimpleName() + " [x:" + getX() + "] [y:" + getY() + "]";
	}

	private float x;
	private float y;
}
