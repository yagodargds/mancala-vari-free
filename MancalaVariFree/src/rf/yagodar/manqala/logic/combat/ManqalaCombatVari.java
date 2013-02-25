package rf.yagodar.manqala.logic.combat;

import java.util.ArrayList;

import rf.yagodar.manqala.logic.model.animated.ManqalaCharacter;
import rf.yagodar.manqala.logic.model.combat.ManqalaCell;
import rf.yagodar.manqala.logic.model.combat.ManqalaGameBoard;
import rf.yagodar.manqala.logic.model.combat.ManqalaGameBoardCells;
import rf.yagodar.manqala.logic.model.combat.ManqalaOpponentCells;
import rf.yagodar.manqala.logic.parameters.SVari;

/**
 * Предназначен для ведения схватки игры <b>Вари</b> семейства Манкала.
 * <br /><br /> 
 * 
 * Реализует специфические правила игры Вари семейства Манкала.
 * 
 * @author Yagodar
 * @version 1.0.3, 15.10.2012
 * @see ManqalaCombat
 */
public class ManqalaCombatVari extends ManqalaCombat {
	/**
	 * Создаёт экземпляр схватки игры Вари семейства Манкала. Должны быть определены оппоненты и тип игровой доски.
	 * 
	 * @param opponents оппоненты на схватку. Не может быть <code>null</code>
	 * @param gameBoardType тип игровой доски схватки. Не может быть <code>null</code>
	 */
	public ManqalaCombatVari(int sVKey, ManqalaCharacter firstCharacter, ManqalaCharacter secondCharacter) {
		this(sVKey, firstCharacter, secondCharacter, null, null);
	}
	
	/**
	 * Создаёт экземпляр схватки игры Вари семейства Манкала. Должны быть определены оппоненты и тип игровой доски.
	 * 
	 * @param opponents оппоненты на схватку. Не может быть <code>null</code>
	 * @param gameBoardType тип игровой доски схватки. Не может быть <code>null</code>
	 * @param firstOpponentCellsGrains массив количества зёрен в создаваемых ячейках первого оппонента (включая амбар)
	 * @param secondOpponentCellsGrains массив количества зёрен в создаваемых ячейках второго оппонента (включая амбар)
	 */
	public ManqalaCombatVari(int sVKey, ManqalaCharacter firstCharacter, ManqalaCharacter secondCharacter, byte[] firstOpponentCellsGrains, byte[] secondOpponentCellsGrains) {
		setSVKey(sVKey);
		setOpponents(new ManqalaOpponentsSet(firstCharacter, secondCharacter));
		setGameBoard(firstOpponentCellsGrains, secondOpponentCellsGrains);
	}
	
	/**
	 * Возвращает исход хода <b>игры Вари семейства Манкала</b> с указанной cell(ячейки). Исход хода не записывается
	 * в текущую gameBoard(игровую доску), а просто просчитывается и возвращается результат просчёта.<br /><br />
	 * <u>Ход просчитывается с учётом следующих правил:</u><br /><br />
	 * - перекладывание зёрен ячейки по другим ячейкам подряд по одному зерну, двигаясь против часовой стрелки.<br /><br /> 
	 * - при обходе лунок во время раскладывания зёрен лунка, с суммой зёрен 12 пропускается, а зерно кладется в следующую за ней лунку.<br /><br /> 
	 * - в свой амбар кладут, в чужой нет.<br /><br />
	 * - если последний из раскладываемых зёрен попадает в такую лунку ряда партнера, которая после раскладывания содержит 2 
	 *  или 3 зерна, включая положенное в нее зерно, то игрок вынимает все эти зёрна из лунки и складывает их в свой амбар. 
	 *  В том случае, когда в ряду партнера справа и слева от лунки, отвечающей указанным требованиям снятия зёрен, имеются лунки, 
	 *  которые содержат 2 или 3 зерна, то зёрна из этих лунок также вынимаются. При этом общее количество освобождаемых лунок может 
	 *  составить три: лунка, в которую попал последнее из раскладываемых зёрен, и по лунке слева и справа от нее.
	 * 
	 * @param cell ячейка, с которой совершается ход.
	 * @return объект исхода хода.
	 * @see ManqalaMoveResultVari
	 */
	@Override
	public ManqalaMoveResult getMoveResult(ManqalaCell cell, ManqalaCharacter walketh) {
		if(cell == null || walketh == null) {
			return null;
		}		

		ManqalaGameBoard gameBoard = getGameBoard();
		if(gameBoard == null) {
			return null;
		}

		ManqalaGameBoardCells gameBoardCells = gameBoard.getGameBoardCells();
		if(gameBoardCells == null) {
			return null;
		}
		
		

		ArrayList<ManqalaMoveResultStep> moveResultSteps = new ArrayList<ManqalaMoveResultStep>();
		
		ManqalaGameBoardCells gameBoardCellsClone = null;
		ManqalaCharacter firstOpponent = null;
		byte walkethWarehouseGloalCellPosId;
		
		ManqalaCell lastCell = null;			

		try {				
			gameBoardCellsClone = gameBoardCells.clone();
			
			firstOpponent = gameBoardCellsClone.getFirstOpponent();
			walkethWarehouseGloalCellPosId = gameBoardCellsClone.getOpponentWarehouse(walketh).getGlobalPositionId(firstOpponent);

			ManqalaOpponentCells cellsClone = gameBoardCellsClone.getOpponentCells(walketh);
			if(cellsClone == null) {
				return null;
			}
			
			ManqalaCell cellClone = cellsClone.getCellByPositionId(cell.getPositionId());
			if(cellClone == null) {
				return null;
			}
			
			if(cellClone.getGrainsCount() > 0) {
				moveResultSteps.add(new ManqalaMoveResultStep(cellClone.getGlobalPositionId(firstOpponent), walkethWarehouseGloalCellPosId, true, false));
				
				ManqalaCell	nextCell = gameBoardCellsClone.getNextCell(cellClone);
				while(!isValidNextCell(walketh, nextCell)) {
					nextCell = gameBoardCellsClone.getNextCell(nextCell);
				}

				lastCell = cellClone.clone();
				for (byte b = 0; b < cellClone.getGrainsCount(); b++) {
					nextCell.incGrainsCount();
					moveResultSteps.add(new ManqalaMoveResultStep(nextCell.getGlobalPositionId(firstOpponent), walkethWarehouseGloalCellPosId, false, false));
					
					lastCell = nextCell.clone();

					nextCell = gameBoardCellsClone.getNextCell(nextCell);
					while(!isValidNextCell(walketh, nextCell)) {
						nextCell = gameBoardCellsClone.getNextCell(nextCell);
					}	
				}

				cellClone.resetGrainsCount();
			}
		}
		catch(CloneNotSupportedException cnse) {
			cnse.printStackTrace();
			return null;
		}
		
		if(lastCell != null) {
			//Если ход закончился на территории противника
			if(!walketh.equals(lastCell.getOwner())) {
				int lastCellPoints = lastCell.getGrainsCount();

				ManqalaOpponentCells enemyCellsClone = gameBoardCellsClone.getOpponentCells(lastCell.getOwner());
				lastCell = enemyCellsClone.getCellByPositionId(lastCell.getPositionId());

				int enemyAllCellsClonePoints = enemyCellsClone.getAllCellsPoints();

				//Если последний из раскладываемых зёрен попадает в такую лунку ряда партнера, которая после раскладывания содержит 2 или 3 зерна, включая положенное в нее зерно, 
				//то игрок вынимает все эти зёрна из лунки и складывает их в свой амбар.
				if(lastCellPoints == 2 || lastCellPoints == 3) {
					//Cвоим ходом нельзя вынуть последние зёрна из его ряда
					if(enemyAllCellsClonePoints > lastCellPoints) {
						putCellGrainsToWarehouse(walketh, lastCell, gameBoardCellsClone);
						moveResultSteps.add(new ManqalaMoveResultStep(lastCell.getGlobalPositionId(firstOpponent), walkethWarehouseGloalCellPosId, false, true));

						enemyAllCellsClonePoints = enemyCellsClone.getAllCellsPoints();

						ManqalaCell leftCell = enemyCellsClone.getPrevCell(lastCell);
						ManqalaCell rightCell = enemyCellsClone.getNextCell(lastCell);

						if(leftCell == null && rightCell == null) {
							return null;
						}				

						int leftCellPoints = 0;
						int rightCellPoints = 0;

						if(leftCell != null && !leftCell.isWarehouse()) {
							leftCellPoints = leftCell.getGrainsCount();
						}

						if(rightCell != null && !rightCell.isWarehouse()) {
							rightCellPoints = rightCell.getGrainsCount();
						}

						//В том случае, когда в ряду партнера слева от лунки, отвечающей указанным требованиям снятия зёрен, имеется лунка, которая содержат 2 или 3 зерна, 
						//то зёрна из этой лунки также вынимаются.
						if(leftCellPoints == 2 || leftCellPoints == 3) {
							//Cвоим ходом нельзя вынуть последние зёрна из его ряда
							if(enemyAllCellsClonePoints > leftCellPoints) {
								putCellGrainsToWarehouse(walketh, leftCell, gameBoardCellsClone);
								moveResultSteps.add(new ManqalaMoveResultStep(leftCell.getGlobalPositionId(firstOpponent), walkethWarehouseGloalCellPosId, false, true));
								
								enemyAllCellsClonePoints = enemyCellsClone.getAllCellsPoints();
							}
						}

						//В том случае, когда в ряду партнера справа от лунки, отвечающей указанным требованиям снятия зёрен, имеется лунка, которая содержат 2 или 3 зерна, 
						//то зёрна из этой лунки также вынимаются.
						if(rightCellPoints == 2 || rightCellPoints == 3) {
							//Cвоим ходом нельзя вынуть последние зёрна из его ряда
							if(enemyAllCellsClonePoints > rightCellPoints) {
								putCellGrainsToWarehouse(walketh, rightCell, gameBoardCellsClone);
								moveResultSteps.add(new ManqalaMoveResultStep(rightCell.getGlobalPositionId(firstOpponent), walkethWarehouseGloalCellPosId, false, true));
								
								enemyAllCellsClonePoints = enemyCellsClone.getAllCellsPoints();
							}
						}					
					}
				}
			}
		}

		return new ManqalaMoveResultVari(cell, walketh, gameBoardCells, gameBoardCellsClone, moveResultSteps);
	}
	
	/**
	 * Определяет победителя(или ничью) схватки игры Вари семейства Манкала.<br /><br />
	 * Победитель тот персонаж, у кого больше очков в амбаре. Если количество очков равное, то определяется ничья. 
	 */
	@Override
	public void spotWinner() {
		ManqalaGameBoard gameBoard = getGameBoard();
		if(gameBoard == null) {
			//TODO Ex
			return;
		}
		
		ManqalaGameBoardCells gameBoardCells = gameBoard.getGameBoardCells();
		if(gameBoardCells == null) {
			//TODO Ex
			return;
		}
		
		ManqalaOpponentsSet opponents = getOpponents();
		if(opponents == null) {
			//TODO Ex
			return;
		}
		
		ManqalaCharacter firstOpponent = opponents.getFirstOpponent();
		if(firstOpponent == null) {
			//TODO Ex
			return;
		}
		
		ManqalaCharacter secondOpponent = opponents.getSecondOpponent();
		if(secondOpponent == null) {
			//TODO Ex
			return;
		}
		
		ManqalaCell firstOpponentWarehouse = gameBoardCells.getOpponentWarehouse(firstOpponent);
		if(firstOpponentWarehouse == null) {
			//TODO Ex
			return;
		}
		
		ManqalaCell secondOpponentWarehouse = gameBoardCells.getOpponentWarehouse(secondOpponent);
		if(secondOpponentWarehouse == null) {
			//TODO Ex
			return;
		}
		
		int firstOpponentWarehousePoints = firstOpponentWarehouse.getGrainsCount();
		int secondOpponentWarehousePoints = secondOpponentWarehouse.getGrainsCount();
		
		if(firstOpponentWarehousePoints > secondOpponentWarehousePoints) {
			setWinner(firstOpponent);
		}
		else if(firstOpponentWarehousePoints < secondOpponentWarehousePoints) {
			setWinner(secondOpponent);
		}
		else {
			setDraw(true);
		}		
	}

	/**
	 * Перекладывает зёрна из ячейки в амбар персонажа <b>переданного в метод набора ячеек</b> игровой доски схватки.
	 * Персонаж не может являться владельцем передаваемой ячейки.
	 * 
	 * @param warehouseOwner владелец амбара, не <code>null</code>
	 * @param cell ячейка, не <code>null</code>
	 * @param gameBoardCells набор ячеек игровой доски схватки, не <code>null</code>
	 */
	private void putCellGrainsToWarehouse(ManqalaCharacter warehouseOwner, ManqalaCell cell, ManqalaGameBoardCells gameBoardCells) {
		if(warehouseOwner == null) {
			//TODO Ex
			return;
		}

		if(cell == null) {
			//TODO Ex
			return;
		}
		
		if(warehouseOwner.equals(cell.getOwner())) {
			return;
		}
		
		if(gameBoardCells == null) {
			//TODO Ex
			return;
		}

		ManqalaCell walkethWarehouse = gameBoardCells.getOpponentWarehouse(warehouseOwner);
		if(walkethWarehouse == null) {
			//TODO Ex
			return;
		}

		for (byte b = 0; b < cell.getGrainsCount(); b++) {
			walkethWarehouse.incGrainsCount();
		}
		
		cell.resetGrainsCount();
	}
	
	private boolean isValidNextCell(ManqalaCharacter walketh, ManqalaCell nextCell) {
		if(nextCell == null) {
			//TODO Ex
		}
		
		if(walketh == null) {
			//TODO Ex
		}

		//В свой амбар зёрна кладут, но в чужой нет.
		if(nextCell.isWarehouse() && !walketh.equals(nextCell.getOwner())) {
			return false;
		}

		//Лунка, с суммой зёрен 12 пропускается.
		if(!nextCell.isWarehouse() && nextCell.getGrainsCount() >= SVari.CELL_MAX_GRAINS) {
			return false;
		}

		return true;
	}
}
