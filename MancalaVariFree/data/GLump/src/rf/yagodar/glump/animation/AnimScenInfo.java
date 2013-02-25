package rf.yagodar.glump.animation;

import java.util.ArrayList;

import rf.yagodar.glump.model.GLumpSVModel;

public class AnimScenInfo {
	public AnimScenInfo(GLumpSVModel<?> animScenModel) {
		this(animScenModel, new ArrayList<AbstractAnimScen>());
	}
	
	public AnimScenInfo(GLumpSVModel<?> animScenModel, AbstractAnimScen animScen) {
		this(animScenModel, new ArrayList<AbstractAnimScen>());
		addAnimScen(animScen);
	}
	
	public AnimScenInfo(GLumpSVModel<?> animScenModel, ArrayList<AbstractAnimScen> animScens) {
		this.animScenModel = animScenModel;
		
		if(animScens != null) {
			this.animScens = animScens;
		}
		else {
			this.animScens = new ArrayList<AbstractAnimScen>();
		}
	}
	
	public void addAnimScen(AbstractAnimScen animScen) {
		if(animScen != null) {
			animScens.add(animScen);
		}
	}
	
	public void addAllAnimScens(ArrayList<AbstractAnimScen> animScens) {
		if(animScens != null) {
			this.animScens.addAll(animScens);
		}
	}
	
	public void apply() {
		if(animScenModel != null && animScens != null && !animScens.isEmpty()) {
			for (AbstractAnimScen animScen : animScens) {
				animScenModel.setAnimScen(animScen);
			}
		}
	}
	
	private final GLumpSVModel<?> animScenModel;
	private final ArrayList<AbstractAnimScen> animScens;
}
