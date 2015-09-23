package ar.uba.fi.talker.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import ar.uba.fi.talker.paint.PaintManager;
import ar.uba.fi.talker.paint.PaintType;

public class Text extends DragComponent {

	protected String value;

	public Text(Context context) {
		super(context);
		value = "";
	}

	@Override
	protected Parcelable onSaveInstanceState() {

		Bundle bundle = new Bundle();
	    bundle.putParcelable("instanceState", super.onSaveInstanceState());
	    bundle.putString("label", this.value);
	    return bundle;
		
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		 if (state instanceof Bundle) {
			Bundle bundle = (Bundle) state;
		    value = bundle.getString("value");
			
			state = bundle.getParcelable("instanceState");
		}
		super.onRestoreInstanceState(state);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		int left = point.x + deltaPoint.x;
		int top = point.y + deltaPoint.y;
		Paint paint = PaintManager.getPaint(PaintType.TEXT);
		canvas.drawText(value, left, top, paint);
		if (EraserStroke.enabled) {
			Rect bounds = this.getBounds();
			left = left + bounds.right - (eraseBitmap.getWidth() / 2);
			top = top + bounds.top - (eraseBitmap.getHeight() / 2);
			canvas.drawBitmap(eraseBitmap, left, top, paint);
		}
	}
	
	@Override
	public boolean isPointInnerBounds(Point outerPoint) {
		Rect bounds = this.getBounds();
		return (point.x < outerPoint.x 
				&& point.y > outerPoint.y
				&& point.x + (bounds.right-bounds.left) + (eraseBitmap.getWidth()/2)  > outerPoint.x 
				&& point.y + bounds.top - (eraseBitmap.getHeight()/2) < outerPoint.y);
	}

	@Override
	public boolean isPointInnerEraseBounds(Point outerPoint) {
		Rect bounds = this.getBounds();
		return (EraserStroke.enabled
				&& point.x + (bounds.right-bounds.left) - (eraseBitmap.getWidth()/2) < outerPoint.x 
				&& point.x + (bounds.right-bounds.left) + (eraseBitmap.getWidth()/2)  > outerPoint.x 
				&& point.y + bounds.top - (eraseBitmap.getHeight()/2) < outerPoint.y
				&& point.y + bounds.top + (eraseBitmap.getHeight()/2) > outerPoint.y);
	}
	
	public void setValue(Editable text) {
		this.value = text.toString();
		Paint paint = PaintManager.getPaint(PaintType.TEXT);
		paint.getTextBounds(this.value, 0, this.value.length(), this.getBounds());
		this.invalidate();
	}
	

}