package rf.yagodar.manqala.logic.combat;

import java.util.ArrayList;

import rf.yagodar.manqala.logic.model.animated.ManqalaCharacter;
import rf.yagodar.manqala.logic.model.combat.ManqalaCell;
import rf.yagodar.manqala.logic.model.combat.ManqalaGameBoardCells;
import rf.yagodar.manqala.logic.model.combat.ManqalaOpponentCells;

/**
 * Предназначен для записи результат хода игры <b>Вари</b> семейства Манкала. Используется для проверки специфических правил игры, а также для 
 * просчёта хода компьютера.
 * 
 * @author Yagodar
 * @version 1.0.1, 16.10.2012
 * @see ManqalaMoveResult
 */
public class ManqalaMoveResultVari extends ManqalaMoveResult {	
	/**
	 * TODO
	 * Создаёт экземпляр исхода хода игры <b>Вари</b> семейства Манкала. Должны быть определен совершающий ход персонаж,
	 * набор ячеек игровой доски до хода, набор ячеек игровой доски после хода.
	 * 
	 * @param walketh совершающий ход прсонаж, не <code>null</code>
	 * @param initGameBoardCells текущий набор ячеек игровой доски, не <code>null</code>
	 * @param resultGameBoardCells набор ячеек игровой доски после хода, не <code>null</code>
	 * @see ManqalaMoveResult#ManqalaMoveResult(ManqalaCharacter, ManqalaGameBoardCells, ManqalaGameBoardCells)
	 */
	public ManqalaMoveResultVari(ManqalaCell startCell, ManqalaCharacter walketh, ManqalaGameBoardCells initGameBoardCells, ManqalaGameBoardCells resultGameBoardCells, ArrayList<ManqalaMoveResultStep> moveResultSteps) {
		super(startCell, walketh, initGameBoardCells, resultGameBoardCells, moveResultSteps);
	}
	
	/**
	 * Проверяет, возможно ли определить победителя после совершения хода в игре <b>Вари</b> семейства Манкала.
	 * <u>Определить победителя можно в следующих ситуациях:</u><br />
	 * - противнику совершающего ход нечем ходить и совершающий ход не может сходить так, чтобы противнику было чем сходить<br />
	 * - совершающему ход нечем ходить и противник совершающего ход не может сходить так, чтобы совершающему ход было чем сходить
	 * 
	 * @return <code>true</code> определить попедителя возможно<br />
	 * 		   <code>false</code> определить попедителя невозможно 
	 */
	@Override
	public boolean canSpotWinner() {
		ManqalaCharacter walketh = getWalketh();
		if(walketh == null) {
			//TODO Ex
		}
		
		ManqalaGameBoardCells resultGameBoardCells = getResultGameBoardCells();
		if(resultGameBoardCells == null) {
			//TODO Ex
		}	
		
		ManqalaOpponentCells walkethEnemyCells = resultGameBoardCells.getOpponentCells(walketh, true);
		if(walkethEnemyCells == null) {
			//TODO Ex
		}
		
		if(walkethEnemyCells.getAllCellsPoints() == 0 && !notZeroPointsTurnExists(walketh)) {
			return true;	
		}
		
		ManqalaOpponentCells walkethCells = resultGameBoardCells.getOpponentCells(walketh);
		if(walkethCells == null) {
			//TODO Ex
		}
		
		if(walkethCells.getAllCellsPoints() == 0 && !notZeroPointsTurnExists(walkethEnemyCells.getOwner())) {
			return true;
		}
		
		return false;
	}

	/**
	 *TODO
	 * Проверяет по правилам ли совершён ход, допустим ли совершённый ход игры <b>Вари</b>. Также выводит сообщение о недопустимости
	 * хода, если задан соответствующий параметр.<br />
	 * <u>Ход будет недопустим в случае:</u><br />
	 * - Если противник совершающего ход очередным ходом сам вынужден вынуть последнее зерно из своего ряда лунок, то совершающему ход необходимо 
	 * хотя бы единственное зерно перевести в его ряд, если это возможно. Если это невозможно, то ход допустим.
	 * 
	 * @param  outputMessage <br />
	 * 			<code>true</code> выводить сообщения о недопустимости хода<br />
	 * 		   <code>false</code> не выводить сообщения о недопустимости хода 
	 * @return <code>true</code> совершённый ход допустим<br />
	 * 		   <code>false</code> совершённый ход не допустим 
	 */
	@Override
	protected boolean checkMoveValidity() {
		return getStartCell().getGrainsCount() != 0 && getStartCell().getOwner().equals(getWalketh()) && !(getResultGameBoardCells().getOpponentCells(getWalketh(), true).getAllCellsPoints() == 0 && notZeroPointsTurnExists(getWalketh()));
	}	

	private boolean notZeroPointsTurnExists(ManqalaCharacter opponent) {		
		if(opponent == null) {
			//TODO Ex
		}
		
		ManqalaGameBoardCells initGameBoardCells = getInitGameBoardCells();
		if(initGameBoardCells == null) {
			//TODO Ex
		}

		ManqalaOpponentCells opponentCells = initGameBoardCells.getOpponentCells(opponent);
		if(opponentCells == null) {
			//TODO Ex
		}

		ArrayList<ManqalaCell> cells = opponentCells.getCells();		
		if(cells == null) {
			//TODO Ex
		}

		for (ManqalaCell cell : cells) {
			if(!cell.isWarehouse() && cell.getGrainsCount() >= cells.size() - cell.getPositionId()) {
				return true;
			}
		}

		return false;
	}
}
