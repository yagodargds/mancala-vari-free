package rf.yagodar.manqala.free.drawing.view;

import java.security.SecureRandom;
import java.util.ArrayList;

import rf.yagodar.glump.Util;
import rf.yagodar.glump.bitmap.ResBitmapManager;
import rf.yagodar.glump.model.GLumpSVModel;
import rf.yagodar.glump.point.Point2D;
import rf.yagodar.glump.polygon.AbstractPolygon;
import rf.yagodar.glump.polygon.PolygonInfo;
import rf.yagodar.glump.polygon.Rectangle;
import rf.yagodar.glump.polygon.Square;
import rf.yagodar.glump.polygon.TextureInfo;
import rf.yagodar.glump.view.AbstractGLumpSVBuilder;
import rf.yagodar.manqala.free.drawing.model.ManqalaVariCellModel;
import rf.yagodar.manqala.free.drawing.model.ManqalaVariGameBoardModel;
import rf.yagodar.manqala.free.drawing.model.ManqalaVariPausePlateModel;
import rf.yagodar.manqala.free.logic.parameters.SVari;
import android.app.Activity;
import android.content.res.TypedArray;
import android.opengl.GLSurfaceView;
import android.util.Log;

public class ManqalaCombatVariSVBuilder extends AbstractGLumpSVBuilder<ManqalaCombatVariSV, ManqalaCombatVariSVBlank> {
	public static ManqalaCombatVariSVBuilder getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new ManqalaCombatVariSVBuilder();
		}
		
		return INSTANCE;
	}

	@Override
	protected ManqalaCombatVariSV createNewSV(Activity activity) {		
		return new ManqalaCombatVariSV(activity, GLSurfaceView.RENDERMODE_WHEN_DIRTY);
	}

	@Override
	protected ManqalaCombatVariSVBlank buildSVBlank(Activity activity, int key, float sVLevelZ, float sVWidth, float sVHeight) {
		ManqalaCombatVariSVBlank sVBlank = null;

		TypedArray sVProperties = activity.getResources().obtainTypedArray(key);
		if(sVProperties != null) {
			sVBlank = createNewSVBlank(sVProperties, sVLevelZ, sVWidth, sVHeight);

			if(sVBlank != null) {
				buildGBBackDropOnSV(sVProperties, sVBlank, sVLevelZ, sVWidth, sVHeight);
				buildCombatInfoBackDropOnScene(sVProperties, sVBlank, sVLevelZ, sVWidth, sVHeight);
				buildCellsBackDropsOnGameBoard(sVProperties, sVBlank, sVLevelZ, sVWidth, sVHeight);
				buildCellsPointsBackDropsOnGameBoard(sVProperties, sVBlank, sVLevelZ, sVWidth, sVHeight);
				buildPlayersInfosBackDropsOnGameBoard(sVProperties, sVBlank, sVLevelZ, sVWidth, sVHeight);
				buildGrainsOnCells(sVProperties, sVBlank, sVLevelZ, sVWidth, sVHeight);
				
				buildHeader(sVProperties, sVBlank, sVLevelZ, sVWidth, sVHeight);
				buildLoadingFramesOnScene(sVProperties, sVBlank, sVLevelZ, sVWidth, sVHeight);
				buildPauseButton(sVProperties, sVBlank, sVLevelZ, sVWidth, sVHeight);
				buildPausePlate(sVProperties, sVBlank, sVLevelZ, sVWidth, sVHeight);
			}

			sVProperties.recycle();
		}

		return sVBlank;
	}

	private ManqalaCombatVariSVBuilder() {
		super();
		logTag = ManqalaCombatVariSVBuilder.class.getSimpleName();
	}

	private ManqalaCombatVariSVBlank createNewSVBlank(TypedArray sVProperties, float sVLevelZ, float sVWidth, float sVHeight) {
		TextureInfo sVTextureInfo = new TextureInfo(sVProperties.getResourceId(ManqalaCombatVariSVProperties.svTex.ordinal(), 0), 
				sVProperties.getFloat(ManqalaCombatVariSVProperties.svTexLS.ordinal(), 0), 
				sVProperties.getFloat(ManqalaCombatVariSVProperties.svTexTT.ordinal(), 0), 
				sVProperties.getFloat(ManqalaCombatVariSVProperties.svTexRS.ordinal(), 0), 
				sVProperties.getFloat(ManqalaCombatVariSVProperties.svTexBT.ordinal(), 0));
		
		PolygonInfo sVPolygonInfo = Util.outsideLocatePolygonCenterCenter(new Point2D(0.0f, 0.0f),
				Util.getMaxX(sVWidth, sVHeight, sVLevelZ),
				Util.getMaxY(sVLevelZ),
				sVTextureInfo.getAspectRatio());

		if(sVPolygonInfo != null) {
			return new ManqalaCombatVariSVBlank(new GLumpSVModel<Rectangle>(null, new Rectangle(sVLevelZ,
					ResBitmapManager.getInstance().getBitmapId(sVTextureInfo.getTexId()),
					sVPolygonInfo.getLeftX(), 
					sVPolygonInfo.getTopY(), 
					sVPolygonInfo.getRightX(), 
					sVPolygonInfo.getBottomY(),
					sVTextureInfo.getLeftS(), 
					sVTextureInfo.getTopT(), 
					sVTextureInfo.getRightS(), 
					sVTextureInfo.getBottomT())));
		}
		else {
			Log.e(logTag, "Error while locating SVBackDrop!");
		}
		
		return null;
	}

	private void buildGBBackDropOnSV(TypedArray sVProperties, ManqalaCombatVariSVBlank sVBlank, float sVLevelZ, float sVWidth, float sVHeight) {
		if(sVBlank.getSVRootModel() != null) {
			float gameBoardMinBorderVDisplayTop = Util.translateLenght(sVLevelZ, sVHeight * sVProperties.getFloat(ManqalaCombatVariSVProperties.gBMinBorderDispTP.ordinal(), 0), sVHeight);
			float gameBoardMinBorderVDisplayBottom = Util.translateLenght(sVLevelZ, sVHeight * sVProperties.getFloat(ManqalaCombatVariSVProperties.gBMinBorderDispBP.ordinal(), 0), sVHeight);

			TextureInfo gbTextureInfo = new TextureInfo(sVProperties.getResourceId(ManqalaCombatVariSVProperties.gBTex.ordinal(), 0), 
					sVProperties.getFloat(ManqalaCombatVariSVProperties.gBTexLS.ordinal(), 0), 
					sVProperties.getFloat(ManqalaCombatVariSVProperties.gBTexTT.ordinal(), 0), 
					sVProperties.getFloat(ManqalaCombatVariSVProperties.gBTexRS.ordinal(), 0), 
					sVProperties.getFloat(ManqalaCombatVariSVProperties.gBTexBT.ordinal(), 0));
			
			PolygonInfo gBPolygonInfo = Util.insideLocatePolygonCenterCenter(new Point2D(0.0f, (gameBoardMinBorderVDisplayBottom - gameBoardMinBorderVDisplayTop) / 2.0f), 
					Util.getMaxX(sVWidth, sVHeight, sVLevelZ) * (1.0f - sVProperties.getFloat(ManqalaCombatVariSVProperties.gBMinBorderDispLRP.ordinal(),0)),
					Util.getMaxY(sVLevelZ) - (gameBoardMinBorderVDisplayTop + gameBoardMinBorderVDisplayBottom) / 2.0f,
					gbTextureInfo.getAspectRatio());

			if(gBPolygonInfo != null) {
				sVBlank.setGameBoardModel(new ManqalaVariGameBoardModel(new Rectangle(sVLevelZ,
						ResBitmapManager.getInstance().getBitmapId(gbTextureInfo.getTexId()),
						gBPolygonInfo.getLeftX(), 
						gBPolygonInfo.getTopY(), 
						gBPolygonInfo.getRightX(), 
						gBPolygonInfo.getBottomY(),
						gbTextureInfo.getLeftS(), 
						gbTextureInfo.getTopT(), 
						gbTextureInfo.getRightS(), 
						gbTextureInfo.getBottomT())));
				
				sVBlank.getSVRootModel().addChildModel(sVBlank.getGameBoardModel(), false);	
			}
			else {
				Log.e(logTag, "Error while locating GBBackDropOnSV!");
			}		
		}
	}

	private void buildCombatInfoBackDropOnScene(TypedArray sVProperties, ManqalaCombatVariSVBlank sVBlank, float sVLevelZ, float sVWidth, float sVHeight) {
		if(sVBlank.getGameBoardModel() != null) {
			sVBlank.setCombatInfoTextColor(sVProperties.getColor(ManqalaCombatVariSVProperties.cITextColor.ordinal(), 0));
			sVBlank.setCombatInfoErrorTextColor(sVProperties.getColor(ManqalaCombatVariSVProperties.cIErrorTextColor.ordinal(), 0));

			float halfAvailableCombatInfoBackDropWidth = Util.getMaxX(sVWidth, sVHeight, sVLevelZ) - Util.translateLenght(sVLevelZ, sVWidth * sVProperties.getFloat(ManqalaCombatVariSVProperties.cIMinBorderDispLRP.ordinal(), 0), sVHeight);

			TextureInfo cILRTextureInfo = new TextureInfo(sVProperties.getResourceId(ManqalaCombatVariSVProperties.cILRTex.ordinal(), 0), 
					sVProperties.getFloat(ManqalaCombatVariSVProperties.cILRTexLS.ordinal(), 0), 
					sVProperties.getFloat(ManqalaCombatVariSVProperties.cILRTexTT.ordinal(), 0), 
					sVProperties.getFloat(ManqalaCombatVariSVProperties.cILRTexRS.ordinal(), 0), 
					sVProperties.getFloat(ManqalaCombatVariSVProperties.cILRTexBT.ordinal(), 0));
			
			TextureInfo cIMTextureInfo = new TextureInfo(sVProperties.getResourceId(ManqalaCombatVariSVProperties.cIMTex.ordinal(), 0));
			
			float availableHeight = Util.getMaxY(sVLevelZ) - Math.abs(sVBlank.getGameBoardModel().getPolygon().getBottomY()) - 2.0f * Util.translateLenght(sVLevelZ, sVHeight * sVProperties.getFloat(ManqalaCombatVariSVProperties.cIMinBorderDispBP.ordinal(), 0), sVHeight);
			
			sVBlank.setCombatInfoTextModel(Util.generateTextPlateModel(sVLevelZ, 
					-halfAvailableCombatInfoBackDropWidth, 
					sVBlank.getGameBoardModel().getPolygon().getBottomY() - Util.translateLenght(sVLevelZ, sVHeight * sVProperties.getFloat(ManqalaCombatVariSVProperties.cIMinBorderDispBP.ordinal(), 0), sVHeight), 
					availableHeight, 
					2.0f * halfAvailableCombatInfoBackDropWidth, 
					availableHeight * sVProperties.getFloat(ManqalaCombatVariSVProperties.cIMinBorderTextETBP.ordinal(), 0),																	
					sVBlank.getSVRootModel(), 
					cILRTextureInfo, 
					cIMTextureInfo));
		}
	}

	private void buildCellsBackDropsOnGameBoard(TypedArray sVProperties, ManqalaCombatVariSVBlank sVBlank, float sVLevelZ, float sVWidth, float sVHeight) {
		if(sVBlank.getGameBoardModel() != null) {	
			float borderCellsVGameBoardLeftRight = sVBlank.getGameBoardModel().getPolygon().getWidth() * sVProperties.getFloat(ManqalaCombatVariSVProperties.cMinBorderGBLRP.ordinal(), 0);				
			float borderCellVCellX = sVBlank.getGameBoardModel().getPolygon().getWidth() * sVProperties.getFloat(ManqalaCombatVariSVProperties.cMinBorderXP.ordinal(), 0);				

			float borderCellsVGameBoardTopBottom = sVBlank.getGameBoardModel().getPolygon().getHeight() * sVProperties.getFloat(ManqalaCombatVariSVProperties.cMinBorderGBTBP.ordinal(), 0);
			float borderCellsVCellsY = sVBlank.getGameBoardModel().getPolygon().getHeight() * (sVProperties.getFloat(ManqalaCombatVariSVProperties.cMinBorderYP.ordinal(), 0) + sVProperties.getFloat(ManqalaCombatVariSVProperties.cMinBorderCYP.ordinal(), 0));

			float cellVWarehouseXMult = sVProperties.getFloat(ManqalaCombatVariSVProperties.cWXMult.ordinal(), 0);
			
			float availableCellWidth = (sVBlank.getGameBoardModel().getPolygon().getWidth() - borderCellsVGameBoardLeftRight * SVari.COUNT_OPPONENTS) / ((float) SVari.CELLS_COUNT + SVari.COUNT_OPPONENTS * cellVWarehouseXMult);		
			float availableWarehouseHeight = sVBlank.getGameBoardModel().getPolygon().getHeight() - borderCellsVGameBoardTopBottom * SVari.COUNT_OPPONENTS;

			float availableWarehouseLeftX = sVBlank.getGameBoardModel().getPolygon().getLeftX() + borderCellsVGameBoardLeftRight;
			float availableWarehouseTopY = sVBlank.getGameBoardModel().getPolygon().getTopY() - borderCellsVGameBoardTopBottom;

			float availableWarehouseWidth = availableCellWidth * cellVWarehouseXMult - borderCellVCellX / 2.0f;
			
			TextureInfo cWTextureInfo = new TextureInfo(sVProperties.getResourceId(ManqalaCombatVariSVProperties.cWTex.ordinal(), 0), 
					sVProperties.getFloat(ManqalaCombatVariSVProperties.cWTexLS.ordinal(), 0), 
					sVProperties.getFloat(ManqalaCombatVariSVProperties.cWTexTT.ordinal(), 0), 
					sVProperties.getFloat(ManqalaCombatVariSVProperties.cWTexRS.ordinal(), 0), 
					sVProperties.getFloat(ManqalaCombatVariSVProperties.cWTexBT.ordinal(), 0));
			
			PolygonInfo firstWarehousePolygonInfo = Util.insideLocatePolygonCenterCenter(	availableWarehouseLeftX, 
					availableWarehouseTopY, 
					availableWarehouseWidth, 
					availableWarehouseHeight, 
					cWTextureInfo.getAspectRatio());

			if(firstWarehousePolygonInfo != null) {
				ManqalaVariCellModel[] cellsModels = new ManqalaVariCellModel[(int) SVari.COUNT_OPPONENTS * (SVari.CELLS_COUNT + 1)];

				ManqalaVariCellModel firstWarehouseModel = new ManqalaVariCellModel(new Rectangle(sVLevelZ, 
						ResBitmapManager.getInstance().getBitmapId(cWTextureInfo.getTexId()), 
						firstWarehousePolygonInfo.getLeftX(), 
						firstWarehousePolygonInfo.getTopY(), 
						firstWarehousePolygonInfo.getRightX(), 
						firstWarehousePolygonInfo.getBottomY(),
						cWTextureInfo.getLeftS(),
						cWTextureInfo.getTopT(),
						cWTextureInfo.getRightS(),
						cWTextureInfo.getBottomT()), 
						true);
				sVBlank.getGameBoardModel().addChildModel(firstWarehouseModel, false);
				cellsModels[SVari.CELLS_COUNT] = firstWarehouseModel;

				firstWarehousePolygonInfo.moveXOn(firstWarehousePolygonInfo.getWidth() + 2.0f * (sVBlank.getGameBoardModel().getPolygon().getCentralPoint().getX() - firstWarehousePolygonInfo.getRightX()));
				
				ManqalaVariCellModel secondWarehouseModel = new ManqalaVariCellModel(new Rectangle(	sVLevelZ, 
						ResBitmapManager.getInstance().getBitmapId(cWTextureInfo.getTexId()), 
						firstWarehousePolygonInfo.getLeftX(), 
						firstWarehousePolygonInfo.getTopY(), 
						firstWarehousePolygonInfo.getRightX(), 
						firstWarehousePolygonInfo.getBottomY(),
						cWTextureInfo.getLeftS(),
						cWTextureInfo.getTopT(),
						cWTextureInfo.getRightS(),
						cWTextureInfo.getBottomT()),
						true);
				
				sVBlank.getGameBoardModel().addChildModel(secondWarehouseModel, false);
				cellsModels[(int) SVari.COUNT_OPPONENTS * SVari.CELLS_COUNT + 1] = secondWarehouseModel;			

				float availableCellLeftX = availableWarehouseLeftX + availableWarehouseWidth + borderCellVCellX;				

				TextureInfo cTextureInfo = new TextureInfo(sVProperties.getResourceId(ManqalaCombatVariSVProperties.cTex.ordinal(), 0), 
						sVProperties.getFloat(ManqalaCombatVariSVProperties.cTexLS.ordinal(), 0), 
						sVProperties.getFloat(ManqalaCombatVariSVProperties.cTexTT.ordinal(), 0), 
						sVProperties.getFloat(ManqalaCombatVariSVProperties.cTexRS.ordinal(), 0), 
						sVProperties.getFloat(ManqalaCombatVariSVProperties.cTexBT.ordinal(), 0));
				
				PolygonInfo firstPosSixCellPolygonInfo = Util.insideLocatePolygonCenterCenter(	availableCellLeftX, 
						availableWarehouseTopY, 
						availableCellWidth - borderCellVCellX, 
						(availableWarehouseHeight - borderCellsVCellsY) / SVari.COUNT_OPPONENTS, 
						cTextureInfo.getAspectRatio());
				
				TextureInfo cSTextureInfo = new TextureInfo(sVProperties.getResourceId(ManqalaCombatVariSVProperties.cSTex.ordinal(), 0), 
						sVProperties.getFloat(ManqalaCombatVariSVProperties.cSTexLS.ordinal(), 0), 
						sVProperties.getFloat(ManqalaCombatVariSVProperties.cSTexTT.ordinal(), 0), 
						sVProperties.getFloat(ManqalaCombatVariSVProperties.cSTexRS.ordinal(), 0), 
						sVProperties.getFloat(ManqalaCombatVariSVProperties.cSTexBT.ordinal(), 0));
				
				if(firstPosSixCellPolygonInfo != null) {
					float cellsXOffset = 2.0f * (firstPosSixCellPolygonInfo.getLeftX() - availableCellLeftX) + firstPosSixCellPolygonInfo.getWidth() + borderCellVCellX;
					float cellsYOffset = -(firstPosSixCellPolygonInfo.getHeight() + 2.0f * (availableWarehouseTopY - firstPosSixCellPolygonInfo.getTopY()) + borderCellsVCellsY);

					int firstCellPozitionId;
					for(byte b = 0; b < SVari.CELLS_COUNT; b++) {
						firstCellPozitionId = SVari.CELLS_COUNT - (b + 1);
						ManqalaVariCellModel firstCellModel = new ManqalaVariCellModel(new Rectangle(	sVLevelZ, 
								ResBitmapManager.getInstance().getBitmapId(cTextureInfo.getTexId()), 
								firstPosSixCellPolygonInfo.getLeftX() + (float) b * cellsXOffset, 
								firstPosSixCellPolygonInfo.getTopY(), 
								firstPosSixCellPolygonInfo.getRightX() + (float) b * cellsXOffset,
								firstPosSixCellPolygonInfo.getBottomY(),
								cTextureInfo.getLeftS(),
								cTextureInfo.getTopT(),
								cTextureInfo.getRightS(),
								cTextureInfo.getBottomT()),
								false);
						
						firstCellModel.setCellSelectedModel(new Rectangle(sVLevelZ, 
								ResBitmapManager.getInstance().getBitmapId(cSTextureInfo.getTexId()), 
								AbstractPolygon.TRANSPARENT_ALPHA, 
								firstPosSixCellPolygonInfo.getLeftX() + (float) b * cellsXOffset, 
								firstPosSixCellPolygonInfo.getTopY(), 
								firstPosSixCellPolygonInfo.getRightX() + (float) b * cellsXOffset,
								firstPosSixCellPolygonInfo.getBottomY(),
								cSTextureInfo.getLeftS(),
								cSTextureInfo.getTopT(),
								cSTextureInfo.getRightS(),
								cSTextureInfo.getBottomT()));
						
						sVBlank.getGameBoardModel().addChildModel(firstCellModel, false);
						cellsModels[firstCellPozitionId] = firstCellModel;

						ManqalaVariCellModel secondCellModel = new ManqalaVariCellModel(new Rectangle(	sVLevelZ, 
								ResBitmapManager.getInstance().getBitmapId(cTextureInfo.getTexId()), 
								firstPosSixCellPolygonInfo.getLeftX() + (float) b * cellsXOffset, 
								firstPosSixCellPolygonInfo.getTopY() + cellsYOffset, 
								firstPosSixCellPolygonInfo.getRightX() + (float) b * cellsXOffset,
								firstPosSixCellPolygonInfo.getBottomY() + cellsYOffset,
								cTextureInfo.getLeftS(),
								cTextureInfo.getTopT(),
								cTextureInfo.getRightS(),
								cTextureInfo.getBottomT()),
								false);
						
						secondCellModel.setCellSelectedModel(new Rectangle(sVLevelZ, 
								ResBitmapManager.getInstance().getBitmapId(cSTextureInfo.getTexId()), 
								AbstractPolygon.TRANSPARENT_ALPHA, 
								firstPosSixCellPolygonInfo.getLeftX() + (float) b * cellsXOffset, 
								firstPosSixCellPolygonInfo.getTopY() + cellsYOffset, 
								firstPosSixCellPolygonInfo.getRightX() + (float) b * cellsXOffset,
								firstPosSixCellPolygonInfo.getBottomY() + cellsYOffset,
								cSTextureInfo.getLeftS(),
								cSTextureInfo.getTopT(),
								cSTextureInfo.getRightS(),
								cSTextureInfo.getBottomT()));
						
						sVBlank.getGameBoardModel().addChildModel(secondCellModel, false);
						cellsModels[SVari.CELLS_COUNT + 1 + b] = secondCellModel;						
					}
					
					sVBlank.getGameBoardModel().setCellsModels(cellsModels);
				}
				else {
					Log.e(logTag, "Error while locating CellsBackDropsOnGameBoard (firstPosSixCellPolygonInfo)!");
				}
			}
			else {
				Log.e(logTag, "Error while locating CellsBackDropsOnGameBoard (firstWarehousePolygonInfo)!");
			}
		}
	}
	
	private void buildCellsPointsBackDropsOnGameBoard(TypedArray sVProperties, ManqalaCombatVariSVBlank sVBlank, float sVLevelZ, float sVWidth, float sVHeight) {
		if(sVBlank.getGameBoardModel() != null && sVBlank.getGameBoardModel().getCellsModels() != null) {
			int[] cellPointsTextColors = new int[(int) SVari.COUNT_OPPONENTS];
			cellPointsTextColors[0] = sVProperties.getColor(ManqalaCombatVariSVProperties.cPFirstTextColor.ordinal(), 0);
			cellPointsTextColors[1] = sVProperties.getColor(ManqalaCombatVariSVProperties.cPSecondTextColor.ordinal(), 0);

			float cellPointsMinBorderVGameBoardEdge = sVBlank.getGameBoardModel().getPolygon().getHeight() * sVProperties.getFloat(ManqalaCombatVariSVProperties.cPMinBorderGBEP.ordinal(), 0);
			float cellPointsMinBorderVCellsEdge = sVBlank.getGameBoardModel().getPolygon().getHeight() * sVProperties.getFloat(ManqalaCombatVariSVProperties.cPMinBorderCEP.ordinal(), 0);

			float availableTopY;
			float availableHeight;

			TextureInfo cPLRTextureInfo = new TextureInfo(sVProperties.getResourceId(ManqalaCombatVariSVProperties.cPLRTex.ordinal(), 0), 
					sVProperties.getFloat(ManqalaCombatVariSVProperties.cPLRTexLS.ordinal(), 0), 
					sVProperties.getFloat(ManqalaCombatVariSVProperties.cPLRTexTT.ordinal(), 0), 
					sVProperties.getFloat(ManqalaCombatVariSVProperties.cPLRTexRS.ordinal(), 0), 
					sVProperties.getFloat(ManqalaCombatVariSVProperties.cPLRTexBT.ordinal(), 0));
			
			TextureInfo cPMTextureInfo = new TextureInfo(sVProperties.getResourceId(ManqalaCombatVariSVProperties.cPMTex.ordinal(), 0));
			
			GLumpSVModel<?>[] cellsPointsTextModels = new GLumpSVModel[(int) SVari.COUNT_OPPONENTS * (SVari.CELLS_COUNT + 1)];			
			for(int i = 0; i < sVBlank.getGameBoardModel().getCellsModels().length; i++) {
				ManqalaVariCellModel cellModel = sVBlank.getGameBoardModel().getCellsModels()[i];				
				if(cellModel != null) {
					if(i <= SVari.CELLS_COUNT) {
						availableTopY = sVBlank.getGameBoardModel().getPolygon().getTopY() - cellPointsMinBorderVGameBoardEdge;
						availableHeight = availableTopY - (cellModel.getPolygon().getTopY() + cellPointsMinBorderVCellsEdge);

					}
					else {
						availableTopY = cellModel.getPolygon().getBottomY() - cellPointsMinBorderVCellsEdge;
						availableHeight = availableTopY - (sVBlank.getGameBoardModel().getPolygon().getBottomY() + cellPointsMinBorderVGameBoardEdge);
					}

					GLumpSVModel<Rectangle> cellPointsModel = Util.generateTextPlateModel(	sVLevelZ, 
							cellModel.getPolygon().getLeftX(), 
							availableTopY, 
							availableHeight, 
							cellModel.getPolygon().getWidth(), 
							availableHeight * sVProperties.getFloat(ManqalaCombatVariSVProperties.cPMinBorderTextETBP.ordinal(), 0),																	
							sVBlank.getGameBoardModel(), 
							cPLRTextureInfo, 
							cPMTextureInfo);

					if(cellPointsModel != null) {				
						cellsPointsTextModels[i] = cellPointsModel;
					}
					else {
						Log.e(logTag, "Error while locating CellsPointsBackDropsOnGameBoard!");
					}
				}
			}
			
			sVBlank.getGameBoardModel().setCellsPointsModels(cellsPointsTextModels);
			sVBlank.getGameBoardModel().setCellPointsTextColor(cellPointsTextColors);
		}
	}

	private void buildPlayersInfosBackDropsOnGameBoard(TypedArray sVProperties, ManqalaCombatVariSVBlank sVBlank, float sVLevelZ, float sVWidth, float sVHeight) {
		if(sVBlank.getGameBoardModel() != null && sVBlank.getGameBoardModel().getCellsModels() != null && sVBlank.getGameBoardModel().getCellsModels()[(int) SVari.COUNT_OPPONENTS * SVari.CELLS_COUNT + 1] != null && sVBlank.getGameBoardModel().getCellsModels()[SVari.CELLS_COUNT] != null) {
			int[] playerInfoTextColors = new int[(int) SVari.COUNT_OPPONENTS];
			playerInfoTextColors[0] = sVProperties.getColor(ManqalaCombatVariSVProperties.pIFirstTextColor.ordinal(), 0);
			playerInfoTextColors[1] = sVProperties.getColor(ManqalaCombatVariSVProperties.pISecondTextColor.ordinal(), 0);

			sVBlank.getGameBoardModel().setPlayerInfoTextColors(playerInfoTextColors);
			
			float playerInfoMinBorderVGameBoardEdge = sVBlank.getGameBoardModel().getPolygon().getHeight() * sVProperties.getFloat(ManqalaCombatVariSVProperties.pIMinBorderGBEP.ordinal(), 0);
			float playerInfoMinBorderVCellsEdge = sVBlank.getGameBoardModel().getPolygon().getHeight() * sVProperties.getFloat(ManqalaCombatVariSVProperties.pIMinBorderCEP.ordinal(), 0);

			GLumpSVModel<?>[] playerInfoTextModels = new GLumpSVModel[(int) SVari.COUNT_OPPONENTS];

			TextureInfo pILRTextureInfo = new TextureInfo(sVProperties.getResourceId(ManqalaCombatVariSVProperties.pILRTex.ordinal(), 0), 
					sVProperties.getFloat(ManqalaCombatVariSVProperties.pILRTexLS.ordinal(), 0), 
					sVProperties.getFloat(ManqalaCombatVariSVProperties.pILRTexTT.ordinal(), 0), 
					sVProperties.getFloat(ManqalaCombatVariSVProperties.pILRTexRS.ordinal(), 0), 
					sVProperties.getFloat(ManqalaCombatVariSVProperties.pILRTexBT.ordinal(), 0));			
			
			TextureInfo pIMTextureInfo = new TextureInfo(sVProperties.getResourceId(ManqalaCombatVariSVProperties.pIMTex.ordinal(), 0));
			
			float availableTopY = sVBlank.getGameBoardModel().getPolygon().getTopY() - playerInfoMinBorderVGameBoardEdge;	
			float availableHeight = availableTopY - (sVBlank.getGameBoardModel().getCellsModels()[(int) SVari.COUNT_OPPONENTS * SVari.CELLS_COUNT + 1].getPolygon().getTopY() + playerInfoMinBorderVCellsEdge);
			playerInfoTextModels[0] = Util.generateTextPlateModel(	sVLevelZ, 
					sVBlank.getGameBoardModel().getCellsModels()[(int) SVari.COUNT_OPPONENTS * SVari.CELLS_COUNT + 1].getPolygon().getLeftX(), 
					availableTopY, 
					availableHeight, 
					sVBlank.getGameBoardModel().getCellsModels()[(int) SVari.COUNT_OPPONENTS * SVari.CELLS_COUNT + 1].getPolygon().getWidth(), 
					availableHeight	* sVProperties.getFloat(ManqalaCombatVariSVProperties.pIMinBorderTextETBP.ordinal(), 0),																	
					sVBlank.getGameBoardModel(), 
					pILRTextureInfo, 
					pIMTextureInfo);

			if(playerInfoTextModels[0] != null) {
				availableTopY = sVBlank.getGameBoardModel().getCellsModels()[SVari.CELLS_COUNT].getPolygon().getBottomY() - playerInfoMinBorderVCellsEdge;
				availableHeight = availableTopY - (sVBlank.getGameBoardModel().getPolygon().getBottomY() + playerInfoMinBorderVGameBoardEdge);
				playerInfoTextModels[1] = Util.generateTextPlateModel(	sVLevelZ, 
						sVBlank.getGameBoardModel().getCellsModels()[SVari.CELLS_COUNT].getPolygon().getLeftX(), 
						availableTopY, 
						availableHeight,
						sVBlank.getGameBoardModel().getCellsModels()[SVari.CELLS_COUNT].getPolygon().getWidth(), 
						availableHeight * sVProperties.getFloat(ManqalaCombatVariSVProperties.pIMinBorderTextETBP.ordinal(), 0),																	
						sVBlank.getGameBoardModel(), 
						pILRTextureInfo, 
						pIMTextureInfo);

				if(playerInfoTextModels[1] == null) {					
					Log.e(logTag, "Error while locating PlayersInfosBackDropsOnGameBoard(playerInfoTextModels[1])!");
				}
				else {
					sVBlank.getGameBoardModel().setPlayerInfoTextModels(playerInfoTextModels);
				}
			}
			else {
				Log.e(logTag, "Error while locating PlayersInfosBackDropsOnGameBoard(playerInfoTextModels[0])!");
			}
		}
	}

	private void buildGrainsOnCells(TypedArray sVProperties, ManqalaCombatVariSVBlank sVBlank, float sVLevelZ, float sVWidth, float sVHeight) {
		if(sVBlank.getGameBoardModel() != null && sVBlank.getGameBoardModel().getCellsModels() != null) {
			float grainModelSideSize = sVBlank.getGameBoardModel().getPolygon().getWidth() * sVProperties.getFloat(ManqalaCombatVariSVProperties.gSizeGBP.ordinal(), 0);
			float grainsMinBorderVCellEdgePart = sVProperties.getFloat(ManqalaCombatVariSVProperties.gMinBorderCEP.ordinal(), 0);
			
			TextureInfo gTextureInfo = new TextureInfo(sVProperties.getResourceId(ManqalaCombatVariSVProperties.gTex.ordinal(), 0), 
					sVProperties.getFloat(ManqalaCombatVariSVProperties.gTexLS.ordinal(), 0), 
					sVProperties.getFloat(ManqalaCombatVariSVProperties.gTexTT.ordinal(), 0), 
					sVProperties.getFloat(ManqalaCombatVariSVProperties.gTexRS.ordinal(), 0), 
					sVProperties.getFloat(ManqalaCombatVariSVProperties.gTexBT.ordinal(), 0));	
			
			float borderGrainsVCellEdge;
			float grainsMinLeftX;
			float grainsMaxTopY;
			
			int warehouseGrainsCount = (int) (SVari.CELL_INIT_GRAINS * SVari.CELLS_COUNT * SVari.COUNT_OPPONENTS);
			int grainsCount;
			
			for (ManqalaVariCellModel cellModel : sVBlank.getGameBoardModel().getCellsModels()) {
				if(cellModel != null) {
					borderGrainsVCellEdge = cellModel.getPolygon().getWidth() * grainsMinBorderVCellEdgePart;

					grainsMinLeftX = cellModel.getPolygon().getLeftX() + borderGrainsVCellEdge;
					grainsMaxTopY = cellModel.getPolygon().getTopY() - borderGrainsVCellEdge;

					if(cellModel.isWarehouse()) {
						grainsCount = warehouseGrainsCount;
					}
					else {
						grainsCount = SVari.CELL_MAX_GRAINS;
					}
					
					ArrayList<GLumpSVModel<?>> grainModels = new ArrayList<GLumpSVModel<?>>();
					
					SecureRandom rand = new SecureRandom();
					for (int i = 0; i < grainsCount; i++) {
						grainModels.add(cellModel.addChildModel(new Square(sVLevelZ, 
								ResBitmapManager.getInstance().getBitmapId(gTextureInfo.getTexId()), 
								AbstractPolygon.TRANSPARENT_ALPHA,
								grainsMinLeftX + rand.nextFloat() * (cellModel.getPolygon().getRightX() - borderGrainsVCellEdge - grainModelSideSize - grainsMinLeftX), 
								grainsMaxTopY - rand.nextFloat() * (grainsMaxTopY - (cellModel.getPolygon().getBottomY() + borderGrainsVCellEdge + grainModelSideSize)), 
								grainModelSideSize,
								gTextureInfo.getLeftS(),
								gTextureInfo.getTopT(),
								gTextureInfo.getRightS(),
								gTextureInfo.getBottomT()),
								false));
					}
					
					cellModel.setGrainModels(grainModels);
				}
			}
		}
	}
	
	private void buildHeader(TypedArray sVProperties, ManqalaCombatVariSVBlank sVBlank, float sVLevelZ, float sVWidth, float sVHeight) {
		if(sVBlank.getGameBoardModel() != null && sVBlank.getGameBoardModel().getCellsModels() != null && sVBlank.getGameBoardModel().getCellsModels()[(int) SVari.CELLS_COUNT] != null && sVBlank.getGameBoardModel().getCellsModels()[(int) SVari.COUNT_OPPONENTS * SVari.CELLS_COUNT + 1] != null) {
			float availableLeftX = sVBlank.getGameBoardModel().getCellsModels()[(int) SVari.CELLS_COUNT].getPolygon().getRightX();
			float initHeightPos = (Util.getMaxY(sVLevelZ) - sVBlank.getGameBoardModel().getPolygon().getTopY());
			float availableTopY = Util.getMaxY(sVLevelZ) - 0.1f * initHeightPos;
			float availableWidthPos = sVBlank.getGameBoardModel().getCellsModels()[(int) SVari.COUNT_OPPONENTS * SVari.CELLS_COUNT + 1].getPolygon().getLeftX() - sVBlank.getGameBoardModel().getCellsModels()[(int) SVari.CELLS_COUNT].getPolygon().getRightX();
			float availableHeightPos = 0.8f * initHeightPos;
			
			TextureInfo lTextureInfo = new TextureInfo(sVProperties.getResourceId(ManqalaCombatVariSVProperties.lTex.ordinal(), 0), 
					sVProperties.getFloat(ManqalaCombatVariSVProperties.lTexLS.ordinal(), 0), 
					sVProperties.getFloat(ManqalaCombatVariSVProperties.lTexTT.ordinal(), 0), 
					sVProperties.getFloat(ManqalaCombatVariSVProperties.lTexRS.ordinal(), 0), 
					sVProperties.getFloat(ManqalaCombatVariSVProperties.lTexBT.ordinal(), 0));
			
			PolygonInfo lPolygonInfo = Util.insideLocatePolygonCenterCenter(
					availableLeftX, 
					availableTopY, 
					availableWidthPos, 
					availableHeightPos, 
					lTextureInfo.getAspectRatio());
			
			sVBlank.getSVRootModel().addChildModel(
					new Rectangle(
							sVLevelZ, 
							ResBitmapManager.getInstance().getBitmapId(lTextureInfo.getTexId()),
							lPolygonInfo.getLeftX(),
							lPolygonInfo.getTopY(), 
							lPolygonInfo.getRightX(),
							lPolygonInfo.getBottomY(),
							lTextureInfo.getLeftS(), 
							lTextureInfo.getTopT(), 
							lTextureInfo.getRightS(), 
							lTextureInfo.getBottomT()), 
					false);
		}
	}
	
	private void buildLoadingFramesOnScene(TypedArray sVProperties, ManqalaCombatVariSVBlank sVBlank, float sVLevelZ, float sVWidth, float sVHeight) {
		if(sVBlank.getGameBoardModel() != null && sVBlank.getGameBoardModel().getCellsModels() != null && sVBlank.getGameBoardModel().getCellsModels()[(int) SVari.COUNT_OPPONENTS * SVari.CELLS_COUNT + 1] != null) {
			ArrayList<GLumpSVModel<?>> loadingFramesModels = new ArrayList<GLumpSVModel<?>>();
			
			float availableLeftX = sVBlank.getGameBoardModel().getCellsModels()[(int) SVari.COUNT_OPPONENTS * SVari.CELLS_COUNT + 1].getPolygon().getLeftX() + sVBlank.getGameBoardModel().getCellsModels()[(int) SVari.COUNT_OPPONENTS * SVari.CELLS_COUNT + 1].getPolygon().getWidth() / 4.0f;
			float availableTopY = Util.getMaxY(sVLevelZ);
			float availableWidthPos = sVBlank.getGameBoardModel().getCellsModels()[(int) SVari.COUNT_OPPONENTS * SVari.CELLS_COUNT + 1].getPolygon().getWidth() / 2.0f;
			float availableHeightPos = Util.getMaxY(sVLevelZ) - sVBlank.getGameBoardModel().getPolygon().getTopY();
			
			TextureInfo lFTextureInfo = new TextureInfo(sVProperties.getResourceId(ManqalaCombatVariSVProperties.lF1Tex.ordinal(), 0), 
					sVProperties.getFloat(ManqalaCombatVariSVProperties.lF1TexLS.ordinal(), 0), 
					sVProperties.getFloat(ManqalaCombatVariSVProperties.lF1TexTT.ordinal(), 0), 
					sVProperties.getFloat(ManqalaCombatVariSVProperties.lF1TexRS.ordinal(), 0), 
					sVProperties.getFloat(ManqalaCombatVariSVProperties.lF1TexBT.ordinal(), 0));
			
			PolygonInfo lFPolygonInfo = Util.insideLocatePolygonCenterCenter(
					availableLeftX, 
					availableTopY, 
					availableWidthPos, 
					availableHeightPos, 
					lFTextureInfo.getAspectRatio());
			
			loadingFramesModels.add(sVBlank.getSVRootModel().addChildModel(
					new Rectangle(
							sVLevelZ, 
							ResBitmapManager.getInstance().getBitmapId(lFTextureInfo.getTexId()), 
							AbstractPolygon.TRANSPARENT_ALPHA, 
							lFPolygonInfo.getLeftX(), 
							lFPolygonInfo.getTopY(), 
							lFPolygonInfo.getRightX(), 
							lFPolygonInfo.getBottomY(), 
							lFTextureInfo.getLeftS(), 
							lFTextureInfo.getTopT(), 
							lFTextureInfo.getRightS(), 
							lFTextureInfo.getBottomT()), 
					false));
			
			lFTextureInfo = new TextureInfo(sVProperties.getResourceId(ManqalaCombatVariSVProperties.lF2Tex.ordinal(), 0), 
					sVProperties.getFloat(ManqalaCombatVariSVProperties.lF2TexLS.ordinal(), 0), 
					sVProperties.getFloat(ManqalaCombatVariSVProperties.lF2TexTT.ordinal(), 0), 
					sVProperties.getFloat(ManqalaCombatVariSVProperties.lF2TexRS.ordinal(), 0), 
					sVProperties.getFloat(ManqalaCombatVariSVProperties.lF2TexBT.ordinal(), 0));
			
			loadingFramesModels.add(sVBlank.getSVRootModel().addChildModel(
					new Rectangle(
							sVLevelZ, 
							ResBitmapManager.getInstance().getBitmapId(lFTextureInfo.getTexId()), 
							AbstractPolygon.TRANSPARENT_ALPHA, 
							lFPolygonInfo.getLeftX(), 
							lFPolygonInfo.getTopY(), 
							lFPolygonInfo.getRightX(), 
							lFPolygonInfo.getBottomY(), 
							lFTextureInfo.getLeftS(), 
							lFTextureInfo.getTopT(), 
							lFTextureInfo.getRightS(), 
							lFTextureInfo.getBottomT()), 
					false));
			
			lFTextureInfo = new TextureInfo(sVProperties.getResourceId(ManqalaCombatVariSVProperties.lF3Tex.ordinal(), 0), 
					sVProperties.getFloat(ManqalaCombatVariSVProperties.lF3TexLS.ordinal(), 0), 
					sVProperties.getFloat(ManqalaCombatVariSVProperties.lF3TexTT.ordinal(), 0), 
					sVProperties.getFloat(ManqalaCombatVariSVProperties.lF3TexRS.ordinal(), 0), 
					sVProperties.getFloat(ManqalaCombatVariSVProperties.lF3TexBT.ordinal(), 0));
			
			loadingFramesModels.add(sVBlank.getSVRootModel().addChildModel(
					new Rectangle(
							sVLevelZ, 
							ResBitmapManager.getInstance().getBitmapId(lFTextureInfo.getTexId()), 
							AbstractPolygon.TRANSPARENT_ALPHA, 
							lFPolygonInfo.getLeftX(), 
							lFPolygonInfo.getTopY(), 
							lFPolygonInfo.getRightX(), 
							lFPolygonInfo.getBottomY(), 
							lFTextureInfo.getLeftS(), 
							lFTextureInfo.getTopT(), 
							lFTextureInfo.getRightS(), 
							lFTextureInfo.getBottomT()), 
					false));
			
			sVBlank.setLoadingFramesModels(loadingFramesModels);
		}
	}
	
	private void buildPauseButton(TypedArray sVProperties, ManqalaCombatVariSVBlank sVBlank, float sVLevelZ, float sVWidth, float sVHeight) {
		if(sVBlank.getGameBoardModel() != null && sVBlank.getGameBoardModel().getCellsModels() != null && sVBlank.getGameBoardModel().getCellsModels()[(int) SVari.CELLS_COUNT] != null) {
			float availableLeftX = sVBlank.getGameBoardModel().getCellsModels()[(int) SVari.CELLS_COUNT].getPolygon().getLeftX();
			float availableHeightPos = (Util.getMaxY(sVLevelZ) - sVBlank.getGameBoardModel().getPolygon().getTopY());
			float availableTopY = Util.getMaxY(sVLevelZ);
			float availableWidthPos = sVBlank.getGameBoardModel().getCellsModels()[(int) SVari.CELLS_COUNT].getPolygon().getWidth();
			
			sVBlank.setPauseButtonAreaModel(sVBlank.getSVRootModel().addChildModel(
					new Rectangle(
							sVLevelZ, 
							availableLeftX, 
							availableTopY, 
							availableLeftX + availableWidthPos, 
							availableTopY - availableHeightPos),
					false));
			
			availableLeftX += sVBlank.getGameBoardModel().getCellsModels()[(int) SVari.CELLS_COUNT].getPolygon().getWidth() / 4.0f;
			availableTopY -= 0.1f * availableHeightPos;
			availableWidthPos /= 2.0f;
			availableHeightPos *= 0.8f;
			
			TextureInfo pDSTextureInfo = new TextureInfo(sVProperties.getResourceId(ManqalaCombatVariSVProperties.pDTex.ordinal(), 0), 
					sVProperties.getFloat(ManqalaCombatVariSVProperties.pDTexLS.ordinal(), 0), 
					sVProperties.getFloat(ManqalaCombatVariSVProperties.pDTexTT.ordinal(), 0), 
					sVProperties.getFloat(ManqalaCombatVariSVProperties.pDTexRS.ordinal(), 0), 
					sVProperties.getFloat(ManqalaCombatVariSVProperties.pDTexBT.ordinal(), 0));
			
			PolygonInfo pDSPolygonInfo = Util.insideLocatePolygonCenterCenter(
					availableLeftX, 
					availableTopY, 
					availableWidthPos, 
					availableHeightPos, 
					pDSTextureInfo.getAspectRatio());
			
			sVBlank.setPauseDeselectedModel(sVBlank.getSVRootModel().addChildModel(
					new Square(
							sVLevelZ, 
							ResBitmapManager.getInstance().getBitmapId(pDSTextureInfo.getTexId()),
							pDSPolygonInfo.getLeftX(),
							pDSPolygonInfo.getTopY(), 
							pDSPolygonInfo.getWidth(),
							pDSTextureInfo.getLeftS(), 
							pDSTextureInfo.getTopT(), 
							pDSTextureInfo.getRightS(), 
							pDSTextureInfo.getBottomT()), 
					false));
			
			pDSTextureInfo = new TextureInfo(sVProperties.getResourceId(ManqalaCombatVariSVProperties.pSTex.ordinal(), 0), 
					sVProperties.getFloat(ManqalaCombatVariSVProperties.pSTexLS.ordinal(), 0), 
					sVProperties.getFloat(ManqalaCombatVariSVProperties.pSTexTT.ordinal(), 0), 
					sVProperties.getFloat(ManqalaCombatVariSVProperties.pSTexRS.ordinal(), 0), 
					sVProperties.getFloat(ManqalaCombatVariSVProperties.pSTexBT.ordinal(), 0));
			
			sVBlank.setPauseSelectedModel(sVBlank.getSVRootModel().addChildModel(
					new Square(
							sVLevelZ, 
							ResBitmapManager.getInstance().getBitmapId(pDSTextureInfo.getTexId()), 
							AbstractPolygon.TRANSPARENT_ALPHA, 
							pDSPolygonInfo.getLeftX(), 
							pDSPolygonInfo.getTopY(), 
							pDSPolygonInfo.getWidth(), 
							pDSTextureInfo.getLeftS(), 
							pDSTextureInfo.getTopT(), 
							pDSTextureInfo.getRightS(), 
							pDSTextureInfo.getBottomT()), 
					false));
		}
	}
	
	private void buildPausePlate(TypedArray sVProperties, ManqalaCombatVariSVBlank sVBlank, float sVLevelZ, float sVWidth, float sVHeight) {
		if(sVBlank.getSVRootModel() != null) {
			float pPMinBorderDispTB = Util.translateLenght(sVLevelZ, sVHeight * sVProperties.getFloat(ManqalaCombatVariSVProperties.pPMinBorderDispTBP.ordinal(), 0), sVHeight);
			float pPMinBorderDispLR = Util.translateLenght(sVLevelZ, sVWidth * sVProperties.getFloat(ManqalaCombatVariSVProperties.pPMinBorderDispLRP.ordinal(), 0), sVHeight);

			float availablePPHeight = 2.0f * (Util.getMaxY(sVLevelZ) - pPMinBorderDispTB);
			float availablePPWidth = 2.0f * (Util.getMaxX(sVWidth, sVHeight, sVLevelZ) - pPMinBorderDispLR);

			sVBlank.setPausePlateModel(
					new ManqalaVariPausePlateModel(
							new Rectangle(
									sVLevelZ, 
									-availablePPWidth / 2.0f, 
									availablePPHeight / 2.0f, 
									availablePPWidth / 2.0f, 
									-availablePPHeight / 2.0f)));
			
			sVBlank.getSVRootModel().addChildModel(sVBlank.getPausePlateModel(), false);	
			
			TextureInfo pPLRTextureInfo = new TextureInfo(sVProperties.getResourceId(ManqalaCombatVariSVProperties.pPLRTex.ordinal(), 0), 
					sVProperties.getFloat(ManqalaCombatVariSVProperties.pPLRTexLS.ordinal(), 0), 
					sVProperties.getFloat(ManqalaCombatVariSVProperties.pPLRTexTT.ordinal(), 0), 
					sVProperties.getFloat(ManqalaCombatVariSVProperties.pPLRTexRS.ordinal(), 0), 
					sVProperties.getFloat(ManqalaCombatVariSVProperties.pPLRTexBT.ordinal(), 0));
			
			TextureInfo pPMTextureInfo = new TextureInfo(sVProperties.getResourceId(ManqalaCombatVariSVProperties.pPMTex.ordinal(), 0));
			
			sVBlank.getPausePlateModel().setpPTextLabelModel(Util.generateTextLabelModel(
					AbstractPolygon.TRANSPARENT_ALPHA,
					sVLevelZ, 
					-availablePPWidth / 2.0f, 
					availablePPHeight / 2.0f,
					availablePPHeight, 
					availablePPWidth, 
					availablePPHeight * sVProperties.getFloat(ManqalaCombatVariSVProperties.pPMinBorderTextETBP.ordinal(), 0),																	
					sVBlank.getPausePlateModel(), 
					pPLRTextureInfo, 
					pPMTextureInfo));
			
			sVBlank.getPausePlateModel().setpPTextColor(sVProperties.getColor(ManqalaCombatVariSVProperties.pPTextColor.ordinal(), 0));
			
			float bHeightSizePP = availablePPHeight * sVProperties.getFloat(ManqalaCombatVariSVProperties.bHeightSizePPP.ordinal(), 0);
			float bWidthSizePP = availablePPWidth * sVProperties.getFloat(ManqalaCombatVariSVProperties.bWidthSizePPP.ordinal(), 0);
			
			TextureInfo bSLRTextureInfo = new TextureInfo(sVProperties.getResourceId(ManqalaCombatVariSVProperties.bSLRTex.ordinal(), 0), 
					sVProperties.getFloat(ManqalaCombatVariSVProperties.bSLRTexLS.ordinal(), 0), 
					sVProperties.getFloat(ManqalaCombatVariSVProperties.bSLRTexTT.ordinal(), 0), 
					sVProperties.getFloat(ManqalaCombatVariSVProperties.bSLRTexRS.ordinal(), 0), 
					sVProperties.getFloat(ManqalaCombatVariSVProperties.bSLRTexBT.ordinal(), 0));
			
			TextureInfo bSMTextureInfo = new TextureInfo(sVProperties.getResourceId(ManqalaCombatVariSVProperties.bSMTex.ordinal(), 0));
			
			sVBlank.getPausePlateModel().setpPHeaderTextLabelModel(Util.generateTextLabelModel(
					AbstractPolygon.TRANSPARENT_ALPHA,
					sVLevelZ,
					-bWidthSizePP / 2.0f, 
					availablePPHeight / 2.0f + bHeightSizePP / 2.0f,
					bHeightSizePP, 
					bWidthSizePP, 
					bHeightSizePP * sVProperties.getFloat(ManqalaCombatVariSVProperties.bMinBorderTextETBP.ordinal(), 0),																	
					sVBlank.getPausePlateModel(), 
					bSLRTextureInfo, 
					bSMTextureInfo));
			
			sVBlank.getPausePlateModel().setpPHeaderTextColor(sVProperties.getColor(ManqalaCombatVariSVProperties.bSTextColor.ordinal(), 0));
			
			float bMinBorderXPP = availablePPWidth * sVProperties.getFloat(ManqalaCombatVariSVProperties.bMinBorderXPPP.ordinal(), 0);
			
			TextureInfo bDLRTextureInfo = new TextureInfo(sVProperties.getResourceId(ManqalaCombatVariSVProperties.bDLRTex.ordinal(), 0), 
					sVProperties.getFloat(ManqalaCombatVariSVProperties.bDLRTexLS.ordinal(), 0), 
					sVProperties.getFloat(ManqalaCombatVariSVProperties.bDLRTexTT.ordinal(), 0), 
					sVProperties.getFloat(ManqalaCombatVariSVProperties.bDLRTexRS.ordinal(), 0), 
					sVProperties.getFloat(ManqalaCombatVariSVProperties.bDLRTexBT.ordinal(), 0));
			
			TextureInfo bDMTextureInfo = new TextureInfo(sVProperties.getResourceId(ManqalaCombatVariSVProperties.bDMTex.ordinal(), 0));
			
			sVBlank.getPausePlateModel().setpPRestartButton(Util.generateButtonModel(
					AbstractPolygon.TRANSPARENT_ALPHA,
					sVLevelZ,
					-availablePPWidth / 2.0f + bMinBorderXPP, 
					-availablePPHeight / 2.0f + bHeightSizePP / 2.0f,
					bHeightSizePP, 
					bWidthSizePP, 
					bHeightSizePP * sVProperties.getFloat(ManqalaCombatVariSVProperties.bMinBorderTextETBP.ordinal(), 0),																	
					sVBlank.getPausePlateModel(),
					bDLRTextureInfo,
					bDMTextureInfo,
					bSLRTextureInfo, 
					bSMTextureInfo));
						
			sVBlank.getPausePlateModel().setpPContinueButton(Util.generateButtonModel(
					AbstractPolygon.TRANSPARENT_ALPHA,
					sVLevelZ,
					-availablePPWidth / 2.0f + 2.0f * bMinBorderXPP + bWidthSizePP, 
					-availablePPHeight / 2.0f + bHeightSizePP / 2.0f,
					bHeightSizePP, 
					bWidthSizePP, 
					bHeightSizePP * sVProperties.getFloat(ManqalaCombatVariSVProperties.bMinBorderTextETBP.ordinal(), 0),																	
					sVBlank.getPausePlateModel(), 
					bDLRTextureInfo,
					bDMTextureInfo,
					bSLRTextureInfo, 
					bSMTextureInfo));
			
			sVBlank.getPausePlateModel().setpPExitButton(Util.generateButtonModel(
					AbstractPolygon.TRANSPARENT_ALPHA,
					sVLevelZ,
					-availablePPWidth / 2.0f + 3.0f * bMinBorderXPP + 2.0f * bWidthSizePP, 
					-availablePPHeight / 2.0f + bHeightSizePP / 2.0f,
					bHeightSizePP, 
					bWidthSizePP, 
					bHeightSizePP * sVProperties.getFloat(ManqalaCombatVariSVProperties.bMinBorderTextETBP.ordinal(), 0),																	
					sVBlank.getPausePlateModel(), 
					bDLRTextureInfo,
					bDMTextureInfo,
					bSLRTextureInfo, 
					bSMTextureInfo));
			
			sVBlank.getPausePlateModel().setpPBDTextColor(sVProperties.getColor(ManqalaCombatVariSVProperties.bDTextColor.ordinal(), 0));
			sVBlank.getPausePlateModel().setpPBSTextColor(sVProperties.getColor(ManqalaCombatVariSVProperties.bSTextColor.ordinal(), 0));
		}
	}
	
	private enum ManqalaCombatVariSVProperties {
		svTex,
		svTexLS,
		svTexTT,
		svTexRS,
		svTexBT,
		gBMinBorderDispLRP,
		gBMinBorderDispTP,
		gBMinBorderDispBP,
		gBTex,
		gBTexLS,
		gBTexTT,
		gBTexRS,
		gBTexBT,
		cIMinBorderDispLRP,
		cIMinBorderDispBP,
		cIMinBorderTextETBP,
		cITextColor,
		cIErrorTextColor,
		cILRTex,
		cILRTexLS,
		cILRTexTT,
		cILRTexRS,
		cILRTexBT,
		cIMTex,
		cMinBorderGBLRP,
		cMinBorderGBTBP,
		cMinBorderXP,
		cMinBorderYP,
		cMinBorderCYP,
		cWXMult,
		cWTex,
		cWTexLS,
		cWTexTT,
		cWTexRS,
		cWTexBT,
		cTex,
		cTexLS,
	    cTexTT,
	    cTexRS,
	    cTexBT,
	    cSTex,
	    cSTexLS,
	    cSTexTT,
	    cSTexRS,
	    cSTexBT,
	    cPMinBorderGBEP,
	    cPMinBorderCEP,
	    cPMinBorderTextETBP,
	    cPFirstTextColor,
	    cPSecondTextColor,
	    cPLRTex,
	    cPLRTexLS,
	    cPLRTexTT,
	    cPLRTexRS,
	    cPLRTexBT,
	    cPMTex,
	    pIMinBorderGBEP,
	    pIMinBorderCEP,
	    pIMinBorderTextETBP,
	    pIFirstTextColor,
	    pISecondTextColor,
	    pILRTex,
	    pILRTexLS,
	    pILRTexTT,
	    pILRTexRS,
	    pILRTexBT,
	    pIMTex,
	    gSizeGBP,
	    gMinBorderCEP,
	    gTex,
	    gTexLS,
	    gTexTT,
	    gTexRS,
	    gTexBT,
	    lTex,
	    lTexLS,
	    lTexTT,
	    lTexRS,
	    lTexBT,
	    lF1Tex,
	    lF1TexLS,
	    lF1TexTT,
	    lF1TexRS,
	    lF1TexBT,
	    lF2Tex,
	    lF2TexLS,
	    lF2TexTT,
	    lF2TexRS,
	    lF2TexBT,
	    lF3Tex,
	    lF3TexLS,
	    lF3TexTT,
	    lF3TexRS,
	    lF3TexBT,
	    pDTex,
	    pDTexLS,
	    pDTexTT,
	    pDTexRS,
	    pDTexBT,
	    pSTex,
	    pSTexLS,
	    pSTexTT,
	    pSTexRS,
	    pSTexBT,
	    pPMinBorderDispTBP,
	    pPMinBorderDispLRP,
	    pPMinBorderTextETBP,
	    pPTextColor,
	    pPLRTex,
	    pPLRTexLS,
	    pPLRTexTT,
	    pPLRTexRS,
	    pPLRTexBT,
	    pPMTex,
	    bHeightSizePPP,
	    bWidthSizePPP,
	    bMinBorderXPPP,
	    bMinBorderTextETBP,
	    bDTextColor,
	    bSTextColor,
	    bDLRTex,
	    bDLRTexLS,
	    bDLRTexTT,
	    bDLRTexRS,
	    bDLRTexBT,
	    bDMTex,
	    bSLRTex,
	    bSLRTexLS,
	    bSLRTexTT,
	    bSLRTexRS,
	    bSLRTexBT,
	    bSMTex;
	}

	private final String logTag;	
	private static ManqalaCombatVariSVBuilder INSTANCE;
}
