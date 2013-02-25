package rf.yagodar.manqala.logic.model.combat;

import java.util.ArrayList;

import rf.yagodar.manqala.logic.combat.ManqalaCombat;
import rf.yagodar.manqala.logic.combat.ManqalaOpponentsSet;
import rf.yagodar.manqala.logic.model.animated.ManqalaCharacter;
import rf.yagodar.manqala.logic.parameters.SVari;

/**
 * Предназначен для управления набором ячеек игровой доски (этот набор подразделяется на собственные наборы ячеек оппонентов)
 * 
 * @author Yagodar
 * @version 1.0.0 18.10.2012
 */
public class ManqalaGameBoardCells implements Cloneable {
	/**
	 * Создаёт экземпляр набора ячеек игровой доски с задаваемыми начальными параметрами.
	 * 
	 * @param manqalaGameBoard игровая доска, для которой создаётся набор ячеек, не <code>null</code>
	 * @param cellsCount количество создаваемых ячеек. Плюсом к этому будет создана ячейка-амбар.
	 * @param cellInitGrains количество зёрен в создаваемых "ячейках - не амбарах"
	 */
	public ManqalaGameBoardCells(ManqalaGameBoard manqalaGameBoard, byte cellsCount, byte cellInitGrains) {
		if(manqalaGameBoard == null) {
			//TODO Ex
			return;
		}
		
		ManqalaCombat combat = manqalaGameBoard.getCombat();
		if(combat == null) {
			//TODO Ex
			return;
		}
		
		ManqalaOpponentsSet manqalaOpponentsSet = combat.getOpponents();		
		if(manqalaOpponentsSet == null) {
			//TODO Ex
			return;
		}
		
		if(cellsCount <= 0) {
			//TODO Ex
			return;
		}
		
		if(cellInitGrains <= 0) {
			//TODO Ex
			return;
		}
		
		ManqalaCharacter firstOpponent = manqalaOpponentsSet.getFirstOpponent();
		ManqalaCharacter secondOpponent = manqalaOpponentsSet.getSecondOpponent();		
		if(firstOpponent == null || secondOpponent == null) {
			//TODO Ex
			return;
		}
		
		this.gameBoard = manqalaGameBoard;
		
		ArrayList<ManqalaCell> firstOpponentCells = new ArrayList<ManqalaCell>();
		ArrayList<ManqalaCell> secondOpponentCells = new ArrayList<ManqalaCell>();
		
		for(byte b = 0; b < cellsCount; b++) {
			firstOpponentCells.add(new ManqalaCell(firstOpponent, cellInitGrains, b));
			secondOpponentCells.add(new ManqalaCell(secondOpponent, cellInitGrains, b));
		}
		
		firstOpponentCells.add(new ManqalaCell(firstOpponent, (byte) 0, (byte) firstOpponentCells.size(), true));
		secondOpponentCells.add(new ManqalaCell(secondOpponent, (byte) 0, (byte) secondOpponentCells.size(), true));
		
		this.opponentsCells.add(new ManqalaOpponentCells(firstOpponent, firstOpponentCells));
		this.opponentsCells.add(new ManqalaOpponentCells(secondOpponent, secondOpponentCells));		
	}
	
	/**
	 * Создаёт экземпляр набора ячеек игровой доски с задаваемыми начальными параметрами.
	 * 
	 * @param manqalaGameBoard игровая доска, для которой создаётся набор ячеек, не <code>null</code>
	 * @param firstOpponentCellsGrains массив количества зёрен в создаваемых ячейках первого оппонента (включая амбар)
	 * @param secondOpponentCellsGrains массив количества зёрен в создаваемых ячейках второго оппонента (включая амбар)
	 */
	public ManqalaGameBoardCells(ManqalaGameBoard manqalaGameBoard, byte[] firstOpponentCellsGrains, byte[] secondOpponentCellsGrains) {
		if(manqalaGameBoard == null) {
			//TODO Ex
			return;
		}
		
		ManqalaCombat combat = manqalaGameBoard.getCombat();
		if(combat == null) {
			//TODO Ex
			return;
		}
		
		ManqalaOpponentsSet manqalaOpponentsSet = combat.getOpponents();		
		if(manqalaOpponentsSet == null) {
			//TODO Ex
			return;
		}
		
		if(firstOpponentCellsGrains.length == 0) {
			//TODO Ex
			return;
		}
		
		if(secondOpponentCellsGrains.length == 0) {
			//TODO Ex
			return;
		}
		
		//TODO в зависимости от типа
		if(firstOpponentCellsGrains.length != secondOpponentCellsGrains.length) {
			//TODO Ex
			return;
		}
		
		ManqalaCharacter firstOpponent = manqalaOpponentsSet.getFirstOpponent();
		ManqalaCharacter secondOpponent = manqalaOpponentsSet.getSecondOpponent();		
		if(firstOpponent == null || secondOpponent == null) {
			//TODO Ex
			return;
		}
		
		this.gameBoard = manqalaGameBoard;
		
		ArrayList<ManqalaCell> firstOpponentCells = new ArrayList<ManqalaCell>();
		ArrayList<ManqalaCell> secondOpponentCells = new ArrayList<ManqalaCell>();
		
		for(byte b = 0; b < firstOpponentCellsGrains.length - 1; b++) {
			firstOpponentCells.add(new ManqalaCell(firstOpponent, firstOpponentCellsGrains[b], b));
			secondOpponentCells.add(new ManqalaCell(secondOpponent, secondOpponentCellsGrains[b], b));
		}
		
		firstOpponentCells.add(new ManqalaCell(firstOpponent, firstOpponentCellsGrains[firstOpponentCellsGrains.length - 1], (byte) firstOpponentCells.size(), true));
		secondOpponentCells.add(new ManqalaCell(secondOpponent, secondOpponentCellsGrains[firstOpponentCellsGrains.length - 1], (byte) secondOpponentCells.size(), true));
		
		this.opponentsCells.add(new ManqalaOpponentCells(firstOpponent, firstOpponentCells));
		this.opponentsCells.add(new ManqalaOpponentCells(secondOpponent, secondOpponentCells));		
	}
	
	/**
	 * Возврашает игровую доску владелицу данного набора ячеек.
	 * 
	 * @return игровая доска - владелица набора ячеек.
	 */
	public ManqalaGameBoard getGameBoard() {
		return this.gameBoard;
	}
	
	/**
	 * Устанавливает игровую доску владелицу данного набора ячеек.
	 */
	public void setGameBoard(ManqalaGameBoard gameBoard) {
		this.gameBoard = gameBoard;
	}
	
	/**
	 * Возврашает схватку - владелицу игровой доски - владелицы данного набора ячеек.
	 * 
	 * @return схватка - владелица игровой доски - владелицы данного набора ячеек.
	 */
	public ManqalaCombat getCombat() {
		ManqalaGameBoard gameBoard = getGameBoard();
		if(gameBoard == null) {
			return null;
		}
		
		return gameBoard.getCombat();
	}
	
	//TODO DOC
	public ManqalaCell getCellByGlobalPosId(byte globalPosId) {
		if(globalPosId >= 0 && globalPosId <= SVari.CELLS_COUNT) {
			return getFirstOpponentCells().getCellByPositionId(globalPosId);
		}
		
		if(globalPosId > SVari.CELLS_COUNT && globalPosId <= 2 * SVari.CELLS_COUNT + 1) {
			return getSecondOpponentCells().getCellByPositionId((byte) (globalPosId - SVari.CELLS_COUNT - 1));
		}
		
		return null;
	}
	
	//TODO DOC
	public ManqalaOpponentCells getFirstOpponentCells() {
		return getOpponentCells(getFirstOpponent());
	}

	//TODO DOC
	public ManqalaOpponentCells getSecondOpponentCells() {
		return getOpponentCells(getSecondOpponent());
	}

	//TODO DOC
	public ManqalaCharacter getFirstOpponent() {
		return getCombat().getFirstOpponent();
	}

	//TODO DOC
	public ManqalaCharacter getSecondOpponent() {
		return getCombat().getSecondOpponent();
	}
	
	/**
	 * Возврашает набор ячеек оппонента.
	 * 
	 * @param opponent персонаж, набор ячеек которого необходимо взять, не <code>null</code>
	 * @return набор ячеек оппонента.
	 */
	public ManqalaOpponentCells getOpponentCells(ManqalaCharacter opponent) {
		return getOpponentCells(opponent, false);
	}
	
	/**
	 * Возврашает набор ячеек оппонента либо противника оппонента в зависимости от переданных параметров.
	 * 
	 * @param opponent персонаж, набор ячеек которого необходимо взять, не <code>null</code>
	 * @param enemyGet <br /><code>true</code> взять набор ячеек противника персонажа<br />
	 * 					<code>false</code> взять набор ячеек персонажа
	 * @return набор ячеек оппонента.
	 */
	public ManqalaOpponentCells getOpponentCells(ManqalaCharacter opponent, boolean enemyGet) {
		if(opponent == null) {
			return null;
		}
		
		for (ManqalaOpponentCells opponentCells : this.opponentsCells) {
			if(enemyGet && !opponent.equals(opponentCells.getOwner())) {
				return opponentCells;
			}
			
			if(!enemyGet && opponent.equals(opponentCells.getOwner())) {
				return opponentCells;
			}
		}
		
		return null;
	}	

	/**
	 * Возвращает ячейку - амбар персонажа.
	 * 
	 * @param opponent персонаж, амбар которого небоходимо взять, не <code>null</code>
	 * @return ячейка - амбар персонажа.
	 */
	public ManqalaCell getOpponentWarehouse(ManqalaCharacter opponent) {
		ManqalaOpponentCells opponentCells = getOpponentCells(opponent);
		if(opponentCells == null) {
			//TODO Ex
			return null;
		}
		
		return opponentCells.getWarehouseCell();
	}

	/**
	 * Возвращает следующую ячейку игровой доски после переданной ячейки. 
	 * Проход идёт в напрвлении с нулевой ячейки до амбара. Причем, если переданная ячейка 
	 * является амбаром, то возвращается нулевая ячейка противника персонажа, владеющего переданной ячейкой.
	 * 
	 * @param cell ячейка, после которой будет взята следующая ячейка, не <code>null</code>
	 * @return следующая ячейка игровой доски после переданной ячейки.
	 */
	public ManqalaCell getNextCell(ManqalaCell cell) {
		if(cell == null) {
			//TODO Ex
			return null;
		}

		ManqalaCharacter opponent = cell.getOwner();
		if(opponent == null) {
			//TODO Ex
			return null;
		}

		ManqalaOpponentCells opponentCells;

		if(cell.isWarehouse()) {
			opponentCells = getOpponentCells(opponent, true);
		} 
		else {
			opponentCells = getOpponentCells(opponent);
		}

		if(opponentCells == null) {
			// TODO Ex
			return null;
		}
		
		return opponentCells.getNextCell(cell);		
	}
	
	/**
	 * Возвращает клон набора ячеек игровой доски. Из объектов-полей клонируется: наборы ячеек оппонентов. 
	 * Игровая доска - владелица набора ячеек клонирована не будет, а будет передана копия ссылки на объект игровой доски - владелицы. 
	 * 
	 * @return клон набора ячеек игрвой доски.
	 */
	@Override
	public ManqalaGameBoardCells clone() throws CloneNotSupportedException {
		ManqalaGameBoardCells gameBoardCellsClone = (ManqalaGameBoardCells) super.clone();
		
		ArrayList<ManqalaOpponentCells> opponentsCellsClone = new ArrayList<ManqalaOpponentCells>();
		for (ManqalaOpponentCells opponentCells : getOpponentsCells()) {
			opponentsCellsClone.add(opponentCells.clone());
		}
		
		gameBoardCellsClone.setOpponentsCells(opponentsCellsClone);
		
		return gameBoardCellsClone;
	}
	
	private ArrayList<ManqalaOpponentCells> getOpponentsCells() {
		return this.opponentsCells;
	}
	
	private void setOpponentsCells(ArrayList<ManqalaOpponentCells> opponentsCells) {
		this.opponentsCells = opponentsCells;
	}
	
	private ArrayList<ManqalaOpponentCells> opponentsCells = new ArrayList<ManqalaOpponentCells>();
	private ManqalaGameBoard gameBoard;
}
