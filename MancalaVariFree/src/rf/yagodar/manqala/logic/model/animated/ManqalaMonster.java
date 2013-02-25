package rf.yagodar.manqala.logic.model.animated;

import java.security.SecureRandom;

import rf.yagodar.manqala.logic.combat.ManqalaCombat;
import rf.yagodar.manqala.logic.combat.ManqalaMoveResult;
import rf.yagodar.manqala.logic.model.combat.ManqalaCell;
import rf.yagodar.manqala.logic.parameters.SVari;

//TODO запилить MONSTER
//TODO DOC

public class ManqalaMonster extends ManqalaCharacter {
	public ManqalaMonster(int charId, String charName) {
		super(charId, charName);
	}
	
	public ManqalaMoveResult makeMove(ManqalaCombat manqalaCombat) {
		ManqalaMoveResult moveResult = null;
		
		if(manqalaCombat != null) {
			try {
				long timeToSimulateAi = 0L;
				for (ManqalaCell cell : manqalaCombat.getGameBoard().getGameBoardCells().getOpponentCells(this).getCells()) {
					if(!cell.isWarehouse() && cell.getGrainsCount() > 0) {
						timeToSimulateAi += 500L;
					}
				}
				
				SecureRandom rand = new SecureRandom();
				ManqalaCell cell = manqalaCombat.getGameBoard().getGameBoardCells().getOpponentCells(this).getCellByPositionId((byte) rand.nextInt(SVari.CELLS_COUNT)); //TODO SVARi

				moveResult = manqalaCombat.makeMove(cell);
				if(moveResult != null) {
					while(!moveResult.isValidMove()) {
						cell = manqalaCombat.getGameBoard().getGameBoardCells().getOpponentCells(this).getCellByPositionId((byte) rand.nextInt(SVari.CELLS_COUNT)); //TODO SVARi
						moveResult = manqalaCombat.makeMove(cell);
					}

					Thread.sleep(timeToSimulateAi);
				}
			} 
			catch (InterruptedException e) {}
		}
		
		return moveResult;
	}
}
