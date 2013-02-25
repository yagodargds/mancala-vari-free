package rf.yagodar.manqala.logic.model.combat;

import rf.yagodar.manqala.logic.combat.ManqalaCombat;
import rf.yagodar.manqala.logic.combat.ManqalaMoveResult;
import rf.yagodar.manqala.logic.parameters.SVari;

/**
 * Предназначен для управления игровой доской игры семейства Манкала.
 * 
 * @author Yagodar
 * @version 1.0.0 18.10.2012
 */
public class ManqalaGameBoard {
	/**
	 * Создаёт экземпляр игровой доски игры семейства Манкала с начальным состоянием, определяемом типом игровой доски.
	 * 
	 * @param combat схватка, в которой будет использоваться эта игровая доска, не <code>null</code>
	 * @param gameBoardType тип создаваемой игровой доски, не <code>null</code>
	 */
	public ManqalaGameBoard(ManqalaCombat combat) {
		this(combat, null, null);
	}
	
	/**
	 * Создаёт экземпляр игровой доски игры семейства Манкала с начальным состоянием, определяемом наборами ячеек.
	 * 
	 * @param combat схватка, в которой будет использоваться эта игровая доска, не <code>null</code>
	 * @param gameBoardType тип создаваемой игровой доски, не <code>null</code>
	 * @param firstOpponentCellsGrains массив количества зёрен в создаваемых ячейках первого оппонента (включая амбар)
	 * @param secondOpponentCellsGrains массив количества зёрен в создаваемых ячейках второго оппонента (включая амбар)
	 */
	public ManqalaGameBoard(ManqalaCombat combat, byte[] firstOpponentCellsGrains, byte[] secondOpponentCellsGrains) {
		if(combat == null) {
			//TODO exception
			return;
		}
		
		this.combat = combat;
		
		initializeCells(firstOpponentCellsGrains, secondOpponentCellsGrains);
	}
		
	/**
	 * Возвращает схватку - владелицу данной игровой доски.
	 * 
	 * @return схватка, которой принадленжит игровая доска.
	 */
	public ManqalaCombat getCombat() {
		return this.combat;
	}		
	
	/**
	 * Устанавливает набор ячеек к игровой доске.
	 * 
	 * @param newCells набор ячеек для игровой доски.
	 */
	public void setGameBoardCells(ManqalaGameBoardCells newCells) {		
		this.cells = newCells;
		this.cells.setGameBoard(this);
	}
	
	/**
	 * Возвращает набор ячеек игровой доски.
	 * 
	 * @return набор ячеек игровой доски.
	 */
	public ManqalaGameBoardCells getGameBoardCells() {		
		return this.cells;
	}	
	
	/**
	 * Применяет ход к игровой доске.
	 * 
	 * @param moveResult совершённый ход, не <code>null</code>
	 */
	public void applyMove(ManqalaMoveResult moveResult) {
		if(moveResult == null) {
			//TODO ex
			return;
		}
		
		ManqalaGameBoardCells resultGameBoardCells = moveResult.getResultGameBoardCells();
		if(resultGameBoardCells == null) {
			//TODO ex
			return;
		}
		
		try {
			setGameBoardCells(resultGameBoardCells.clone());
		}
		catch (CloneNotSupportedException cnse) {
			cnse.printStackTrace();
		}
	}	
	
	public void initializeCells(byte cellsCount, byte cellInitGrains) {
		if(cellsCount > 0 && cellInitGrains > 0) {
			this.cells = new ManqalaGameBoardCells(this, cellsCount, cellInitGrains);
		}
	}
	
	private void initializeCells(byte[] firstOpponentCellsGrains, byte[] secondOpponentCellsGrains) {
		if(firstOpponentCellsGrains == null || secondOpponentCellsGrains == null) {
			initializeCells(SVari.CELLS_COUNT, SVari.CELL_INIT_GRAINS);
		}
		else if(firstOpponentCellsGrains.length != 0 && firstOpponentCellsGrains.length == secondOpponentCellsGrains.length) {
			this.cells = new ManqalaGameBoardCells(this, firstOpponentCellsGrains, secondOpponentCellsGrains);
		}
	}
	
	private ManqalaGameBoardCells cells;
	private ManqalaCombat combat;
}
