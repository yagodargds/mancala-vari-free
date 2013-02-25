package rf.yagodar.glump.parser;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParser;

import android.app.Activity;
import android.util.Log;
import android.util.SparseArray;
//TODO DOC
public class SurfaceViewPropertiesXmlParser {	
	public static SurfaceViewPropertiesXmlParser getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new SurfaceViewPropertiesXmlParser();
		}
		
		return INSTANCE;
	}
	
	public <T extends Enum<T>> HashMap<String, Object> getParsedProperties(Activity activity, int scenePropertiesXmlResId, T[] surfaceViewPropertiesEnumValues) {
		if(activity == null || scenePropertiesXmlResId == 0 || surfaceViewPropertiesEnumValues == null || surfaceViewPropertiesEnumValues.length == 0) {
			return null;
		}
		
		if(surfaceViewPropertiesByXmlResId.get(scenePropertiesXmlResId) == null) {
			XmlPullParser xpp;		
			try {
				xpp = activity.getResources().getXml(scenePropertiesXmlResId);
			}
			catch(Exception e) {
				Log.e(logTag, "Error while get XmlPullParser with resource id [" + scenePropertiesXmlResId + "]. Parsing failed.", e);
				return null;
			}			
			
			HashMap<String, Object> parsedProperties = new HashMap<String, Object>();
			try {				
				int event = xpp.next();
				
				T property;
				Class<T> surfaceViewPropertiesEnumType = surfaceViewPropertiesEnumValues[0].getDeclaringClass();
				while(event != XmlPullParser.END_DOCUMENT) {
					if(event == XmlPullParser.START_TAG && xmlSetTag.equals(xpp.getName())) {
						property = Enum.valueOf(surfaceViewPropertiesEnumType, xpp.getAttributeValue(null, xmlSetTagAttrName));
						if(property != null) {
							if(property instanceof ISurfaceViewProperties) {
								((ISurfaceViewProperties)property).setSourceValue(xpp.getAttributeValue(null, xmlSetTagAttrValue));								
							}
							else {
								Log.e(logTag, "Error! SurfaceViewProperties must implement [" + ISurfaceViewProperties.class.getSimpleName() + "]. Parsing failed.");
								return null;
							}
						}
					}

					event = xpp.next();
				}
				
				for(int i = 0; i < surfaceViewPropertiesEnumValues.length; i++) {
					parsedProperties.put(surfaceViewPropertiesEnumValues[i].toString(), ((ISurfaceViewProperties)surfaceViewPropertiesEnumValues[i]).parseValue(activity));
				}
			} 
			catch(Exception e) {
				Log.e(logTag, "Error! Parsing failed.", e);
				return null;
			}
			
			surfaceViewPropertiesByXmlResId.put(scenePropertiesXmlResId, parsedProperties);
		}
		
		return surfaceViewPropertiesByXmlResId.get(scenePropertiesXmlResId);
	}

	private SurfaceViewPropertiesXmlParser() {
		xmlSetTag = "set";
		xmlSetTagAttrName = "name";
		xmlSetTagAttrValue = "value";
		logTag = SurfaceViewPropertiesXmlParser.class.getSimpleName();
		surfaceViewPropertiesByXmlResId = new SparseArray<HashMap<String, Object>>();
	}
	
	private final String xmlSetTag;
	private final String xmlSetTagAttrName;
	private final String xmlSetTagAttrValue;
	private final String logTag;
	private final SparseArray<HashMap<String, Object>> surfaceViewPropertiesByXmlResId;
	
	private static SurfaceViewPropertiesXmlParser INSTANCE = null;	
}
