package rf.yagodar.glump.polygon;
//TODO DOC
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.util.ArrayList;

import rf.yagodar.glump.point.Point2D;

public class RegularPolygon extends AbstractPolygon {			
	public RegularPolygon(float levelZ, float centerX, float centerY, float radius, float sidesCount) {
		super(levelZ, new float[] { centerX, centerY, radius, sidesCount });
	}
	
	public RegularPolygon(float levelZ, int bitmapId, boolean textPolygon, float centerX, float centerY, float radius, float sidesCount, float textureCoordsMult) {
		super(levelZ, bitmapId, textPolygon, new float[] { centerX, centerY, radius, sidesCount }, new float[] { sidesCount, textureCoordsMult });
	}

	@Override
	public boolean isContainsPoint(float x, float y) {
		// TODO isContainsPoint
		return false;
	}

	@Override
	public Point getCentralPoint() {
		return new Point(getPolygonPoints().get(0).getX(), getPolygonPoints().get(0).getY());
	}
	
	@Override
	protected ArrayList<Point> calcPolygonPoints(float[] polygonPointsParams) {
		ArrayList<Point> polygonPoints = new ArrayList<Point>();
		
		//central point
		polygonPoints.add(new Point(polygonPointsParams[0], polygonPointsParams[1]));

		//radius = polygonPointsParams[2]
		//sidesCount = (int) polygonPointsParams[3];
		
		ArrayList<Point> multFigurePoints = getMultPolygonPoints((int) polygonPointsParams[3]);
		for (Point multPoint2D : multFigurePoints) {
			polygonPoints.add(new Point(polygonPointsParams[0] + polygonPointsParams[2] * multPoint2D.getX(), polygonPointsParams[1] + polygonPointsParams[2] * multPoint2D.getY()));
		}
		
		return polygonPoints;
	}

	@Override
	protected int calcNumberOfIndecies() {
		return getSidesCount() * TRIANGLE_INDEX_SIZE;
	}
	
	@Override
	protected ShortBuffer calcIndexBuffer() {		
		ByteBuffer byIndexBuffer = ByteBuffer.allocateDirect(SH_INDEX_BYTE_CAPACITY * getNumberOfIndecies());
		byIndexBuffer.order(ByteOrder.nativeOrder());
		
		ShortBuffer shIndexBuffer = byIndexBuffer.asShortBuffer();
		
		final short index1 = 0;
		short index3;		
		for (int i = 0; i < getSidesCount(); i++) {
			shIndexBuffer.put(index1);
			
			shIndexBuffer.put((short) (i + 1));
			
			index3 = (short) (i + 2);			
			if (index3 == getSidesCount() + 1) {
				index3 = 1;
			}
			
			shIndexBuffer.put(index3);
		}

		shIndexBuffer.position(0);
		
		return shIndexBuffer;
	}	

	@Override
	protected ArrayList<Point> calcTexturePoints(float[] texturePointsParams) {		
		ArrayList<Point> texturePoints = new ArrayList<Point>();
		
		//textureParam = texturePointsParams[1];
		texturePoints.add(new Point(texturePointsParams[1], texturePointsParams[1]));
		
		//sidesCount = (int) texturePointsParams[0];
		ArrayList<Point> multFigurePoints = getMultPolygonPoints((int) texturePointsParams[0]);
		for (Point point : multFigurePoints) {
			texturePoints.add(new Point(texturePointsParams[1] + texturePointsParams[1] * point.getX(), texturePointsParams[1] + texturePointsParams[1] * point.getY()));
		}
		
		return texturePoints;
	}

	private ArrayList<Point> getMultPolygonPoints(int sidesCount) {
		ArrayList<Point> multPolygonPoints = new ArrayList<Point>();
		
		float[] polygonAngles = getPolygonAngles(sidesCount);		
		for(int i = 0; i < polygonAngles.length; i++) {
			multPolygonPoints.add(new Point((float) Math.cos(Math.toRadians(polygonAngles[i])), (float) Math.sin(Math.toRadians(polygonAngles[i]))));
		}
		
		return multPolygonPoints;
	}

	private float[] getPolygonAngles(int sidesCount) {
		float[] polygonAngles = new float[sidesCount];
		
		float commonAngle = FULL_CIRCLE_DEGREES / polygonAngles.length;		
		float firstAngle = FULL_CIRCLE_DEGREES - (QUARTER_CIRCLE_DEGREES + commonAngle / HALF_DIVIDER);		
		
		float curAngle = firstAngle;
		
		polygonAngles[0] = curAngle;
		for(int i = 1; i < polygonAngles.length; i++) {
			curAngle -= commonAngle;
			polygonAngles[i] = curAngle;
		}

		return polygonAngles;
	}
		
	private int getSidesCount() {
		return getPolygonPoints().size() - 1;
	}
	
	private final static float FULL_CIRCLE_DEGREES = 360.0f;
	private final static float QUARTER_CIRCLE_DEGREES = 90.0f;
	private final static float HALF_DIVIDER = 2.0f;	
}
