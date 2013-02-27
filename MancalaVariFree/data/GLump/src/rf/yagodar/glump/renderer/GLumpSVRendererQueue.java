package rf.yagodar.glump.renderer;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;


public class GLumpSVRendererQueue {
	public GLumpSVRendererQueue() {
		this(false);
	}
	
	public GLumpSVRendererQueue(boolean isLooped) {
		this(isLooped, null);
	}
	
	public GLumpSVRendererQueue(boolean isLooped, GLumpSVRendererQueueNode finishNode) {
		this.isLooped = isLooped;
		nodes = new LinkedBlockingQueue<GLumpSVRendererQueueNode>();
		this.finishNode = finishNode;
		isStopped = false;
		listeners = new ArrayList<IGLumpSVRendererQueueListener>();
	}
	
	public boolean isLooped() {
		return isLooped;
	}

	public void offerNode(GLumpSVRendererQueueNode node) {
		if(node != null) {
			nodes.offer(node);
		}
	}
	
	public void offerAllNodes(ArrayList<GLumpSVRendererQueueNode> nodes) {
		if(nodes != null && !nodes.isEmpty()) {
			for (GLumpSVRendererQueueNode node : nodes) {
				this.nodes.offer(node);
			}
		}
	}
	
	public GLumpSVRendererQueueNode pollNode() {
		return nodes.poll();
	}
	
	public boolean applyNode(GLumpSVRendererQueueNode node) {
		if(node != null) {
			node.applyNode();
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean applyFinishNode() {
		return applyNode(finishNode);
	}
	
	public void clearListeners() {
		synchronized (listeners) {
		listeners.clear();
		}
	}
	
	public void addListener(IGLumpSVRendererQueueListener listener) {
		if(listener != null) {
			synchronized (listeners) {
				listeners.add(listener);
			}
		}
	}
	
	public void onRendered() {
		synchronized (listeners) {
			for (IGLumpSVRendererQueueListener listener : listeners) {
				listener.onRendered();
			}
		}
	}
	
	public void onInterrupted() {
		synchronized (listeners) {
			for (IGLumpSVRendererQueueListener listener : listeners) {
				listener.onInterrupted();
			}
		}
	}
	
	public void stopRender() {
		isStopped = true;
	}
	
	public boolean isStopped() {
		return isStopped;
	}
	
	private ArrayList<IGLumpSVRendererQueueListener> listeners;
	private boolean isStopped;
	private final boolean isLooped;
	private final GLumpSVRendererQueueNode finishNode;
	private final Queue<GLumpSVRendererQueueNode> nodes;
}
