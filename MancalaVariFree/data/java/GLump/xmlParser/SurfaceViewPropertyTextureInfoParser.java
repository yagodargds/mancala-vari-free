package rf.yagodar.glump.parser;

import android.app.Activity;
import android.util.Log;
import rf.yagodar.glump.bitmap.ResBitmapManager;
import rf.yagodar.glump.polygon.TextureInfo;
import rf.yagodar.manqala.free.free.output.SystemMessage;

public class SurfaceViewPropertyTextureInfoParser implements ISurfaceViewPropertyParser {
	public static SurfaceViewPropertyTextureInfoParser getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new SurfaceViewPropertyTextureInfoParser();
		}
		
		return INSTANCE;
	}
	
	@Override
	public TextureInfo parseValue(Activity activity, ISurfaceViewProperties property) {
		TextureInfo propertyValue = null;

		if(property != null) {
			String propertyStrValue = property.getSourceValue();
			if(propertyStrValue == null) {			
				if(property.getDefaultValue() == null) {
					propertyValue = new TextureInfo(null, TextureInfo.RECTANGLE_TEXTURE_POINTS_PARAMS_DEFAULT);
					SystemMessage.logErrWithoutAnyValue(logTag, property.toString());
				}
				else {
					propertyStrValue = property.getDefaultValue();
					propertyValue = parseTextureInfoStrValue(propertyStrValue);

					if(propertyValue == null) {
						propertyValue = new TextureInfo(null, TextureInfo.RECTANGLE_TEXTURE_POINTS_PARAMS_DEFAULT);
						SystemMessage.logErrDefValue(logTag, property.toString(), propertyStrValue);
					}
				}
			}
			else {
				propertyValue = parseTextureInfoStrValue(propertyStrValue);

				if(propertyValue == null) {
					SystemMessage.logWarnSrcValue(logTag, property.toString(), propertyStrValue);

					propertyStrValue = property.getDefaultValue();
					propertyValue = parseTextureInfoStrValue(propertyStrValue);

					if(propertyValue == null) {
						propertyValue = new TextureInfo(null, TextureInfo.RECTANGLE_TEXTURE_POINTS_PARAMS_DEFAULT);
						SystemMessage.logErrDefValue(logTag, property.toString(), propertyStrValue);
					}
				}
			}
		}

		return propertyValue;
	}

	private TextureInfo parseTextureInfoStrValue(String textureInfoStrValue) {
		TextureInfo textureInfo = null;

		String splittedTextureInfoStrValue[] = textureInfoStrValue.split(";");
		if(splittedTextureInfoStrValue.length == 1 || splittedTextureInfoStrValue.length == 2) {
			String drawableName = splittedTextureInfoStrValue[0];

			if(splittedTextureInfoStrValue.length == 1 || ResBitmapProvider.getInstance().isNoTextureBitmapId(drawableName)) {
				textureInfo = new TextureInfo(drawableName, TextureInfo.RECTANGLE_TEXTURE_POINTS_PARAMS_DEFAULT);
			}
			else {
				float texturePointsParams[];
				String texturePointsParamsStrValue = splittedTextureInfoStrValue[1];
				String splittedTexturePointsParamsStrValue[] = texturePointsParamsStrValue.split(",");
				texturePointsParams = new float[splittedTexturePointsParamsStrValue.length];
				try {
					for(int i = 0; i < texturePointsParams.length; i++) {
						texturePointsParams[i] = Float.parseFloat(splittedTexturePointsParamsStrValue[i]);
					}

					if(TextureInfo.checkTexturePointsParams(texturePointsParams)) {
						textureInfo = new TextureInfo(drawableName, texturePointsParams);
					}
				}
				catch(Exception e1) {}
			}			
		}

		if(textureInfo == null) {
			Log.e(logTag, textureInfoParsingErrorMsg);
		}

		return textureInfo;
	}
	
	private SurfaceViewPropertyTextureInfoParser() {
		textureInfoParsingErrorMsg = "Error parsing TextureInfo str value. Format examples for TextureInfo: 1) [{drawable_name};{float leftS},{float topT},{float rightS},{float bottomT}]. Right conditions: 0.0f <= leftS, rightS, bottomT, topT <= 1.0f && leftS < rightS && bottomT < topT. 2) [{drawable_name}]. In that case values would be: leftS = bottomT = 0.0f; rightS = topT = 1.0f";//TODO system messages
		logTag = SurfaceViewPropertyTextureInfoParser.class.getSimpleName();
	}

	private final String textureInfoParsingErrorMsg;
	private final String logTag;
	
	private static SurfaceViewPropertyTextureInfoParser INSTANCE = null;
}