package rf.yagodar.manqala.free.drawing.listener;

import rf.yagodar.glump.renderer.GLumpSVRendererQueue;
import rf.yagodar.glump.renderer.IGLumpSVRendererQueueListener;
import rf.yagodar.manqala.free.drawing.view.ManqalaCombatVariSV;
import rf.yagodar.manqala.free.logic.combat.ManqalaCombatVari;
import rf.yagodar.manqala.free.logic.combat.ManqalaMoveResult;
import rf.yagodar.manqala.free.logic.model.animated.ManqalaMonster;

public class MonsterRendererQueueListener implements IGLumpSVRendererQueueListener {

	public MonsterRendererQueueListener(ManqalaMonster monster, ManqalaCombatVari manqalaCombatVari, ManqalaCombatVariSV manqalaCombatVariSV, PlayerRendererQueueListener playerRendererQueueListener, PlayerPauseRendererQueueListener playerPauseRendererQueueListener) {
		this.monster= monster;
		this.manqalaCombatVari = manqalaCombatVari;
		this.manqalaCombatVariSV = manqalaCombatVariSV;
		this.playerRendererQueueListener = playerRendererQueueListener;
		this.playerPauseRendererQueueListener = playerPauseRendererQueueListener;
		moveResult = null;
		isMoveResultErr = false;
	}

	@Override
	public void onRendered() {
		if(manqalaCombatVari != null) {
			if(!manqalaCombatVari.isCombating() && playerPauseRendererQueueListener != null) {
				playerPauseRendererQueueListener.setAllowTouchEvent(false);

				manqalaCombatVariSV.pauseMainRender();

				GLumpSVRendererQueue rendererQueue = new GLumpSVRendererQueue();

				rendererQueue.addListener(playerPauseRendererQueueListener);
				rendererQueue.offerAllNodes(manqalaCombatVariSV.showGameOverPlate(manqalaCombatVari));

				manqalaCombatVariSV.requestAdditionalRender(rendererQueue);
			}
			else if(monster != null && playerRendererQueueListener != null && manqalaCombatVariSV != null) {
				if(moveResult == null && !isMoveResultErr) {
					GLumpSVRendererQueue lFrendererQueue = manqalaCombatVariSV.drawMonsterThink();
					lFrendererQueue.addListener(this);

					manqalaCombatVariSV.requestMainRender(lFrendererQueue);
					moveResult = monster.makeMove(manqalaCombatVari);
					lFrendererQueue.stopRender();

					if(moveResult == null) {
						isMoveResultErr = true;
					}
				}
				else if(moveResult != null && !isMoveResultErr) {
					GLumpSVRendererQueue monsterMoveRendererQueue = new GLumpSVRendererQueue();

					monsterMoveRendererQueue.addListener(playerRendererQueueListener);

					monsterMoveRendererQueue.offerAllNodes(manqalaCombatVariSV.drawApplyMoveResult(
							manqalaCombatVariSV.getGameBoardModel().getCellsModels()[moveResult.getStartCell().getGlobalPositionId(manqalaCombatVari.getFirstOpponent())],
							moveResult.getMoveResultSteps(),
							manqalaCombatVari));

					manqalaCombatVariSV.requestMainRender(monsterMoveRendererQueue);

					moveResult = null;
					isMoveResultErr = false;
				}
				else {
					playerRendererQueueListener.setAllowTouchEvent(true);
				}
			}
		}
	}

	private ManqalaMonster monster;
	private ManqalaCombatVari manqalaCombatVari;
	private ManqalaCombatVariSV manqalaCombatVariSV;
	private PlayerRendererQueueListener playerRendererQueueListener;
	private PlayerPauseRendererQueueListener playerPauseRendererQueueListener;
	private ManqalaMoveResult moveResult;
	private boolean isMoveResultErr;
}
