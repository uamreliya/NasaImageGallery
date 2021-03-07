package udit.android.nasaimagegallery.GalleryView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import udit.android.nasaimagegallery.Adapter.GalleryAdapter;
import udit.android.nasaimagegallery.FullScreenImage.FullScreenFragment;
import udit.android.nasaimagegallery.Model.Data;
import udit.android.nasaimagegallery.R;
import udit.android.nasaimagegallery.Util.NetworkState;

public class MainActivity extends AppCompatActivity implements MainActivityViewInterface {

    MainActivityPresenter mainActivityPresenter;
    static RecyclerView rvGalleryThumbnails;
    NetworkState networkState;
    static LinearLayout networkErrorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        init();

    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    private void init() {

        rvGalleryThumbnails = findViewById(R.id.rv_gallery_thumbnails);
        networkErrorLayout = findViewById(R.id.network_error_layout);

        networkState = new NetworkState();

        registerReceiver(networkState, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        mainActivityPresenter = new MainActivityPresenter(this, this);

        mainActivityPresenter.getJSONData();

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        rvGalleryThumbnails.setLayoutManager(layoutManager);
        rvGalleryThumbnails.setItemAnimator(new DefaultItemAnimator());
    }

    public static void networkError(boolean val) {
        if (val) {
            networkErrorLayout.setVisibility(View.GONE);
            rvGalleryThumbnails.setVisibility(View.VISIBLE);
        } else {
            networkErrorLayout.setVisibility(View.VISIBLE);
            rvGalleryThumbnails.setVisibility(View.GONE);
        }
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void setJSONData(List<Data> data) {

        GalleryAdapter galleryAdapter = new GalleryAdapter(data);
        rvGalleryThumbnails.setAdapter(galleryAdapter);

        rvGalleryThumbnails.addOnItemTouchListener(new GalleryAdapter.RecyclerTouchListener(getApplicationContext(), rvGalleryThumbnails, new GalleryAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("images", (Serializable) data);
                bundle.putInt("position", position);

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                FullScreenFragment newFragment = FullScreenFragment.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "slideshow");
            }
        }));
    }

    @Override
    public void readJSONFileError(String error) {
        Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkState);
    }
}