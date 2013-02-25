package rf.yagodar.glump.polygon;
//TODO DOC
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import javax.microedition.khronos.opengles.GL10;

import rf.yagodar.glump.animation.AbstractAnimScen;
import rf.yagodar.glump.bitmap.BitmapProvider;
import rf.yagodar.glump.point.Point2D;

public abstract class AbstractPolygon {	
	public void draw(GL10 gl) {
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		
		gl.glVertexPointer(VERTEX_POINTER_SIZE, GL10.GL_FLOAT, VERTEX_POINTER_STRIDE, fVertexBuffer);

		gl.glColor4f(DEFAULT_RED, DEFAULT_GREEN, DEFAULT_BLUE, alpha);
		
		if(texturable) {				
			gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);						
			gl.glTexCoordPointer(TEX_COORD_POINTER_SIZE, GL10.GL_FLOAT, TEX_COORD_POINTER_STRIDE, fTextureBuffer);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, BitmapProvider.getInstance().getTextureName(bitmapId));				
			tuneTextureParams(gl);				
			drawElements(gl);				
			gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		}
		else {
			gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
			gl.glColorPointer(COLOR_POINTER_SIZE, GL10.GL_FLOAT, COLOR_POINTER_STRIDE, fColorBuffer);											
			drawElements(gl);				
			gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
		}
		
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}	
	
	public int getBitmapId() {
		return bitmapId;
	}
		
	public boolean isContainsPoint(Point2D point2D) {
		boolean result = false;
		
		if(point2D != null) {
			result = isContainsPoint(point2D.getX(), point2D.getY());
		}
		
		return result;
	}
	
	abstract public boolean isContainsPoint(float x, float y);
	
	public ArrayList<Point2D> getPolygonPoints() {
		return polygonPoints;
	}
	
	abstract public Point2D getCentralPoint();
	
	public float getLevelZ() {
		return levelZ;
	}
	
	public void setAnimScen(AbstractAnimScen animScen) {
		if(animScen != null) {
			animScenQueue.offer(animScen);
		}
	}
	
	public boolean refresh() {
		if(animScenQueue.peek() == null) {
			return false;
		}
		
		if(!animScenQueue.peek().applyNextStep(this)) {
			animScenQueue.poll();
			return refresh();
		}
		
		fVertexBuffer = calcVertexBuffer();
		return true;
	}
	
	abstract public void move(float destX);
	
	abstract public void scale(float mult);
	
	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}
	
	public boolean isTransparent() {
		return alpha == TRANSPARENT_ALPHA;
	}
	
	protected AbstractPolygon(Float levelZ, float[] polygonPointsParams) {
		this(levelZ, DEFAULT_ALPHA, polygonPointsParams);
	}
	
	protected AbstractPolygon(Float levelZ, Float alpha, float[] polygonPointsParams) {
		this(levelZ, alpha, polygonPointsParams, null);
	}
	
	protected AbstractPolygon(Float levelZ, float[] polygonPointsParams, float[] polygonColorParams) {
		this(levelZ, DEFAULT_ALPHA, polygonPointsParams, polygonColorParams);
	}
	
	protected AbstractPolygon(Float levelZ, Float alpha, float[] polygonPointsParams, float[] polygonColorParams) {
		calcPolygon(levelZ, polygonPointsParams, alpha);		
		colorPoints = calcColorPoints(polygonColorParams);
		fColorBuffer = calcColorBuffer();
	}

	protected AbstractPolygon(Float levelZ, Integer bitmapId, float[] polygonPointsParams) {		
		this(levelZ, bitmapId, polygonPointsParams, null);
	}
	
	protected AbstractPolygon(Float levelZ, Integer bitmapId, float[] polygonPointsParams, float[] texturePointsParams) {		
		this(levelZ, bitmapId, DEFAULT_ALPHA, polygonPointsParams, texturePointsParams);
	}
	
	protected AbstractPolygon(Float levelZ, Integer bitmapId, float alpha, float[] polygonPointsParams, float[] texturePointsParams) {	
		calcPolygon(levelZ, polygonPointsParams, alpha);
		texturable = true;		
		this.bitmapId = bitmapId;
		texturePoints = calcTexturePoints(texturePointsParams);
		fTextureBuffer = calcTextureBuffer();
	}
	
	protected void calcPolygon(float levelZ, float[] polygonPointsParams, float alpha) {
		setLevelZ(levelZ);
		setAlpha(alpha);
		polygonPoints = calcPolygonPoints(polygonPointsParams);
		fVertexBuffer = calcVertexBuffer();
		numOfIndecies = calcNumberOfIndecies();
		shIndexBuffer = calcIndexBuffer();
		animScenQueue = new LinkedList<AbstractAnimScen>();
	}
		
	abstract protected ArrayList<Point2D> calcPolygonPoints(float[] polygonPointsParams);
	
	abstract protected int calcNumberOfIndecies();
	
	protected int getNumberOfIndecies() {
		return numOfIndecies;
	}
	
	abstract protected ShortBuffer calcIndexBuffer();
	
	abstract protected ArrayList<Point2D> calcTexturePoints(float[] texturePointsParams);
	
	protected int getVertexesCount() {
		return getPolygonPoints().size();
	}
	
	protected void tuneTextureParams(GL10 gl) {
		//Параметры фильтрации текстур при уменьшении; default:GL10.GL_NEAREST_MIPMAP_LINEAR	
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
		
		//Параметры фильтрации текстур при увеличении; default:GL10.GL_LINEAR
		//gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

		//Повтор текстуры, если координата > 1.0; default:GL10.GL_REPEAT
		//gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
		//gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);
	}
	
	protected void setLevelZ(float levelZ) {
		this.levelZ = levelZ;
	}
	
	private FloatBuffer calcVertexBuffer() {
		ByteBuffer byVertexBuffer = ByteBuffer.allocateDirect(F_VERTEX_POINTER_BYTE_CAPACITY * polygonPoints.size());
		byVertexBuffer.order(ByteOrder.nativeOrder());
		
		FloatBuffer fVertexBuffer = byVertexBuffer.asFloatBuffer();
		
		for (Point2D figurePoint : getPolygonPoints()) {
			fVertexBuffer.put(figurePoint.getX());
			fVertexBuffer.put(figurePoint.getY());
			fVertexBuffer.put(getLevelZ());
		}
		
		fVertexBuffer.position(0);

		return fVertexBuffer;
	}
	
	private float[][] calcColorPoints(float[] colorPointsParams) {
		float[][] colorPoints = new float[getVertexesCount()][COLOR_POINTER_SIZE];
		
		//TODO На данный момент поддерживается один цвет на весь полигон.
		float[] commonColorPoint = new float[COLOR_POINTER_SIZE];
		if(colorPointsParams == null || colorPointsParams.length != 4) {
			commonColorPoint = TRANSPARENT_COLOR_POINT;
		}
		else {
			commonColorPoint = colorPointsParams;
		}
		
		for (int i = 0; i < colorPoints.length; i++) {
			colorPoints[i] = commonColorPoint;
		}
		
		return colorPoints;
	}
	
	private FloatBuffer calcColorBuffer() {
		ByteBuffer byColorBuffer = ByteBuffer.allocateDirect(F_COLOR_POINTER_BYTE_CAPACITY * colorPoints.length);
		byColorBuffer.order(ByteOrder.nativeOrder());
		
		FloatBuffer fColorBuffer = byColorBuffer.asFloatBuffer();
	
		for (float[] colorPoint : colorPoints) {
			fColorBuffer.put(colorPoint[0]);
			fColorBuffer.put(colorPoint[1]);
			fColorBuffer.put(colorPoint[2]);
			fColorBuffer.put(colorPoint[3]);
		}
		
		fColorBuffer.position(0);

		return fColorBuffer;
	}
	
	private FloatBuffer calcTextureBuffer() {
		ByteBuffer byTextureBuffer = ByteBuffer.allocateDirect(F_TEX_COORD_POINTER_BYTE_CAPACITY * texturePoints.size());
		byTextureBuffer.order(ByteOrder.nativeOrder());
		
		FloatBuffer fTextureBuffer = byTextureBuffer.asFloatBuffer();
		
		for (Point2D texturePoint : texturePoints) {
			fTextureBuffer.put(texturePoint.getX());
			fTextureBuffer.put(texturePoint.getY());
		}
		
		fTextureBuffer.position(0);

		return fTextureBuffer;
	}
	
	private void drawElements(GL10 gl) {
		gl.glPushMatrix();
		gl.glDrawElements(GL10.GL_TRIANGLES, getNumberOfIndecies(), GL10.GL_UNSIGNED_SHORT, shIndexBuffer);	
		gl.glPopMatrix();
	}
	
	public final static float TRANSPARENT_ALPHA = 0.0f;
	
	protected final static int TRIANGLE_INDEX_SIZE = 3;
	protected final static int SH_INDEX_BYTE_CAPACITY = Short.SIZE / Byte.SIZE;
	
	private float levelZ;
	private ArrayList<Point2D> polygonPoints;
	private FloatBuffer fVertexBuffer;
	private int numOfIndecies;
	private ShortBuffer shIndexBuffer;
	private float[][] colorPoints;
	private FloatBuffer fColorBuffer;	
	private int bitmapId;
	private ArrayList<Point2D> texturePoints;
	private FloatBuffer fTextureBuffer;
	private boolean texturable = false;
	private Queue<AbstractAnimScen> animScenQueue;
	private float alpha;
	
	private final static float[] TRANSPARENT_COLOR_POINT = new float[] { 0.0f, 0.0f, 0.0f, 0.0f };
	
	private final static int VERTEX_POINTER_SIZE = 3;
	private final static int VERTEX_POINTER_STRIDE = 0;			
	private final static int F_VERTEX_POINTER_BYTE_CAPACITY = VERTEX_POINTER_SIZE * (Float.SIZE / Byte.SIZE);
	
	private final static int TEX_COORD_POINTER_SIZE = 2;
	private final static int TEX_COORD_POINTER_STRIDE = 0;
	private final static int F_TEX_COORD_POINTER_BYTE_CAPACITY = TEX_COORD_POINTER_SIZE * (Float.SIZE / Byte.SIZE);
	
	private final static int COLOR_POINTER_SIZE = 4;
	private final static int COLOR_POINTER_STRIDE = 0;
	private final static int F_COLOR_POINTER_BYTE_CAPACITY = COLOR_POINTER_SIZE * (Float.SIZE / Byte.SIZE);
	
	private final static float DEFAULT_RED = 1.0f;
	private final static float DEFAULT_GREEN = 1.0f;
	private final static float DEFAULT_BLUE = 1.0f;
	private final static float DEFAULT_ALPHA = 1.0f;
}
