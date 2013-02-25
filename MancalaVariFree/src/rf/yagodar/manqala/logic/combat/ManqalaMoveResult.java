package rf.yagodar.manqala.logic.combat;

import java.util.ArrayList;

import rf.yagodar.manqala.logic.model.animated.ManqalaCharacter;
import rf.yagodar.manqala.logic.model.combat.ManqalaCell;
import rf.yagodar.manqala.logic.model.combat.ManqalaGameBoardCells;

/**
 * Предназначен для записи результата хода игры семейства Манкала. Используется для проверки специфических правил игры, а также для 
 * просчёта хода компьютера.
 * 
 * @author Yagodar
 * @version 1.0.1, 16.10.2012
 */
public abstract class ManqalaMoveResult {
	/**
	 *TODO
	 * Создаёт экземпляр исхода хода игры семейства Манкала. Должны быть определен совершающий ход персонаж,
	 * набор ячеек игровой доски до хода, набор ячеек игровой доски после хода.
	 * 
	 * @param walketh совершающий ход прсонаж, не <code>null</code>
	 * @param initGameBoardCells текущий набор ячеек игровой доски, не <code>null</code>
	 * @param resultGameBoardCells набор ячеек игровой доски после хода, не <code>null</code>
	 */
	public ManqalaMoveResult(ManqalaCell startCell, ManqalaCharacter walketh, ManqalaGameBoardCells initGameBoardCells, ManqalaGameBoardCells resultGameBoardCells, ArrayList<ManqalaMoveResultStep> moveResultSteps) {
		this.startCell = startCell;
		this.walketh = walketh;
		this.initGameBoardCells = initGameBoardCells;
		this.resultGameBoardCells = resultGameBoardCells;
		this.moveResultSteps = moveResultSteps;
		isValidMove = checkMoveValidity();
	}
	
	/**
	 * Возвращает набор ячеек игровой доски после хода.
	 * 
	 * @return набор ячеек игровой доски после хода
	 */
	public ManqalaGameBoardCells getResultGameBoardCells() {
		return resultGameBoardCells;
	}
	
	/**
	 * Проверяет по правилам ли совершён ход, допустим ли совершённый ход. Сообщение о недопустимости хода
	 * не выводит.
	 * 
	 * @return <code>true</code> совершённый ход допустим<br />
	 * 		   <code>false</code> совершённый ход не допустим 
	 */
	public boolean isValidMove() {
		return isValidMove;
	}
	
	
	/**
	 * Проверяет, возможно ли определить победителя после совершения хода.
	 * 
	 * @return <code>true</code> определить попедителя возможно<br />
	 * 		   <code>false</code> определить попедителя невозможно 
	 */
	abstract public boolean canSpotWinner();
	
	public ManqalaCell getStartCell() {
		return startCell;
	}
	
	public ArrayList<ManqalaMoveResultStep> getMoveResultSteps() {
		return moveResultSteps;
	}

	/**
	 * Возвращает персонажа, который совершает ход.
	 * 
	 * @return совершающий ход персонаж
	 */
	protected ManqalaCharacter getWalketh() {
		return walketh;
	}
	
	/**
	 * Возвращает набор ячеек игровой доски до хода.
	 * 
	 * @return набор ячеек игровой доски до хода
	 */
	protected ManqalaGameBoardCells getInitGameBoardCells() {
		return initGameBoardCells;
	}
	
	abstract protected boolean checkMoveValidity();
	
	private ManqalaCell startCell;
	private ManqalaCharacter walketh;
	private ManqalaGameBoardCells initGameBoardCells;
	private ManqalaGameBoardCells resultGameBoardCells;
	private final ArrayList<ManqalaMoveResultStep> moveResultSteps;
	private final boolean isValidMove;
}
