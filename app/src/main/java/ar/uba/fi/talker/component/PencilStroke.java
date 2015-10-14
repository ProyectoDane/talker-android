package ar.uba.fi.talker.component;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.MotionEvent;
import ar.uba.fi.talker.paint.PaintManager;
import ar.uba.fi.talker.paint.PaintType;

public class PencilStroke extends Component {

	private ArrayList<PencilPoint> points;
	private Path path;
	
	public PencilStroke(Context context) {
		super(context);
		points = new ArrayList<PencilPoint>();
		path = new Path();
	}
		
	@Override
	protected Parcelable onSaveInstanceState() {
		Bundle bundle = new Bundle();
		bundle.putParcelable("instanceState", super.onSaveInstanceState());
		bundle.putParcelableArrayList("points", this.points);
		return bundle;
	}
	
	@Override
	protected void onRestoreInstanceState(Parcelable state) {

		 if (state instanceof Bundle) {
			Bundle bundle = (Bundle) state;
             this.points = bundle.getParcelableArrayList("points");

			state = bundle.getParcelable("instanceState");
		}
		super.onRestoreInstanceState(state);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		Paint paint = this.getPaint(); 
		path.reset();
		for (PencilPoint point : points) {
			if (point.initial) {
				path.moveTo(point.x, point.y);
				canvas.drawPoint(point.x, point.y, paint);
			} else {
				path.lineTo(point.x, point.y);
			}
			if (point.end) {
				canvas.drawPoint(point.x, point.y, paint);
			}
		}

		canvas.drawPath(path, paint);
	}
	
	@Override
	public boolean performClick() {
		return super.performClick();
	}

	public Paint getPaint() {
		return PaintManager.getPaint(PaintType.REGULAR);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float eventX = event.getAxisValue(MotionEvent.AXIS_X);
		float eventY = event.getAxisValue(MotionEvent.AXIS_Y);
		PencilPoint point = new PencilPoint();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			point.initial = true;
		case MotionEvent.ACTION_MOVE:
			point.x = (int) eventX;
			point.y = (int) eventY;
			break;
		case MotionEvent.ACTION_UP:
			point.end = true;
			point.x = (int) eventX;
			point.y = (int) eventY;
			this.toggleActive();
		}
		points.add(point);
		
		this.invalidate();
		return this.performClick();
	}

}
