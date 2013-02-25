package rf.yagodar.glump.polygon;


public class TextureInfo {
	public TextureInfo(int texId, float leftS, float topT, float rightS, float bottomT) {		
		this.texId = texId;
		this.leftS = leftS;
		this.topT = topT;
		this.rightS = rightS;
		this.bottomT = bottomT;
		aspectRatio = (rightS - leftS) / (topT - bottomT);
	}
	
	public TextureInfo(int texId, float[] texturePointsParams) {
		this(texId, texturePointsParams[0], texturePointsParams[1], texturePointsParams[2], texturePointsParams[3]);
	}
	
	public TextureInfo(int texId) {
		this(texId, RECTANGLE_TEXTURE_POINTS_PARAMS_DEFAULT);
	}

	public TextureInfo() {	
		this(0, RECTANGLE_TEXTURE_POINTS_PARAMS_DEFAULT);
	}
	
	public int getTexId() {
		return texId;
	}

	public float getLeftS() {
		return leftS;
	}

	public float getTopT() {
		return topT;
	}

	public float getRightS() {
		return rightS;
	}

	public float getBottomT() {
		return bottomT;
	}

	public float getAspectRatio() {
		return aspectRatio;
	}

	private final int texId;
	private final float leftS;
	private final float topT;
	private final float rightS;
	private final float bottomT;
	private final float aspectRatio;

	public static final int RECTANGLE_TEXTURE_POINTS_PARAMS_COUNT = 4;
	public static final float[] RECTANGLE_TEXTURE_POINTS_PARAMS_DEFAULT = new float[] { 0.0f, 1.0f, 1.0f, 2.0f };
}
