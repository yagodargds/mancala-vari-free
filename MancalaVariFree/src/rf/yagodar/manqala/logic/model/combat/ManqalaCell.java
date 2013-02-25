package rf.yagodar.manqala.logic.model.combat;

import rf.yagodar.manqala.logic.model.animated.ManqalaCharacter;
import rf.yagodar.manqala.logic.parameters.SVari;

/**
 * Предназначен для хранения информации и управления ячейкой игры семейства Манкала.
 * 
 * @author Aza
 * @author Yagodar
 * @version 1.0.0 17.10.2012
 */
public class ManqalaCell implements Cloneable {
	/**
	 * Создаёт экземпляр <b>"ячейки - не амбара"</b> игры семейства Манкала. 
	 * Должны быть опреелены: владелец ячейки, количество зёрен в создаваемой ячейке, номер позиции ячейки в наборе ячеек владельца.
	 * 
	 * @param cellOwner владелец ячейки, не <code>null</code>
	 * @param grainsCount количество зёрен в ячейке
	 * @param positionId номер позиции ячейки в наборе ячеек владельца
	 */
	public ManqalaCell(ManqalaCharacter cellOwner, byte grainsCount, byte positionId) {
		this(cellOwner, grainsCount, positionId, false);
	}
	
	/**
	 * Создаёт экземпляр ячейки игры семейства Манкала. В зависимости от параметра <code>isWarehouse</code> <b>может быть как амбаром, так и простой ячейкой</b>.  
	 * Должны быть опреелены: владелец ячейки, количество зёрен в создаваемой ячейке, номер позиции ячейки в наборе ячеек владельца.
	 * 
	 * @param cellOwner владелец ячейки, не <code>null</code>
	 * @param grainsCount количество зёрен в ячейке
	 * @param positionId номер позиции ячейки в наборе ячеек владельца
	 * @param isWarehouse <br /><code>true</code> создаваемая ячейка является амбаром<br />
	 * 						<code>false</code> создаваемая ячейка не является амбаром
	 */
	public ManqalaCell(ManqalaCharacter cellOwner, byte grainsCount, byte positionId, boolean isWarehouse) {
		this.grainsCount = grainsCount;
		this.cellOwner = cellOwner;
		this.positionId = positionId;
		this.isWarehouse = isWarehouse;
	}	
	
	/**
	 * Возвращает количество зёрен в ячейке.
	 * 
	 * @return количество зёрен в ячейке.
	 */
	public byte getGrainsCount() {
		return grainsCount;
	}
	
	/**
	 * Добавляет зерно к общему числу зёрен ячейки.
	 */
	public void incGrainsCount() {
		grainsCount++;
	}
	
	public void resetGrainsCount() {
		grainsCount = 0;
	}
	
	/**
	 * Возвращает владельца ячейки.
	 * 
	 * @return владелец ячейки
	 */
	public ManqalaCharacter getOwner() {
		return this.cellOwner;
	}
	
	/**
	 * Проверяет является ли данная ячейка амбаром.
	 * 
	 * @return <code>true</code> ячейка является амбаром<br />
	 * 			<code>false</code> ячейка не является амбаром
	 */
	public boolean isWarehouse() {
		return this.isWarehouse;
	}	
		
	
	/**
	 * Возвращает номер позиции ячейки в наборе ячеек владельца
	 * 
	 * @return номер позиции ячейки в наборе ячеек владельца.
	 */
	public byte getPositionId() {
		return this.positionId;
	}
	
	//TODO вари в abstract не должно быть
	public byte getGlobalPositionId(ManqalaCharacter firstOpponent) {
		if(firstOpponent != null) {
			if(getOwner().equals(firstOpponent)) {
				return positionId;
			}
			else {
				return (byte) (positionId + SVari.CELLS_COUNT + 1);
			}
		}
		
		return -1;
	}

	/**
	 * Сравнивает текущий объект ячейки с объектом <code>o</code>.
	 * Если <code>o == null</code> вернёт <code>false</code>.
	 * Если <code>o</code> не является экземпляром <code>ManqalaCell</code> вернёт <code>false</code>.
	 * У равных ячеек должны быть: один и тот же владелец, один и тот же номер  позиции в наборе ячеек владельца, одно и то же количество зерён.
	 * 
	 * @return <code>true</code> объекты равны<br />
	 * 			<code>false</code> объекты различаются
	 */
	@Override
	public boolean equals(Object o) {
		if(o == null) {
			return false;
		}
		
		if(!(o instanceof ManqalaCell)) {
			return false;
		}
		
		if(!(((ManqalaCell)o).getOwner().equals(getOwner()))) {
			return false;
		}
		
		if(!(((ManqalaCell)o).getPositionId() == getPositionId())) {
			return false;
		}
		
		if(!(((ManqalaCell)o).getGrainsCount() == getGrainsCount())) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Возвращает клон ячейки. 
	 * Владелец ячейки клонирован не будет, а будет передана копия ссылки на объект владельца. 
	 * 
	 * @return клон ячейки
	 */
	@Override
	public ManqalaCell clone() throws CloneNotSupportedException {
		return (ManqalaCell) super.clone();
	}

	private byte grainsCount;
	private final byte positionId;
	private final boolean isWarehouse;
	private final ManqalaCharacter cellOwner;
}