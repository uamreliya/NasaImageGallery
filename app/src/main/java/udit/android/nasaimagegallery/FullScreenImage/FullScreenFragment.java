package udit.android.nasaimagegallery.FullScreenImage;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import udit.android.nasaimagegallery.Model.Data;
import udit.android.nasaimagegallery.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FullScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FullScreenFragment extends DialogFragment {

    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.pagerCount)
    TextView pagerCount;
    private List<Data> dataList;
    private MyViewPagerAdapter myViewPagerAdapter;
    private int selectedImage = 0;

    public FullScreenFragment() {
        // Required empty public constructor
    }

    public static FullScreenFragment newInstance() {
        FullScreenFragment fragment = new FullScreenFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_full_screen, container, false);
        ButterKnife.bind(this, view);


        selectedImage = getArguments().getInt("position");
        dataList = (List<Data>) getArguments().getSerializable("images");

        myViewPagerAdapter = new MyViewPagerAdapter(getActivity(), dataList);
        viewpager.setAdapter(myViewPagerAdapter);
        viewpager.addOnPageChangeListener(viewPagerChangeListener);

        setCurrentPosition(selectedImage);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme);
    }

    private void setCurrentPosition(int selectedImage) {
        viewpager.setCurrentItem(selectedImage, false);
        setImageData(selectedImage);
    }

    private void setImageData(int selectedImage) {
        pagerCount.setText((selectedImage + 1) + " of " + dataList.size());
    }

    ViewPager.OnPageChangeListener viewPagerChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            setImageData(position);
        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


}