package rf.yagodar.glump.polygon;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.util.SparseArray;

public class TextTextureInfo extends TextureInfo {
	public TextTextureInfo(int textColor, Bitmap bitmap, float leftX, float topY, float rightX, float bottomY) {
		super(0, leftX / (float)bitmap.getWidth(), topY / (float)bitmap.getWidth(), rightX / (float)bitmap.getWidth(), bottomY / (float)bitmap.getWidth());
		
		this.textColor = textColor;
		this.leftX = (int)Math.floor((double)leftX);
		this.topY = (int)Math.floor((double)topY);
		this.rightX = (int)Math.floor((double)rightX);
		this.bottomY = (int)Math.floor((double)bottomY);
		
		textColorPixels = new SparseArray<ArrayList<Integer>>();
		for(int y = this.bottomY + 1; y < this.topY; y++) {
			for(int x = this.leftX + 1; x < this.rightX; x++) {
				if(bitmap.getPixel(x, y) == textColor) {
					if(textColorPixels.indexOfKey(y) < 0) {
						textColorPixels.put(y, new ArrayList<Integer>());
					}
					textColorPixels.get(y).add(x);
				}
			}
		}
	}

	public boolean setTextColor(int newTextColor, Bitmap bitmap) {
		if(bitmap != null && textColor != newTextColor) {
			for(int yIndx = 0; yIndx < textColorPixels.size(); yIndx++) {
				for(int x : textColorPixels.get(textColorPixels.keyAt(yIndx))) {
					bitmap.setPixel(x, textColorPixels.keyAt(yIndx), newTextColor);
				}
			}
			
			textColor = newTextColor;
			
			return true;
		}
		
		return false;
	}
	
	private int textColor;
	private int leftX;
	private int topY;
	private int rightX;
	private int bottomY;
	private SparseArray<ArrayList<Integer>> textColorPixels;
}
