package ar.uba.fi.talker.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapUtil {

	public static Bitmap createFromPath(String path, Resources resources) {
		
		if (path.contains("/")){
			return BitmapFactory.decodeFile(path);
		} else {
			return BitmapFactory.decodeResource(resources, Integer.parseInt(path));
		}
	}

	
}
