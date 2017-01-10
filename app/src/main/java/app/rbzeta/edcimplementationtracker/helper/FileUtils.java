package app.rbzeta.edcimplementationtracker.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import app.rbzeta.edcimplementationtracker.application.AppConfig;

import static com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage;

/**
 * Created by Robyn on 11/29/2016.
 */

public class FileUtils {
    public static File getImageStorageDirectory() {
        return Environment
                .getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES + AppConfig.DIR_IMG_FILE_NAME);
    }

    public static Bitmap getCorrectedOrientationBitmap(Bitmap bmp,String path){
        ExifInterface ei = null;
        try {
            ei = new ExifInterface(path);
        } catch (IOException e) {

        }
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        switch(orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(bmp, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(bmp, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(bmp, 270);
            case ExifInterface.ORIENTATION_NORMAL:
                return bmp;
            default:
                return bmp;
        }
    }

    public static File createImageFile() throws IOException {

        String timeStamp = StringUtils.getStringTimeStamp();
        String imageFileName = AppConfig.PREFIX_IMG_FILE_NAME + timeStamp;
        File storageDir = FileUtils.getImageStorageDirectory();

        if (!storageDir.exists()){
            if (!storageDir.mkdirs()){
                return null;
            }
        }

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                AppConfig.FORMAT_JPG,         /* suffix */
                storageDir      /* directory */
        );
        return image;
    }

    public static File transformImageForUpload(File img,int width, int height){
        // Get the dimensions of the View
        int targetW = width;
        int targetH = height;

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(img.getAbsolutePath(), bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(img.getAbsolutePath(), bmOptions);

        bitmap = FileUtils.getCorrectedOrientationBitmap(bitmap,img.getAbsolutePath());

        OutputStream os;

        File file = null;
        try {
            file = FileUtils.createImageFile();

            os = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,os);
            os.flush();
            os.close();

            img.delete();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            //Log.d(TAG,"File not found : " + file.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
            //Log.d(TAG,e.getLocalizedMessage());
        }

        return file;
    }
}
