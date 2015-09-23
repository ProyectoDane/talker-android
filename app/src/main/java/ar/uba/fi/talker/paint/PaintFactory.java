/**
 * 
 */
package ar.uba.fi.talker.paint;

import android.graphics.Color;
import android.graphics.Paint;
import ar.uba.fi.talker.component.Setting;

/**
 * @author ssoria
 * 
 */
public final class PaintFactory {

	public static Integer LABEL_TEXT_SIZE = 40;
	
	
	public static Paint createPaint(PaintType type) {

		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		switch (type) {
		case ERASE:
			paint.setStyle(Paint.Style.STROKE);
			paint.setFlags(Paint.LINEAR_TEXT_FLAG);
			break;
		case TEXT:
			paint.setStyle(Paint.Style.FILL_AND_STROKE);
			paint.setFlags(Paint.LINEAR_TEXT_FLAG);
			paint.setStrokeJoin(Paint.Join.MITER);
			paint.setFakeBoldText(true);

			break;
		case LABEL:
			paint.setStyle(Paint.Style.FILL_AND_STROKE);
			paint.setFlags(Paint.LINEAR_TEXT_FLAG);
			paint.setStrokeJoin(Paint.Join.MITER);
			paint.setFakeBoldText(true);
			break;
		case REGULAR:
		default:
			paint.setStyle(Paint.Style.STROKE);
			paint.setFlags(Paint.LINEAR_TEXT_FLAG);
			break;
		}

		return paint;
	}

	public static void definePaint(Paint paint, PaintType type, Setting settings) {
		Integer textColor;
		Integer shadowColor;
		switch (type) {
		case ERASE:
			paint.setStrokeWidth(settings.getEraserSize());
			break;
		case TEXT:
			paint.setTextSize(settings.getTextSize());
			textColor = Color.parseColor(settings.getTextColor());
			paint.setColor(textColor);
			shadowColor = PaintFactory.getComplimentColor(textColor);
			paint.setShadowLayer(5, 0, 0, shadowColor);
			break;
		case LABEL:
			paint.setTextSize(LABEL_TEXT_SIZE);
			textColor = Color.BLACK;
			paint.setColor(textColor);
			shadowColor = PaintFactory.getComplimentColor(textColor);
			paint.setShadowLayer(5, 0, 0, shadowColor);
			break;
		case REGULAR:
		default:
			paint.setStrokeWidth(settings.getPencilSize());
			paint.setColor(Color.parseColor(settings.getPencilColor()));
			break;
		}
	}

	/**
	 * Returns the opposite color.
	 * 
	 * @param color
	 * @return int color
	 */
	private static int getComplimentColor(int color) {
		// get existing colors
		int alpha = Color.alpha(color);
		int red = Color.red(color);
		int blue = Color.blue(color);
		int green = Color.green(color);

		// find compliments
		red = (~red) & 0xff;
		blue = (~blue) & 0xff;
		green = (~green) & 0xff;

		return Color.argb(alpha, red, green, blue);
	}
}
