package rf.yagodar.manqala.free.drawing.view;

import java.util.ArrayList;

import rf.yagodar.glump.animation.AnimScenInfo;
import rf.yagodar.glump.animation.HomogenAnimScenBuilder;
import rf.yagodar.glump.model.GLumpButtonModel;
import rf.yagodar.glump.model.GLumpSVModel;
import rf.yagodar.glump.model.GLumpSVModelEditInfo;
import rf.yagodar.glump.renderer.GLumpSVRendererQueue;
import rf.yagodar.glump.renderer.GLumpSVRendererQueueNode;
import rf.yagodar.glump.view.GLumpSV;
import rf.yagodar.manqala.free.R;
import rf.yagodar.manqala.free.drawing.model.ManqalaVariCellModel;
import rf.yagodar.manqala.free.drawing.model.ManqalaVariGameBoardModel;
import rf.yagodar.manqala.free.logic.combat.ManqalaCombatVari;
import rf.yagodar.manqala.free.logic.combat.ManqalaMoveResultStep;
import rf.yagodar.manqala.free.logic.parameters.SVari;
import android.content.Context;

public class ManqalaCombatVariSV extends GLumpSV<ManqalaCombatVariSVBlank> {
	public ManqalaCombatVariSV(Context context, int glSurfaceViewRenderMode) {
		super(context, glSurfaceViewRenderMode);
	}

	public ManqalaCombatVariSV(Context context) {
		super(context);
	}

	public ManqalaVariGameBoardModel getGameBoardModel() {
		return getSVBlank().getGameBoardModel();
	}
	
	public ArrayList<GLumpSVRendererQueueNode> drawApplyMoveResult(ManqalaVariCellModel startCellModel, ArrayList<ManqalaMoveResultStep> moveResultSteps, ManqalaCombatVari manqalaCombatVari) {
		ArrayList<GLumpSVRendererQueueNode> rendererQueueNodes = new ArrayList<GLumpSVRendererQueueNode>();
		
		if(startCellModel != null && moveResultSteps != null && !moveResultSteps.isEmpty() && manqalaCombatVari != null) {
			rendererQueueNodes.add(drawCellSelected(startCellModel));
			rendererQueueNodes.addAll(drawUpdateGrainsAndPoints(moveResultSteps));
			rendererQueueNodes.add(drawCellDeselected(startCellModel));
			rendererQueueNodes.add(drawCombatInfo(manqalaCombatVari));
		}
		
		return rendererQueueNodes;
	}
	
	public ArrayList<GLumpSVRendererQueueNode> drawNotValidMoveResult(ManqalaVariCellModel startCellModel, ManqalaCombatVari manqalaCombatVari) {
		ArrayList<GLumpSVRendererQueueNode> rendererQueueNodes = new ArrayList<GLumpSVRendererQueueNode>();
		
		if(startCellModel != null && manqalaCombatVari != null) {
			rendererQueueNodes.add(drawCellSelected(startCellModel));
			rendererQueueNodes.add(drawCombatErrInfo());
			rendererQueueNodes.add(drawCellDeselected(startCellModel));
			rendererQueueNodes.add(drawCombatInfo(manqalaCombatVari));
		}
		
		return rendererQueueNodes;
	}
	
	public ArrayList<GLumpSVRendererQueueNode> drawInitSV(ManqalaCombatVari manqalaCombatVari) {
		ArrayList<GLumpSVRendererQueueNode> rendererQueueNodes = new ArrayList<GLumpSVRendererQueueNode>();
		
		if(manqalaCombatVari != null) {
			rendererQueueNodes.add(drawInitMonsterThink());
			rendererQueueNodes.add(drawInitCellSelection());
			rendererQueueNodes.add(drawPlayersInfo(manqalaCombatVari.getFirstOpponent().getCharName(), manqalaCombatVari.getSecondOpponent().getCharName()));
			rendererQueueNodes.add(drawInitGrainsAndPoints(manqalaCombatVari));
			rendererQueueNodes.add(drawCombatInfo(manqalaCombatVari, false));
		}
		
		return rendererQueueNodes;
	}
	
	
	
	public GLumpSVRendererQueue drawMonsterThink() {
		GLumpSVRendererQueueNode finishRendererQueueNode = new GLumpSVRendererQueueNode();
		for (GLumpSVModel<?> lFModel : getSVBlank().getLoadingFramesModels()) {
			finishRendererQueueNode.addAnimScenInfo(new AnimScenInfo(lFModel, HomogenAnimScenBuilder.generateTransparentAnimScen(DISAPPEAR_TRANSPARENT_ANIM_NODES, GLumpSV.ANIMATION_STEP_MILISEC)));
		}
		
		GLumpSVRendererQueue lFrendererQueue = new GLumpSVRendererQueue(true, finishRendererQueueNode);
		
		int prevIndx;
		for (int i = 0; i < getSVBlank().getLoadingFramesModels().size(); i++) {
			GLumpSVRendererQueueNode rendererQueueNode = new GLumpSVRendererQueueNode();
			
			if(i == 0) {
				prevIndx = getSVBlank().getLoadingFramesModels().size() - 1;
			}
			else {
				prevIndx = i - 1;
			}
			
			rendererQueueNode.addAnimScenInfo(new AnimScenInfo(getSVBlank().getLoadingFramesModels().get(prevIndx), HomogenAnimScenBuilder.generateTransparentAnimScen(DISAPPEAR_TRANSPARENT_ANIM_NODES, GLumpSV.ANIMATION_STEP_MILISEC)));
			rendererQueueNode.addAnimScenInfo(new AnimScenInfo(getSVBlank().getLoadingFramesModels().get(i), HomogenAnimScenBuilder.generateTransparentAnimScen(APPEAR_TRANSPARENT_ANIM_NODES, GLumpSV.ANIMATION_STEP_MILISEC)));
			
			lFrendererQueue.offerNode(rendererQueueNode);
		}
		
		return lFrendererQueue;
	}
	
	public ArrayList<GLumpSVRendererQueueNode> showPausePlate() {
		return showPausePlate(true, getContext().getString(R.string.pause_label), getContext().getString(R.string.pause_header));
	}
	
	public ArrayList<GLumpSVRendererQueueNode> showGameOverPlate(ManqalaCombatVari manqalaCombatVari) {
		if(manqalaCombatVari != null) {
			if(!manqalaCombatVari.isCombating()) {
				String pausePlateText;
				
				if(manqalaCombatVari.isDraw()) {
					pausePlateText = getContext().getString(R.string.draw);
				}
				else {
					pausePlateText = String.format(getContext().getString(R.string.you_won_$user$), manqalaCombatVari.getWinner().getCharName());
				}
				
				return showPausePlate(false, pausePlateText, getContext().getString(R.string.game_over_header));
			}			
		}
		
		return null;
	}
	
	public ArrayList<GLumpSVRendererQueueNode> hidePausePlate() {
		ArrayList<GLumpSVRendererQueueNode> rendererQueueNodes = new ArrayList<GLumpSVRendererQueueNode>();
		
		GLumpSVRendererQueueNode hidePausePlateNode = new GLumpSVRendererQueueNode();
		hidePausePlateNode.addAnimScenInfo(new AnimScenInfo(getSVBlank().getPausePlateModel().getpPExitButton().getButtonDeselectedModel(), HomogenAnimScenBuilder.generateTransparentAnimScen(DISAPPEAR_TRANSPARENT_ANIM_NODES, GLumpSV.ANIMATION_STEP_MILISEC)));
		hidePausePlateNode.addAnimScenInfo(new AnimScenInfo(getSVBlank().getPausePlateModel().getpPExitButton().getButtonSelectedModel(), HomogenAnimScenBuilder.generateTransparentAnimScen(DISAPPEAR_TRANSPARENT_ANIM_NODES, GLumpSV.ANIMATION_STEP_MILISEC)));
		hidePausePlateNode.addAnimScenInfo(new AnimScenInfo(getSVBlank().getPausePlateModel().getpPContinueButton().getButtonDeselectedModel(), HomogenAnimScenBuilder.generateTransparentAnimScen(DISAPPEAR_TRANSPARENT_ANIM_NODES, GLumpSV.ANIMATION_STEP_MILISEC)));
		hidePausePlateNode.addAnimScenInfo(new AnimScenInfo(getSVBlank().getPausePlateModel().getpPContinueButton().getButtonSelectedModel(), HomogenAnimScenBuilder.generateTransparentAnimScen(DISAPPEAR_TRANSPARENT_ANIM_NODES, GLumpSV.ANIMATION_STEP_MILISEC)));
		hidePausePlateNode.addAnimScenInfo(new AnimScenInfo(getSVBlank().getPausePlateModel().getpPRestartButton().getButtonDeselectedModel(), HomogenAnimScenBuilder.generateTransparentAnimScen(DISAPPEAR_TRANSPARENT_ANIM_NODES, GLumpSV.ANIMATION_STEP_MILISEC)));
		hidePausePlateNode.addAnimScenInfo(new AnimScenInfo(getSVBlank().getPausePlateModel().getpPRestartButton().getButtonSelectedModel(), HomogenAnimScenBuilder.generateTransparentAnimScen(DISAPPEAR_TRANSPARENT_ANIM_NODES, GLumpSV.ANIMATION_STEP_MILISEC)));
		hidePausePlateNode.addAnimScenInfo(new AnimScenInfo(getSVBlank().getPausePlateModel().getpPHeaderTextLabelModel(), HomogenAnimScenBuilder.generateTransparentAnimScen(DISAPPEAR_TRANSPARENT_ANIM_NODES, GLumpSV.ANIMATION_STEP_MILISEC)));
		hidePausePlateNode.addAnimScenInfo(new AnimScenInfo(getSVBlank().getPausePlateModel().getpPTextLabelModel(), HomogenAnimScenBuilder.generateTransparentAnimScen(DISAPPEAR_TRANSPARENT_ANIM_NODES, GLumpSV.ANIMATION_STEP_MILISEC)));
		
		rendererQueueNodes.add(hidePausePlateNode);
		
		GLumpSVRendererQueueNode deselectPauseModelNode = new GLumpSVRendererQueueNode();
		deselectPauseModelNode.addAnimScenInfo(new AnimScenInfo(getSVBlank().getPauseSelectedModel(), HomogenAnimScenBuilder.generateTransparentAnimScen(DISAPPEAR_TRANSPARENT_ANIM_NODES, GLumpSV.ANIMATION_STEP_MILISEC)));
		deselectPauseModelNode.addAnimScenInfo(new AnimScenInfo(getSVBlank().getPauseDeselectedModel(), HomogenAnimScenBuilder.generateTransparentAnimScen(APPEAR_TRANSPARENT_ANIM_NODES, GLumpSV.ANIMATION_STEP_MILISEC)));
		rendererQueueNodes.add(deselectPauseModelNode);
		
		rendererQueueNodes.add(showCPPIText());
		
		clearPauseTextModels();
		
		return rendererQueueNodes;
	}
	
	public GLumpSVRendererQueueNode drawRestartButtonSelect() {
		return drawButtonSelect(getSVBlank().getPausePlateModel().getpPRestartButton(), getContext().getString(R.string.btn_c_restart_label), getSVBlank().getPausePlateModel().getpPBSTextColor());
	}
	
	public GLumpSVRendererQueueNode drawRestartButtonDeselect() {
		return drawButtonDeselect(getSVBlank().getPausePlateModel().getpPRestartButton(), getContext().getString(R.string.btn_c_restart_label), getSVBlank().getPausePlateModel().getpPBDTextColor());
	}
	
	public GLumpSVRendererQueueNode drawContinueButtonSelect() {
		return drawButtonSelect(getSVBlank().getPausePlateModel().getpPContinueButton(), getContext().getString(R.string.btn_c_continue_label), getSVBlank().getPausePlateModel().getpPBSTextColor());
	}
	
	public GLumpSVRendererQueueNode drawContinueButtonDeselect() {
		return drawButtonDeselect(getSVBlank().getPausePlateModel().getpPContinueButton(), getContext().getString(R.string.btn_c_continue_label), getSVBlank().getPausePlateModel().getpPBDTextColor());
	}
	
	public GLumpSVRendererQueueNode drawExitButtonSelect() {
		return drawButtonSelect(getSVBlank().getPausePlateModel().getpPExitButton(), getContext().getString(R.string.btn_c_exit_label), getSVBlank().getPausePlateModel().getpPBSTextColor());
	}
	
	public GLumpSVRendererQueueNode drawExitButtonDeselect() {
		return drawButtonDeselect(getSVBlank().getPausePlateModel().getpPExitButton(), getContext().getString(R.string.btn_c_exit_label), getSVBlank().getPausePlateModel().getpPBDTextColor());
	}
	
	public void clearPauseTextModels() {
		if(getSVBlank().getPausePlateModel().getpPTextLabelModel().getTextModel() != null) {
			getSVBlank().getPausePlateModel().getpPTextLabelModel().getTextModel().clearChildModels(true);
		}

		if(getSVBlank().getPausePlateModel().getpPContinueButton().getButtonSelectedModel().getTextModel() != null) {
			getSVBlank().getPausePlateModel().getpPContinueButton().getButtonSelectedModel().getTextModel().clearChildModels(true);
		}

		if(getSVBlank().getPausePlateModel().getpPContinueButton().getButtonDeselectedModel().getTextModel() != null) {
			getSVBlank().getPausePlateModel().getpPContinueButton().getButtonDeselectedModel().getTextModel().clearChildModels(true);
		}

		if(getSVBlank().getPausePlateModel().getpPRestartButton().getButtonSelectedModel().getTextModel() != null) {
			getSVBlank().getPausePlateModel().getpPRestartButton().getButtonSelectedModel().getTextModel().clearChildModels(true);
		}

		if(getSVBlank().getPausePlateModel().getpPRestartButton().getButtonDeselectedModel().getTextModel() != null) {
			getSVBlank().getPausePlateModel().getpPRestartButton().getButtonDeselectedModel().getTextModel().clearChildModels(true);
		}

		if(getSVBlank().getPausePlateModel().getpPExitButton().getButtonSelectedModel().getTextModel() != null) {
			getSVBlank().getPausePlateModel().getpPExitButton().getButtonSelectedModel().getTextModel().clearChildModels(true);
		}

		if(getSVBlank().getPausePlateModel().getpPExitButton().getButtonDeselectedModel().getTextModel() != null) {
			getSVBlank().getPausePlateModel().getpPExitButton().getButtonDeselectedModel().getTextModel().clearChildModels(true);
		}

		if(getSVBlank().getPausePlateModel().getpPHeaderTextLabelModel().getTextModel() != null) {
			getSVBlank().getPausePlateModel().getpPHeaderTextLabelModel().getTextModel().clearChildModels(true);
		}
	}
	
	private GLumpSVRendererQueueNode drawButtonSelect(GLumpButtonModel button, String text, int textColor) {
		GLumpSVRendererQueueNode renderQueueNode = new GLumpSVRendererQueueNode();
		renderQueueNode.addAnimScenInfo(new AnimScenInfo(button.getButtonDeselectedModel(), HomogenAnimScenBuilder.generateTransparentAnimScen(DISAPPEAR_TRANSPARENT_ANIM_NODES, GLumpSV.ANIMATION_STEP_MILISEC)));
		renderQueueNode.addAnimScenInfo(new AnimScenInfo(button.getButtonSelectedModel(), HomogenAnimScenBuilder.generateTransparentAnimScen(APPEAR_TRANSPARENT_ANIM_NODES, GLumpSV.ANIMATION_STEP_MILISEC)));
		renderQueueNode.addModelEditInfo(button.getButtonSelectedModel().getTextModel().drawText(text, textColor));
		
		return renderQueueNode;
	}
	
	private GLumpSVRendererQueueNode drawButtonDeselect(GLumpButtonModel button, String text, int textColor) {
		GLumpSVRendererQueueNode renderQueueNode = new GLumpSVRendererQueueNode();
		renderQueueNode.addAnimScenInfo(new AnimScenInfo(button.getButtonSelectedModel(), HomogenAnimScenBuilder.generateTransparentAnimScen(DISAPPEAR_TRANSPARENT_ANIM_NODES, GLumpSV.ANIMATION_STEP_MILISEC)));
		renderQueueNode.addAnimScenInfo(new AnimScenInfo(button.getButtonDeselectedModel(), HomogenAnimScenBuilder.generateTransparentAnimScen(APPEAR_TRANSPARENT_ANIM_NODES, GLumpSV.ANIMATION_STEP_MILISEC)));
		renderQueueNode.addModelEditInfo(button.getButtonDeselectedModel().getTextModel().drawText(text, textColor));
		
		return renderQueueNode;
	}
	
	private GLumpSVRendererQueueNode drawPlayersInfo(String firstOpponentName, String secondOpponentName) {
		GLumpSVRendererQueueNode rendererQueueNode = new GLumpSVRendererQueueNode();
		
		rendererQueueNode.addModelEditInfo(getSVBlank().getGameBoardModel().getPlayerInfoTextModels()[0].drawText(firstOpponentName, getSVBlank().getGameBoardModel().getPlayerInfoTextColors()[0]));
		rendererQueueNode.addModelEditInfo(getSVBlank().getGameBoardModel().getPlayerInfoTextModels()[1].drawText(secondOpponentName, getSVBlank().getGameBoardModel().getPlayerInfoTextColors()[1]));
		
		return rendererQueueNode;
	}
	
	private GLumpSVRendererQueueNode drawCombatErrInfo() {
		GLumpSVRendererQueueNode rendererQueueNode = new GLumpSVRendererQueueNode();
		
		rendererQueueNode.addModelEditInfo(getSVBlank().getCombatInfoTextModel().drawText(getContext().getString(R.string.illegal_turn), getSVBlank().getCombatInfoErrorTextColor()));
		rendererQueueNode.addAnimScenInfo(new AnimScenInfo(getSVBlank().getCombatInfoTextModel(), HomogenAnimScenBuilder.generateScaleAnimScen(ERR_MSG_APPEAR_SCALE_ANIM_NODES, getSVBlank().getCombatInfoTextModel().getPolygon(), ERR_MSG_APPEAR_SCALE_ANIM_TIME)));
		
		return rendererQueueNode;
	}
	
	private GLumpSVRendererQueueNode drawCombatInfo(ManqalaCombatVari manqalaCombatVari) {
		return drawCombatInfo(manqalaCombatVari, true);
	}
	
	private GLumpSVRendererQueueNode drawCombatInfo(ManqalaCombatVari manqalaCombatVari, boolean addAnimScenInfo) {
		GLumpSVRendererQueueNode rendererQueueNode = null;

		rendererQueueNode = new GLumpSVRendererQueueNode();

		String msg;
		if(!manqalaCombatVari.isCombating()) {
			if(manqalaCombatVari.isDraw()) {
				msg = getContext().getString(R.string.draw);
			}
			else {
				msg = String.format(getContext().getString(R.string.you_won_$user$), manqalaCombatVari.getWinner().getCharName());
			}
		}
		else {
			msg = String.format(getContext().getString(R.string.your_turn_$user$), manqalaCombatVari.getWalketh().getCharName());
		}

		rendererQueueNode.addModelEditInfo(getSVBlank().getCombatInfoTextModel().drawText(msg, getSVBlank().getCombatInfoTextColor()));

		if(addAnimScenInfo) {
			rendererQueueNode.addAnimScenInfo(new AnimScenInfo(getSVBlank().getCombatInfoTextModel(), HomogenAnimScenBuilder.generateScaleAnimScen(MSG_APPEAR_SCALE_ANIM_NODES, getSVBlank().getCombatInfoTextModel().getPolygon(), MSG_APPEAR_SCALE_ANIM_TIME)));
		}

		return rendererQueueNode;
	}
	
	private GLumpSVRendererQueueNode drawInitMonsterThink() {
		GLumpSVRendererQueueNode rendererQueueNode = new GLumpSVRendererQueueNode();
		
		for (GLumpSVModel<?> lFModel : getSVBlank().getLoadingFramesModels()) {
			rendererQueueNode.addAnimScenInfo(new AnimScenInfo(lFModel, HomogenAnimScenBuilder.generateTransparentAnimScen(DISAPPEAR_TRANSPARENT_ANIM_NODES, GLumpSV.ANIMATION_STEP_MILISEC)));
		}
		
		return rendererQueueNode;
	}
	
	private GLumpSVRendererQueueNode drawInitCellSelection() {
		GLumpSVRendererQueueNode rendererQueueNode = new GLumpSVRendererQueueNode();
		
		for (ManqalaVariCellModel cellModel : getSVBlank().getGameBoardModel().getCellsModels()) {
			rendererQueueNode.addAnimScenInfo(new AnimScenInfo(cellModel.getCellSelectedModel(), HomogenAnimScenBuilder.generateTransparentAnimScen(DISAPPEAR_TRANSPARENT_ANIM_NODES, GLumpSV.ANIMATION_STEP_MILISEC)));
		}
		
		return rendererQueueNode;
	}
	
	private GLumpSVRendererQueueNode drawInitGrainsAndPoints(ManqalaCombatVari manqalaCombatVari) {
		GLumpSVRendererQueueNode rendererQueueNode = new GLumpSVRendererQueueNode();

		ArrayList<GLumpSVModelEditInfo> modelEditInfos = new ArrayList<GLumpSVModelEditInfo>();
		ArrayList<AnimScenInfo> animScenInfos = new ArrayList<AnimScenInfo>();

		for (int cellGlobalPosId = 0; cellGlobalPosId < getSVBlank().getGameBoardModel().getCellsModels().length; cellGlobalPosId++) {
			ManqalaVariCellModel cellModel = getSVBlank().getGameBoardModel().getCellsModels()[cellGlobalPosId];
			GLumpSVModel<?> cellPointsModel = getSVBlank().getGameBoardModel().getCellsPointsModels()[cellGlobalPosId];

			int colorIndex = 0;
			if(cellGlobalPosId > SVari.CELLS_COUNT) {				
				colorIndex = 1;
			}

			int newGrainsCount = manqalaCombatVari.getGameBoard().getGameBoardCells().getCellByGlobalPosId((byte) cellGlobalPosId).getGrainsCount();
			
			modelEditInfos.add(cellPointsModel.drawText("" + newGrainsCount, getSVBlank().getGameBoardModel().getCellPointsTextColors()[colorIndex]));
			
			cellModel.initDelGrainModels();
			animScenInfos.addAll(cellModel.animAddGrainModels(newGrainsCount, APPEAR_TRANSPARENT_ANIM_NODES, GLumpSV.ANIMATION_STEP_MILISEC));
			
			cellModel.resetTranspGrainModelIndx();
		}

		rendererQueueNode.addAllAnimScenInfo(animScenInfos);
		rendererQueueNode.addAllModelEditInfo(modelEditInfos);

		return rendererQueueNode;
	}
	
	private ArrayList<GLumpSVRendererQueueNode> drawUpdateGrainsAndPoints(ArrayList<ManqalaMoveResultStep> moveResultSteps) {
		ArrayList<GLumpSVRendererQueueNode> rendererQueueNodes = new ArrayList<GLumpSVRendererQueueNode>();

		for (ManqalaMoveResultStep moveResultStep : moveResultSteps) {
			int cellGlobalPosId = moveResultStep.getGlobalCellPosId();
			int walkethWarehouseGlobalCellPosId = moveResultStep.getWalkethWarehouseGlobalCellPosId();

			ManqalaVariCellModel cellModel = getSVBlank().getGameBoardModel().getCellsModels()[cellGlobalPosId];
			GLumpSVModel<?> cellPointsModel = getSVBlank().getGameBoardModel().getCellsPointsModels()[cellGlobalPosId];
			
			int colorIndex = 0;
			if(cellGlobalPosId > SVari.CELLS_COUNT) {				
				colorIndex = 1;
			}

			int warehouseColorIndex = 0;
			if(walkethWarehouseGlobalCellPosId > SVari.CELLS_COUNT) {				
				warehouseColorIndex = 1;
			}

			if(moveResultStep.isBonus()) {
				GLumpSVRendererQueueNode bonusTextRendererNode = new GLumpSVRendererQueueNode();
				bonusTextRendererNode.addModelEditInfo(getSVBlank().getCombatInfoTextModel().drawText(getContext().getString(R.string.bonus), getSVBlank().getCombatInfoTextColor()));
				bonusTextRendererNode.addAnimScenInfo(new AnimScenInfo(getSVBlank().getCombatInfoTextModel(), HomogenAnimScenBuilder.generateScaleAnimScen(MSG_APPEAR_SCALE_ANIM_NODES, getSVBlank().getCombatInfoTextModel().getPolygon(), MSG_APPEAR_SCALE_ANIM_TIME)));
				rendererQueueNodes.add(bonusTextRendererNode);

				GLumpSVRendererQueueNode delBonusGrainsRendererNode = new GLumpSVRendererQueueNode();
				int addToWarehouseGrainsCount = cellModel.getGrainsCount();
				delBonusGrainsRendererNode.addAllAnimScenInfo(cellModel.animDelGrainModels(DISAPPEAR_TRANSPARENT_ANIM_NODES, GRAIN_TRANSPARENT_ANIM_TIME));
				delBonusGrainsRendererNode.addModelEditInfo(cellPointsModel.drawText("" + cellModel.getGrainsCount(), getSVBlank().getGameBoardModel().getCellPointsTextColors()[colorIndex]));
				delBonusGrainsRendererNode.addAnimScenInfo(new AnimScenInfo(cellPointsModel, HomogenAnimScenBuilder.generateScaleAnimScen(MSG_APPEAR_SCALE_ANIM_NODES, cellPointsModel.getPolygon(), MSG_APPEAR_SCALE_ANIM_TIME)));
				rendererQueueNodes.add(delBonusGrainsRendererNode);

				GLumpSVRendererQueueNode addBonusGrainsRendererNode = new GLumpSVRendererQueueNode();
				
				ManqalaVariCellModel warehouseCellModel = getSVBlank().getGameBoardModel().getCellsModels()[walkethWarehouseGlobalCellPosId];
				GLumpSVModel<?> warehouseCellPointsModel = getSVBlank().getGameBoardModel().getCellsPointsModels()[walkethWarehouseGlobalCellPosId];
				addBonusGrainsRendererNode.addAllAnimScenInfo(warehouseCellModel.animAddGrainModels(addToWarehouseGrainsCount, APPEAR_TRANSPARENT_ANIM_NODES, GRAIN_TRANSPARENT_ANIM_TIME));
				addBonusGrainsRendererNode.addModelEditInfo(warehouseCellPointsModel.drawText("" + warehouseCellModel.getGrainsCount(), getSVBlank().getGameBoardModel().getCellPointsTextColors()[warehouseColorIndex]));
				addBonusGrainsRendererNode.addAnimScenInfo(new AnimScenInfo(warehouseCellPointsModel, HomogenAnimScenBuilder.generateScaleAnimScen(MSG_APPEAR_SCALE_ANIM_NODES, warehouseCellPointsModel.getPolygon(), MSG_APPEAR_SCALE_ANIM_TIME)));
				rendererQueueNodes.add(addBonusGrainsRendererNode);
			}
			else {
				GLumpSVRendererQueueNode rendererQueueNode = new GLumpSVRendererQueueNode();
				if(moveResultStep.isStartCell()) {
					rendererQueueNode.addAllAnimScenInfo(cellModel.animDelGrainModels(DISAPPEAR_TRANSPARENT_ANIM_NODES, GRAIN_TRANSPARENT_ANIM_TIME));
				}
				else {
					rendererQueueNode.addAnimScenInfo(cellModel.animIncGrainModel(APPEAR_TRANSPARENT_ANIM_NODES, GRAIN_TRANSPARENT_ANIM_TIME));
				}
				
				rendererQueueNode.addModelEditInfo(cellPointsModel.drawText("" + cellModel.getGrainsCount(), getSVBlank().getGameBoardModel().getCellPointsTextColors()[colorIndex]));
				rendererQueueNode.addAnimScenInfo(new AnimScenInfo(cellPointsModel, HomogenAnimScenBuilder.generateScaleAnimScen(MSG_APPEAR_SCALE_ANIM_NODES, cellPointsModel.getPolygon(), MSG_APPEAR_SCALE_ANIM_TIME)));

				rendererQueueNodes.add(rendererQueueNode);
			}
		}

		for (ManqalaMoveResultStep moveResultStep : moveResultSteps) {
			getSVBlank().getGameBoardModel().getCellsModels()[moveResultStep.getGlobalCellPosId()].resetTranspGrainModelIndx();
		}

		return rendererQueueNodes;
	}
	
	private GLumpSVRendererQueueNode drawCellSelected(ManqalaVariCellModel cellModel) {
		GLumpSVRendererQueueNode rendererQueueNode = new GLumpSVRendererQueueNode();
		
		rendererQueueNode.addAnimScenInfo(new AnimScenInfo(cellModel.getCellSelectedModel(), HomogenAnimScenBuilder.generateTransparentAnimScen(APPEAR_TRANSPARENT_ANIM_NODES, CELL_SELECT_TRANSPARENT_ANIM_TIME)));
		
		return rendererQueueNode;
	}
	
	private GLumpSVRendererQueueNode drawCellDeselected(ManqalaVariCellModel cellModel) {
		GLumpSVRendererQueueNode rendererQueueNode = new GLumpSVRendererQueueNode();
		
		rendererQueueNode.addAnimScenInfo(new AnimScenInfo(cellModel.getCellSelectedModel(), HomogenAnimScenBuilder.generateTransparentAnimScen(DISAPPEAR_TRANSPARENT_ANIM_NODES, CELL_SELECT_TRANSPARENT_ANIM_TIME)));
		
		return rendererQueueNode;
	}
	
	private ArrayList<GLumpSVRendererQueueNode> showPausePlate(boolean showContinueButton, String pausePlateText, String pausePlateHeaderText) {
		ArrayList<GLumpSVRendererQueueNode> rendererQueueNodes = new ArrayList<GLumpSVRendererQueueNode>();
		
		rendererQueueNodes.add(hideCPPIText());
		
		if(showContinueButton) {
			GLumpSVRendererQueueNode selectPauseModelNode = new GLumpSVRendererQueueNode();
			selectPauseModelNode.addAnimScenInfo(new AnimScenInfo(getSVBlank().getPauseDeselectedModel(), HomogenAnimScenBuilder.generateTransparentAnimScen(DISAPPEAR_TRANSPARENT_ANIM_NODES, GLumpSV.ANIMATION_STEP_MILISEC)));
			selectPauseModelNode.addAnimScenInfo(new AnimScenInfo(getSVBlank().getPauseSelectedModel(), HomogenAnimScenBuilder.generateTransparentAnimScen(APPEAR_TRANSPARENT_ANIM_NODES, GLumpSV.ANIMATION_STEP_MILISEC)));
			rendererQueueNodes.add(selectPauseModelNode);
		}

		GLumpSVRendererQueueNode showPausePlateNode = new GLumpSVRendererQueueNode();

		showPausePlateNode.addAnimScenInfo(new AnimScenInfo(getSVBlank().getPausePlateModel().getpPTextLabelModel(), HomogenAnimScenBuilder.generateTransparentAnimScen(APPEAR_TRANSPARENT_ANIM_NODES, GLumpSV.ANIMATION_STEP_MILISEC)));
		showPausePlateNode.addModelEditInfo(getSVBlank().getPausePlateModel().getpPTextLabelModel().getTextModel().drawText(pausePlateText, getSVBlank().getPausePlateModel().getpPTextColor()));
		
		showPausePlateNode.addAnimScenInfo(new AnimScenInfo(getSVBlank().getPausePlateModel().getpPHeaderTextLabelModel(), HomogenAnimScenBuilder.generateTransparentAnimScen(APPEAR_TRANSPARENT_ANIM_NODES, GLumpSV.ANIMATION_STEP_MILISEC)));
		showPausePlateNode.addModelEditInfo(getSVBlank().getPausePlateModel().getpPHeaderTextLabelModel().getTextModel().drawText(pausePlateHeaderText, getSVBlank().getPausePlateModel().getpPHeaderTextColor()));
		
		showPausePlateNode.addAnimScenInfo(new AnimScenInfo(getSVBlank().getPausePlateModel().getpPRestartButton().getButtonDeselectedModel(), HomogenAnimScenBuilder.generateTransparentAnimScen(APPEAR_TRANSPARENT_ANIM_NODES, GLumpSV.ANIMATION_STEP_MILISEC)));
		showPausePlateNode.addModelEditInfo(getSVBlank().getPausePlateModel().getpPRestartButton().getButtonDeselectedModel().getTextModel().drawText(getContext().getString(R.string.btn_c_restart_label), getSVBlank().getPausePlateModel().getpPBDTextColor()));
		
		if(showContinueButton) {
			showPausePlateNode.addAnimScenInfo(new AnimScenInfo(getSVBlank().getPausePlateModel().getpPContinueButton().getButtonDeselectedModel(), HomogenAnimScenBuilder.generateTransparentAnimScen(APPEAR_TRANSPARENT_ANIM_NODES, GLumpSV.ANIMATION_STEP_MILISEC)));
			showPausePlateNode.addModelEditInfo(getSVBlank().getPausePlateModel().getpPContinueButton().getButtonDeselectedModel().getTextModel().drawText(getContext().getString(R.string.btn_c_continue_label), getSVBlank().getPausePlateModel().getpPBDTextColor()));
		}
		
		showPausePlateNode.addAnimScenInfo(new AnimScenInfo(getSVBlank().getPausePlateModel().getpPExitButton().getButtonDeselectedModel(), HomogenAnimScenBuilder.generateTransparentAnimScen(APPEAR_TRANSPARENT_ANIM_NODES, GLumpSV.ANIMATION_STEP_MILISEC)));
		showPausePlateNode.addModelEditInfo(getSVBlank().getPausePlateModel().getpPExitButton().getButtonDeselectedModel().getTextModel().drawText(getContext().getString(R.string.btn_c_exit_label), getSVBlank().getPausePlateModel().getpPBDTextColor()));
		
		rendererQueueNodes.add(showPausePlateNode);
		
		return rendererQueueNodes;
	}
	
	private GLumpSVRendererQueueNode hideCPPIText() {
		GLumpSVRendererQueueNode rendererQueueNode = new GLumpSVRendererQueueNode();
		
		if(getSVBlank().getGameBoardModel().getPlayerInfoTextModels() != null) {
			for (GLumpSVModel<?> pITextModel : getSVBlank().getGameBoardModel().getPlayerInfoTextModels()) {
				rendererQueueNode.addAnimScenInfo(new AnimScenInfo(pITextModel, HomogenAnimScenBuilder.generateTransparentAnimScen(DISAPPEAR_TRANSPARENT_ANIM_NODES, GLumpSV.ANIMATION_STEP_MILISEC)));
			}
		}
		
		if(getSVBlank().getGameBoardModel().getCellsPointsModels() != null) {
			for (GLumpSVModel<?> cPModel : getSVBlank().getGameBoardModel().getCellsPointsModels()) {
				rendererQueueNode.addAnimScenInfo(new AnimScenInfo(cPModel, HomogenAnimScenBuilder.generateTransparentAnimScen(DISAPPEAR_TRANSPARENT_ANIM_NODES, GLumpSV.ANIMATION_STEP_MILISEC)));
			}
		}
		
		return rendererQueueNode;
	}
	
	private GLumpSVRendererQueueNode showCPPIText() {
		GLumpSVRendererQueueNode rendererQueueNode = new GLumpSVRendererQueueNode();
		
		if(getSVBlank().getGameBoardModel().getPlayerInfoTextModels() != null) {
			for (GLumpSVModel<?> pITextModel : getSVBlank().getGameBoardModel().getPlayerInfoTextModels()) {
				rendererQueueNode.addAnimScenInfo(new AnimScenInfo(pITextModel, HomogenAnimScenBuilder.generateTransparentAnimScen(APPEAR_TRANSPARENT_ANIM_NODES, GLumpSV.ANIMATION_STEP_MILISEC)));
			}
		}
		
		if(getSVBlank().getGameBoardModel().getCellsPointsModels() != null) {
			for (GLumpSVModel<?> cPModel : getSVBlank().getGameBoardModel().getCellsPointsModels()) {
				rendererQueueNode.addAnimScenInfo(new AnimScenInfo(cPModel, HomogenAnimScenBuilder.generateTransparentAnimScen(APPEAR_TRANSPARENT_ANIM_NODES, GLumpSV.ANIMATION_STEP_MILISEC)));
			}
		}
		
		return rendererQueueNode;
	}
	
	//TODO мб перенести осюда?
	private static final Float[] MSG_APPEAR_SCALE_ANIM_NODES = new Float[] { 1.0f, 1.1f, 1.0f };
	private static final long MSG_APPEAR_SCALE_ANIM_TIME = MSG_APPEAR_SCALE_ANIM_NODES.length * GLumpSV.ANIMATION_STEP_MILISEC;
	private static final Float[] ERR_MSG_APPEAR_SCALE_ANIM_NODES = new Float[] { 1.0f, 1.1f, 1.0f };
	private static final long ERR_MSG_APPEAR_SCALE_ANIM_TIME = ERR_MSG_APPEAR_SCALE_ANIM_NODES.length * GLumpSV.ANIMATION_STEP_MILISEC;
	
	private static final Float[] APPEAR_TRANSPARENT_ANIM_NODES = new Float[] { 0.0f, 1.0f };
	private static final Float[] DISAPPEAR_TRANSPARENT_ANIM_NODES = new Float[] { 1.0f, 0.0f };
	
	private static final long CELL_SELECT_TRANSPARENT_ANIM_TIME = 2 * APPEAR_TRANSPARENT_ANIM_NODES.length * GLumpSV.ANIMATION_STEP_MILISEC;
	
	private static final long GRAIN_TRANSPARENT_ANIM_TIME = APPEAR_TRANSPARENT_ANIM_NODES.length * GLumpSV.ANIMATION_STEP_MILISEC;
}
