package rf.yagodar.glump.view;

import android.app.Activity;
import android.util.SparseArray;

public abstract class AbstractGLumpSVBuilder<T extends GLumpSV<V>, V extends GLumpSVBlank> {
	public T buildSV(Activity activity, int sVKey) {
		T sV = createNewSV(activity);
		V sVBlank = sVBankByIntKey.get(sVKey);

		if(sV != null && sVBlank == null) {
			sVBlank = buildSVBlank(activity, sVKey, sV.getLevelZ(), sV.getDisplayMetrics().widthPixels, sV.getDisplayMetrics().heightPixels);
			if(sVBlank != null) {
				sVBankByIntKey.put(sVKey, sVBlank);
			}
		}

		return mountSV(sV, sVBlank);		
	}
	
	abstract protected T createNewSV(Activity activity);
	abstract protected V buildSVBlank(Activity activity, int key, float sVLevelZ, float sVWidth, float sVHeight);
	
	private T mountSV(T sV, V sVBlank) {
		if(sV != null && sVBlank != null) {
			sV.setSVBlank(sVBlank);
		}
		
		return sV;
	}
	
	private SparseArray<V> sVBankByIntKey = new SparseArray<V>();
}
