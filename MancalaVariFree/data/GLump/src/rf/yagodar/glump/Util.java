package rf.yagodar.glump;

import java.util.HashMap;

import rf.yagodar.glump.bitmap.ResBitmapManager;
import rf.yagodar.glump.model.GLumpButtonModel;
import rf.yagodar.glump.model.GLumpSVModel;
import rf.yagodar.glump.model.GLumpTextLabelModel;
import rf.yagodar.glump.point.Point2D;
import rf.yagodar.glump.polygon.AbstractPolygon;
import rf.yagodar.glump.polygon.PolygonInfo;
import rf.yagodar.glump.polygon.Rectangle;
import rf.yagodar.glump.polygon.TextureInfo;
import rf.yagodar.glump.renderer.GLumpSVRenderer;
//TODO optimize
public class Util {
	public static float getMaxX(float screenWidth, float screenHeight, float levelZ) {
		return getMaxY(levelZ) * screenWidth / screenHeight;
	}
	
	public static float getMaxY(float levelZ) {
		if(!MAX_Y_BY_LEVEL_Z.containsKey(levelZ)) {
			MAX_Y_BY_LEVEL_Z.put(levelZ, (GLumpSVRenderer.LOOK_AT_EYE_Z - levelZ) * (float) Math.tan(Math.toRadians((double) (GLumpSVRenderer.PERSPECTIVE_Y_FOV / 2.0f))));
		}
		
		return MAX_Y_BY_LEVEL_Z.get(levelZ);
	}
	
	public static Point2D getPoint(float screenX, float screenY, float screenWidth, float screenHeight, float levelZ) {		
		return new Point2D(getMaxX(screenWidth, screenHeight, levelZ) * (2.0f * screenX / screenWidth - 1.0f), getMaxY(levelZ) * (1.0f - 2.0f * screenY / screenHeight));
	}
	
	public static float translateLenght(float levelZ, float lenghtPix, float screenHeightPix) {		
		return (lenghtPix / screenHeightPix) * 2.0f * getMaxY(levelZ);
	}
	
	public static PolygonInfo insideLocatePolygonRightCenter(float availableRightX, float availableTopY, float availableWidthPos, float availableHeightPos, float polygonAspectRatio) {
		PolygonInfo polygonInfo = null;
		
		if(availableWidthPos > 0.0f && availableHeightPos > 0.0f && polygonAspectRatio > 0.0f) {
			float initAvailableHeightPos = availableHeightPos;
			
			float availableAspectRatio = availableWidthPos / availableHeightPos;

			float aspectRatioDownLimit = polygonAspectRatio - (SCENE_ASPECT_RATIO_ACCURACY * 100.0f);
			float aspectRatioUpLimit = polygonAspectRatio + (SCENE_ASPECT_RATIO_ACCURACY * 100.0f);
			
			if(!(availableAspectRatio > aspectRatioDownLimit && availableAspectRatio < aspectRatioUpLimit)) {
				boolean widthDecr = availableAspectRatio > aspectRatioDownLimit;
				while(!(availableAspectRatio > aspectRatioDownLimit && availableAspectRatio < aspectRatioUpLimit)) {
					if(widthDecr) {
						availableWidthPos -= SCENE_ASPECT_RATIO_ACCURACY;
					}
					else {
						availableHeightPos -= SCENE_ASPECT_RATIO_ACCURACY;
					}

					availableAspectRatio = availableWidthPos / availableHeightPos;
				}
				
				if(!widthDecr) {
					availableTopY -= (initAvailableHeightPos - availableHeightPos) / 2.0f;
				}
			}		
			
			polygonInfo = new PolygonInfo(false, availableRightX, availableTopY, availableWidthPos, availableHeightPos);
		}
		
		return polygonInfo;
	}
	
	public static PolygonInfo insideLocatePolygonLeftCenter(float availableLeftX, float availableTopY, float availableSquareSidePos, float polygonAspectRatio) {
		return insideLocatePolygonLeftCenter(availableLeftX, availableTopY, availableSquareSidePos, availableSquareSidePos, polygonAspectRatio);
	}
	
	public static PolygonInfo insideLocatePolygonLeftCenter(float availableLeftX, float availableTopY, float availableWidthPos, float availableHeightPos, float polygonAspectRatio) {
		PolygonInfo polygonInfo = null;
		
		if(availableWidthPos > 0.0f && availableHeightPos > 0.0f && polygonAspectRatio > 0.0f) {
			float initAvailableHeightPos = availableHeightPos;
			
			float availableAspectRatio = availableWidthPos / availableHeightPos;

			float aspectRatioDownLimit = polygonAspectRatio - (SCENE_ASPECT_RATIO_ACCURACY * 100.0f);
			float aspectRatioUpLimit = polygonAspectRatio + (SCENE_ASPECT_RATIO_ACCURACY * 100.0f);
			
			if(!(availableAspectRatio > aspectRatioDownLimit && availableAspectRatio < aspectRatioUpLimit)) {
				boolean widthDecr = availableAspectRatio > aspectRatioDownLimit;
				while(!(availableAspectRatio > aspectRatioDownLimit && availableAspectRatio < aspectRatioUpLimit)) {
					if(widthDecr) {
						availableWidthPos -= SCENE_ASPECT_RATIO_ACCURACY;
					}
					else {
						availableHeightPos -= SCENE_ASPECT_RATIO_ACCURACY;
					}

					availableAspectRatio = availableWidthPos / availableHeightPos;
				}
				
				if(!widthDecr) {
					availableTopY -= (initAvailableHeightPos - availableHeightPos) / 2.0f;
				}
			}
			
			polygonInfo = new PolygonInfo(true, availableLeftX, availableTopY, availableWidthPos, availableHeightPos);
		}
		
		return polygonInfo;
	}
	
	public static PolygonInfo insideLocatePolygonCenterCenter(float availableLeftX, float availableTopY, float availableWidthPos, float availableHeightPos, float polygonAspectRatio) {
		PolygonInfo polygonInfo = null;
		
		if(availableWidthPos > 0.0f && availableHeightPos > 0.0f && polygonAspectRatio > 0.0f) {
			float initAvailableHeightPos = availableHeightPos;
			float initAvailableWidthPos = availableWidthPos;
			
			float availableAspectRatio = availableWidthPos / availableHeightPos;

			float aspectRatioDownLimit = polygonAspectRatio - (SCENE_ASPECT_RATIO_ACCURACY * 100.0f);
			float aspectRatioUpLimit = polygonAspectRatio + (SCENE_ASPECT_RATIO_ACCURACY * 100.0f);
			
			if(!(availableAspectRatio > aspectRatioDownLimit && availableAspectRatio < aspectRatioUpLimit)) {
				boolean widthDecr = availableAspectRatio > aspectRatioDownLimit;			
				while(!(availableAspectRatio > aspectRatioDownLimit && availableAspectRatio < aspectRatioUpLimit)) {
					if(widthDecr) {
						availableWidthPos -= SCENE_ASPECT_RATIO_ACCURACY;
					}
					else {
						availableHeightPos -= SCENE_ASPECT_RATIO_ACCURACY;
					}

					availableAspectRatio = availableWidthPos / availableHeightPos;
				}
				
				if(!widthDecr) {
					availableTopY -= (initAvailableHeightPos - availableHeightPos) / 2.0f;
				}
				else {
					availableLeftX += (initAvailableWidthPos - availableWidthPos) / 2.0f;
				}
			}		
			
			polygonInfo = new PolygonInfo(true, true, availableLeftX, availableTopY, availableWidthPos, availableHeightPos);
		}
		
		return polygonInfo;
	}
	
	public static PolygonInfo insideLocatePolygonCenterCenter(Point2D centralPoint, float halfAvailableWidthPos, float halfAvailableHeightPos, float polygonAspectRatio) {
		PolygonInfo polygonInfo = null;
		
		if(centralPoint != null && halfAvailableWidthPos > 0.0f && halfAvailableHeightPos > 0.0f && polygonAspectRatio > 0.0f) {
			float availableAspectRatio = halfAvailableWidthPos / halfAvailableHeightPos;

			float aspectRatioDownLimit = polygonAspectRatio - (SCENE_ASPECT_RATIO_ACCURACY * 100.0f);
			float aspectRatioUpLimit = polygonAspectRatio + (SCENE_ASPECT_RATIO_ACCURACY * 100.0f);

			if(!(availableAspectRatio > aspectRatioDownLimit && availableAspectRatio < aspectRatioUpLimit)) {
				boolean widthDecr = availableAspectRatio > aspectRatioDownLimit;			
				while(!(availableAspectRatio > aspectRatioDownLimit && availableAspectRatio < aspectRatioUpLimit)) {
					if(widthDecr) {
						halfAvailableWidthPos -= SCENE_ASPECT_RATIO_ACCURACY;
					}
					else {
						halfAvailableHeightPos -= SCENE_ASPECT_RATIO_ACCURACY;
					}

					availableAspectRatio = halfAvailableWidthPos / halfAvailableHeightPos;
				}
			}

			polygonInfo = new PolygonInfo(centralPoint, halfAvailableWidthPos, halfAvailableHeightPos);
		}
		
		return polygonInfo;
	}

	public static PolygonInfo outsideLocatePolygonCenterCenter(Point2D centralPoint, float halfAvailableWidtPos, float halfAvailableHeightPos, float polygonAspectRatio) {
		PolygonInfo polygonInfo = null;
		if(centralPoint != null && halfAvailableWidtPos > 0.0f && halfAvailableHeightPos > 0.0f && polygonAspectRatio > 0.0f) {
			float availableAspectRatio = halfAvailableWidtPos / halfAvailableHeightPos;

			float aspectRatioDownLimit = polygonAspectRatio - (SCENE_ASPECT_RATIO_ACCURACY * 100.0f);
			float aspectRatioUpLimit = polygonAspectRatio + (SCENE_ASPECT_RATIO_ACCURACY * 100.0f);

			if(!(availableAspectRatio > aspectRatioDownLimit && availableAspectRatio < aspectRatioUpLimit)) {
				boolean widthIncr = availableAspectRatio < aspectRatioDownLimit;			
				while(!(availableAspectRatio > aspectRatioDownLimit && availableAspectRatio < aspectRatioUpLimit)) {
					if(widthIncr) {
						halfAvailableWidtPos += SCENE_ASPECT_RATIO_ACCURACY;
					}
					else {
						halfAvailableHeightPos += SCENE_ASPECT_RATIO_ACCURACY;
					}

					availableAspectRatio = halfAvailableWidtPos / halfAvailableHeightPos;
				}
			}

			polygonInfo = new PolygonInfo(centralPoint, halfAvailableWidtPos, halfAvailableHeightPos);
		}
		return polygonInfo;
	}
	
	//TODO в одну модель
	public static <V extends AbstractPolygon> GLumpSVModel<Rectangle> generateTextPlateModel(float levelZ, float availableLeftX, float availableTopY, float availableHeight, float availableWidth, float textModelYBorder, GLumpSVModel<V> parentModel, TextureInfo leftRightBackDropTextureInfo,TextureInfo middleBackDropTextureInfo) {
		GLumpSVModel<Rectangle> textPlateModel = null;
		
		if(parentModel != null && leftRightBackDropTextureInfo != null && middleBackDropTextureInfo != null && availableHeight > 0.0f && availableWidth > 0.0f) {
			float initAvailableHeight = availableHeight;
			
			float neededAspectRatioDownLimit = 2.0f * leftRightBackDropTextureInfo.getAspectRatio() + 1.0f - (SCENE_ASPECT_RATIO_ACCURACY * 100.0f);
			float availableAspectRatio =  availableWidth / availableHeight;

			while(availableAspectRatio < neededAspectRatioDownLimit) {
				availableHeight -= SCENE_ASPECT_RATIO_ACCURACY;
				availableAspectRatio =  availableWidth / availableHeight;
			}

			availableTopY -= (initAvailableHeight - availableHeight) / 2.0f;
							
			PolygonInfo leftPolygonInfo = insideLocatePolygonLeftCenter(	availableLeftX, 
																			availableTopY,
																			availableHeight * leftRightBackDropTextureInfo.getAspectRatio(),
																			availableHeight, 
																			leftRightBackDropTextureInfo.getAspectRatio());	
	
			float textModelWidth = availableWidth - 2.0f * leftPolygonInfo.getWidth();
			float leftPolygonXOffsetToRightPolygon = leftPolygonInfo.getWidth() + textModelWidth;
			int leftRightBackDropBitmapId = ResBitmapManager.getInstance().getBitmapId(leftRightBackDropTextureInfo.getTexId());
			
			// left backdrop
			parentModel.addChildModel(new Rectangle(	levelZ,
														leftRightBackDropBitmapId,
														leftPolygonInfo.getLeftX(), 
														leftPolygonInfo.getTopY(), 
														leftPolygonInfo.getRightX(), 
														leftPolygonInfo.getBottomY(),
														leftRightBackDropTextureInfo.getLeftS(),
														leftRightBackDropTextureInfo.getTopT(),
														leftRightBackDropTextureInfo.getRightS(),
														leftRightBackDropTextureInfo.getBottomT()), false);						
	
	
	
			// right backdrop			
			parentModel.addChildModel(new Rectangle(	levelZ,
														leftRightBackDropBitmapId,
														leftPolygonInfo.getLeftX() + leftPolygonXOffsetToRightPolygon, 
														leftPolygonInfo.getTopY(), 
														leftPolygonInfo.getRightX() + leftPolygonXOffsetToRightPolygon, 
														leftPolygonInfo.getBottomY(),
														leftRightBackDropTextureInfo.getRightS(),
														leftRightBackDropTextureInfo.getTopT(),
														leftRightBackDropTextureInfo.getLeftS(),
														leftRightBackDropTextureInfo.getBottomT()), false);
			
			// middle backdrop
			parentModel.addChildModel(new Rectangle(	levelZ,
														ResBitmapManager.getInstance().getBitmapId(middleBackDropTextureInfo.getTexId()),
														leftPolygonInfo.getRightX(), 
														leftPolygonInfo.getTopY(), 
														leftPolygonInfo.getRightX() + textModelWidth, 
														leftPolygonInfo.getBottomY(),
														middleBackDropTextureInfo.getLeftS(),
														middleBackDropTextureInfo.getBottomT(),
														middleBackDropTextureInfo.getRightS() * textModelWidth / availableHeight,
														middleBackDropTextureInfo.getTopT()), false);
			
			textPlateModel = parentModel.addChildModel(new Rectangle(	levelZ,
																		leftPolygonInfo.getRightX(), 
																		leftPolygonInfo.getTopY() - textModelYBorder, 
																		leftPolygonInfo.getRightX() + textModelWidth, 
																		leftPolygonInfo.getBottomY() + textModelYBorder), false);
		}
		
		return textPlateModel;
	}
	
	public static <V extends AbstractPolygon> GLumpTextLabelModel generateTextLabelModel(float alpha, float levelZ, float availableLeftX, float availableTopY, float availableHeight, float availableWidth, float textModelYBorder, GLumpSVModel<V> parentModel, TextureInfo leftRightBackDropTextureInfo, TextureInfo middleBackDropTextureInfo) {
		GLumpTextLabelModel textLabelModel = null;
		
		if(parentModel != null && leftRightBackDropTextureInfo != null && middleBackDropTextureInfo != null && availableHeight > 0.0f && availableWidth > 0.0f) {
			float initAvailableHeight = availableHeight;
			
			float neededAspectRatioDownLimit = 2.0f * leftRightBackDropTextureInfo.getAspectRatio() + 1.0f - (SCENE_ASPECT_RATIO_ACCURACY * 100.0f);
			float availableAspectRatio =  availableWidth / availableHeight;

			while(availableAspectRatio < neededAspectRatioDownLimit) {
				availableHeight -= SCENE_ASPECT_RATIO_ACCURACY;
				availableAspectRatio =  availableWidth / availableHeight;
			}

			availableTopY -= (initAvailableHeight - availableHeight) / 2.0f;
			
			Rectangle modelPolygon = new Rectangle(	levelZ,
													availableLeftX, 
													availableTopY, 
													availableLeftX + availableWidth, 
													availableTopY - availableHeight);
			
			
			PolygonInfo leftPolygonInfo = insideLocatePolygonLeftCenter(	availableLeftX, 
																			availableTopY,
																			availableHeight * leftRightBackDropTextureInfo.getAspectRatio(),
																			availableHeight, 
																			leftRightBackDropTextureInfo.getAspectRatio());	
	
			float textModelWidth = availableWidth - 2.0f * leftPolygonInfo.getWidth();
			float leftPolygonXOffsetToRightPolygon = leftPolygonInfo.getWidth() + textModelWidth;
			int leftRightBackDropBitmapId = ResBitmapManager.getInstance().getBitmapId(leftRightBackDropTextureInfo.getTexId());
			
			// left backdrop
			Rectangle leftBackDropPolygon = new Rectangle(	levelZ,
														leftRightBackDropBitmapId,
														alpha,
														leftPolygonInfo.getLeftX(), 
														leftPolygonInfo.getTopY(), 
														leftPolygonInfo.getRightX(), 
														leftPolygonInfo.getBottomY(),
														leftRightBackDropTextureInfo.getLeftS(),
														leftRightBackDropTextureInfo.getTopT(),
														leftRightBackDropTextureInfo.getRightS(),
														leftRightBackDropTextureInfo.getBottomT());						
	
	
	
			// right backdrop			
			Rectangle rightBackDropPolygon = new Rectangle(	levelZ,
														leftRightBackDropBitmapId,
														alpha,
														leftPolygonInfo.getLeftX() + leftPolygonXOffsetToRightPolygon, 
														leftPolygonInfo.getTopY(), 
														leftPolygonInfo.getRightX() + leftPolygonXOffsetToRightPolygon, 
														leftPolygonInfo.getBottomY(),
														leftRightBackDropTextureInfo.getRightS(),
														leftRightBackDropTextureInfo.getTopT(),
														leftRightBackDropTextureInfo.getLeftS(),
														leftRightBackDropTextureInfo.getBottomT());
			
			// middle backdrop
			Rectangle middleBackDropPolygon = new Rectangle(	levelZ,
														ResBitmapManager.getInstance().getBitmapId(middleBackDropTextureInfo.getTexId()),
														alpha,
														leftPolygonInfo.getRightX(), 
														leftPolygonInfo.getTopY(), 
														leftPolygonInfo.getRightX() + textModelWidth, 
														leftPolygonInfo.getBottomY(),
														middleBackDropTextureInfo.getLeftS(),
														middleBackDropTextureInfo.getBottomT(),
														middleBackDropTextureInfo.getRightS() * textModelWidth / availableHeight,
														middleBackDropTextureInfo.getTopT());
			
			Rectangle textModel = new Rectangle(	levelZ,
													leftPolygonInfo.getRightX(), 
													leftPolygonInfo.getTopY() - textModelYBorder, 
													leftPolygonInfo.getRightX() + textModelWidth, 
													leftPolygonInfo.getBottomY() + textModelYBorder);
			
			textLabelModel = new GLumpTextLabelModel(modelPolygon, leftBackDropPolygon, middleBackDropPolygon, rightBackDropPolygon, textModel, false);
			parentModel.addChildModel(textLabelModel, false);
			
			return textLabelModel;
		}
		
		return textLabelModel;
	}
	
	public static <V extends AbstractPolygon> GLumpButtonModel generateButtonModel(float alpha, float levelZ, float availableLeftX, float availableTopY, float availableHeight, float availableWidth, float textModelYBorder, GLumpSVModel<V> parentModel, TextureInfo leftRightDBackDropTextureInfo,TextureInfo middleDBackDropTextureInfo, TextureInfo leftRightSBackDropTextureInfo,TextureInfo middleSBackDropTextureInfo) {
		GLumpButtonModel buttonModel = null;
		
		Rectangle modelPolygon = new Rectangle(	levelZ,
				availableLeftX, 
				availableTopY, 
				availableLeftX + availableWidth, 
				availableTopY - availableHeight);
		
		buttonModel = new GLumpButtonModel(modelPolygon);
		parentModel.addChildModel(buttonModel, false);
		
		buttonModel.setButtonDeselectedModel(generateTextLabelModel(alpha, levelZ, availableLeftX, availableTopY, availableHeight, availableWidth, textModelYBorder, buttonModel, leftRightDBackDropTextureInfo, middleDBackDropTextureInfo));
		buttonModel.setButtonSelectedModel(generateTextLabelModel(AbstractPolygon.TRANSPARENT_ALPHA, levelZ, availableLeftX, availableTopY, availableHeight, availableWidth, textModelYBorder, buttonModel, leftRightSBackDropTextureInfo, middleSBackDropTextureInfo));
		
		return buttonModel;
	}
	
	private static HashMap<Float, Float> MAX_Y_BY_LEVEL_Z = new HashMap<Float, Float>();
	private static final float SCENE_ASPECT_RATIO_ACCURACY = 0.0001f;
}
