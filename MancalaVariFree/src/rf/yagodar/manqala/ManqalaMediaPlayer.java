package rf.yagodar.manqala;

import android.content.Context;
import android.media.MediaPlayer;

public class ManqalaMediaPlayer {
	public static ManqalaMediaPlayer getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new ManqalaMediaPlayer();
		}
		
		return INSTANCE;
	}
	
	public void play(final Context context) {
		if(mediaPlayer == null) {
			mediaPlayer = MediaPlayer.create(context, R.raw.soundtrack);
		}
		
		mediaPlayer.setLooping(true);
		mediaPlayer.start();
	}
	
	public void pause() {
		if(mediaPlayer != null) {
			mediaPlayer.pause();
		}
	}
	
	public void resume() {
		if(mediaPlayer != null) {
			mediaPlayer.start();
		}
	}
	
	private MediaPlayer mediaPlayer;
	
	private static ManqalaMediaPlayer INSTANCE;
}
