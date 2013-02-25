package rf.yagodar.manqala.logic.ai;

//TODO запилить TREE
//TODO DOC

import java.util.LinkedList;
import java.util.List;

import rf.yagodar.manqala.logic.combat.ManqalaMoveResult;
import rf.yagodar.manqala.logic.model.animated.ManqalaCharacter;
import rf.yagodar.manqala.logic.model.combat.ManqalaCell;
import rf.yagodar.manqala.logic.model.combat.ManqalaGameBoardCells;

public class TreeStap 
{
	public TreeStap addNode(TreeStap node)
	{
		listNode.add(node);
		
		return node;
	}
	
	public List<TreeStap> getNodes()
	{
		return listNode;
	}
	
	public ManqalaGameBoardCells getState() {
		return state;
	}

	public void setState(ManqalaGameBoardCells state) {
		this.state = state;
	}
	
	public void fill(ManqalaGameBoardCells state, int belaw)
	{
		//TODO Aza! тщательно проверь.
		if (belaw <= 0)
			return;
		
		setState(state);

		ManqalaGameBoardCells newState;
		try {
			newState = state.clone();
			
			ManqalaCharacter player = newState.getCombat().getWalketh();

			for(ManqalaCell cell : newState.getOpponentCells(player).getCells())
			{
				ManqalaMoveResult moveResult = newState.getCombat().getMoveResult(cell, player);
				if(moveResult.isValidMove()) {
					newState = moveResult.getResultGameBoardCells().clone();
					addNode(new TreeStap()).fill(newState, belaw - 1);
				}
			}
		} 
		catch (CloneNotSupportedException cnse) {
			cnse.printStackTrace();
		}
	}

	private ManqalaGameBoardCells state = null;
	private List<TreeStap> listNode = new LinkedList<TreeStap>();
}
