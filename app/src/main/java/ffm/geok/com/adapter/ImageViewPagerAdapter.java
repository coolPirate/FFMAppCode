package ffm.geok.com.adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.io.File;
import java.util.List;

import ffm.geok.com.R;
import ffm.geok.com.uitls.GlideImageLoader;
import uk.co.senab.photoview.PhotoView;

public class ImageViewPagerAdapter extends PagerAdapter {
    List<File> imgs;
    Context mContext;

    public ImageViewPagerAdapter(Context mContext, List<File> imgs) {
        this.imgs = imgs;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return imgs.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        File imgUrl = imgs.get(position);
        LinearLayout view = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.img_browse, null);
        PhotoView img = (PhotoView) view.findViewById(R.id.img_plan);
        GlideImageLoader.getInstance().displayItemImage(mContext, imgUrl, img);
        ((ViewPager) container).addView(view);
        return view;

    }
}
