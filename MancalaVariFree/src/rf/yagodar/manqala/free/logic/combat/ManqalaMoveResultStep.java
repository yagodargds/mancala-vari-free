package rf.yagodar.manqala.free.logic.combat;

public class ManqalaMoveResultStep {
	public ManqalaMoveResultStep(byte globalCellPosId, byte walkethWarehouseGlobalCellPosId, boolean isStartCell, boolean isBonus) {
		this.globalCellPosId = globalCellPosId;
		this.walkethWarehouseGlobalCellPosId = walkethWarehouseGlobalCellPosId;
		this.isStartCell = isStartCell;
		this.isBonus = isBonus;
	}
	
	public byte getGlobalCellPosId() {
		return globalCellPosId;
	}
	
	public byte getWalkethWarehouseGlobalCellPosId() {
		return walkethWarehouseGlobalCellPosId;
	}

	public boolean isStartCell() {
		return isStartCell;
	}
	
	public boolean isBonus() {
		return isBonus;
	}

	private final byte globalCellPosId;
	private final byte walkethWarehouseGlobalCellPosId;
	private final boolean isStartCell;
	private final boolean isBonus;
}
