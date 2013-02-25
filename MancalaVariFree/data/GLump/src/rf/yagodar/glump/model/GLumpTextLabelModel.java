package rf.yagodar.glump.model;

import rf.yagodar.glump.polygon.Rectangle;

public class GLumpTextLabelModel extends GLumpSVModel<Rectangle> {
	public GLumpTextLabelModel(Rectangle modelPolygon, Rectangle leftBackDropPolygon, Rectangle middleBackDropPolygon, Rectangle rightBackDropPolygon, Rectangle textModelPolygon, boolean addPolygonToRenderer) {
		super(modelPolygon);
		
		addChildModel(leftBackDropPolygon, addPolygonToRenderer);
		addChildModel(middleBackDropPolygon, addPolygonToRenderer);
		addChildModel(rightBackDropPolygon, addPolygonToRenderer);
		this.textModel = addChildModel(textModelPolygon, addPolygonToRenderer);
	}
	
	public GLumpSVModel<Rectangle> getTextModel() {
		return textModel;
	}

	private final GLumpSVModel<Rectangle> textModel;
}
