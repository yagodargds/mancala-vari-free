package rf.yagodar.glump.point;

public class Point3D extends Point2D {
	public Point3D(float x, float y, float z) {
		super(x, y);
		this.z = z;
	}

	public float getZ() {
		return z;
	}
	
	public void setZ(float z) {
		this.z = z;
	}
	
	@Override
	public Point3D clone() throws CloneNotSupportedException {
		return (Point3D) super.clone();
	}
	
	@Override
	public String toString() {		
		return getClass().getSimpleName() + " [x:" + getX() + "] [y:" + getY() + "] [z:" + getZ()+ "]";
	}

	private float z;
}
