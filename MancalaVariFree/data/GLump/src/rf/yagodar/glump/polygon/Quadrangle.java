package rf.yagodar.glump.polygon;
//TODO DOC
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.util.ArrayList;

import rf.yagodar.glump.point.Point2D;

public class Quadrangle extends AbstractPolygon {
	public Quadrangle(Float levelZ, Float leftTopX, Float leftTopY, Float rightTopX, Float rightTopY, Float rightBottomX, Float rightBottomY, Float leftBottomX, Float leftBottomY) {
		this(levelZ, new float[] { leftTopX, leftTopY, rightTopX, rightTopY, rightBottomX, rightBottomY, leftBottomX, leftBottomY });
	}
	
	public Quadrangle(Float levelZ, Float alpha, Float leftTopX, Float leftTopY, Float rightTopX, Float rightTopY, Float rightBottomX, Float rightBottomY, Float leftBottomX, Float leftBottomY) {
		this(levelZ, alpha, new float[] { leftTopX, leftTopY, rightTopX, rightTopY, rightBottomX, rightBottomY, leftBottomX, leftBottomY });
	}
	
	public Quadrangle(Float levelZ, Float leftTopX, Float leftTopY, Float rightTopX, Float rightTopY, Float rightBottomX, Float rightBottomY, Float leftBottomX, Float leftBottomY, Float redColor, Float greenColor, Float blueColor, Float alphaColor) {
		this(levelZ, new float[] { leftTopX, leftTopY, rightTopX, rightTopY, rightBottomX, rightBottomY, leftBottomX, leftBottomY }, new float[] { redColor, greenColor, blueColor, alphaColor });
	}
	
	public Quadrangle(Float levelZ, Float alpha, Float leftTopX, Float leftTopY, Float rightTopX, Float rightTopY, Float rightBottomX, Float rightBottomY, Float leftBottomX, Float leftBottomY, Float redColor, Float greenColor, Float blueColor, Float alphaColor) {
		this(levelZ, alpha, new float[] { leftTopX, leftTopY, rightTopX, rightTopY, rightBottomX, rightBottomY, leftBottomX, leftBottomY }, new float[] { redColor, greenColor, blueColor, alphaColor });
	}

	public Quadrangle(Float levelZ, Integer bitmapId, Float leftTopX, Float leftTopY, Float rightTopX, Float rightTopY, Float rightBottomX, Float rightBottomY, Float leftBottomX, Float leftBottomY) {		
		this(levelZ, bitmapId, new float[] { leftTopX, leftTopY, rightTopX, rightTopY, rightBottomX, rightBottomY, leftBottomX, leftBottomY });
	}
	
	public Quadrangle(Float levelZ, Integer bitmapId, Float leftTopX, Float leftTopY, Float rightTopX, Float rightTopY, Float rightBottomX, Float rightBottomY, Float leftBottomX, Float leftBottomY, Float leftTopS, Float leftTopT, Float rightTopS, Float rightTopT, Float rightBottomS, Float rightBottomT, Float leftBottomS, Float leftBottomT) {
		this(levelZ, bitmapId, new float[] { leftTopX, leftTopY, rightTopX, rightTopY, rightBottomX, rightBottomY, leftBottomX, leftBottomY }, new float[] { leftTopS, leftTopT, rightTopS, rightTopT, rightBottomS, rightBottomT, leftBottomS, leftBottomT });
	}
	
	public Quadrangle(Float levelZ, Integer bitmapId, Float alpha, Float leftTopX, Float leftTopY, Float rightTopX, Float rightTopY, Float rightBottomX, Float rightBottomY, Float leftBottomX, Float leftBottomY, Float leftTopS, Float leftTopT, Float rightTopS, Float rightTopT, Float rightBottomS, Float rightBottomT, Float leftBottomS, Float leftBottomT) {	
		this(levelZ, bitmapId, alpha, new float[] { leftTopX, leftTopY, rightTopX, rightTopY, rightBottomX, rightBottomY, leftBottomX, leftBottomY }, new float[] { leftTopS, leftTopT, rightTopS, rightTopT, rightBottomS, rightBottomT, leftBottomS, leftBottomT });
	}
	
	@Override
	public boolean isContainsPoint(float x, float y) {
		// TODO calc
		return false;
	}	
	
	@Override
	public Point2D getCentralPoint() {
		//TODO calc
		return null;
	}

	@Override
	public void move(float destX) {
		// TODO calc
	}

	@Override
	public void scale(float mult) {
		// TODO calc
	}
	
	protected Quadrangle(float levelZ, float[] polygonPointsParams) {
		super(levelZ, polygonPointsParams);
	}
	
	protected Quadrangle(float levelZ, float alpha, float[] polygonPointsParams) {
		super(levelZ, alpha, polygonPointsParams, null);
	}
	
	protected Quadrangle(float levelZ, float[] polygonPointsParams, float[] polygonColorParams) {
		super(levelZ, polygonPointsParams, polygonColorParams);
	}
	
	protected Quadrangle(float levelZ, float alpha, float[] polygonPointsParams, float[] polygonColorParams) {
		super(levelZ, alpha, polygonPointsParams, polygonColorParams);
	}
	
	protected Quadrangle(float levelZ, int bitmapId, float[] polygonPointsParams) {		
		super(levelZ, bitmapId, polygonPointsParams);
	}
	
	protected Quadrangle(float levelZ, int bitmapId, float[] polygonPointsParams, float[] texturePointsParams) {
		super(levelZ, bitmapId, polygonPointsParams, texturePointsParams);
	}
	
	protected Quadrangle(float levelZ, int bitmapId, float alpha, float[] polygonPointsParams, float[] texturePointsParams) {	
		super(levelZ, bitmapId, alpha, polygonPointsParams, texturePointsParams);
	}
	
	@Override
	protected ArrayList<Point2D> calcPolygonPoints(float[] polygonPointsParams) {
		ArrayList<Point2D> polygonPoints = new ArrayList<Point2D>();
		
		polygonPoints.add(new Point2D(polygonPointsParams[0], polygonPointsParams[1]));
		polygonPoints.add(new Point2D(polygonPointsParams[2], polygonPointsParams[3]));
		polygonPoints.add(new Point2D(polygonPointsParams[4], polygonPointsParams[5]));
		polygonPoints.add(new Point2D(polygonPointsParams[6], polygonPointsParams[7]));
		
		return polygonPoints;
	}

	@Override
	protected int calcNumberOfIndecies() {
		return (getPolygonPoints().size() / 2) * TRIANGLE_INDEX_SIZE;
	}

	@Override
	protected ShortBuffer calcIndexBuffer() {
		ByteBuffer byIndexBuffer = ByteBuffer.allocateDirect(SH_INDEX_BYTE_CAPACITY * getNumberOfIndecies());
		byIndexBuffer.order(ByteOrder.nativeOrder());
		
		ShortBuffer shIndexBuffer = byIndexBuffer.asShortBuffer();
		shIndexBuffer.put((short) 0);
		shIndexBuffer.put((short) 1);
		shIndexBuffer.put((short) 2);
		shIndexBuffer.put((short) 2);
		shIndexBuffer.put((short) 3);
		shIndexBuffer.put((short) 0);

		shIndexBuffer.position(0);
		
		return shIndexBuffer;
	}

	@Override
	protected ArrayList<Point2D> calcTexturePoints(float[] texturePointsParams) {
		ArrayList<Point2D> texturePoints = new ArrayList<Point2D>();
		
		if(texturePointsParams == null || texturePointsParams.length == 0) {
			texturePoints.add(new Point2D(0.0f, 0.0f)); 
			texturePoints.add(new Point2D(1.0f, 0.0f)); 
			texturePoints.add(new Point2D(1.0f, 1.0f)); 
			texturePoints.add(new Point2D(0.0f, 1.0f)); 
		}
		else {
			texturePoints.add(new Point2D(texturePointsParams[6], texturePointsParams[7])); 
			texturePoints.add(new Point2D(texturePointsParams[4], texturePointsParams[5])); 
			texturePoints.add(new Point2D(texturePointsParams[2], texturePointsParams[3])); 
			texturePoints.add(new Point2D(texturePointsParams[0], texturePointsParams[1])); 
		}
		
		return texturePoints;
	}
}
