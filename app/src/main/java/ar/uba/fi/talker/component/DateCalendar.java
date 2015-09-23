package ar.uba.fi.talker.component;

import java.util.Calendar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import ar.uba.fi.talker.paint.PaintManager;
import ar.uba.fi.talker.paint.PaintType;

public class DateCalendar extends DragComponent {

	private final Paint paint;
	private Bitmap mImage;
	
	public DateCalendar(Context arg0) {
		super(arg0);
		paint = PaintManager.getPaint(PaintType.REGULAR);
		this.setDrawingCacheEnabled(true);
	}

	@Override
	protected Parcelable onSaveInstanceState() {

		Bundle bundle = new Bundle();
	    bundle.putParcelable("instanceState", super.onSaveInstanceState());
	    bundle.putParcelable("image", this.mImage);
	    return bundle;
		
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		 if (state instanceof Bundle) {
			Bundle bundle = (Bundle) state;
		    mImage = bundle.getParcelable("image");
			
			state = bundle.getParcelable("instanceState");
		}
		super.onRestoreInstanceState(state);
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		int left = point.x + deltaPoint.x;
		int top = point.y + deltaPoint.y;
		if (mImage != null) {
			canvas.drawBitmap(mImage, left, top, paint);
		}
		if (EraserStroke.enabled) {
			left = left + mImage.getWidth() - (eraseBitmap.getWidth() / 2);
			top = top - (eraseBitmap.getHeight() / 2);
			canvas.drawBitmap(eraseBitmap, left, top, paint);
		}
	}
	
	@Override
	public boolean isPointInnerBounds(Point outerPoint) {
		return (mImage != null 
				&& point.x < outerPoint.x 
				&& point.y - eraseBitmap.getHeight() < outerPoint.y
				&& point.x + mImage.getWidth() + eraseBitmap.getWidth() > outerPoint.x 
				&& point.y + mImage.getHeight() > outerPoint.y);
	}

	@Override
	public boolean isPointInnerEraseBounds(Point outerPoint) {
		return (EraserStroke.enabled 
				&& point.x + mImage.getWidth() - (eraseBitmap.getWidth() / 2) < outerPoint.x
				&& point.y - eraseBitmap.getHeight() / 2 < outerPoint.y
				&& point.x + mImage.getWidth() + (eraseBitmap.getWidth() / 2) > outerPoint.x 
				&& point.y + eraseBitmap.getHeight() / 2 > outerPoint.y);
	}
	
	public void setContent(Calendar calendar, int idImage) {
		String dateText = String.format("%1$tA %1$td %1$tB %1$tY", calendar);
		String dayParams[] = dateText.split(" "); 
		String dayOfWeek = dayParams[0];
		String numberDay = dayParams[1];
		String month = dayParams[2];
		String year = dayParams[3];
   		
		mImage = drawTextToBitmap(idImage, dayOfWeek.toUpperCase(), numberDay, month.toUpperCase(), year);
	}

	public Bitmap drawTextToBitmap(int resourceId, String dayOfWeek, String numberDay, String month, String year) {
		try {
			float widthProportion = 0.12f;
			float heightProportion = 0.875f;
			float textProportion = 0.19f;
			float bigTextProportion = 0.286f;
			
			DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
			
			int width = (int) (displayMetrics.widthPixels * widthProportion);
			width = width < 120 ? 120 : width;
			int heigth = (int) (width * heightProportion);
			int smallTextSize = (int) (heigth * textProportion);
			int bigTextSize = (int) (heigth * bigTextProportion);
			int whiteSpaces = (heigth - ((3 * smallTextSize) + bigTextSize))/4;
			int nextLinePos = smallTextSize+(whiteSpaces/2);
			
			Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), resourceId);
			bitmap =  Bitmap.createScaledBitmap(bitmap, width, heigth, true);
			android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();
			// set default bitmap config if none
			if (bitmapConfig == null) {
				bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
			}
			// resource bitmaps are immutable, so we need to convert it to mutable one
			bitmap = bitmap.copy(bitmapConfig, true);

			Canvas canvas = new Canvas(bitmap);

			Paint paintMonth = new Paint();
			paintMonth.setColor(Color.BLACK);
			paintMonth.setTextSize(smallTextSize);
			Rect bounds = new Rect();
			paintMonth.getTextBounds(month, 0, month.length(), bounds);
			int widthMonth = (bitmap.getWidth() - bounds.width())/2;
			canvas.drawText(month, widthMonth, nextLinePos, paintMonth);
			nextLinePos += bigTextSize+whiteSpaces;
			
			Paint paintNumberDay = new Paint();
			paintNumberDay.setColor(Color.RED);
			paintNumberDay.setTextSize(bigTextSize);
			Rect boundsNumberDay = new Rect();
			paintNumberDay.getTextBounds(numberDay, 0, numberDay.length(), boundsNumberDay);
			int widthNumberDay = (bitmap.getWidth() - boundsNumberDay.width())/2;
			canvas.drawText(numberDay, widthNumberDay, nextLinePos, paintNumberDay);
			nextLinePos += smallTextSize+whiteSpaces;
			
			Paint paintDayOfWeek = new Paint();
			paintDayOfWeek.setColor(Color.BLACK);
			paintDayOfWeek.setTextSize(smallTextSize);
			Rect boundsDayOfWeek = new Rect();
			paintDayOfWeek.getTextBounds(dayOfWeek, 0, dayOfWeek.length(), boundsDayOfWeek);
			int widthDayOfWeek = (bitmap.getWidth() - boundsDayOfWeek.width())/2;
			canvas.drawText(dayOfWeek, widthDayOfWeek, nextLinePos, paintDayOfWeek);
			nextLinePos += smallTextSize+whiteSpaces;
			
			Paint paintYear = new Paint();
			paintYear.setColor(Color.BLACK);
			paintYear.setTextSize(smallTextSize);
			Rect boundsYear = new Rect();
			paintYear.getTextBounds(year, 0, year.length(), boundsYear);
			int widthYear = (bitmap.getWidth() - boundsYear.width())/2;
			canvas.drawText(year, widthYear, nextLinePos, paintYear);
			
			bitmap.setDensity(Bitmap.DENSITY_NONE);
			return bitmap;
		} catch (Exception e) {
			return null;
		}
	}

}
