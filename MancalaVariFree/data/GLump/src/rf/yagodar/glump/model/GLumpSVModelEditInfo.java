package rf.yagodar.glump.model;

import java.util.ArrayList;

import rf.yagodar.glump.polygon.AbstractPolygon;

public class GLumpSVModelEditInfo {
	public GLumpSVModelEditInfo(GLumpSVModel<?> modelToEdit, ArrayList<GLumpSVModel<?>> modelsToDel, ArrayList<AbstractPolygon> polygonsToAdd) {
		this.modelToEdit = modelToEdit;
		this.modelsToDel = modelsToDel;
		this.polygonsToAdd = polygonsToAdd;
	}
	
	public void apply() {
		if(modelToEdit != null) {
			modelToEdit.removeAllChildModels(modelsToDel);
			modelToEdit.addAllChildModels(polygonsToAdd);
		}
	}

	private final GLumpSVModel<?> modelToEdit;
	private final ArrayList<GLumpSVModel<?>> modelsToDel;
	private final ArrayList<AbstractPolygon> polygonsToAdd;
}
