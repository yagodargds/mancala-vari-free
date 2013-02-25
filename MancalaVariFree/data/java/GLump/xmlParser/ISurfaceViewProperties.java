package rf.yagodar.glump.parser;

import android.app.Activity;

public interface ISurfaceViewProperties {
	public void setSourceValue(String sourceValue);
	public String getSourceValue();
	public String getDefaultValue();
	public Object parseValue(Activity activity);
}
