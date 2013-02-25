package rf.yagodar.glump.parser;

import rf.yagodar.manqala.free.free.output.SystemMessage;
import android.app.Activity;

public class SurfaceViewPropertyFloatParser implements ISurfaceViewPropertyParser {
	public static SurfaceViewPropertyFloatParser getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new SurfaceViewPropertyFloatParser();
		}
		
		return INSTANCE;
	}
	
	@Override
	public Float parseValue(Activity activity, ISurfaceViewProperties property) {
		float propertyValue = 0.0f;
		
		if(property != null) {
			String propertyStrValue = property.getSourceValue();
			if(propertyStrValue == null) {			
				if(property.getDefaultValue() == null) {
					SystemMessage.logErrWithoutAnyValue(logTag, property.toString());
				}
				else {
					propertyStrValue = property.getDefaultValue();

					try {
						propertyValue = Float.parseFloat(propertyStrValue);
					}
					catch(NumberFormatException e2) {
						SystemMessage.logErrDefValue(logTag, property.toString(), propertyStrValue);
					}
				}
			}
			else {
				try {
					propertyValue = Float.parseFloat(propertyStrValue);
				}
				catch(NumberFormatException e1) {
					SystemMessage.logWarnSrcValue(logTag, property.toString(), propertyStrValue);
					
					propertyStrValue = property.getDefaultValue();

					try {
						propertyValue = Float.parseFloat(propertyStrValue);
					}
					catch(NumberFormatException e2) {
						SystemMessage.logErrDefValue(logTag, property.toString(), propertyStrValue);
					}
				}
			}
		}
		
		return propertyValue;
	}
	
	private SurfaceViewPropertyFloatParser() {
		logTag = SurfaceViewPropertyFloatParser.class.getSimpleName();
	}
	
	private final String logTag;
	
	private static SurfaceViewPropertyFloatParser INSTANCE = null;
}