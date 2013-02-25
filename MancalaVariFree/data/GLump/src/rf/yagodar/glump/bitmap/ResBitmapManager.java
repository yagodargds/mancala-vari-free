package rf.yagodar.glump.bitmap;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.TextPaint;
import android.util.SparseIntArray;

public class ResBitmapManager {
	public static ResBitmapManager getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new ResBitmapManager();
		}
		
		return INSTANCE;
	}
		
	public void addToBitmapProvider(int drawableResId, Bitmap bitmap) {
		if(drawableResId != 0 && bitmap != null) {
			bitmapIdByDrawableResId.put(drawableResId, BitmapProvider.getInstance().addBitmap(bitmap));
		}
	}
	
	public int getBitmapId(int drawableResId) {
		int bitmapId = bitmapIdByDrawableResId.get(drawableResId);
		
		if(bitmapId == 0) {
			return dummyBitmapId;
		}
		
		return bitmapIdByDrawableResId.get(drawableResId);
	}
		
	protected ResBitmapManager() {
		super();
		bitmapIdByDrawableResId = new SparseIntArray();
		dummyBitmapId = generateDummyBitmap();
	}
	
	protected int generateDummyBitmap() {
		Bitmap bitmap = Bitmap.createBitmap(64, 64, Bitmap.Config.ARGB_4444);		
		bitmap.eraseColor(Color.BLACK);
		
		TextPaint textPaint = new TextPaint();
		Canvas canvas = new Canvas(bitmap);
		
		textPaint.setColor(Color.rgb(150, 237, 253));		
		canvas.drawRect(1, 1, 63, 63, textPaint);
		
		textPaint.setTextSize(13);
		textPaint.setColor(Color.WHITE);						
		canvas.drawText("GLump", 10, 27, textPaint);
		canvas.drawText("no_texture", 2, 41, textPaint);	
		
		return BitmapProvider.getInstance().addBitmap(bitmap);
	}
	
	private SparseIntArray bitmapIdByDrawableResId;
	private int dummyBitmapId;
	
	private static ResBitmapManager INSTANCE;
}
