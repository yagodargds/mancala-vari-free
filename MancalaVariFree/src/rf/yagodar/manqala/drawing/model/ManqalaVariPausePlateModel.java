package rf.yagodar.manqala.drawing.model;

import rf.yagodar.glump.model.GLumpButtonModel;
import rf.yagodar.glump.model.GLumpSVModel;
import rf.yagodar.glump.model.GLumpTextLabelModel;
import rf.yagodar.glump.polygon.Rectangle;

public class ManqalaVariPausePlateModel extends GLumpSVModel<Rectangle> {
	public ManqalaVariPausePlateModel(Rectangle modelPolygon) {
		super(modelPolygon);
	}
	
	public GLumpTextLabelModel getpPTextLabelModel() {
		return pPTextLabelModel;
	}
	
	public void setpPTextLabelModel(GLumpTextLabelModel pPTextLabelModel) {
		this.pPTextLabelModel = pPTextLabelModel;
	}
	
	public int getpPTextColor() {
		return pPTextColor;
	}
	
	public void setpPTextColor(int pPTextColor) {
		this.pPTextColor = pPTextColor;
	}
	
	public GLumpTextLabelModel getpPHeaderTextLabelModel() {
		return pPHeaderTextLabelModel;
	}
	
	public void setpPHeaderTextLabelModel(GLumpTextLabelModel pPHeaderTextLabelModel) {
		this.pPHeaderTextLabelModel = pPHeaderTextLabelModel;
	}
	
	public int getpPHeaderTextColor() {
		return pPHeaderTextColor;
	}
	
	public void setpPHeaderTextColor(int pPHeaderTextColor) {
		this.pPHeaderTextColor = pPHeaderTextColor;
	}
	
	public GLumpButtonModel getpPRestartButton() {
		return pPRestartButton;
	}
	
	public void setpPRestartButton(GLumpButtonModel pPRestartButton) {
		this.pPRestartButton = pPRestartButton;
	}
	
	public GLumpButtonModel getpPContinueButton() {
		return pPContinueButton;
	}
	
	public void setpPContinueButton(GLumpButtonModel pPContinueButton) {
		this.pPContinueButton = pPContinueButton;
	}
	
	public GLumpButtonModel getpPExitButton() {
		return pPExitButton;
	}
	
	public void setpPExitButton(GLumpButtonModel pPExitButton) {
		this.pPExitButton = pPExitButton;
	}
	
	public int getpPBDTextColor() {
		return pPBDTextColor;
	}
	
	public void setpPBDTextColor(int pPBDTextColor) {
		this.pPBDTextColor = pPBDTextColor;
	}
	
	public int getpPBSTextColor() {
		return pPBSTextColor;
	}
	
	public void setpPBSTextColor(int pPBSTextColor) {
		this.pPBSTextColor = pPBSTextColor;
	}

	private GLumpTextLabelModel pPTextLabelModel;
	private int pPTextColor;
	private GLumpTextLabelModel pPHeaderTextLabelModel;
	private int pPHeaderTextColor;
	private GLumpButtonModel pPRestartButton;
	private GLumpButtonModel pPContinueButton;
	private GLumpButtonModel pPExitButton;
	private int pPBDTextColor;
	private int pPBSTextColor;
}
