package ar.uba.fi.talker.component;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import android.content.Context;
import android.util.Log;

public class ComponentFactory {
	private static final String TAG = "ComponentFactory";

	public static Component createComponent(ComponentType type, Context context) {

		try {
			
			Constructor<? extends Component> constructor = type.className().getConstructor(Context.class);
			return constructor.newInstance(context);
			
		} catch (NoSuchMethodException e) {
			Log.e(TAG, e.getMessage(), e);
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			Log.e(TAG, e.getMessage(), e);
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			Log.e(TAG, e.getMessage(), e);
			e.printStackTrace();
		} catch (InstantiationException e) {
			Log.e(TAG, e.getMessage(), e);
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			Log.e(TAG, e.getMessage(), e);
			e.printStackTrace();
		}
		return null;
	}
}
