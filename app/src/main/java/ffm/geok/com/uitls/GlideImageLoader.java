package ffm.geok.com.uitls;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lzy.imagepicker.loader.ImageLoader;

import java.io.File;

public class GlideImageLoader implements ImageLoader {
    private static GlideImageLoader imageLoaderUtil;
    public static GlideImageLoader getInstance() {
        if (null == imageLoaderUtil) {
            synchronized (GlideImageLoader.class) {
                if (null == imageLoaderUtil) {
                    imageLoaderUtil = new GlideImageLoader();
                }
            }
        }
        return imageLoaderUtil;
    }

    public void displayItemImage(Context mContext, File file, ImageView img) {
        Glide.with(mContext).load(file).into(img);
    }

    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
        Glide.with(activity).load(path).into(imageView);
    }

    @Override
    public void displayImagePreview(Activity activity, String path, ImageView imageView, int width, int height) {
        Glide.with(activity).load(path).into(imageView);
    }

    @Override
    public void clearMemoryCache() {

    }
}
