package udit.android.nasaimagegallery.FullScreenImage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.List;

import udit.android.nasaimagegallery.Model.Data;
import udit.android.nasaimagegallery.R;

public class MyViewPagerAdapter extends PagerAdapter {

    private LayoutInflater layoutInflater;
    private Context context;
    private List<Data> dataList;

    public MyViewPagerAdapter(Context context, List<Data> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.fullscreen_preview, container, false);

        ImageView imageViewFullScreen = view.findViewById(R.id.iv_fullscreen);

        Glide.with(context).load(dataList.get(position).getHdurl())
                .thumbnail(0.5f)
                .transition(new DrawableTransitionOptions().crossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageViewFullScreen);

        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((View) object);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
