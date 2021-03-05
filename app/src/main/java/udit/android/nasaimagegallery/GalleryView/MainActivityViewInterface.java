package udit.android.nasaimagegallery.GalleryView;

import java.util.List;

import udit.android.nasaimagegallery.Model.Data;

public interface MainActivityViewInterface {

    void setJSONData(List<Data> data);

    void readJSONFileError(String error);
}
