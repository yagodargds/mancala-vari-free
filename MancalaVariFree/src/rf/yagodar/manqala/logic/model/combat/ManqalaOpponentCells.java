package rf.yagodar.manqala.logic.model.combat;

import java.util.ArrayList;

import rf.yagodar.manqala.logic.model.animated.ManqalaCharacter;

/**
 * Предназначен для управления собственным набором ячеек игровой доски оппонента.
 * 
 * @author Yagodar
 * @version 1.0.0 18.10.2012
 */
public class ManqalaOpponentCells implements Cloneable {
	/**
	 * Создаёт экземпляр собственного набора ячеек игровой доски оппонента с задаваемыми начальными параметрами.
	 * 
	 * @param cellsOwner персонаж - владелец набора ячеек, не <code>null</code>
	 * @param cells массив ячеек с зёрнами, не <code>null</code> и не пустой
	 */
	public ManqalaOpponentCells(ManqalaCharacter cellsOwner, ArrayList<ManqalaCell> cellsArray) {
		if(cellsOwner == null) {
			//TODO Ex
		}
		
		if(cellsArray == null || cellsArray.isEmpty()) {
			//TODO Ex
		}
		
		this.cellsOwner = cellsOwner;
		setCells(cellsArray);
	}
	
	/**
	 * Возвращает персонажа - владельца набора ячеек.
	 * 
	 * @return персонаж - владелец набора ячеек.
	 */
	public ManqalaCharacter getOwner() {
		return this.cellsOwner;
	}
	
	/**
	 * Возвращает ячейку набора ячеек персонажа по id позиции этой ячейки.
	 * ID позиции - порядковый номер ячейки, отсчёт начинается с крайней левой до амбара.
	 * 
	 * @param posId id позиции ячейки в наборе ячеек персонажа
	 * @return ячейка с переданным posId
	 */
	public ManqalaCell getCellByPositionId(byte posId) {
		ArrayList<ManqalaCell> cells = getCells();
		if(cells == null) {
			//TODO ex
			return null;
		}
		
		for (ManqalaCell cell : cells) {
			if(cell.getPositionId() == posId) {
				return cell;
			}
		}
		
		return null;
	}
	
	/**
	 * Возвращает следующую ячейку набора ячеек персонажа игровой доски после переданной ячейки. 
	 * Проход идёт в напрвлении с нулевой ячейки до амбара. 
	 * Если переданая ячейка не содержится в наборе ячеек персонажа, то метод возвратит ячейку с нулевой позицией.
	 * Если переданая ячейка - амбар в наборе ячеек персонажа, то метод возвратит ячейку с нулевой позицией.
	 * 
	 * @param cell ячейка, после которой будет взята следующая ячейка, не <code>null</code>
	 * @return следующая ячейка игровой доски после переданной ячейки.
	 */
	public ManqalaCell getNextCell(ManqalaCell initCell) {
		if(initCell == null) {
			//TODO ex
			return null;
		}
		
		ArrayList<ManqalaCell> cells = getCells();
		if(cells == null) {
			//TODO ex
			return null;
		}
		
		if(!cells.contains(initCell) || initCell.isWarehouse()) {
			return getCellByPositionId((byte) (0));
		}
		else {
			byte initCellPosId = initCell.getPositionId();
			return getCellByPositionId((byte) (initCellPosId + 1));
		}		
	}
	
	/**
	 * Возвращает предыдущую ячейку набора ячеек персонажа игровой доски перед переданной ячейкой. 
	 * Проход идёт в напрвлении с амбара до нулевой ячейки. 
	 * Если переданая ячейка не содержится в наборе ячеек персонажа, то метод возвратит ячейку - амбар.
	 * Если переданая ячейка с нулевой позицией в наборе ячеек персонажа, то метод возвратит ячейку - амбар.
	 * 
	 * @param cell ячейка, перед которой будет взята предыдущая ячейка, не <code>null</code>
	 * @return предыдкщая ячейка игровой доски перед переданной ячейкой.
	 */
	public ManqalaCell getPrevCell(ManqalaCell initCell) {
		if(initCell == null) {
			//TODO ex
			return null;
		}
		
		ArrayList<ManqalaCell> cells = getCells();
		if(cells == null) {
			//TODO ex
			return null;
		}
		
		if(!cells.contains(initCell) || initCell.getPositionId() == 0) {
			return getWarehouseCell();
		}
		else {
			byte initCellPosId = initCell.getPositionId();
			return getCellByPositionId((byte) (initCellPosId - 1));
		}
	}
	
	/**
	 * Возвращает сумму зёрен всех ячеек набора ячеек персонажа, исключая амбар.
	 * 
	 * @return сумма зёрен ячеек, кроме амбара.
	 */
	public int getAllCellsPoints() {		
		ArrayList<ManqalaCell> cells = getCells();
		if(cells == null) {
			//TODO ex
			return 0;
		}
		
		int allCellsPoints = 0;
		for (ManqalaCell cell : cells) {
			if(cell != null && !cell.isWarehouse()) {
				allCellsPoints += cell.getGrainsCount();
			}
		}
		
		return allCellsPoints;
	}
	
	/**
	 * Возвращает ячейку-амбар набора ячеек персонажа.
	 * 
	 * @return ячейка-амбар набора ячеек персонажа
	 */
	public ManqalaCell getWarehouseCell() {
		ArrayList<ManqalaCell> cells = getCells();
		if(cells == null) {
			//TODO ex
			return null;
		}
		
		for (ManqalaCell cell : cells) {
			if(cell != null && cell.isWarehouse()) {
				return cell;
			}
		}
		
		return null;
	}
	
	/**
	 * Возвращает все ячейки набора ячеек персонажа, включая ячейку-амбар.
	 * 
	 * @return всё ячейки набора ячеек персонажа.
	 */
	public ArrayList<ManqalaCell> getCells() {
		return this.cells;
	}

	/**
	 * Устанавливает ячейки набора ячеек персонажа. Эти ячейки должны включать ячейку-амбар.
	 */
	public void setCells(ArrayList<ManqalaCell> cells) {
		this.cells = cells;
	}

	/**
	 * Возвращает клон данного набора ячеек персонажа. Клонируются также все ячейки в наборе. 
	 * Владелец набора не клонируется, копируется ссылка на объект владельца.
	 * 
	 * @return клон набора ячеек персонажа
	 */
	@Override
	public ManqalaOpponentCells clone() throws CloneNotSupportedException {
		ManqalaOpponentCells opponentCellsClone = (ManqalaOpponentCells) super.clone();
		
		ArrayList<ManqalaCell> cellsClone = new ArrayList<ManqalaCell>();
		for (ManqalaCell cell : getCells()) {
			cellsClone.add(cell.clone());
		}
		
		opponentCellsClone.setCells(cellsClone);
		
		return opponentCellsClone;
	}
	
	private ArrayList<ManqalaCell> cells;
	private final ManqalaCharacter cellsOwner;
}