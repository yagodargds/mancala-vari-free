package rf.yagodar.manqala.free.free;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

//TODO DOC
public class ManqalaXmlParser {	
	public static ManqalaXmlParser getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new ManqalaXmlParser();
		}
		
		return INSTANCE;
	}
	
	public Properties getProperties(XmlPullParser xpp) {
		if(xpp == null) {
			//TODO LOG?
		}
		else if(!propertiesByXpp.containsKey(xpp)) {
			Properties properties = new Properties();
			try {				
				int event = xpp.next();
				while(event != XmlPullParser.END_DOCUMENT) {
					if(event == XmlPullParser.START_TAG && xmlSetTag.equals(xpp.getName())) {
						properties.setProperty(xpp.getAttributeValue(null, xmlSetTagAttrName), xpp.getAttributeValue(null, xmlSetTagAttrValue));
					}

					event = xpp.next();
				}
			} 
			catch (XmlPullParserException e) {
				e.printStackTrace();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}

			propertiesByXpp.put(xpp, properties);
		}
		
		return propertiesByXpp.get(xpp);
	}
	
	private HashMap<XmlPullParser, Properties> propertiesByXpp = new HashMap<XmlPullParser, Properties>();
	
	private final String xmlSetTag = "set";
	private final String xmlSetTagAttrName = "name";
	private final String xmlSetTagAttrValue = "value";
	
	private static ManqalaXmlParser INSTANCE = null;	
}
