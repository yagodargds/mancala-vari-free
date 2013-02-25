package rf.yagodar.manqala.free.drawing.view;

import java.util.ArrayList;

import rf.yagodar.glump.model.GLumpSVModel;
import rf.yagodar.glump.polygon.Rectangle;
import rf.yagodar.glump.view.GLumpSVBlank;
import rf.yagodar.manqala.free.drawing.model.ManqalaVariGameBoardModel;
import rf.yagodar.manqala.free.drawing.model.ManqalaVariPausePlateModel;

public class ManqalaCombatVariSVBlank extends GLumpSVBlank {
	public ManqalaCombatVariSVBlank(GLumpSVModel<Rectangle> sVBackdropRootModel) {
		super(sVBackdropRootModel);
	}

	public ManqalaVariPausePlateModel getPausePlateModel() {
		return pausePlateModel;
	}

	public void setPausePlateModel(ManqalaVariPausePlateModel pausePlateModel) {
		this.pausePlateModel = pausePlateModel;
	}

	public GLumpSVModel<?> getPauseButtonAreaModel() {
		return pauseButtonAreaModel;
	}

	public void setPauseButtonAreaModel(GLumpSVModel<?> pauseButtonAreaModel) {
		this.pauseButtonAreaModel = pauseButtonAreaModel;
	}

	public GLumpSVModel<?> getPauseDeselectedModel() {
		return pauseDeselectedModel;
	}

	public void setPauseDeselectedModel(GLumpSVModel<?> pauseDeselectedModel) {
		this.pauseDeselectedModel = pauseDeselectedModel;
	}

	public GLumpSVModel<?> getPauseSelectedModel() {
		return pauseSelectedModel;
	}

	public void setPauseSelectedModel(GLumpSVModel<?> pauseSelectedModel) {
		this.pauseSelectedModel = pauseSelectedModel;
	}

	public void setLoadingFramesModels(ArrayList<GLumpSVModel<?>> loadingFramesModels) {
		this.loadingFramesModels = loadingFramesModels;
	}
	
	public ArrayList<GLumpSVModel<?>> getLoadingFramesModels() {
		return loadingFramesModels;
	}

	public void setCombatInfoTextModel(GLumpSVModel<Rectangle> combatInfoTextModel) {
		this.combatInfoTextModel = combatInfoTextModel;
	}
	
	public GLumpSVModel<Rectangle> getCombatInfoTextModel() {
		return this.combatInfoTextModel;
	}
	
	public void setCombatInfoTextColor(int combatInfoTextColor) {
		this.combatInfoTextColor = combatInfoTextColor;
	}
	
	public int getCombatInfoTextColor() {
		return this.combatInfoTextColor;
	}
	
	public void setCombatInfoErrorTextColor(int combatInfoErrorTextColor) {
		this.combatInfoErrorTextColor = combatInfoErrorTextColor;
	}
	
	public int getCombatInfoErrorTextColor() {
		return this.combatInfoErrorTextColor;
	}
	
	public void setGameBoardModel(ManqalaVariGameBoardModel gameBoardModel) {
		this.gameBoardModel = gameBoardModel;			
	}
	
	public ManqalaVariGameBoardModel getGameBoardModel() {
		return gameBoardModel;
	}

	private GLumpSVModel<?> pauseButtonAreaModel;
	private GLumpSVModel<?> pauseDeselectedModel;
	private GLumpSVModel<?> pauseSelectedModel;
	private ManqalaVariPausePlateModel pausePlateModel;
	private ArrayList<GLumpSVModel<?>> loadingFramesModels;
	private GLumpSVModel<Rectangle> combatInfoTextModel;
	private int combatInfoTextColor;
	private int combatInfoErrorTextColor;
	private ManqalaVariGameBoardModel gameBoardModel;
}
