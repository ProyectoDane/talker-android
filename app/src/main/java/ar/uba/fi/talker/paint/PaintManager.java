/**
 * 
 */
package ar.uba.fi.talker.paint;

import java.util.HashMap;
import java.util.Map;

import android.graphics.Paint;
import ar.uba.fi.talker.component.Setting;

/**
 * @author ssoria
 *
 */
public final class PaintManager {

	private final Map<PaintType, Paint> paints;
	
	private static PaintManager instance;
	
	private static Setting settings;
	
	private PaintManager() {
		paints = new HashMap<PaintType, Paint>();
	}	
	
	protected static PaintManager getInstance() {
		if (instance == null) {
			instance = new PaintManager();
		}
		
		return instance;
	}
	
	public static Paint getPaint(PaintType type) {
		Paint paint = getInstance().paints.get(type);
		if (paint == null) {
			paint = PaintFactory.createPaint(type);
			getInstance().paints.put(type, paint);
		}
		PaintFactory.definePaint(paint, type, settings);
		return paint;
	}

	public static void setSettings(Setting sharedPref) {
		PaintManager.settings = sharedPref;
	}

	public static Setting getSettings() {
		return settings;
	}
	
}
