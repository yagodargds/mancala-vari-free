package rf.yagodar.glump.bitmap;

import java.util.ArrayList;

import rf.yagodar.glump.polygon.AbstractPolygon;
import rf.yagodar.glump.polygon.Rectangle;
import rf.yagodar.glump.polygon.TextureInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.text.TextPaint;
import android.util.SparseArray;

public class TextBitmapManager {
	public static TextBitmapManager getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new TextBitmapManager();
		}

		return INSTANCE;
	}

	public void drawText(String text) {
		if(text != null && !text.equals("") && textPaint.getFontSpacing() < bitmapSideSize) {
			float[] textWidths = new float[text.length()];
			textPaint.getTextWidths(text, textWidths);

			for(int charIndx = 0; charIndx < text.length(); charIndx++) {
				getTextureInfo(text.charAt(charIndx), textWidths[charIndx]);
			}
		}
	}

	public ArrayList<AbstractPolygon> drawText(Rectangle textModelPolygon, String text, int textColor) {
		ArrayList<AbstractPolygon> polygonsToAdd = null;

		try {
			if(textModelPolygon != null && text != null && !text.equals("") && textPaint.getFontSpacing() < bitmapSideSize) {
				float textModelWidth = textModelPolygon.getWidth();

				float[] textWidths = new float[text.length()];
				textPaint.getTextWidths(text, textWidths);

				float translatedTextScaleMult = textModelPolygon.getHeight() / textPaint.getFontSpacing();

				float textWidth = 0.0f;
				for(int i = 0; i < textWidths.length; i++) {
					textWidths[i] *= translatedTextScaleMult;
					textWidth += textWidths[i];
				}

				if(textModelWidth < textWidth) {
					if(text.length() <= dotCharsCount) {
						return polygonsToAdd;
					}
					else {
						float dotCharWidth = translatedTextScaleMult * dotCharWidthPix;
						float dotCharsWidth = dotCharsCount * dotCharWidth;
						if(textModelWidth < textWidths[0] + dotCharsWidth) {
							return polygonsToAdd;
						}
						else {
							float checkDotCharWidth = dotCharsWidth + dotCharWidth;
							textWidth = 0.0f;
							int i;
							for(i = 0; i < textWidths.length - dotCharsCount; i++) {
								textWidth += textWidths[i];
								if(textModelWidth < textWidth + checkDotCharWidth) {									
									break;
								}
							}

							for(int j = i + 1; j <= i + dotCharsCount; j++) {
								if(j < textWidths.length) {
									textWidths[j] = dotCharWidth;
									textWidth += textWidths[j];
								}
							}

							text = text.substring(0, i + 1);
							for(int j = 0; j < dotCharsCount; j++) {
								text += dotChar;
							}
						}
					}
				}

				polygonsToAdd = new ArrayList<AbstractPolygon>();

				float leftX = textModelPolygon.getLeftX() + (textModelWidth - textWidth) / 2.0f;
				TextureInfo textureInfo;
				for(int charIndx = 0; charIndx < text.length(); charIndx++) {
					if(charIndx < textWidths.length) {
						textureInfo = getTextureInfo(text.charAt(charIndx), textWidths[charIndx] / translatedTextScaleMult, textColor);
						if(textureInfo != null) {
							Rectangle charPolygon = new Rectangle(textModelPolygon.getLevelZ(), 
									textureInfo.getTexId(), 
									leftX, textModelPolygon.getTopY(), 
									leftX + textWidths[charIndx], 
									textModelPolygon.getBottomY(), 
									textureInfo.getLeftS(), 
									textureInfo.getTopT(), 
									textureInfo.getRightS(), 
									textureInfo.getBottomT());

							polygonsToAdd.add(charPolygon);
						}
						leftX += textWidths[charIndx];
					}
				}
			}
		}
		catch(Exception e) {}
		
		return polygonsToAdd;
	}

	protected TextBitmapManager() {
		bitmapSideSize = 256;
		textureInfoByColorByChar = new SparseArray<SparseArray<TextureInfo>>();
		textPaint = initTextPaint();
		bitmapSpacePixByBitmapId = new SparseArray<float[]>();
		canvasByBitmapId = new SparseArray<Canvas>();
		initDotParams();
	}

	protected int generateTextBitmap() {
		Bitmap bitmap = Bitmap.createBitmap(bitmapSideSize, bitmapSideSize, Bitmap.Config.ARGB_8888);		
		bitmap.eraseColor(Color.TRANSPARENT);

		int bitmapId = BitmapProvider.getInstance().addBitmap(bitmap);
		
		bitmapSpacePixByBitmapId.put(bitmapId, new float[(int)((float) bitmap.getHeight() / (textPaint.getFontSpacing() + textPaint.getFontMetrics().descent))]);
		for(int rawN = 0; rawN < bitmapSpacePixByBitmapId.get(bitmapId).length; rawN++) {
			bitmapSpacePixByBitmapId.get(bitmapId)[rawN] = bitmapSideSize;
		}
		
		canvasByBitmapId.put(bitmapId, new Canvas(bitmap));

		return bitmapId;
	}

	private TextPaint initTextPaint() {
		TextPaint textPaint = new TextPaint();

		textPaint.setTextSize(30);
		textPaint.setAntiAlias(true);
		textPaint.setStrokeJoin(Join.ROUND);
		textPaint.setStrokeMiter(10);
		textPaint.setStrokeWidth(1.5f);

		return textPaint;
	}

	private void initDotParams() {
		dotChar = ".";
		float[] textWidths = new float[dotChar.length()];
		textPaint.getTextWidths(dotChar, textWidths);
		dotCharWidthPix = 0.0f;
		for(int i = 0; i < textWidths.length; i++) {
			dotCharWidthPix += textWidths[i];
		}
		dotCharsCount = 2;
	}

	private TextureInfo getTextureInfo(char textChar, float textCharWidth) {
		return getTextureInfo(textChar, textCharWidth, Color.BLACK);
	}

	private TextureInfo getTextureInfo(char textChar, float textCharWidthPix, int textColor) {
		TextureInfo textureInfo = null;

		if(textCharWidthPix < bitmapSideSize) {
			if(textureInfoByColorByChar.indexOfKey(textChar) < 0) {
				textureInfoByColorByChar.put(textChar, new SparseArray<TextureInfo>());
			}		

			if(textureInfoByColorByChar.get(textChar).indexOfKey(textColor) < 0) {
				int bitmapId = 0;
				int rawN = 0;
				int bitmapIdIndx = 0;
				boolean bitmapFounded = false;
				boolean newBitmapGenerated = false;
				while(!bitmapFounded) {
					if(bitmapIdIndx == bitmapSpacePixByBitmapId.size()) {
						bitmapId = generateTextBitmap();
						newBitmapGenerated = true;
					}
					else {
						bitmapId = bitmapSpacePixByBitmapId.keyAt(bitmapIdIndx);
					}

					rawN = 0;
					while(!bitmapFounded && rawN != bitmapSpacePixByBitmapId.get(bitmapId).length) {
						if(bitmapSpacePixByBitmapId.get(bitmapId)[rawN] > textCharWidthPix) {
							bitmapFounded = true;
						}
						else {
							rawN++;
						}
					}

					bitmapIdIndx++;
				}

				float charX = (float) BitmapProvider.getInstance().getBitmap(bitmapId).getWidth() - bitmapSpacePixByBitmapId.get(bitmapId)[rawN];
				float charY = ((float) (rawN + 1)) * (textPaint.getFontSpacing() + textPaint.getFontMetrics().descent);					
				bitmapSpacePixByBitmapId.get(bitmapId)[rawN] -= textCharWidthPix + textPaint.getStrokeWidth();

				/*textPaint.setStyle(Style.STROKE);
				textPaint.setColor(Color.BLACK);
				canvasByBitmapId.get(bitmapId).drawText("" + textChar, charX, charY - textPaint.getFontMetrics().descent, textPaint);*/

				textPaint.setStyle(Style.FILL_AND_STROKE);
				textPaint.setColor(textColor);
				canvasByBitmapId.get(bitmapId).drawText("" + textChar, charX, charY - textPaint.getFontMetrics().descent, textPaint);

				textureInfo = new TextureInfo(bitmapId, 
						charX / (float) BitmapProvider.getInstance().getBitmap(bitmapId).getWidth(), 
						charY / (float) BitmapProvider.getInstance().getBitmap(bitmapId).getHeight(), 
						(charX + textCharWidthPix) / (float) BitmapProvider.getInstance().getBitmap(bitmapId).getWidth(), 
						(charY - textPaint.getFontSpacing()) / (float) BitmapProvider.getInstance().getBitmap(bitmapId).getHeight());
				
				textureInfoByColorByChar.get(textChar).put(textColor, textureInfo);

				if(newBitmapGenerated) {
					BitmapProvider.getInstance().addNewBitmapId(bitmapId);
				}
				else {
					BitmapProvider.getInstance().addEditedBitmapId(bitmapId);
				}
			}
			else {
				textureInfo = textureInfoByColorByChar.get(textChar).get(textColor);
			}
		}

		return textureInfo;
	}

	private String dotChar;
	private float dotCharWidthPix;
	private int dotCharsCount;

	private final int bitmapSideSize;
	private final SparseArray<SparseArray<TextureInfo>> textureInfoByColorByChar;
	private final SparseArray<float[]> bitmapSpacePixByBitmapId;
	private final SparseArray<Canvas> canvasByBitmapId;
	private final TextPaint textPaint;

	private static TextBitmapManager INSTANCE;	
}
