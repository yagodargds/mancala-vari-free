package rf.yagodar.glump.animation;

import rf.yagodar.glump.polygon.AbstractPolygon;
import android.util.Log;
import android.util.SparseArray;

public abstract class AbstractHomogenAnimScen<T> extends AbstractAnimScen {
	public AbstractHomogenAnimScen(T[] nodes, long animationTime) {
		this(nodes, null, animationTime);
	}
	
	public AbstractHomogenAnimScen(T[] nodes, AbstractPolygon parentPolygon, long animationTime) {
		super(parentPolygon, animationTime, false);

		this.nodes = nodes;
		if(this.nodes != null) {
			if(!writeAnimations(animationTime)) {
				setAnimations(null);
				Log.e(LOG_TAG, "Error writing animations! [animationTime:(" + animationTime + ")]!");
			}
		}
		else {
			Log.e(LOG_TAG, "Illegal init attr! [nodes:(" + nodes + ");]!");
		}
	}
	
	@Override
	protected boolean writeAnimations(long animationTime) {
		if(getNodes() != null && getNodes().length > 1) {
			float nodeLenght;
			float nodesLenght = 0.0f;
			SparseArray<Float> nodeLenghts = new SparseArray<Float>();
			
			T startNode = getNodes()[0];
			for (int i = 1; i < getNodes().length; i++) {
				nodeLenght = calcLenght(startNode, getNodes()[i]);
				nodesLenght += nodeLenght;
				nodeLenghts.put(i - 1, nodeLenght);
				startNode = getNodes()[i];
			}

			for (int i = 0; i < getNodes().length - 1; i++) {
				addAnimation(createAnim(getNodes()[i], getNodes()[i + 1], (long) Math.floor((double) animationTime * ((double) nodeLenghts.get(i) / (double) nodesLenght ))));
			}
			
			return true;
		}
		
		return false;
	}
	
	abstract protected float calcLenght(T start, T dest);
	
	abstract protected AbstractAnim<T> createAnim(T start, T dest, long animationTime);
	
	protected T[] getNodes() {
		return nodes;
	}
	
	private T[] nodes;
	
	private static final String LOG_TAG = AbstractHomogenAnimScen.class.getSimpleName();
}
