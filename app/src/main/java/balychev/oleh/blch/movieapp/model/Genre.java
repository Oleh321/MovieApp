package balychev.oleh.blch.movieapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Genre implements Parcelable {

   @SerializedName("id")
   private Integer id;
   @SerializedName("name")
   private String name;

   public Genre(Integer id) {
       this.id = id;
   }

   public Genre(Integer id, String name) {
       this.id = id;
       this.name = name;
   }

   public Genre(Parcel in) {
       this.id = (Integer) in.readValue(Integer.class.getClassLoader());
       this.name = in.readString();
   }

   @Override
   public void writeToParcel(Parcel dest, int flags) {
       dest.writeValue(this.id);
       dest.writeString(this.name);
   }

   @Override
   public int describeContents() {
       return 0;
   }

   public static final Creator<Genre> CREATOR = new Creator<Genre>() {
       @Override
       public Genre createFromParcel(Parcel in) {
           return new Genre(in);
       }

       @Override
       public Genre[] newArray(int size) {
           return new Genre[size];
       }
   };

   @Override
   public boolean equals(Object o) {
       if (this == o) return true;
       if (o == null || getClass() != o.getClass()) return false;
       Genre genre = (Genre) o;
       return Objects.equals(id, genre.id);
   }

   @Override
   public int hashCode() {

       return Objects.hash(id);
   }

    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }

    @Override
   public String toString() {
       return "Genre{" +
               "id=" + id +
               ", name='" + name + '\'' +
               '}';
   }
}
