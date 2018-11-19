package ffm.geok.com.uitls;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.util.Base64;
import android.view.View;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtils {

    //压缩阀值 300kb
    private static final int COMPRESS_KB = 300;

    /**
     * 质量压缩方法,
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > COMPRESS_KB) { // 循环判断如果压缩后图片是否大于300kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * @param imgPath
     * @return
     */
    public static String imgToBase64(String imgPath) {
        Bitmap bitmap = null;
        if (imgPath != null && imgPath.length() > 0) {
            bitmap = readBitmap(imgPath);
        }
        if (bitmap == null) {
            //bitmap not found!!
        }
        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);

            out.flush();
            out.close();

            byte[] imgBytes = out.toByteArray();
            return Base64.encodeToString(imgBytes, Base64.DEFAULT);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return null;
        } finally {
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static String getImgStr(String imgFile) {
        //将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        InputStream in = null;
        byte[] data = null;
        //读取图片字节数组
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(Base64.encodeToString(data, Base64.DEFAULT));
    }

    public static Bitmap readBitmap(String imgPath) {
        try {
            return BitmapFactory.decodeFile(imgPath);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return null;
        }

    }


    // 将bitmap转成byte数组
    public static byte[] getBitmapByte(Bitmap bitmap) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }

    public static byte[] bitmap2Bytes(Bitmap bm) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] buffer = baos.toByteArray();
            return buffer;
        }catch (Exception es){}
        return  null;
    }
    public static Bitmap drawRoundBitmap(Bitmap bitmap, boolean drawFrame,
                                         final int frameColor, final float frameWidth) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff000000;
        final int width = bitmap.getWidth();
        final int height = bitmap.getHeight();
        final Rect rect = new Rect(0, 0, width, height);
        final float radius = ((float) Math.min(width, height)) / 2;
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(width / 2, height / 2, radius, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        if (drawFrame) {
            paint.reset();
            paint.setColor(frameColor);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(frameWidth);
            paint.setAntiAlias(true);
            canvas.drawCircle(width / 2, height / 2, radius - frameWidth / 2,
                    paint);
        }

        return output;
    }

    public static Bitmap compressImage(String srcPath, int maxWidth, int maxHeight) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(srcPath, opts);
        opts.inJustDecodeBounds = false;

        int srcWidth = opts.outWidth;
        int srcHeight = opts.outHeight;
        int dstWidth = maxWidth;
        int dstHeight = maxHeight;
        if (srcWidth <= srcHeight && dstWidth > dstHeight
                || srcWidth >= srcHeight && dstWidth < dstHeight) {
            dstWidth = opts.outHeight;
            dstHeight = opts.outWidth;
        }
        int sampleSize = 1;

        if (srcWidth > srcHeight && srcWidth > dstWidth) {
            sampleSize = (int) (opts.outWidth / dstWidth);
        } else if (srcWidth < srcHeight && srcHeight > dstHeight) {
            sampleSize = (int) (opts.outHeight / dstHeight);
        }
        if (sampleSize <= 0) {
            sampleSize = 1;
        }
        opts.inSampleSize = sampleSize;

        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        opts.inPurgeable = true;
        opts.inInputShareable = true;

        Bitmap dstBitmap = BitmapFactory.decodeFile(srcPath, opts);
        return compressImage(dstBitmap, maxWidth, maxHeight, true);
    }

    public static Bitmap compressImage(Bitmap srcBitmap, int maxWidth, int maxHeight, boolean recycle) {
        int srcWidth = srcBitmap.getWidth();
        int srcHeight = srcBitmap.getHeight();
        int dstWidth = maxWidth;
        int dstHeight = maxHeight;
        if (srcWidth <= srcHeight && dstWidth > dstHeight
                || srcWidth >= srcHeight && dstWidth < dstHeight) {
            dstWidth = maxHeight;
            dstHeight = maxWidth;
        }
        float wRatio = (float) (((float) dstWidth) / (float) srcWidth);
        float hRatio = (float) ((float) dstHeight / (float) srcHeight);
        float ratio = Float.compare(wRatio, hRatio) <= 0 ? wRatio : hRatio;
        if (Float.compare(ratio, 1.0f) >= 0) {
            return srcBitmap;
        }
        dstWidth = (int) (srcBitmap.getWidth() * ratio);
        dstHeight = (int) (srcBitmap.getHeight() * ratio);
        try {
            Bitmap dstBitmap = Bitmap.createBitmap(dstWidth, dstHeight,
                    Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(dstBitmap);
            RectF rect = new RectF(0, 0, dstWidth, dstHeight);
            canvas.drawBitmap(srcBitmap, null, rect, null);
            if (recycle) {
                srcBitmap.recycle();
                srcBitmap = null;
            }
            canvas = null;
            return dstBitmap;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }

        return srcBitmap;
    }

    public static Bitmap transform(Matrix scaler, Bitmap source, int targetWidth, int targetHeight, boolean scaleUp, boolean recycle) {
        int deltaX = source.getWidth() - targetWidth;
        int deltaY = source.getHeight() - targetHeight;
        if (!scaleUp && (deltaX < 0 || deltaY < 0)) {
            /*
             * In this case the bitmap is smaller, at least in one dimension,
             * than the target. Transform it by placing as much of the image as
             * possible into the target and leaving the top/bottom or left/right
             * (or both) black.
             */
            Bitmap b2 = Bitmap.createBitmap(targetWidth, targetHeight,
                    Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b2);

            int deltaXHalf = Math.max(0, deltaX / 2);
            int deltaYHalf = Math.max(0, deltaY / 2);
            Rect src = new Rect(deltaXHalf, deltaYHalf, deltaXHalf
                    + Math.min(targetWidth, source.getWidth()), deltaYHalf
                    + Math.min(targetHeight, source.getHeight()));
            int dstX = (targetWidth - src.width()) / 2;
            int dstY = (targetHeight - src.height()) / 2;
            Rect dst = new Rect(dstX, dstY, targetWidth - dstX, targetHeight
                    - dstY);
            c.drawBitmap(source, src, dst, null);
            if (recycle) {
                source.recycle();
            }
            return b2;
        }
        float bitmapWidthF = source.getWidth();
        float bitmapHeightF = source.getHeight();

        float bitmapAspect = bitmapWidthF / bitmapHeightF;
        float viewAspect = (float) targetWidth / targetHeight;

        if (bitmapAspect > viewAspect) {
            float scale = targetHeight / bitmapHeightF;
            if (scale < .9F || scale > 1F) {
                scaler.setScale(scale, scale);
            } else {
                scaler = null;
            }
        } else {
            float scale = targetWidth / bitmapWidthF;
            if (scale < .9F || scale > 1F) {
                scaler.setScale(scale, scale);
            } else {
                scaler = null;
            }
        }

        Bitmap b1;
        if (scaler != null) {
            b1 = Bitmap.createBitmap(source, 0, 0, source.getWidth(),
                    source.getHeight(), scaler, true);
        } else {
            b1 = source;
        }

        if (recycle && b1 != source) {
            source.recycle();
        }

        int dx1 = Math.max(0, b1.getWidth() - targetWidth);
        int dy1 = Math.max(0, b1.getHeight() - targetHeight);

        Bitmap b2 = Bitmap.createBitmap(b1, dx1 / 2, dy1 / 2, targetWidth,
                targetHeight);

        if (b2 != b1) {
            if (recycle || b1 != source) {
                b1.recycle();
            }
        }

        return b2;
    }

    public static Bitmap zoom(Bitmap srcBitmap, int newHeight, int newWidth,boolean recycle) {
        int srcWidth = srcBitmap.getWidth();
        int srcHeight = srcBitmap.getHeight();

        float scaleWidth = ((float) newWidth) / srcWidth;
        float scaleHeight = ((float) newHeight) / srcHeight;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcWidth,
                srcHeight, matrix, true);
        if (resizedBitmap != null) {
            srcBitmap.recycle();
            return resizedBitmap;
        } else {
            return srcBitmap;
        }
    }

    public static Bitmap scaleBitmap(Bitmap srcBitmap, int maxWidth,int maxHeight, boolean recycle) {
        int srcWidth = srcBitmap.getWidth();
        int srcHeight = srcBitmap.getHeight();
        int dstWidth = maxWidth;
        int dstHeight = maxHeight;
        if (srcWidth <= srcHeight && dstWidth > dstHeight
                || srcWidth >= srcHeight && dstWidth < dstHeight) {
            dstWidth = maxHeight;
            dstHeight = maxWidth;
        }
        float wRatio = (float) (((float) dstWidth) / (float) srcWidth);
        float hRatio = (float) ((float) dstHeight / (float) srcHeight);
        float ratio = Float.compare(wRatio, hRatio) <= 0 ? wRatio : hRatio;
        return zoom(srcBitmap, (int) (srcWidth * ratio),
                (int) (srcHeight * ratio), recycle);
    }

    public static boolean writeToCacheJPEG(Bitmap bitmap, String path) {
        boolean rtn = false;
        if (bitmap != null && path != null) {
            rtn = writeToCacheJPEG(bitmap, path, 100);
        }
        return rtn;
    }

    public static boolean writeToCacheJPEG(Bitmap bitmap, String path,int quality) {
        boolean rtn = false;
        if (bitmap != null && path != null) {
            try {
                File file = new File(path);
                rtn = true;
                String parent = file.getParent();
                File parentFile = new File(parent);
                if (!parentFile.exists()) {
                    rtn = parentFile.mkdirs();
                }
                if (rtn) {
                    rtn = file.createNewFile();
                    if (rtn) {
                        final FileOutputStream fos = new FileOutputStream(file);
                        final BufferedOutputStream bos = new BufferedOutputStream(
                                fos, 16384);
                        rtn = bitmap.compress(Bitmap.CompressFormat.JPEG,
                                quality, bos);
                        bos.flush();
                        bos.close();
                        fos.close();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return rtn;
    }

    /**
     * Bitmap缩小的方法
     *
     * @param bitmap
     * @param ratio  缩小比例
     * @return
     */
    public static Bitmap smallBitmap(Bitmap bitmap, float ratio) {
        Matrix matrix = new Matrix();
        matrix.postScale(ratio, ratio); // 长和宽放大缩小的比例

        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
        return resizeBmp;
    }

    /**
     * 将指定路径图片转换为bitmap
     *
     * @param filePath
     * @return
     */
    public static Bitmap decodeBitmap(String filePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
        if (bitmap == null) {//bitmap为空

        }
        float realWidth = options.outWidth;
        float realHeight = options.outHeight;
        int scale = (int) ((realHeight > realWidth ? realHeight : realWidth) / 240);
        if (scale <= 0) {
            scale = 1;
        }
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(filePath, options);
        return bitmap;
    }

    /**
     * 将bitmap保存到本地
     *
     * @param bm       数据源
     * @param fileName 保存的文件名
     * @param path     保存的路劲
     * @return 保存后文件对象
     * @throws IOException
     */
    public static File saveBitmapFile(Bitmap bm, String fileName, String path) throws IOException {
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        File foder = null;
        if (sdCardExist) {
            foder = new File(Environment.getExternalStorageDirectory(), path);
        } else {
            foder = new File(Environment.getDownloadCacheDirectory(), path);
        }
        if (!foder.exists()) {
            foder.mkdirs();
        }
        File saveFile = new File(foder, fileName);
        if (!saveFile.exists()) {
            saveFile.createNewFile();
        }
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(saveFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();
        return saveFile;
    }

    /**
     * 获取固定高度的bitmap对象
     * @param srcPath
     * @return
     */
    public static Bitmap getimage(String srcPath) {
        try {
            BitmapFactory.Options newOpts = new BitmapFactory.Options();
            // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
            newOpts.inJustDecodeBounds = true;
            Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

            newOpts.inJustDecodeBounds = false;
            int w = newOpts.outWidth;
            int h = newOpts.outHeight;
            // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
            float hh = 960f;// 这里设置高度为960f
            float ww = 640f;// 这里设置宽度为640f
            // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
            int be = 1;// be=1表示不缩放
            if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
                be = (int) (newOpts.outWidth / ww);
            } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
                be = (int) (newOpts.outHeight / hh);
            }
            if (be <= 0)
                be = 1;
            newOpts.inSampleSize = be;// 设置缩放比例
            // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
            bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
            return compressImage(bitmap);
        }catch (Exception es){}
        return null;
    }

    /**
     * View转换为Bitmap及getDrawingCache=null的解决方法
     * @param view  目标视图
     * @param bitmapWidth   视图宽
     * @param bitmapHeight  视图高
     * @return
     */
    public static Bitmap convertViewToBitmap(View view, int bitmapWidth, int bitmapHeight){
        Bitmap bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
        view.draw(new Canvas(bitmap));

        return bitmap;
    }

    /**
     * 绘制白色背景
     * @param color 背景色
     * @param orginBitmap   原始bitmap
     * @return
     */
    public static Bitmap drawBg4Bitmap(int color, Bitmap orginBitmap) {
        Paint paint = new Paint();
        paint.setColor(color);
        Bitmap bitmap = Bitmap.createBitmap(orginBitmap.getWidth(),
                orginBitmap.getHeight(), orginBitmap.getConfig());
        Canvas canvas = new Canvas(bitmap);
        canvas.drawRect(0, 0, orginBitmap.getWidth(), orginBitmap.getHeight(), paint);
        canvas.drawBitmap(orginBitmap, 0, 0, paint);
        return bitmap;
    }

    /*获取缩略图*/
    public static Bitmap getThumbnail(String absolutePath,int width,int height) {
        //得到原图片
        Bitmap tempBitmap = readBitmap(absolutePath);
        //得到缩略图
        Bitmap resultBitmap = ThumbnailUtils.extractThumbnail(tempBitmap, width, height);
        return resultBitmap;
    }
}
