package rf.yagodar.manqala.logic.ai;

//TODO запилить AI
//TODO написать DOC

import rf.yagodar.manqala.logic.exception.ai.CreateAIFailed;
import rf.yagodar.manqala.logic.model.combat.ManqalaGameBoardCells;

public class AI {

	public AI(ManqalaGameBoardCells state) throws CreateAIFailed
	{
		this.state = state;
		
		if(!initTreeStap())
			throw new CreateAIFailed("Error initialization AI.");
	}
	
	public int nextStap() throws CreateAIFailed
	{
		
		return 0;
	}
	
	private boolean initTreeStap()
	{
		tree = new TreeStap();
		tree.fill(state, belaw);
		
		return true;
	}
	
	private TreeStap tree = null;
	private ManqalaGameBoardCells state = null;
	private int belaw = 5;
}
