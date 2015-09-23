package ar.uba.fi.talker.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;

public final class ImageUtils {
	static int MAX_SIZE_IMAGE = 512;
	public static byte[] transformImage(Resources res, long imageViewId) {
		Bitmap image = BitmapFactory.decodeResource(res, (int) imageViewId);
		BitmapDrawable drawable = new BitmapDrawable(res, image);
		drawable.setAlpha(100);
		image = drawable.getBitmap();
		int newHeight = (int) (image.getHeight() * (512.0 / image.getWidth()));
		Bitmap scaled = Bitmap.createScaledBitmap(image, 512, newHeight, true);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		scaled.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		byte[] bytes = stream.toByteArray();
		return bytes;
	}
	
	public static byte[] transformImage(Bitmap image) {
		int newHeight = (int) (image.getHeight() * (512.0 / image.getWidth()));
		Bitmap scaled = Bitmap.createScaledBitmap(image, 512, newHeight, true);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		scaled.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		byte[] bytes = stream.toByteArray();
		return bytes;
	}

	public static void saveFileInternalStorage(String name, Bitmap b, Context context, int orientation) {

		FileOutputStream out;
		try {
			out = context.openFileOutput(name, Context.MODE_PRIVATE);
			b = resizeBitmapWithOrientation(b, orientation);
			b.compress(Bitmap.CompressFormat.JPEG, 100, out);		
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Matrix generateMatrix(Bitmap image, int orientation) {
		
		float scale = Math.min(((float)MAX_SIZE_IMAGE / image.getWidth()), ((float)MAX_SIZE_IMAGE / image.getHeight()));

		// createa matrix for the manipulation
		Matrix matrix = new Matrix();
		// resize the bit map
		matrix.postScale(scale, scale);
		// rotate the Bitmap
		matrix.postRotate(orientation);
		return matrix;
	}
	
	/**
	 * Recreate the new Bitmap
	 * @param image
	 * @param orientation
	 * @return {@link Bitmap}
	 */
	public static Bitmap resizeBitmapWithOrientation(Bitmap image, int orientation) {
		Matrix matrix = generateMatrix(image, orientation);
		Bitmap resizedBitmap = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, true);
		return resizedBitmap;
	}
	
	public static int getImageRotation(Context context, Uri imageUri) {
	    try {
	        ExifInterface exif = new ExifInterface(imageUri.getPath());
	        int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

	        if (rotation == ExifInterface.ORIENTATION_UNDEFINED)
	            return getRotationFromMediaStore(context, imageUri);
	        else return exifToDegrees(rotation);
	    } catch (IOException e) {
	        return 0;
	    }
	}

	public static int getRotationFromMediaStore(Context context, Uri imageUri) {
	    String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media.ORIENTATION};
	    Cursor cursor = context.getContentResolver().query(imageUri, columns, null, null, null);
	    if (cursor == null) return 0;

	    cursor.moveToFirst();

	    int orientationColumnIndex = cursor.getColumnIndex(columns[1]);
	    return cursor.getInt(orientationColumnIndex);
	}

	private static int exifToDegrees(int exifOrientation) {
	    if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
	        return 90;
	    } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
	        return 180;
	    } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
	        return 270;
	    } else {
	        return 0;
	    }
	}
}
