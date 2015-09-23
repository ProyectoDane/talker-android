package ar.uba.fi.talker.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Editable;
import ar.uba.fi.talker.paint.PaintManager;
import ar.uba.fi.talker.paint.PaintType;

public class LabelText extends Text {

	public LabelText(Context context) {
		super(context);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Paint paint = PaintManager.getPaint(PaintType.LABEL);
		canvas.drawText(value, point.x + deltaPoint.x, point.y + deltaPoint.y, paint);
	}

	@Override
	public void setValue(Editable text) {
		value = text.toString();
		Paint paint = PaintManager.getPaint(PaintType.LABEL);
		paint.getTextBounds(value, 0, value.length(), getBounds());
		this.invalidate();
	}

}