package udit.android.nasaimagegallery.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

import udit.android.nasaimagegallery.Model.Data;
import udit.android.nasaimagegallery.R;
import udit.android.nasaimagegallery.Util.OnSwipeListener;

public class MyViewPagerAdapter extends PagerAdapter {

    private LayoutInflater layoutInflater;
    private Context context;
    private List<Data> dataList;
    private BottomSheetDialog bottomSheetDialog;
    private TextView tvSwipeUp;
    private ImageView ivUpArrow;

    public MyViewPagerAdapter(Context context, List<Data> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @SuppressLint("ClickableViewAccessibility")
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.fullscreen_preview, container, false);

        ImageView imageViewFullScreen = view.findViewById(R.id.iv_fullscreen);
        tvSwipeUp = view.findViewById(R.id.tv_swipe_up);
        ivUpArrow = view.findViewById(R.id.iv_up_arrow);

        Glide.with(context).load(dataList.get(position).getHdurl())
                .thumbnail(0.5f)
                .transition(new DrawableTransitionOptions().crossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageViewFullScreen);

        imageViewFullScreen.setOnTouchListener(new OnSwipeListener(context) {
            @Override
            public void onSwipeTop() {
                super.onSwipeTop();
                setBottomSheetDialog(dataList.get(position));
            }
        });

        container.addView(view);
        return view;
    }

    public void setBottomSheetDialog(Data data) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_description_layout, null, false);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.SheetDialog);

        TextView tvTitle = view.findViewById(R.id.tv_title);
        TextView tvExplanation = view.findViewById(R.id.tv_explanation);
        TextView tvCopyright = view.findViewById(R.id.tv_copyright);

        tvTitle.setText(data.getTitle());
        tvExplanation.setText(data.getExplanation());
        if (data.getCopyright() != null)
            tvCopyright.setText(data.getCopyright());
        else
            tvCopyright.setVisibility(View.GONE);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.show();

        bottomSheetDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                bottomSheetDialog.dismiss();

            }
        });
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
        if (bottomSheetDialog != null && bottomSheetDialog.isShowing())
            bottomSheetDialog.dismiss();
    }
}
