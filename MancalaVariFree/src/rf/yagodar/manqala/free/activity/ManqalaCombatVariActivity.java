package rf.yagodar.manqala.free.activity;

import rf.yagodar.glump.Util;
import rf.yagodar.glump.bitmap.BitmapProvider;
import rf.yagodar.glump.point.Point2D;
import rf.yagodar.glump.renderer.GLumpSVRendererQueue;
import rf.yagodar.manqala.free.ManqalaMediaPlayer;
import rf.yagodar.manqala.free.R;
import rf.yagodar.manqala.free.database.ManqalaCharactersDBManager;
import rf.yagodar.manqala.free.database.ManqalaCombatVariDBManager;
import rf.yagodar.manqala.free.drawing.listener.MonsterRendererQueueListener;
import rf.yagodar.manqala.free.drawing.listener.PlayerContinueRendererQueueListener;
import rf.yagodar.manqala.free.drawing.listener.PlayerExitRendererQueueListener;
import rf.yagodar.manqala.free.drawing.listener.PlayerPauseRendererQueueListener;
import rf.yagodar.manqala.free.drawing.listener.PlayerRendererQueueListener;
import rf.yagodar.manqala.free.drawing.listener.PlayerRestartRendererQueueListener;
import rf.yagodar.manqala.free.drawing.model.ManqalaVariCellModel;
import rf.yagodar.manqala.free.drawing.view.ManqalaCombatVariSV;
import rf.yagodar.manqala.free.drawing.view.ManqalaCombatVariSVBuilder;
import rf.yagodar.manqala.free.logic.combat.ManqalaCombatVari;
import rf.yagodar.manqala.free.logic.combat.ManqalaMoveResultVari;
import rf.yagodar.manqala.free.logic.model.animated.ManqalaMonster;
import rf.yagodar.manqala.free.logic.model.combat.ManqalaCell;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.MotionEvent;

public class ManqalaCombatVariActivity extends Activity {
	@Override
	public boolean onTouchEvent(MotionEvent event) {		
		if(event.getAction() == MotionEvent.ACTION_UP && manqalaCombatVariSV != null && playerPauseRendererQueueListener != null && playerPauseRendererQueueListener.isAllowTouchEvent()) {
			Point2D eventPoint = Util.getPoint(event.getX(), event.getY(), manqalaCombatVariSV.getDisplayMetrics().widthPixels, manqalaCombatVariSV.getDisplayMetrics().heightPixels, manqalaCombatVariSV.getLevelZ());
			
			if(!manqalaCombatVariSV.isMainRenderPaused()) {
				if(manqalaCombatVariSV.getSVBlank().getPauseButtonAreaModel().isContainsPoint(eventPoint)) {
					showPausePlate();
				}
				else if(manqalaCombatVari != null && manqalaCombatVari.isCombating() && playerRendererQueueListener != null && playerRendererQueueListener.isAllowTouchEvent()) {
					playerRendererQueueListener.setAllowTouchEvent(false);

					if(manqalaCombatVariSV.getGameBoardModel().isContainsPoint(eventPoint)) {
						ManqalaCell cell = null;
						ManqalaVariCellModel cellModel = null;

						for(int i = 0; i < manqalaCombatVariSV.getGameBoardModel().getCellsModels().length; i++) {
							if(manqalaCombatVariSV.getGameBoardModel().getCellsModels()[i].isContainsPoint(eventPoint)) {
								cell = manqalaCombatVari.getGameBoard().getGameBoardCells().getCellByGlobalPosId((byte) i);
								cellModel = manqalaCombatVariSV.getGameBoardModel().getCellsModels()[i];
								break;
							}
						}	

						ManqalaMoveResultVari moveResult = (ManqalaMoveResultVari) manqalaCombatVari.makeMove(cell);
						if(moveResult != null) {
							GLumpSVRendererQueue rendererQueue = new GLumpSVRendererQueue();

							if(moveResult.isValidMove()) {
								if(manqalaCombatVari.getWalketh().isPlayer()) {
									rendererQueue.addListener(playerRendererQueueListener);
								}
								else {
									rendererQueue.addListener(monsterRendererQueueListener);
								}

								rendererQueue.offerAllNodes(manqalaCombatVariSV.drawApplyMoveResult(cellModel, moveResult.getMoveResultSteps(), manqalaCombatVari));

								ManqalaCombatVariDBManager.getInstance().saveManqalaCombatVari(manqalaCombatVari);
							}
							else {
								rendererQueue.addListener(playerRendererQueueListener);

								rendererQueue.offerAllNodes(manqalaCombatVariSV.drawNotValidMoveResult(cellModel, manqalaCombatVari));
							}

							manqalaCombatVariSV.requestMainRender(rendererQueue);
						}
						else {
							playerRendererQueueListener.setAllowTouchEvent(true);
						}
					}
					else {
						playerRendererQueueListener.setAllowTouchEvent(true);
					}
				}
			}
			else {
				if(manqalaCombatVariSV.getSVBlank().getPausePlateModel().getpPRestartButton().getButtonDeselectedModel().isContainsPoint(eventPoint)) {
					playerPauseRendererQueueListener.setAllowTouchEvent(false);
					
					GLumpSVRendererQueue rendererQueue = new GLumpSVRendererQueue();
					
					rendererQueue.addListener(playerRestartRendererQueueListener);
					
					rendererQueue.offerNode(manqalaCombatVariSV.drawRestartButtonSelect());
					rendererQueue.offerNode(manqalaCombatVariSV.drawRestartButtonDeselect());
					
					manqalaCombatVariSV.requestAdditionalRender(rendererQueue);
					
				}
				else if(manqalaCombatVariSV.getSVBlank().getPausePlateModel().getpPContinueButton().getButtonDeselectedModel().isContainsPoint(eventPoint)) {
					if(manqalaCombatVari != null && manqalaCombatVari.isCombating()) {
						hidePausePlate();
					}
				}
				else if(manqalaCombatVariSV.getSVBlank().getPausePlateModel().getpPExitButton().getButtonDeselectedModel().isContainsPoint(eventPoint)) {
					playerPauseRendererQueueListener.setAllowTouchEvent(false);
					
					GLumpSVRendererQueue rendererQueue = new GLumpSVRendererQueue();
					
					rendererQueue.addListener(playerExitRendererQueueListener);
					
					rendererQueue.offerNode(manqalaCombatVariSV.drawExitButtonSelect());
					rendererQueue.offerNode(manqalaCombatVariSV.drawExitButtonDeselect());
					rendererQueue.offerAllNodes(manqalaCombatVariSV.hidePausePlate());
					
					manqalaCombatVariSV.requestAdditionalRender(rendererQueue);
				}
			}
		}
		return true;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	

		SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.shared_prefs_file_name), Context.MODE_PRIVATE);
		if(!sharedPref.getAll().isEmpty() && sharedPref.contains(getString(R.string.pref_key_start_new_combat))) {
			if(sharedPref.getBoolean(getString(R.string.pref_key_start_new_combat), true)) {
				manqalaCombatVari = new ManqalaCombatVari(sharedPref.getInt(getString(R.string.pref_key_s_v_key), R.array.csv_air), 
						ManqalaCharactersDBManager.getInstance().getCharacter(sharedPref.getString(getString(R.string.pref_key_first_name), getString(R.string.player_1_player_name))), 
						ManqalaCharactersDBManager.getInstance().getCharacter(sharedPref.getString(getString(R.string.pref_key_second_name), getString(R.string.player_2_player_name))));

				manqalaCombatVari.start(sharedPref.getInt(getString(R.string.pref_key_walketh), 0));

				manqalaCombatVari.setCompanyState(sharedPref.getInt(getString(R.string.pref_key_company_state), -1));
				
				ManqalaCombatVariDBManager.getInstance().saveManqalaCombatVari(manqalaCombatVari);
				
				Editor sharedPrefsEditor = getApplicationContext().getSharedPreferences(getString(R.string.shared_prefs_file_name), Context.MODE_PRIVATE).edit();
				sharedPrefsEditor.putBoolean(getString(R.string.pref_key_start_new_combat), false);
				sharedPrefsEditor.commit();
			}
			else {
				manqalaCombatVari = ManqalaCombatVariDBManager.getInstance().getSavedManqalaCombatVari();
			}

			manqalaCombatVariSV = ManqalaCombatVariSVBuilder.getInstance().buildSV(this, manqalaCombatVari.getSVKey());
		}
		
		if(manqalaCombatVariSV != null && manqalaCombatVari != null) {
			manqalaCombatVariSV.setRenderer();
			
			setContentView(manqalaCombatVariSV);

			playerPauseRendererQueueListener = new PlayerPauseRendererQueueListener();
			playerRendererQueueListener = new PlayerRendererQueueListener(this, manqalaCombatVari);
			
			if(manqalaCombatVari.getFirstOpponent().isMonster()) {
				monsterRendererQueueListener = new MonsterRendererQueueListener(this, (ManqalaMonster) manqalaCombatVari.getFirstOpponent(), manqalaCombatVari, manqalaCombatVariSV, playerRendererQueueListener, playerPauseRendererQueueListener);
			}
			else if(manqalaCombatVari.getSecondOpponent().isMonster()) {
				monsterRendererQueueListener = new MonsterRendererQueueListener(this, (ManqalaMonster) manqalaCombatVari.getSecondOpponent(), manqalaCombatVari, manqalaCombatVariSV, playerRendererQueueListener, playerPauseRendererQueueListener);
			}
			
			playerRestartRendererQueueListener = new PlayerRestartRendererQueueListener(this, manqalaCombatVariSV, playerPauseRendererQueueListener, manqalaCombatVari, playerRendererQueueListener, monsterRendererQueueListener);
			playerContinueRendererQueueListener = new PlayerContinueRendererQueueListener(manqalaCombatVariSV);
			playerExitRendererQueueListener = new PlayerExitRendererQueueListener(this, manqalaCombatVari);
			
			GLumpSVRendererQueue rendererQueue = new GLumpSVRendererQueue();
			
			rendererQueue.addListener(playerPauseRendererQueueListener);
			
			rendererQueue.offerAllNodes(manqalaCombatVariSV.drawInitSV(manqalaCombatVari));

			if(manqalaCombatVari.isCombating()) {
				if(manqalaCombatVari.getWalketh().isPlayer()) {
					rendererQueue.addListener(playerRendererQueueListener);
				}
				else {
					rendererQueue.addListener(monsterRendererQueueListener);
				}
			}
			else {
				rendererQueue.addListener(playerRendererQueueListener);
			}
			
			manqalaCombatVariSV.requestMainRender(rendererQueue);
		}
		
		ManqalaActivityManager.getInstance().setCombatActivity(this);
	}
	
	@Override
	public void onBackPressed() {
		if(manqalaCombatVariSV != null && playerPauseRendererQueueListener != null && playerPauseRendererQueueListener.isAllowTouchEvent()) {
			if(!manqalaCombatVariSV.isMainRenderPaused()) {
				showPausePlate();
			}
			else if(manqalaCombatVari.isCombating()) {
				hidePausePlate();
			}
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		
		ManqalaMediaPlayer.getInstance().pause();
		
		SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.shared_prefs_file_name), Context.MODE_PRIVATE);
		if(!sharedPref.getAll().isEmpty()) {
			if(sharedPref.contains(getString(R.string.pref_key_combat_paused)) && !sharedPref.getBoolean(getString(R.string.pref_key_combat_paused), false) && manqalaCombatVariSV != null) {
				showPausePlate();
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.shared_prefs_file_name), Context.MODE_PRIVATE);
		if(!sharedPref.getAll().isEmpty()) {
			if(sharedPref.contains(getString(R.string.pref_key_toogle_music)) && sharedPref.getBoolean(getString(R.string.pref_key_toogle_music), true)) {
				ManqalaMediaPlayer.getInstance().resume();
			}
			
			if(sharedPref.contains(getString(R.string.pref_key_combat_paused)) && sharedPref.getBoolean(getString(R.string.pref_key_combat_paused), false) && manqalaCombatVariSV != null) {
				showPausePlate();
			}
		}
	}
	
	@Override
	protected void onDestroy() {		
		super.onDestroy();
		
		if(manqalaCombatVariSV != null) {
			manqalaCombatVariSV.interruptMainRenderThread();
			
			manqalaCombatVariSV = null;
		}
		
		BitmapProvider.getInstance().clearTextureNames();
	}
	
	public void showPausePlate() {
		playerPauseRendererQueueListener.setAllowTouchEvent(false);
		
		manqalaCombatVariSV.pauseMainRender();

		GLumpSVRendererQueue rendererQueue = new GLumpSVRendererQueue();
		
		rendererQueue.addListener(playerPauseRendererQueueListener);
		
		if(manqalaCombatVari.isCombating()) {
			rendererQueue.offerAllNodes(manqalaCombatVariSV.showPausePlate());
		}
		else {
			rendererQueue.offerAllNodes(manqalaCombatVariSV.showGameOverPlate(manqalaCombatVari));
		}
		
		manqalaCombatVariSV.requestAdditionalRender(rendererQueue);
		
		Editor sharedPrefsEditor = getApplicationContext().getSharedPreferences(getString(R.string.shared_prefs_file_name), Context.MODE_PRIVATE).edit();
		sharedPrefsEditor.putBoolean(getString(R.string.pref_key_combat_paused), true);
		sharedPrefsEditor.commit();
	}
	
	public void hidePausePlate() {
		playerPauseRendererQueueListener.setAllowTouchEvent(false);
		
		GLumpSVRendererQueue rendererQueue = new GLumpSVRendererQueue();
		
		rendererQueue.addListener(playerContinueRendererQueueListener);
		rendererQueue.addListener(playerPauseRendererQueueListener);
		
		rendererQueue.offerNode(manqalaCombatVariSV.drawContinueButtonSelect());
		rendererQueue.offerNode(manqalaCombatVariSV.drawContinueButtonDeselect());
		rendererQueue.offerAllNodes(manqalaCombatVariSV.hidePausePlate());
		
		manqalaCombatVariSV.requestAdditionalRender(rendererQueue);
		
		Editor sharedPrefsEditor = getApplicationContext().getSharedPreferences(getString(R.string.shared_prefs_file_name), Context.MODE_PRIVATE).edit();
		sharedPrefsEditor.putBoolean(getString(R.string.pref_key_combat_paused), false);
		sharedPrefsEditor.commit();
	}

	private ManqalaCombatVari manqalaCombatVari;
	private ManqalaCombatVariSV manqalaCombatVariSV;
	private PlayerRestartRendererQueueListener playerRestartRendererQueueListener;
	private PlayerContinueRendererQueueListener playerContinueRendererQueueListener;
	private PlayerExitRendererQueueListener playerExitRendererQueueListener;
	private PlayerPauseRendererQueueListener playerPauseRendererQueueListener;
	private PlayerRendererQueueListener playerRendererQueueListener;
	private MonsterRendererQueueListener monsterRendererQueueListener;
}
