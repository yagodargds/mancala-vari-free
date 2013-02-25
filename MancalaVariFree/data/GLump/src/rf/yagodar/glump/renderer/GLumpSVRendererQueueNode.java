package rf.yagodar.glump.renderer;

import java.util.ArrayList;

import rf.yagodar.glump.animation.AnimScenInfo;
import rf.yagodar.glump.model.GLumpSVModelEditInfo;

public class GLumpSVRendererQueueNode {
	public GLumpSVRendererQueueNode() {
		modelEditInfos = new ArrayList<GLumpSVModelEditInfo>();
		animScenInfos = new ArrayList<AnimScenInfo>();
	}
	
	public void addModelEditInfo(GLumpSVModelEditInfo modelEditInfo) {
		if(modelEditInfo != null) {
			modelEditInfos.add(modelEditInfo);
		}
	}
	
	public void addAllModelEditInfo(ArrayList<GLumpSVModelEditInfo> modelEditInfos) {
		if(modelEditInfos != null) {
			this.modelEditInfos.addAll(modelEditInfos);
		}
	}
	
	public void addAnimScenInfo(AnimScenInfo animScenInfo) {
		if(animScenInfo != null) {
			animScenInfos.add(animScenInfo);
		}
	}
	
	public void addAllAnimScenInfo(ArrayList<AnimScenInfo> animScenInfos) {
		if(animScenInfos != null) {
			this.animScenInfos.addAll(animScenInfos);
		}
	}
	
	public void applyNode() {
		for (GLumpSVModelEditInfo modelEditInfo : modelEditInfos) {
			if(modelEditInfo != null) {
				modelEditInfo.apply();
			}
		}
		
		for (AnimScenInfo animScenInfo : animScenInfos) {
			if(animScenInfo != null) {
				animScenInfo.apply();
			}
		}
	}
	
	private final ArrayList<GLumpSVModelEditInfo> modelEditInfos;
	private final ArrayList<AnimScenInfo> animScenInfos;
	
}
