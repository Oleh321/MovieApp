package balychev.oleh.blch.movieapp.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import balychev.oleh.blch.movieapp.model.Crew;


public class CrewResponse {
    @SerializedName("id")
    private int id;
    @SerializedName("crew")
    private List<Crew> crew;

    public List<Crew> getCrew() {
        return crew;
    }
}
