package rf.yagodar.glump.parser;

import rf.yagodar.manqala.free.free.output.SystemMessage;
import android.app.Activity;
import android.util.SparseArray;

public class SurfaceViewPropertyDrawableResNamesParser implements ISurfaceViewPropertyParser {
	public static SurfaceViewPropertyDrawableResNamesParser getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new SurfaceViewPropertyDrawableResNamesParser();
		}
		
		return INSTANCE;
	}
	
	@Override
	public SparseArray<String> parseValue(Activity activity, ISurfaceViewProperties property) {
		SparseArray<String> propertyValue = null;

		if(property != null) {
			String propertyStrValue = property.getSourceValue();
			if(propertyStrValue == null) {			
				if(property.getDefaultValue() == null) {
					propertyValue = new SparseArray<String>();
					SystemMessage.logErrWithoutAnyValue(logTag, property.toString());
				}
				else {
					propertyStrValue = property.getDefaultValue();
					propertyValue = parseDrawableResIdsStrValue(activity, propertyStrValue);

					if(propertyValue == null) {
						propertyValue = new SparseArray<String>();
						SystemMessage.logErrDefValue(logTag, property.toString(), propertyStrValue);
					}
				}
			}
			else {
				propertyValue = parseDrawableResIdsStrValue(activity, propertyStrValue);

				if(propertyValue == null) {
					SystemMessage.logWarnSrcValue(logTag, property.toString(), propertyStrValue);

					propertyStrValue = property.getDefaultValue();
					propertyValue = parseDrawableResIdsStrValue(activity, propertyStrValue);

					if(propertyValue == null) {
						propertyValue = new SparseArray<String>();
						SystemMessage.logErrDefValue(logTag, property.toString(), propertyStrValue);
					}
				}
			}
		}

		return propertyValue;
	}

	private SparseArray<String> parseDrawableResIdsStrValue(Activity activity, String drawableResIdsStrValue) {
		if(drawableResIdsStrValue != null) {
			String splittedDrawableResIdsStrValue[] = drawableResIdsStrValue.split(";");
			SparseArray<String> drawableResIds = new SparseArray<String>();

			int drawableResId = 0;
			for(int i = 0; i < splittedDrawableResIdsStrValue.length; i++) {
				String drawableResName = splittedDrawableResIdsStrValue[i];

				try {
					drawableResId = activity.getResources().getIdentifier(drawableResName, "drawable", activity.getPackageName());				
				}
				catch(Exception e1) {}

				if(drawableResId != 0) {
					drawableResIds.put(drawableResId, drawableResName);
				}
			}

			return drawableResIds;
		}
		
		return null;
	}
	
	private SurfaceViewPropertyDrawableResNamesParser() {
		logTag = SurfaceViewPropertyDrawableResNamesParser.class.getSimpleName();
	}
	
	private final String logTag;
	
	private static SurfaceViewPropertyDrawableResNamesParser INSTANCE = null;
}