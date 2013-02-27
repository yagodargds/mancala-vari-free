package rf.yagodar.glump.bitmap;

import java.util.ArrayList;
import java.util.HashSet;

import android.graphics.Bitmap;

public class BitmapProvider {
	public static BitmapProvider getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new BitmapProvider();
		}
		
		return INSTANCE;
	}
	
	public void addEditedBitmapId(int bitmapId) {
		editedBitmapIds.add(bitmapId);
	}
	
	public HashSet<Integer> getEditedBitmapIds() {
		return editedBitmapIds;
	}

	public void clearEditedBitmapIds() {
		editedBitmapIds.clear();
	}

	public void addNewBitmapId(int bitmapId) {
		newBitmapIds.add(bitmapId);
	}
	
	public HashSet<Integer> getNewBitmapIds() {
		return newBitmapIds;
	}

	public void clearNewBitmapIds() {
		newBitmapIds.clear();
	}
	
	public int addBitmap(Bitmap bitmap) {
		if(bitmap != null) {
			bitmaps.add(bitmap);
		}
		
		return bitmaps.indexOf(bitmap);
	}
	
	public ArrayList<Bitmap> getBitmaps() {
		return bitmaps;
	}
	
	public Bitmap getBitmap(int bitmapId) {
		return bitmaps.get(bitmapId);
	}
	
	public void setTextureNames(int[] texturesNames) {
		if(texturesNames != null) {
			this.textureNames = texturesNames;
		}
	}
	
	public void addTextureNames(int[] texturesNamesToAdd) {
		if(texturesNamesToAdd != null && texturesNamesToAdd.length > 0) {
			int[] newTexturesNames = new int[textureNames.length + texturesNamesToAdd.length];
			System.arraycopy(textureNames, 0, newTexturesNames, 0, textureNames.length);
			System.arraycopy(texturesNamesToAdd, 0, newTexturesNames, textureNames.length, texturesNamesToAdd.length);
			setTextureNames(newTexturesNames);
		}
	}
	
	public int getTextureName(int bitmapId) {
		try {
			if(textureNames == null || bitmapId < 0 || bitmapId >= textureNames.length) {
				return 0;
			}

			return textureNames[bitmapId];
		}
		catch(Exception e) {
			return 0;
		}
	}
	
	public void clearTextureNames() {
		textureNames = null;
	}
	
	protected int[] getTextureNames() {
		return textureNames;
	}
	
	protected BitmapProvider() {
		newBitmapIds = new HashSet<Integer>();
		editedBitmapIds = new HashSet<Integer>();
		bitmaps = new ArrayList<Bitmap>();
	}
	
	private final HashSet<Integer> newBitmapIds;
	private final HashSet<Integer> editedBitmapIds;
	private final ArrayList<Bitmap> bitmaps;
	private int[] textureNames;
	
	private static BitmapProvider INSTANCE;
}
