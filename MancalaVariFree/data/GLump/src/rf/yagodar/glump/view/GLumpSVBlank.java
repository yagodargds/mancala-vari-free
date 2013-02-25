package rf.yagodar.glump.view;

import rf.yagodar.glump.model.GLumpSVModel;
import rf.yagodar.glump.polygon.Rectangle;

public class GLumpSVBlank {	
	public GLumpSVBlank(GLumpSVModel<Rectangle> sVBackdropRootModel) {
		this.sVRootModel = sVBackdropRootModel;
	}
	
	public GLumpSVModel<Rectangle> getSVRootModel() {
		return sVRootModel;
	}
	
	private GLumpSVModel<Rectangle> sVRootModel;
}
