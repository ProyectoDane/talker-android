package ar.uba.fi.talker.view;

import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import ar.uba.fi.talker.component.Component;
import ar.uba.fi.talker.component.ComponentFactory;
import ar.uba.fi.talker.component.ComponentType;
import ar.uba.fi.talker.component.DateCalendar;
import ar.uba.fi.talker.component.DragComponent;
import ar.uba.fi.talker.component.Image;
import ar.uba.fi.talker.component.Text;
import ar.uba.fi.talker.paint.PaintManager;
import ar.uba.fi.talker.paint.PaintType;
import ar.uba.fi.talker.utils.BackgroundUtil;

public class Scenario extends FrameLayout {

	private static final String CHILD_TYPE = "Child_Type_";
	private static final String CHILD_LABEL = "Child_";

	private ComponentType activeComponentType;

	private Component activeComponent;

	private List<Component> draggableComponents;

	private Bitmap mImage = null;

	private int viewIdIncremental = new Random().nextInt();

	private Bundle bundle = null;

	public Scenario(Context context) {
		super(context);
		this.init();
	}

	public Scenario(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.init();
	}

	public Scenario(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.init();
	}

	private void init() {
		this.setActiveComponentType(ComponentType.PENCIL);
		draggableComponents = new LinkedList<Component>();
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		int childCount = this.getChildCount();
		if (bundle == null) {
			bundle = new Bundle();
		}
		
		bundle.putParcelable("instanceState", super.onSaveInstanceState());
		if (childCount > 0) {
			int realChildCount = 0;
			for (; childCount > 0; childCount--) {
				Component child = (Component) this.getChildAt(0);
				this.removeView(child);
				if (child.getVisibility() == VISIBLE) {
					bundle.putString(CHILD_TYPE + realChildCount, child.getClass().getName());
					bundle.putParcelable(CHILD_LABEL + realChildCount, child.getParcelable());
					realChildCount++;
				}
			}
			bundle.putInt("childCount", realChildCount);
		}
		
		return bundle;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		if (state instanceof Bundle) {
			Bundle bundle = (Bundle) state;
			int childCount = bundle.getInt("childCount");
			for (int index = 0; index < childCount; index++) {
				String name = bundle.getString(CHILD_TYPE + index);
				Parcelable data = bundle.getParcelable(CHILD_LABEL + index);
                try {
                    Class<Component> classComponent = (Class<Component>) Class.forName(name);
                    Component child = ComponentFactory.createComponentByName(
                            classComponent, getContext());
                    if (child != null) {
                        child.restoreFromParcelable(data);
                        if (child instanceof DragComponent) {
                            draggableComponents.add(child);
                        }
                        this.addView(child);
                    }
                } catch (ClassNotFoundException e) {
                    Log.e("SCENARIO", "invalid type", e);
                }
			}
			state = bundle.getParcelable("instanceState");
		}
		super.onRestoreInstanceState(state);
	}

	@Override
	public boolean performClick() {
		return true;
	}

	@Override
	public void invalidate() {
		super.invalidate();
		for (Component child : draggableComponents) {
			child.invalidate();
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			boolean foundElement = false;
			int eventX = (int) event.getAxisValue(MotionEvent.AXIS_X);
			int eventY = (int) event.getAxisValue(MotionEvent.AXIS_Y);
			Point point = new Point(eventX, eventY);
			
			Iterator<Component> i = draggableComponents.iterator();
			while (i.hasNext()) {
				Component child = i.next();
				if (child.isPointInnerBounds(point)) {
					foundElement = true;
					if (child.isPointInnerEraseBounds(point)) {
						child.setVisibility(GONE);
					    i.remove();
					}
					activeComponent = child;
					activeComponent.toggleActive();
				}
			}

			if (!foundElement) {
				activeComponent = ComponentFactory.createComponent(
						activeComponentType, getContext());

				this.addView(activeComponent,
						android.view.ViewGroup.LayoutParams.MATCH_PARENT,
						android.view.ViewGroup.LayoutParams.MATCH_PARENT);

				activeComponent.setId(viewIdIncremental++);
				activeComponent.setSaveEnabled(true);
				activeComponent.setSaveFromParentEnabled(true);
			}

		}
		activeComponent.onTouchEvent(event);
		return this.performClick();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (mImage != null) {
			int width = this.getWidth();
			int height = this.getHeight();
			Bitmap resized = Bitmap.createScaledBitmap(mImage, width, height,
					true);
			BitmapShader fillBMPshader = new BitmapShader(resized,
					Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);

			Paint fillPaint = PaintManager.getPaint(PaintType.ERASE);
			fillPaint.setShader(fillBMPshader);
			mImage = null;
		}

		super.onDraw(canvas);
	}

	public void setBackgroundImage(Bitmap image, boolean isHistory) {
		if (isHistory) {
			mImage = image;
		} else {
			mImage = BackgroundUtil.toGrayscale(image);
		}
		BitmapDrawable background = new BitmapDrawable(getResources(), mImage);			
		BackgroundUtil.setBackground(this, background);
	}

	public void clear() {
		this.removeAllViews();
	}

	public void setActiveComponentType(ComponentType type) {
		this.activeComponentType = type;
	}

	public void setText(Editable text) {
		Component alterComponent = ComponentFactory.createComponent(
				ComponentType.TEXT, getContext());

		android.view.ViewGroup.LayoutParams layoutParams = this
				.getLayoutParams();
		layoutParams.height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
		layoutParams.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;

		this.addView(alterComponent, layoutParams);
		((Text) alterComponent).setValue(text);
		alterComponent.toggleActive();
		draggableComponents.add(alterComponent);
	}

	public void addImage(Bitmap image, String label) {
		Component alterComponent = ComponentFactory.createComponent(
				ComponentType.IMAGE, getContext());

		android.view.ViewGroup.LayoutParams layoutParams = this
				.getLayoutParams();
		layoutParams.height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
		layoutParams.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
		this.addView(alterComponent, layoutParams);
		
		((Image) alterComponent).setContent(image, label);
		alterComponent.toggleActive();
		draggableComponents.add(alterComponent);
	}
	
	public void addCalendar(Calendar calendar, int idImage) {
		Component alterComponent = ComponentFactory.createComponent(
				ComponentType.DATECALENDAR, getContext());

		android.view.ViewGroup.LayoutParams layoutParams = this
				.getLayoutParams();
		layoutParams.height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
		layoutParams.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
		this.addView(alterComponent, layoutParams);

		((DateCalendar) alterComponent).setContent(calendar, idImage);
		alterComponent.toggleActive();
		draggableComponents.add(alterComponent);
	}
	
	public void restore() {
		if (bundle != null) {
			this.onRestoreInstanceState(bundle);
		}
	}

	public List<Component> getDraggableComponents() {
		return draggableComponents;
	}
	
}
