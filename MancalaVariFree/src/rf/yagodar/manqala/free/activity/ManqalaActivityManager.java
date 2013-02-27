package rf.yagodar.manqala.free.activity;

public class ManqalaActivityManager {
	public static ManqalaActivityManager getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new ManqalaActivityManager();
		}
		
		return INSTANCE;
	}
	
	public void setCombatActivity(ManqalaCombatVariActivity combatActivity) {
		this.combatActivity = combatActivity;
	}
	
	public boolean isCombatActivityExists() {
		return combatActivity != null;
	}
	
	private ManqalaActivityManager() {
		combatActivity = null;
	}

	private ManqalaCombatVariActivity combatActivity;
	
	private static ManqalaActivityManager INSTANCE;
}
