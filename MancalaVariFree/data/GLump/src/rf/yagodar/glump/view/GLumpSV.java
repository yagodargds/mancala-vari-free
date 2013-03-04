package rf.yagodar.glump.view;

import java.util.ArrayList;
import java.util.Collection;

import rf.yagodar.glump.model.GLumpSVModel;
import rf.yagodar.glump.polygon.AbstractPolygon;
import rf.yagodar.glump.renderer.GLumpSVRenderer;
import rf.yagodar.glump.renderer.GLumpSVRendererQueue;
import rf.yagodar.glump.renderer.GLumpSVRendererQueueNode;
import rf.yagodar.glump.renderer.IGLumpSVRendererQueueListener;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class GLumpSV<T extends GLumpSVBlank> extends GLSurfaceView {
	public GLumpSV(Context context, float levelZ, int glSurfaceViewRenderMode) {
		super(context);
		
		this.levelZ = levelZ;
		
		displayMetrics = new DisplayMetrics();
		((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);

		setEGLConfigChooser(false);
		renderer = GLumpSVRenderer.getInstance();
		this.glSurfaceViewRenderMode = glSurfaceViewRenderMode;
		
		rendererListeners = new ArrayList<IGLumpSVRendererQueueListener>();
	}
	
	public GLumpSV(Context context, float levelZ) {
		this(context, levelZ, GLSurfaceView.RENDERMODE_WHEN_DIRTY);
	}
	
	public GLumpSV(Context context, int glSurfaceViewRenderMode) {
		this(context, 0.0f, glSurfaceViewRenderMode);
	}
	
	public GLumpSV(Context context) {
		this(context, 0.0f, GLSurfaceView.RENDERMODE_WHEN_DIRTY);
	}
	
	public void setRenderer() {
		setRenderer(renderer);
		setRenderMode(glSurfaceViewRenderMode);
	}
	
	public void requestAdditionalRender(GLumpSVRendererQueue rendererQueue) {
		additionalRenderConveyor = new RenderConveyor(rendererQueue);
		additionalRenderThread = new Thread(additionalRenderConveyor);
		additionalRenderThread.start();
	}
	
	public void requestMainRender(GLumpSVRendererQueue rendererQueue) {
		mainRenderConveyor = new RenderConveyor(rendererQueue);
		mainRenderThread = new Thread(mainRenderConveyor);
		mainRenderThread.start();
	}
	
	public void pauseMainRender() {
		if(mainRenderConveyor != null) {
			mainRenderConveyor.setPaused(true);
		}
	}
	
	public boolean isMainRenderPaused() {
		if(mainRenderConveyor != null) {
			return mainRenderConveyor.isPaused();
		}
		
		return false;
	}
	
	public void continueMainRender() {
		if(mainRenderConveyor != null) {
			mainRenderConveyor.setPaused(false);
		}
	}
	
	public void interruptMainRenderThread() {
		if(mainRenderThread != null) {
			mainRenderThread.interrupt();
			mainRenderThread = null;
			
		}
	}
	
	public float getLevelZ() {
		return levelZ;
	}
	
	public DisplayMetrics getDisplayMetrics() {
		return displayMetrics;
	}
	
	public AbstractPolygon addPolygonToRender(AbstractPolygon polygon) {
		if(polygon != null) {
			renderer.addPolygon(polygon);
		}
		
		return polygon;
	}
	
	public void addAllPolygonsToRender(Collection<? extends AbstractPolygon> polygons) {
		if(polygons != null) {
			renderer.addAllPolygons(polygons);
		}
	}
	
	public AbstractPolygon removePolygonFromRender(AbstractPolygon polygon) {
		if(polygon != null) {
			renderer.removePolygon(polygon);
		}
		
		return polygon;		
	}
	
	public void removeAllPolygonsFromRender(Collection<? extends AbstractPolygon> polygons) {
		if(polygons != null) {
			renderer.removeAllPolygons(polygons);
		}
	}
	
	public void addRendererListener(IGLumpSVRendererQueueListener rendererListener) {
		if(rendererListener != null) {
			rendererListeners.add(rendererListener);
		}
	}
	
	public void setSVBlank(T sVBlank) {
		if(sVBlank != null) {
			registerSVRootModel(sVBlank.getSVRootModel());
			this.sVBlank = sVBlank;
		}
	}
	
	public T getSVBlank() {
		return sVBlank;
	}
	
	public long getAnimStepMilisec() {
		return animStepMilisec;
	}

	public void setAnimStepMilisec(long animStepMilisec) {
		this.animStepMilisec = animStepMilisec;
	}

	private <V extends AbstractPolygon> void registerSVRootModel(GLumpSVModel<V> sVRootModel) {
		if(sVRootModel != null && sVRootModel.isRootModel()) {
			renderer.clearPolygons();
			
			sVRootModel.setSurfaceView(this);
			renderer.addAllPolygons(sVRootModel.getPolygons(true));
		}
	}
	
	private class RenderConveyor implements Runnable {
		public RenderConveyor(GLumpSVRendererQueue rendererQueue) {
			this.rendererQueue = rendererQueue;
		}
		
		@Override
		public void run() {
			try {
				if(rendererQueue != null) {
					do {
						if(!isPaused()) {
							GLumpSVRendererQueueNode nextNode = rendererQueue.pollNode();
							if(rendererQueue.applyNode(nextNode)) {
								GLumpSV.super.requestRender();
								while(renderer.refreshPolygons()) {
									while(isPaused()) {
										Thread.sleep(getAnimStepMilisec());
									}
									
									GLumpSV.super.requestRender();
									Thread.sleep(getAnimStepMilisec());
								}

								if(rendererQueue.isLooped()) {
									rendererQueue.offerNode(nextNode);
								}
							}
							else {
								rendererQueue.stopRender();
							}
						}
						else {
							Thread.sleep(getAnimStepMilisec());
						}
					}
					while(!rendererQueue.isStopped());

					if(rendererQueue.applyFinishNode()) {
						GLumpSV.super.requestRender();
						while(renderer.refreshPolygons()) {
							while(isPaused()) {
								Thread.sleep(getAnimStepMilisec());
							}
							GLumpSV.super.requestRender();
							Thread.sleep(getAnimStepMilisec());
						}
					}
					
					while(isPaused()) {
						Thread.sleep(getAnimStepMilisec());
					}
					
					rendererQueue.onRendered();
				}
			}
			catch (Exception e) {
				rendererQueue.onInterrupted();
			}
		}
		
		public void setPaused(boolean isPaused) {
			this.isPaused = isPaused;
		}
		
		public boolean isPaused() {
			return isPaused;
		}
		
		private GLumpSVRendererQueue rendererQueue;
		private boolean isPaused;
	}
	
	private final float levelZ;
	private final DisplayMetrics displayMetrics;
	private final GLumpSVRenderer renderer;
	private final ArrayList<IGLumpSVRendererQueueListener> rendererListeners;
	private T sVBlank;
	private RenderConveyor mainRenderConveyor;
	private Thread mainRenderThread;
	private RenderConveyor additionalRenderConveyor;
	private Thread additionalRenderThread;
	private final int glSurfaceViewRenderMode;
	private long animStepMilisec = NORMAL_ANIM_STEP_MILISEC;
	
	public final static long NORMAL_ANIM_STEP_MILISEC = 100L;
}
