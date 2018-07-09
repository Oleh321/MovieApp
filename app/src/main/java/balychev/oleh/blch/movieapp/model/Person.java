package balychev.oleh.blch.movieapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Person {

    @SerializedName("id")
    private int id;
    @SerializedName("profile_path")
    private String profilePath;
    @SerializedName("name")
    private String name;




    public int getId() {
        return id;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public String getName() {
        return name;
    }




}
