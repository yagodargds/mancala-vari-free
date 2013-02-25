package rf.yagodar.glump.parser;

import android.app.Activity;
import rf.yagodar.manqala.free.free.output.SystemMessage;

public class SurfaceViewPropertyColorParser implements ISurfaceViewPropertyParser {
	public static SurfaceViewPropertyColorParser getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new SurfaceViewPropertyColorParser();
		}
		
		return INSTANCE;
	}
	
	@Override
	public Integer parseValue(Activity activity, ISurfaceViewProperties property) {
		int propertyValue = 0;
		
		if(property != null) {
			String propertyStrValue = property.getSourceValue();
			if(propertyStrValue == null) {			
				if(property.getDefaultValue() == null) {
					SystemMessage.logErrWithoutAnyValue(logTag, property.toString());
				}
				else {
					propertyStrValue = property.getDefaultValue();

					try {
						propertyValue = (int) Long.parseLong(propertyStrValue, intRadix);
					}
					catch(NumberFormatException e2) {
						SystemMessage.logErrDefValue(logTag, property.toString(), propertyStrValue);
					}
				}
			}
			else {
				try {
					propertyValue = (int) Long.parseLong(propertyStrValue, intRadix);
				}
				catch(NumberFormatException e1) {
					SystemMessage.logWarnSrcValue(logTag, property.toString(), propertyStrValue);
					
					propertyStrValue = property.getDefaultValue();

					try {
						propertyValue = (int) Long.parseLong(propertyStrValue, intRadix);
					}
					catch(NumberFormatException e2) {
						SystemMessage.logErrDefValue(logTag, property.toString(), propertyStrValue);
					}
				}
			}
		}
		
		return propertyValue;
	}
	
	private SurfaceViewPropertyColorParser() {
		intRadix = 16;
		logTag = SurfaceViewPropertyColorParser.class.getSimpleName();
	}
	
	private final int intRadix;
	private final String logTag;
	
	private static SurfaceViewPropertyColorParser INSTANCE = null;
}
