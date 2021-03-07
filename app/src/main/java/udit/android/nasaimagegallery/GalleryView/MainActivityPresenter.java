package udit.android.nasaimagegallery.GalleryView;

import android.app.Activity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import udit.android.nasaimagegallery.Model.Data;

public class MainActivityPresenter implements MainActivityPresenterInterface {

    Activity activity;
    MainActivityViewInterface mainActivityViewInterface;

    public MainActivityPresenter(Activity activity, MainActivityViewInterface mainActivityViewInterface) {
        this.activity = activity;
        this.mainActivityViewInterface = mainActivityViewInterface;
    }

    @Override
    public void getJSONData() {

        String jsonFile = null;
        try {
            InputStream inputStream = activity.getAssets().open("data.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            jsonFile = new String(buffer, "UTF-8");
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            List<Data> imageDataList = gson.fromJson(jsonFile, new TypeToken<List<Data>>() {
            }.getType());

            Collections.sort(imageDataList, new Comparator<Data>() {

                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                @Override
                public int compare(Data data, Data t1) {
                    try {
                        return dateFormat.parse(data.getDate()).compareTo(dateFormat.parse(t1.getDate()));
                    } catch (ParseException e) {
                        throw new IllegalArgumentException(e);
                    }
                }
            });
            Collections.reverse(imageDataList);

            mainActivityViewInterface.setJSONData(imageDataList);
        } catch (IOException e) {
            e.printStackTrace();
            mainActivityViewInterface.readJSONFileError(e.getLocalizedMessage());
        }

    }
}
