package rf.yagodar.manqala.free.logic.combat;

import rf.yagodar.manqala.free.logic.combat.ManqalaOpponentsSet.Opponent;
import rf.yagodar.manqala.free.logic.model.animated.ManqalaCharacter;
import rf.yagodar.manqala.free.logic.model.combat.ManqalaCell;
import rf.yagodar.manqala.free.logic.model.combat.ManqalaGameBoard;
import rf.yagodar.manqala.free.logic.parameters.SVari;

/**
 * Предназначен для ведения схватки игры семейства Манкала. Мáнкáлá — семейство настольных игр для двух игроков. Игры семейства Манкала пошаговые.
 * <br /><br />
 * <u>Схватка игры семейства Манкала:</u><br />
 * - начинатся и заканчиватся;<br />
 * - проводится на специальных игровых досках;<br />
 * - в ней определены 2 оппонента, которые делают свой ход по очереди;<br />
 * - победитель схватки определяется по набранным очкам. 
 * 
 * @author Yagodar
 * @version 1.0.2, 14.10.2012
 */
public abstract class ManqalaCombat {
	/**
	 * Запускает схватку, c предварительно заданными параметрами игровой доски(если не заданы заранее, то используются параметры по умолчанию). Также проверяются
	 * параметры, необходимые для запуска схватки: firstWalketh(первый ходящий) не <code>null</code>, 
	 * определены оппоненты схватки и firstWalketh из их числа, определена gameBoard(игровая доска).
	 * Если схватка уже запущена, то она останавливается без определения победителя.
	 * 
	 * @param walketh персонаж(монстр или игрок), который будет ходить первым. //TODO переписать DOC
	 */
	public void start(int walkethOpponentTypeId) {
		if(isCombating()) {
			stop();			
		}
		
		this.walkethOpponentTypeId = walkethOpponentTypeId;
		
		setWalketh(getOpponents().getOpponent(Opponent.values()[walkethOpponentTypeId]));
		setCombating(true);
	}
	
	public void restart() {
		stop();
		getGameBoard().initializeCells(SVari.CELLS_COUNT, SVari.CELL_INIT_GRAINS);
		start(walkethOpponentTypeId);
	}

	/**
	 * Устанавливает, находится ли схватка в процессе игры.
	 * 
	 * @param combating <br /><code>true</code> схватка в процессе игры<br />
	 * 					<code>false</code> схватка не в процессе игры
	 */
	public void setCombating(boolean combating) {
		this.combating = combating;
	}

	/**
	 * Возвращает состояние схватки - в процессе игры или нет.
	 * @return <code>true</code> схватка в процессе игры<br />
	 * 			<code>false</code> схватка не в процессе игры
	 */
	public boolean isCombating() {
		return(this.combating);
	}

	/**
	 * Заканчивает схватку.
	 */
	public void stop() {
		setCombating(false);
	}

	public int getSVKey() {
		return sVKey;
	}
	
	/**
	 * Возвращает оппонентов схватки.
	 *
	 * @return устанавленные оппоненты.
	 */
	public ManqalaOpponentsSet getOpponents() {
		return(this.opponents);
	}
	
	//TODO DOC
	public ManqalaCharacter getFirstOpponent() {
		return opponents.getFirstOpponent();
	}
	
	//TODO DOC
	public ManqalaCharacter getSecondOpponent() {
		return opponents.getSecondOpponent();
	}

	/**
	 * Возвращает установленную gameBoard(игровую доску).
	 * 
	 * @return gameBoard(игровая доска)
	 */
	public ManqalaGameBoard getGameBoard() {
		return(this.gameBoard);
	}

	/**
	 * TODO исправить описание
	 * Совершает ход с указанной cell(ячейки). Cell не может быть <code>null</code> и gameBoard(игровая доска) 
	 * должна быть определена. Если ходящий не является владельцем данной cell, то выводится сообщение об этом
	 * и ход не делается. Ход просчитывается заранее и создаётся объект <code>ManqalaMoveResult</code>. Затем 
	 * проверяется правильность хода в соответствии с конкрентыми правилами (для каждого типа игр семейства
	 * Манкала данный объект свой). Если ход соответствует правилам, то результат хода применяется к текущей 
	 * gameBoard(игровая доска). Если после очередного хода можно определить попедителя, то он определяется и
	 * схватка заканчивается. Иначе выбирается следующий ходящий. 
	 * 
	 * @param cell ячейка игровой доски, с которой будет произведён ход
	 * 
	 * @see ManqalaMoveResult
	 */
	public ManqalaMoveResult makeMove(ManqalaCell cell) {
		ManqalaMoveResult moveResult = null;
		
		if(cell != null && !cell.isWarehouse()) {
			moveResult = getMoveResult(cell, getWalketh());
			if(moveResult != null && moveResult.isValidMove()) {
				gameBoard.applyMove(moveResult);

				if(moveResult.canSpotWinner()) {
					spotWinner();
					stop();
				}
				else {
					setNextWalketh();
				}
			}
		}

		return moveResult;
	}

	/**
	 * Возвращает текущего ходящего.
	 * 
	 * @return текущий ходящий
	 */
	public ManqalaCharacter getWalketh() {
		return(this.walketh);
	}
	
	public Opponent getWalkethOpponentType() {
		if(walketh.equals(getOpponents().getFirstOpponent())) {
			return(Opponent.FIRST);
		}
		else {
			return(Opponent.SECOND);
		}
	}

	/**
	 * Проверяет, является ли opponent текущим ходящим. Если оппоненты не заданы, либо opponent 
	 * не входит в список играющих, то вернёт <code>false</code>. Иначе сравнит с текущим ходящим
	 * и вернёт результат проверки.
	 * 
	 * @param opponent персонаж для проверки
	 * @return <code>true</code> opponent является ходящим<br />
	 * 			<code>false</code> opponent не является ходящим
	 */
	public boolean isWalketh(ManqalaCharacter opponent) {
		if(opponent == null) { 
			return(false);
		}

		ManqalaOpponentsSet opponents = getOpponents();
		if(opponents == null || !opponents.isOpponent(opponent)) {
			return(false);
		}

		return(opponent.equals(getWalketh()));
	}

	/**
	 * Устанавливает текущего ходящего персонажа. newWalketh не может быть <code>null</code>,
	 * также оппоненты должны быть определены и newWalketh должен являться одним из них.
	 * 
	 * @param newWalketh новый ходящий персонаж.
	 */
	public void setWalketh(ManqalaCharacter newWalketh) {
		if(newWalketh == null) { 
			//TODO exception
			return;
		}

		ManqalaOpponentsSet opponents = getOpponents();
		if(opponents == null || !opponents.isOpponent(newWalketh)) {
			//TODO exception
			return;
		}

		this.walketh = newWalketh;
	}

	/**
	 * Устанавливает следующего ходящего после текущего ходящего. Оппоненты и текущий ходящий должны быть определены.
	 * Если текущий ходящий - первый персонаж, то следующим будет второй. И наоборот.
	 */
	public void setNextWalketh() {
		ManqalaOpponentsSet opponents = getOpponents();
		if(opponents == null) {
			//TODO exception
			return;
		}

		ManqalaCharacter walketh = getWalketh();
		if(walketh == null) {
			//TODO exception
			return;
		}

		ManqalaCharacter firstOpponent = opponents.getFirstOpponent();
		ManqalaCharacter secondOpponent = opponents.getSecondOpponent();
		if(firstOpponent == null || secondOpponent == null) {
			//TODO exception
			return;
		}

		if(walketh.equals(firstOpponent)) {
			setWalketh(secondOpponent);
		}
		else if(walketh.equals(secondOpponent)) {
			setWalketh(firstOpponent);
		}		
	}

	/**
	 * Возвращает победителя схватки. Может быть <code>null</code>, если ничья или если схватка ещё не закончена.
	 * 
	 * @return winner(победитель)
	 */
	public ManqalaCharacter getWinner() {
		return this.winner;
	}

	/**
	 * Проверяет ничьёй ли окончилась схватка. Если схватка ещё в процессе игры, то данный параметр будет неопределён.
	 * 
	 * @return <code>true</code> ничья<br />
	 * 			<code>false</code> не ничья.
	 */
	public boolean isDraw() {
		return isDraw;
	}
	
	/**
	 * TODO
	 * Возвращает исход хода с указанной cell(ячейки). Для каждого типа игр семейства
	 * Манкала возвращаемый объект свой(для проверки правил игры). Исход хода не записывается
	 * в текущую gameBoard(игровую доску), а просто просчитывается и возвращается результат просчёта.
	 * 
	 * @param cell ячейка, с которой совершается ход.
	 * @return объект исхода хода.
	 * @see ManqalaMoveResult
	 */
	abstract public ManqalaMoveResult getMoveResult(ManqalaCell cell, ManqalaCharacter walketh);

	/**
	 * Определяет победителя схватки между оппонентами, либо определяет ничью.
	 */
	abstract public void spotWinner();

	/**
	 * Устанавливает ничьёй или нет закончилась схватка.
	 * 
	 * @param isDraw <br /><code>true</code> ничья<br />
	 * 				<code>false</code> не ничья.
	 */
	public void setDraw(boolean isDraw) {
		this.isDraw = isDraw;
	}

	/**
	 * Устанавливает победителя схватки, если не ничья. Если ничья, то winner будет неопределён.
	 * 
	 * @param winner победитель схватки.
	 */
	public void setWinner(ManqalaCharacter winner) {
		this.winner = winner;
	}
	
	public int getCompanyState() {
		return companyState;
	}

	public void setCompanyState(int companyState) {
		this.companyState = companyState;
	}

	protected void setSVKey(int sVKey) {
		this.sVKey = sVKey;
	}
	
	/**
	 * Устанавливает оппонентов на схватку. Оппоненты не могут быть <code>null</code>.
	 * 
	 * @param opponents устанавливаемые оппоненты.
	 */
	protected void setOpponents(ManqalaOpponentsSet opponents) {
		if(opponents != null) {
			this.opponents = opponents;
		}
	}

	/**
	 * Устанавливает gameBoard(игровую доску) для проведения схватки в зависимости от переданного типа gameBoard.
	 * Другими словами создаёт новый экземпляр gameBoard и записывает вместо текущего.
	 * 
	 * @param gameBoardType тип игровой доски.
	 * @param firstOpponentCellsGrains массив количества зёрен в создаваемых ячейках первого оппонента (включая амбар)
	 * @param secondOpponentCellsGrains массив количества зёрен в создаваемых ячейках второго оппонента (включая амбар)
	 */
	protected void setGameBoard(byte[] firstOpponentCellsGrains, byte[] secondOpponentCellsGrains) {
		this.gameBoard = new ManqalaGameBoard(this, firstOpponentCellsGrains, secondOpponentCellsGrains);
	}
	
	public enum MoveErrorResult {
		OK,
		NOT_VALID,
		OTHER_ERROR;
	}
	
	private int sVKey; 
	private boolean combating = false;
	private ManqalaGameBoard gameBoard;
	private ManqalaCharacter walketh;
	private ManqalaOpponentsSet opponents;
	private ManqalaCharacter winner;
	private boolean isDraw = false;
	private int walkethOpponentTypeId;
	private int companyState = -1;
}
