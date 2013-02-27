package rf.yagodar.glump.model;

import java.util.ArrayList;

import rf.yagodar.glump.animation.AbstractAnimScen;
import rf.yagodar.glump.bitmap.TextBitmapManager;
import rf.yagodar.glump.point.Point2D;
import rf.yagodar.glump.polygon.AbstractPolygon;
import rf.yagodar.glump.polygon.Rectangle;
import rf.yagodar.glump.view.GLumpSV;

public class GLumpSVModel<T extends AbstractPolygon> {
	public GLumpSVModel(GLumpSV<?> surfaceView, T modelPolygon) {
		this(true, surfaceView, modelPolygon);
	}
	
	public GLumpSVModel(T modelPolygon) {
		this(false, null, modelPolygon);
	}
	
	public <V extends AbstractPolygon> GLumpSVModel<V> addChildModel(V childModelPolygon) {
		return this.addChildModel(childModelPolygon, true);
	}
	
	public <V extends AbstractPolygon> GLumpSVModel<V> addChildModel(V childModelPolygon, boolean addPolygonToRenderer) {
		if(childModelPolygon == null) {
			//TODO LOG?
			return null;
		}
		else {
			GLumpSVModel<V> childModel = new GLumpSVModel<V>(childModelPolygon);
			childModel.setParentModel(this);			
			childModels.add(childModel);
			if(addPolygonToRenderer) {
				getSurfaceView().addPolygonToRender(childModelPolygon);
			}
			
			return childModel;
		}
	}
	
	public <V extends AbstractPolygon> void addAllChildModels(ArrayList<V> childModelsPolygons) {
		this.addAllChildModels(childModelsPolygons, true);
	}
	
	public <V extends AbstractPolygon> void addAllChildModels(ArrayList<V> childModelsPolygons, boolean addPolygonsToRenderer) {
		if(childModelsPolygons == null) {
			//TODO LOG?
		}
		else {
			for (V childModelPolygon : childModelsPolygons) {
				this.addChildModel(childModelPolygon, addPolygonsToRenderer);
			}
		}
	}
	
	public <V extends AbstractPolygon> GLumpSVModel<V> addChildModel(GLumpSVModel<V> childModel) {
		return this.addChildModel(childModel, true);
	}
	
	public <V extends AbstractPolygon> GLumpSVModel<V> addChildModel(GLumpSVModel<V> childModel, boolean addPolygonsToRenderer) {
		if(childModel == null) {
			//TODO LOG?
		}
		else {		
			childModel.setParentModel(this);
			childModels.add(childModel);
			if(addPolygonsToRenderer) {
				getSurfaceView().addAllPolygonsToRender(childModel.getPolygons(true));
			}
		}
		
		return childModel;
	}
	
	public <V extends AbstractPolygon> void removeChildModel(GLumpSVModel<V> childModel) {
		this.removeChildModel(childModel, true);
	}
	
	public <V extends AbstractPolygon> void removeChildModel(GLumpSVModel<V> childModel, boolean removePolygonsFromRenderer) {
		if(childModel != null) {
			ArrayList<GLumpSVModel<?>> childModelsToRemove = new ArrayList<GLumpSVModel<?>>();
			childModelsToRemove.add(childModel);
			this.removeAllChildModels(childModelsToRemove, removePolygonsFromRenderer);
		}
	}
	
	public ArrayList<AbstractPolygon> removeAllChildModels(ArrayList<GLumpSVModel<?>> childModelsToRemove) {
		return this.removeAllChildModels(childModelsToRemove, true);
	}
	
	public ArrayList<AbstractPolygon> removeAllChildModels(ArrayList<GLumpSVModel<?>> childModelsToRemove, boolean removePolygonsFromRenderer) {
		ArrayList<AbstractPolygon> polygonsToDel = null;
		
		if(childModelsToRemove != null) {
			polygonsToDel = new ArrayList<AbstractPolygon>();
						
			for(GLumpSVModel<?> childModel : childModelsToRemove) {
				childModel.addAllPolygonsTo(polygonsToDel);
			}
			
			if(removePolygonsFromRenderer) {
				getSurfaceView().removeAllPolygonsFromRender(polygonsToDel);
			}
			
			childModels.removeAll(childModelsToRemove);
		}
		
		return polygonsToDel;
	}

	/*public void retainAllChildModels(ArrayList<GLumpSVModel<?>> childModelsToRetain) {
		if(childModelsToRetain != null) {
			for (GLumpSVModel<?> childModel : childModels) {
				if(!childModelsToRetain.contains(childModel)) {
					getSurfaceView().removePolygonFromRender(childModel.getPolygon());
				}
			}
			
			childModels.retainAll(childModelsToRetain);
		}
	}*/
	
	public ArrayList<AbstractPolygon> clearChildModels(boolean removePolygonsFromRenderer) {
		ArrayList<AbstractPolygon> polygonsToDel = getPolygons(true);
		
		if(removePolygonsFromRenderer) {
			getSurfaceView().removeAllPolygonsFromRender(polygonsToDel);
		}
		
		childModels.clear();
		
		return polygonsToDel;
	}

	public boolean isContainsPoint(Point2D point2D) {
		return modelPolygon.isContainsPoint(point2D);
	}

	public GLumpSVModelEditInfo drawText(String text, int textColor) {
		if(getPolygon() instanceof Rectangle) {
			return new GLumpSVModelEditInfo(this, childModels, TextBitmapManager.getInstance().drawText((Rectangle) getPolygon(), text, textColor));
		}
		
		return null;
	}

	public ArrayList<GLumpSVModel<?>> getChildModels() {
		synchronized (childModels) {
			return childModels;
		}
	}
	
	public boolean isRootModel() {
		return rootModel;
	}
	
	public void setSurfaceView(GLumpSV<?> surfaceView) {
		if(rootModel) {
			this.surfaceView = surfaceView;
		}
	}
	
	public GLumpSV<?> getSurfaceView() {
		if(rootModel) {
			return surfaceView;
		}
		else {
			return parentModel.getSurfaceView();	
		}
	}

	public T getPolygon() {
		return modelPolygon;
	}
	
	public void setAnimScen(AbstractAnimScen animScen) {
		if(animScen != null) {
			for(AbstractPolygon polygon : getPolygons(true)) {
				polygon.setAnimScen(animScen.clone());
			}
		}
	}
	
	public ArrayList<AbstractPolygon> getPolygons(boolean includeOwnPolygon) {
		ArrayList<AbstractPolygon> polygons = new ArrayList<AbstractPolygon>();
		
		if(includeOwnPolygon) {
			polygons.add(modelPolygon);
		}
		
		for(GLumpSVModel<?> childModel : getChildModels()) {
			childModel.addAllPolygonsTo(polygons);
		}
		
		return polygons;
	}
	
	private <V extends AbstractPolygon> void setParentModel(GLumpSVModel<V> parentModel) {
		this.parentModel = parentModel;
	}
	
	private GLumpSVModel(boolean rootModel, GLumpSV<?> surfaceView, T modelPolygon) {
		this.modelPolygon = modelPolygon;
		
		if(rootModel) {
			this.surfaceView = surfaceView;
		}
		
		this.rootModel = rootModel;		
		childModels = new ArrayList<GLumpSVModel<?>>();
	}
	
	private void addAllPolygonsTo(ArrayList<AbstractPolygon> polygons) {
		polygons.add(modelPolygon);
		for(GLumpSVModel<?> childModel : getChildModels()) {
			childModel.addAllPolygonsTo(polygons);
		}
	}
		
	private GLumpSV<?> surfaceView;
	private boolean rootModel;
	private GLumpSVModel<?> parentModel;
	private final T modelPolygon;
	private final ArrayList<GLumpSVModel<?>> childModels;
}
