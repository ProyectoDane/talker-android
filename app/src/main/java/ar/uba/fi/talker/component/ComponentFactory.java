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
        } catch (IllegalArgumentException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (InvocationTargetException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (InstantiationException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (IllegalAccessException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return null;
    }

    public static Component createComponentByName(Class<Component> component, Context context) {

        try {

            Constructor<? extends Component> constructor = component.getConstructor(Context.class);
            return constructor.newInstance(context);

        } catch (NoSuchMethodException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (InvocationTargetException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (InstantiationException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (IllegalAccessException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return null;
    }
}
