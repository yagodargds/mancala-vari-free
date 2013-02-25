package rf.yagodar.manqala.free.drawing.model;

import rf.yagodar.glump.model.GLumpSVModel;
import rf.yagodar.glump.polygon.Rectangle;

public class ManqalaVariGameBoardModel extends GLumpSVModel<Rectangle> {	
	public ManqalaVariGameBoardModel(Rectangle modelPolygon) {
		super(modelPolygon);
	}
	
	public ManqalaVariCellModel[] getCellsModels() {
		return cellsModels;
	}

	public void setCellsModels(ManqalaVariCellModel[] cellsModels) {
		this.cellsModels = cellsModels;
	}

	public GLumpSVModel<?>[] getCellsPointsModels() {
		return cellsPointsModels;
	}
	
	public void setCellsPointsModels(GLumpSVModel<?>[] cellsPointsModels) {
		this.cellsPointsModels = cellsPointsModels;
	}
	
	public int[] getCellPointsTextColors() {
		return this.cellPointsTextColors;
	}
	
	public void setCellPointsTextColor(int[] cellPointsTextColors) {
		this.cellPointsTextColors = cellPointsTextColors;
	}
	
	public GLumpSVModel<?>[] getPlayerInfoTextModels() {
		return playerInfoTextModels;
	}
	
	public void setPlayerInfoTextModels(GLumpSVModel<?>[] playerInfoTextModels) {
		this.playerInfoTextModels = playerInfoTextModels;
	}	
	
	public int[] getPlayerInfoTextColors() {
		return this.playerInfoTextColors;
	}
	
	public void setPlayerInfoTextColors(int[] playerInfoTextColors) {
		this.playerInfoTextColors = playerInfoTextColors;
	}
	
	private ManqalaVariCellModel[] cellsModels;
	private GLumpSVModel<?>[] cellsPointsModels;
	private int[] cellPointsTextColors;
	private GLumpSVModel<?>[] playerInfoTextModels;
	private int[] playerInfoTextColors;
}
