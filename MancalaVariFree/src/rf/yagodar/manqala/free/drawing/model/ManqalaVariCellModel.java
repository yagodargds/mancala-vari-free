package rf.yagodar.manqala.free.drawing.model;

import java.util.ArrayList;

import rf.yagodar.glump.animation.AnimScenInfo;
import rf.yagodar.glump.animation.HomogenAnimScenBuilder;
import rf.yagodar.glump.model.GLumpSVModel;
import rf.yagodar.glump.point.Point2D;
import rf.yagodar.glump.polygon.AbstractPolygon;
import rf.yagodar.glump.polygon.Rectangle;

public class ManqalaVariCellModel extends GLumpSVModel<Rectangle> {
	public ManqalaVariCellModel(Rectangle modelPolygon, boolean isWarehouse) {
		super(modelPolygon);
		this.isWarehouse = isWarehouse;
		grainsCount = 0;
		resetTranspGrainModelIndx();
	}
	
	@Override
	public boolean isContainsPoint(Point2D point2D) {
		return !isWarehouse && super.isContainsPoint(point2D);
	}

	public boolean isWarehouse() {
		return isWarehouse;
	}
	
	public void setCellSelectedModel(Rectangle cSModelPolygon) {
		this.cellSelectModel = addChildModel(cSModelPolygon, false);
	}
	
	public GLumpSVModel<Rectangle> getCellSelectedModel() {
		return cellSelectModel;
	}

	public int getGrainsCount() {
		return grainsCount;
	}
	
	public AnimScenInfo animIncGrainModel(Float[] animAddNodes, long animStep, long animTime) {
		AnimScenInfo animScenInfo = null;

		ArrayList<GLumpSVModel<?>> transpGrainModels = getGrainModels(true);
		if(transpGrainModelIndx < transpGrainModels.size()) {
			animScenInfo = new AnimScenInfo(transpGrainModels.get(transpGrainModelIndx), HomogenAnimScenBuilder.generateTransparentAnimScen(animAddNodes, animStep, animTime));
			transpGrainModelIndx++;
			grainsCount++;
		}

		return animScenInfo;
	}
	
	public ArrayList<AnimScenInfo> animAddGrainModels(int addGrainsCount, Float[] animAddNodes, long animStep, long animTime) {
		ArrayList<AnimScenInfo> animScenInfos = new ArrayList<AnimScenInfo>();
		
		for (int i = 0; i < addGrainsCount; i++) {
			animScenInfos.add(animIncGrainModel(animAddNodes, animStep, animTime));
		}

		return animScenInfos;
	}
	
	public ArrayList<AnimScenInfo> animDelGrainModels(Float[] animDelNodes, long animStep, long animTime) {
		ArrayList<AnimScenInfo> animScenInfos = new ArrayList<AnimScenInfo>();
		
		for (GLumpSVModel<?> nonTranspGrainModel : getGrainModels(false)) {
			animScenInfos.add(new AnimScenInfo(nonTranspGrainModel, HomogenAnimScenBuilder.generateTransparentAnimScen(animDelNodes, animStep, animTime)));
		}
		
		if(transpGrainModelIndx > 0) {
			ArrayList<GLumpSVModel<?>> transpGrainNode = getGrainModels(true);
			
			for (int i = transpGrainModelIndx - 1; i >= 0; i--) {
				animScenInfos.add(new AnimScenInfo(transpGrainNode.get(i), HomogenAnimScenBuilder.generateTransparentAnimScen(animDelNodes, animStep, animTime)));
			}
			
			resetTranspGrainModelIndx();
		}
		
		grainsCount = 0;

		return animScenInfos;
	}
	
	public void resetTranspGrainModelIndx() {
		transpGrainModelIndx = 0;
	}
	
	public void initDelGrainModels() {
		for (GLumpSVModel<?> nonTranspGrainModel : getGrainModels(false)) {
			nonTranspGrainModel.getPolygon().setAlpha(AbstractPolygon.TRANSPARENT_ALPHA);
		}
		
		resetTranspGrainModelIndx();
		
		grainsCount = 0;
	}
	
	private ArrayList<GLumpSVModel<?>> getGrainModels(boolean transparent) {
		ArrayList<GLumpSVModel<?>> grainModels = new ArrayList<GLumpSVModel<?>>();
		
		for (GLumpSVModel<?> grainModel : this.grainModels) {
			if(transparent && grainModel.getPolygon().isTransparent()) {
				grainModels.add(grainModel);
			}
			else if(!transparent && !grainModel.getPolygon().isTransparent()) {
				grainModels.add(grainModel);
			}
		}

		return grainModels;
	}
	
	public void setGrainModels(ArrayList<GLumpSVModel<?>> grainModels) {		
		this.grainModels = grainModels;
	}

	private ArrayList<GLumpSVModel<?>> grainModels;
	private GLumpSVModel<Rectangle> cellSelectModel;
	private int grainsCount;
	private int transpGrainModelIndx;
	
	private final boolean isWarehouse;
}
