package ar.uba.fi.talker.component;

import android.content.Context;
import android.graphics.Paint;
import ar.uba.fi.talker.paint.PaintManager;
import ar.uba.fi.talker.paint.PaintType;

public class EraserStroke extends PencilStroke {
	
	public static boolean enabled = true;

	public EraserStroke(Context context) {
		super(context);
		
	}
		
	@Override
	public Paint getPaint() {
		return PaintManager.getPaint(PaintType.ERASE);
	}

}
