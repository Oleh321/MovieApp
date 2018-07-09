package balychev.oleh.blch.movieapp.model;

import com.google.gson.annotations.SerializedName;


public class Crew {

    @SerializedName("credit_id")
    private String creditId;
    @SerializedName("department")
    private String department;
    @SerializedName("id")
    private int id;
    @SerializedName("job")
    private String job;
    @SerializedName("name")
    private String name;
    @SerializedName("profile_path")
    private String profilePath;

    public Crew(String creditId, String department, int id, String job, String name, String profilePath) {
        this.creditId = creditId;
        this.department = department;
        this.id = id;
        this.job = job;
        this.name = name;
        this.profilePath = profilePath;
    }

    @Override
    public String toString() {
        return "Crew{" +
                "job='" + job + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public String getJob() {
        return job;
    }

    public String getName() {
        return name;
    }
}
