package com.huangyezhaobiao.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.util.Log;

import com.huangyezhaobiao.constans.AppConstants;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class UploadPicUtil {

    public static final int MAX_IMAGE_SIZE = AppConstants.UPLOAD_PIC_MAX_SIZE; // k

    public static boolean needRotate(String filePath) {
        try {
            ExifInterface exif = new ExifInterface(filePath);
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return true;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return true;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return true;
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void rotatedBitmap(Context context, String filePath, String dstFilePath) throws IOException {
        File file = new File(dstFilePath);
        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        ExifInterface exif = new ExifInterface(filePath);
        int orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);
        Matrix matrix = null;
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix = new Matrix();
                matrix.postRotate(90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix = new Matrix();
                matrix.postRotate(270);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix = new Matrix();
                matrix.postRotate(180);
                break;
        }
        Bitmap bmp = getSmallBitmap(context,
                Uri.fromFile(new File(filePath)));
        if (matrix != null) {
            Bitmap bmp2 = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
                    bmp.getHeight(), matrix, true);
            bmp.recycle();
            FileOutputStream fos = new FileOutputStream(dstFilePath);
            bmp2.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
            bmp2.recycle();
        } else {
            FileOutputStream fos = new FileOutputStream(dstFilePath);
            FileInputStream fis = new FileInputStream(filePath);
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            fos.close();
            fis.close();
        }
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 :
                (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 :
                (int) Math.min(Math.floor(w / minSideLength),
                        Math.floor(h / minSideLength));
        if (upperBound < lowerBound) {
            return lowerBound;
        }
        if ((maxNumOfPixels == -1) &&
                (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int initialSize = computeInitialSampleSize(options, reqWidth, reqHeight);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 2;
        }
        return roundedSize;

    }

    public static Bitmap getSmallBitmap(Context context, Uri uri) {
        return getBitmap(context, uri, -1, 128 * 128);
    }

    public static Bitmap getBitmap(Context context, Uri uri, int maxWidth,
                                   int maxHeight) {
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            InputStream is = context.getContentResolver().openInputStream(uri);
            BitmapFactory.decodeStream(is, null, options);
            is.close();

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;

            is = context.getContentResolver().openInputStream(uri);
            Bitmap bmp = BitmapFactory.decodeStream(is, null, options);
            is.close();
            return bmp;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void compressImage(Bitmap image, String dstFilePath) {
        if (image == null) {
            return;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 100;
        while (baos.toByteArray().length / 1024 > MAX_IMAGE_SIZE) {
            baos.reset();
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
            options -= 10;
        }
        image.recycle();
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(dstFilePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, fos);
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmap.recycle();
    }

    public static void compressImage(String srcPath, String dstFilePath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(srcPath, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = 800f;
        float ww = 480f;
        int be = 1;
        if (w > h && w > ww) {
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        Log.e("shenzhixinPic", "bitmap:" + (bitmap == null) + ",path:" + srcPath);
        compressImage(bitmap, dstFilePath);
    }
}
