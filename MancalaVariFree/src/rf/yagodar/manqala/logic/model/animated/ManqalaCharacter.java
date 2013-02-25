package rf.yagodar.manqala.logic.model.animated;

//TODO DOC

public class ManqalaCharacter {
	public ManqalaCharacter(int charId, String charName) {
		this.charId = charId;
		this.charName = charName;
	}

	public int getCharId() {
		return charId;
	}
	
	public String getCharName() {
		return charName;
	}
	
	public boolean isMonster() {
		return this instanceof ManqalaMonster;
	}
	
	public boolean isPlayer() {
		return this instanceof ManqalaPlayer;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == null) {
			return false;
		}
		
		if(!(o instanceof ManqalaCharacter)) {
			return false;
		}

		if(((ManqalaCharacter) o).getCharId() != getCharId()) {
			return false;
		}

		return true;
	}

	private int charId;
	private String charName;
}
