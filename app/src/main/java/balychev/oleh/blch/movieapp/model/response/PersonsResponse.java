package balychev.oleh.blch.movieapp.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import balychev.oleh.blch.movieapp.model.Person;


public class PersonsResponse  {

    @SerializedName("page")
    private int page;
    @SerializedName("results")
    private ArrayList<Person> results;
    @SerializedName("total_results")
    private int totalResults;
    @SerializedName("total_pages")
    private int totalPages;


    public int getPage() {
        return page;
    }

    public ArrayList<Person> getResults() {
        return results;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

}
