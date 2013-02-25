package rf.yagodar.glump.model;

import rf.yagodar.glump.polygon.Rectangle;

public class GLumpButtonModel extends GLumpSVModel<Rectangle> {
	public GLumpButtonModel(Rectangle modelPolygon) {
		super(modelPolygon);
	}

	public void setButtonDeselectedModel(GLumpTextLabelModel buttonDeselectedModel) {
		this.buttonDeselectedModel = buttonDeselectedModel;
	}

	public void setButtonSelectedModel(GLumpTextLabelModel buttonSelectedModel) {
		this.buttonSelectedModel = buttonSelectedModel;
	}

	public GLumpTextLabelModel getButtonDeselectedModel() {
		return buttonDeselectedModel;
	}
	public GLumpTextLabelModel getButtonSelectedModel() {
		return buttonSelectedModel;
	}

	private GLumpTextLabelModel buttonDeselectedModel;
	private GLumpTextLabelModel buttonSelectedModel;
}
