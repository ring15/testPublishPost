package com.founq.sdk.testpublishpost;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 图片工具类
 */
public class BitmapUtils {
    private static final int BITMAP_COMPRESS_QUALITY = 70;

    public static String compressBitmap(Context context, Uri uri) {
        String cachePaths = null;
        try {
            Bitmap bitmap = createImageThumbnail(context, uri);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, BITMAP_COMPRESS_QUALITY, baos);
            File temp = File.createTempFile("tmp", ".jpg", context.getCacheDir());
            cachePaths = temp.getPath();
            Log.d("BitmapUtils", "保存到临时文件夹" + cachePaths);
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(temp));
            baos.writeTo(bos);
            bos.flush();
            bos.close();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cachePaths;
    }


    public static String compressBitmap(Context context, Uri uri, String imgName) {
        String cachePaths = null;
        try {
            Bitmap bitmap = createImageThumbnail(context, uri);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, BITMAP_COMPRESS_QUALITY, baos);
            File temp = new File(context.getCacheDir(), imgName + ".jpg");
            cachePaths = temp.getPath();
            Log.d("BitmapUtils", "保存到临时文件夹" + cachePaths);
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(temp));
            baos.writeTo(bos);
            bos.flush();
            bos.close();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cachePaths;
    }

    public static Bitmap createImageThumbnail(Context context, Uri uri) throws FileNotFoundException {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        opts.inSampleSize = computeSampleSize(opts, -1, 5000 * 5000);
        opts.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, opts);
    }

    /*
     * Compute the sample size as a function of minSideLength
     * and maxNumOfPixels.
     * minSideLength is used to specify that minimal width or height of a
     * bitmap.
     * maxNumOfPixels is used to specify the maximal size in pixels that is
     * tolerable in terms of memory usage.
     *
     * The function returns a sample size based on the constraints.
     * Both size and minSideLength can be passed in as IImage.UNCONSTRAINED,
     * which indicates no care of the corresponding constraint.
     * The functions prefers returning a sample size that
     * generates a smaller bitmap, unless minSideLength = IImage.UNCONSTRAINED.
     *
     * Also, the function rounds up the sample size to a power of 2 or multiple
     * of 8 because BitmapFactory only honors sample size this way.
     * For example, BitmapFactory downsamples an image by 2 even though the
     * request is 3. So we round up the sample size to avoid OOM.
     */
    public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));
        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }
}
