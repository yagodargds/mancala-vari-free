package rf.yagodar.glump.renderer;
//TODO DOC
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import rf.yagodar.glump.bitmap.BitmapProvider;
import rf.yagodar.glump.polygon.AbstractPolygon;
import android.graphics.Bitmap;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.opengl.GLUtils;

public class GLumpSVRenderer implements Renderer {
	public static GLumpSVRenderer getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new GLumpSVRenderer();
		}
		
		return INSTANCE;
	}
	
	//Вызывается, когда создана новая поверхность для представления
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {		
		//Выключить GL_DITHER - сглаживание цветовых компонентов или индексов до того, как они записаны в color buffer
		//По умолчанию GL_DITHER и GL_MULTISAMPLE включены, остальные выключены.
		//gl.glDisable(GL10.GL_DITHER);

		//Указать hints
		//По умолчанию у всех hint установлено GL_DONT_CARE - нет предпочтений
		//target - GL_PERSPECTIVE_CORRECTION_HINT - качество цвета и интерполяция текстурных координат
		//mode - GL_FASTEST - наивысшая производительность
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);

		//Указывает цвет, который будет использоваться методом gl.glClear
		//glClearColor(red, green, blue, alpha), все значения в диапазоне [0, 1]
		//По умолчанию (0,0,0,0)
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

		//Указание способа техники затенения. Необходимо для просчёта освещения.
		//По умолчанию GL_SMOOTH - плавно, то есть цвет рассчитывается для каждой точки.
		//gl.glShadeModel(GL10.GL_SMOOTH);

		//Включить GL_DEPTH_TEST - делать сравнение глубин и обновлять depth buffer.
		gl.glEnable(GL10.GL_DEPTH_TEST);
		
		//Включить GL_BLEND - смешивать цвета
		gl.glEnable(GL10.GL_BLEND);
		
		//Задание коэффициентов функции прозрачности
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		gl.glPixelStorei(GL10.GL_UNPACK_ALIGNMENT, BYTE_UNPACK_ALIGMENT);

		gl.glEnable(GL10.GL_TEXTURE_2D);		
		
		gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_MODULATE); // with lighting
		
		//gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_REPLACE); // without lighting		
		
		bindTextures(gl);
	}

	//Вызывается, когда изменяется поверхность - я так понял, поворот экрана из книжного в альбомный вид и обратно, но также например усечение экрана и т.п.
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		//Указание прямоугольной области на экране, на которую проецируется видимый объём
		gl.glViewport(	0,			//x нижнего левого угла прямоугольника	
						0,			//y нижнего левого угла прямоугольника
						width,		//ширина прямоугольника
						height);	//высота прямоугольника

		//Устанавливает текущую matrix
		//По умолчанию GL_MODELVIEW - применить последующие матричные операции к стэку видовой matrix
		//GL_PROJECTION - применить последующие матричные операции к стэку проекционной matrix - указать, что нужна перспективная проекция
		gl.glMatrixMode(GL10.GL_PROJECTION);

		//Заменяет текущую matrix единичной matrix:
		//1 0 0 0
		//0 1 0 0
		//0 0 1 0
		//0 0 0 1
		gl.glLoadIdentity();

		//Отношение ширины к высоте экрана
		float aspectRatio = (float) width / height;
		
		//Указание области сцены - пространство, ограниченное 3D параллелепипедом - усеченная пирамида или видимый объём
		GLU.gluPerspective(	gl, 
							PERSPECTIVE_Y_FOV,	//y угол пирамиды от точки обзора камеры между прямыми, соединяющими рёбра передней стороны параллелепипеда
							aspectRatio, 
							PERSPECTIVE_Z_NEAR,	//z расстояние до передней стороны параллелепипеда от камеры
							PERSPECTIVE_Z_FAR);	//z расстояние до задней стороны параллелепипеда от камеры
	}

	//Вызывается, когда производится основное рисование
	@Override
	public void onDrawFrame(GL10 gl) {
		//Выключить GL_DITHER - сглаживание цветовых компонентов или индексов до того, как они записаны в color buffer
		//По умолчанию GL_DITHER и GL_MULTISAMPLE включены, остальные выключены.
		//gl.glDisable(GL10.GL_DITHER);

		//Очистка поверхности рисования.
		//GL_COLOR_BUFFER_BIT - очистить цвет
		//GL_DEPTH_BUFFER_BIT - очистить глубину
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		//Устанавливает текущую matrix
		//По умолчанию GL_MODELVIEW - применить последующие матричные операции к стэку видовой matrix
		gl.glMatrixMode(GL10.GL_MODELVIEW);

		//Заменяет текущую matrix единичной matrix:
		//1 0 0 0
		//0 1 0 0
		//0 0 1 0
		//0 0 0 1
		gl.glLoadIdentity();

		//Управление направлением камеры.
		GLU.gluLookAt(	gl,

						//Точка обзора	(расположение камеры)
						LOOK_AT_EYE_X, 
						LOOK_AT_EYE_Y, 
						LOOK_AT_EYE_Z, 
		
						//Точка, на которую смотрит камера (направление камеры)
						LOOK_AT_CENTER_X, 
						LOOK_AT_CENTER_Y, 
						LOOK_AT_CENTER_Z, 
		
						//Вектор ориентации (камера ориентирована вертикально, горизонтально или наклонена)
						LOOK_AT_UP_X, 
						LOOK_AT_UP_Y, 
						LOOK_AT_UP_Z);
		
		refreshTextures(gl);
		
		draw(gl);
	}
	
	public void addAllPolygons(Collection<? extends AbstractPolygon> polygons) {
		if(polygons != null) {
			synchronized (this.polygons) {
				this.polygons.addAll(polygons);
			}
		}
	}
	
	public void removeAllPolygons(Collection<? extends AbstractPolygon> polygons) {
		if(polygons != null) {
			synchronized (this.polygons) {
				this.polygons.removeAll(polygons);
			}
		}
	}
	
	public void addPolygon(AbstractPolygon polygon) {
		if(polygon != null) {
			synchronized (polygons) {
				polygons.add(polygon);
			}
		}
	}
	
	public void removePolygon(AbstractPolygon polygon) {
		if(polygon != null) {
			synchronized (polygons) {
				while(polygons.remove(polygon));
			}
		}
	}
	
	public void clearPolygons() {
		synchronized (polygons) {
			polygons.clear();
		}
	}
	
	public boolean refreshPolygons() {
		boolean generalResult = false;
		
		synchronized (polygons) {
			for (AbstractPolygon polygon : polygons) {
				if(polygon.refresh() == true && generalResult == false) {
					generalResult = true;
				}
			}
		}
		
		return generalResult;
	}
	
	private void draw(GL10 gl) {
		synchronized (polygons) {
			for (AbstractPolygon polygon : polygons) {
				polygon.draw(gl);
			}
		}
	}
	
	private void refreshTextures(GL10 gl) {
		bindEditedTextures(gl);
		bindNewTextures(gl);
	}
	
	private void bindTextures(GL10 gl) {	
		ArrayList<Bitmap> bitmaps = BitmapProvider.getInstance().getBitmaps();
		if(!bitmaps.isEmpty()) {
			int[] texturesNames = null;
			texturesNames = new int[bitmaps.size()];
			gl.glGenTextures(bitmaps.size(), texturesNames, 0);		
			for (int bitmapId = 0; bitmapId < bitmaps.size(); bitmapId++) {
				bindTexture(gl, texturesNames[bitmapId], bitmaps.get(bitmapId));
			}
			BitmapProvider.getInstance().setTextureNames(texturesNames);
		}
	}
	
	private void bindEditedTextures(GL10 gl) {
		HashSet<Integer> editedBitmapIds = BitmapProvider.getInstance().getEditedBitmapIds();
		if(!editedBitmapIds.isEmpty()) {
			Iterator<Integer> bitmapIdsIterator = editedBitmapIds.iterator();
			while(bitmapIdsIterator.hasNext()) {
				int bitmapId = bitmapIdsIterator.next();
				bindTexture(gl, BitmapProvider.getInstance().getTextureName(bitmapId), BitmapProvider.getInstance().getBitmap(bitmapId));
			}
			BitmapProvider.getInstance().clearEditedBitmapIds();
		}
	}
	
	private void bindNewTextures(GL10 gl) {	
		HashSet<Integer> newBitmapIds = BitmapProvider.getInstance().getNewBitmapIds();
		if(!newBitmapIds.isEmpty()) {
			int[] texturesNames = null;
			texturesNames = new int[newBitmapIds.size()];
			gl.glGenTextures(newBitmapIds.size(), texturesNames, 0);
			
			Iterator<Integer> bitmapIdsIterator = newBitmapIds.iterator();
			int bitmapIdIndx = 0;
			while(bitmapIdsIterator.hasNext()) {
				bindTexture(gl, texturesNames[bitmapIdIndx], BitmapProvider.getInstance().getBitmap(bitmapIdsIterator.next()));
				bitmapIdIndx++;
			}
			BitmapProvider.getInstance().addTextureNames(texturesNames);
			BitmapProvider.getInstance().clearNewBitmapIds();
		}
	}

	private void bindTexture(GL10 gl, int textureName, Bitmap bitmap) {
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureName);		
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
	}
	
	private GLumpSVRenderer() {
		polygons = new ArrayList<AbstractPolygon>();
	}
	
	private ArrayList<AbstractPolygon> polygons;
	
	private final static int BYTE_UNPACK_ALIGMENT = 1;
	
	public final static float PERSPECTIVE_Y_FOV = 60.0f;
	public final static float PERSPECTIVE_Z_NEAR = 3.0f;
	private final static float PERSPECTIVE_Z_FAR = 7.0f;

	private final static float LOOK_AT_EYE_X = 0.0f;
	private final static float LOOK_AT_EYE_Y = 0.0f;
	public final static float LOOK_AT_EYE_Z = 5.0f;

	private final static float LOOK_AT_CENTER_X = 0.0f;
	private final static float LOOK_AT_CENTER_Y = 0.0f;
	private final static float LOOK_AT_CENTER_Z = 0.0f;

	private final static float LOOK_AT_UP_X = 0.0f;
	private final static float LOOK_AT_UP_Y = 1.0f;
	private final static float LOOK_AT_UP_Z = 0.0f;
	
	private static GLumpSVRenderer INSTANCE = null;
}
