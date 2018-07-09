package balychev.oleh.blch.movieapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import balychev.oleh.blch.movieapp.model.Movie;

public class MovieDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movieapp.db";
    private static final int DATABASE_VERSION = 2;

    private SQLiteOpenHelper mDbHelper;
    private SQLiteDatabase db;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void open(){
        db = mDbHelper.getWritableDatabase();
    }

    public void close(){
        mDbHelper.close();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabaseDatabase) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + DataName.TABLE_MOVIE + " (" +
                DataName.COLUMN_MOVIE_ID + " INTEGER PRIMARY KEY," +
                DataName.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                DataName.COLUMN_MOVIE_POSTER_PATH + " TEXT, " +
                DataName.COLUMN_MOVIE_RELEASE_DATE + " TEXT, " +
                DataName.COLUMN_MOVIE_VOTE_AVERAGE + " REAL, " +
                DataName.COLUMN_MOVIE_OVERVIEW  + " TEXT, " +
                DataName.COLUMN_MOVIE_DIRECTOR  + " TEXT " +
                "); ";
 
        final String SQL_CREATE_GENRE_MOVIE_TABLE = "CREATE TABLE " + DataName.TABLE_GENRE_MOVIE + " (" +
                DataName.COLUMN_GENRE_ID + " INTEGER," +
                DataName.COLUMN_MOVIE_ID + " INTEGER " +
                "); ";

        sqLiteDatabaseDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
        sqLiteDatabaseDatabase.execSQL(SQL_CREATE_GENRE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabaseDatabase, int oldVersion, int newVersion) {
        sqLiteDatabaseDatabase.execSQL("DROP TABLE IF EXISTS " + DataName.TABLE_MOVIE );
        sqLiteDatabaseDatabase.execSQL("DROP TABLE IF EXISTS " + DataName.TABLE_GENRE_MOVIE);
        onCreate(sqLiteDatabaseDatabase);
    }

    public void addMovie(Movie movie){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues movieValues = new ContentValues();
        movieValues.put(DataName.COLUMN_MOVIE_ID, movie.getId());
        movieValues.put(DataName.COLUMN_MOVIE_TITLE, movie.getTitle());
        movieValues.put(DataName.COLUMN_MOVIE_DIRECTOR, movie.getDirector());
        movieValues.put(DataName.COLUMN_MOVIE_POSTER_PATH, movie.getPosterPath());
        movieValues.put(DataName.COLUMN_MOVIE_OVERVIEW, movie.getOverview());
        movieValues.put(DataName.COLUMN_MOVIE_RELEASE_DATE, movie.getReleaseDate());
        movieValues.put(DataName.COLUMN_MOVIE_VOTE_AVERAGE, movie.getVoteAverage());

        db.insert(DataName.TABLE_MOVIE, null, movieValues);

        ContentValues genreValues = new ContentValues();
        for (Integer id : movie.getGenreIds()){
            genreValues.put(DataName.COLUMN_GENRE_ID, id);
            genreValues.put(DataName.COLUMN_MOVIE_ID, movie.getId());
            db.insert(DataName.TABLE_GENRE_MOVIE, null, genreValues);
        }

        db.close();
    }

    public ArrayList<Movie> getAllMovies(){
        String[] movieColumns = {
                DataName.COLUMN_MOVIE_ID,
                DataName.COLUMN_MOVIE_TITLE,
                DataName.COLUMN_MOVIE_DIRECTOR,
                DataName.COLUMN_MOVIE_POSTER_PATH,
                DataName.COLUMN_MOVIE_OVERVIEW,
                DataName.COLUMN_MOVIE_RELEASE_DATE,
                DataName.COLUMN_MOVIE_VOTE_AVERAGE
        };

        String[] movieGenreColumns = {
                DataName.COLUMN_GENRE_ID,
                DataName.COLUMN_MOVIE_ID
        };

        ArrayList<Movie> movies = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor movieCursor = db.query(DataName.TABLE_MOVIE, movieColumns, null, null,
                null, null, null);

        String selection =  DataName.COLUMN_MOVIE_ID + " = ?";


        if (movieCursor.moveToFirst()){
            do {
                Movie movie = new Movie();
                movie.setId(Integer.parseInt(movieCursor.getString(movieCursor.getColumnIndex(DataName.COLUMN_MOVIE_ID))));
                movie.setTitle(movieCursor.getString(movieCursor.getColumnIndex(DataName.COLUMN_MOVIE_TITLE)));
                movie.setDirector(movieCursor.getString(movieCursor.getColumnIndex(DataName.COLUMN_MOVIE_DIRECTOR)));
                movie.setOverview(movieCursor.getString(movieCursor.getColumnIndex(DataName.COLUMN_MOVIE_OVERVIEW)));
                movie.setPosterPath(movieCursor.getString(movieCursor.getColumnIndex(DataName.COLUMN_MOVIE_POSTER_PATH)));
                movie.setReleaseDate(movieCursor.getString(movieCursor.getColumnIndex(DataName.COLUMN_MOVIE_RELEASE_DATE)));
                movie.setVoteAverage(Double.parseDouble(movieCursor.getString(movieCursor.getColumnIndex(DataName.COLUMN_MOVIE_VOTE_AVERAGE))));

                String[] selectionArgs = {String.valueOf(movie.getId())};

                Cursor genreMovieCursor = db.query(DataName.TABLE_GENRE_MOVIE, movieGenreColumns, selection,
                        selectionArgs, null, null, null);

                ArrayList<Integer> genreIds = new ArrayList<>();
                if (genreMovieCursor.moveToFirst()){
                    do{
                        genreIds.add(Integer.parseInt(genreMovieCursor.getString(genreMovieCursor.getColumnIndex(DataName.COLUMN_GENRE_ID))));
                    } while (genreMovieCursor.moveToNext());
                }
                movie.setGenreIds(genreIds);
                genreMovieCursor.close();

                movies.add(movie);

            }while (movieCursor.moveToNext());
        }

        movieCursor.close();
        db.close();

        return movies;
    }

    public Movie getMovieById(int id){
        String[] movieColumns = {
                DataName.COLUMN_MOVIE_ID,
                DataName.COLUMN_MOVIE_TITLE,
                DataName.COLUMN_MOVIE_DIRECTOR,
                DataName.COLUMN_MOVIE_POSTER_PATH,
                DataName.COLUMN_MOVIE_OVERVIEW,
                DataName.COLUMN_MOVIE_RELEASE_DATE,
                DataName.COLUMN_MOVIE_VOTE_AVERAGE
        };

        String[] movieGenreColumns = {
                DataName.COLUMN_GENRE_ID,
                DataName.COLUMN_MOVIE_ID
        };

        SQLiteDatabase db = this.getReadableDatabase();

        String selection = DataName.COLUMN_MOVIE_ID+ " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        Cursor movieCursor = db.query(DataName.TABLE_MOVIE, movieColumns, selection,  selectionArgs,
                null, null, null);

        String selectionGenre =  DataName.TABLE_MOVIE + "." + DataName.COLUMN_MOVIE_ID + " = ?";


        if (movieCursor.moveToFirst()){
                Movie movie = new Movie();
                movie.setId(Integer.parseInt(movieCursor.getString(movieCursor.getColumnIndex(DataName.COLUMN_MOVIE_ID))));
                movie.setDirector(movieCursor.getString(movieCursor.getColumnIndex(DataName.COLUMN_MOVIE_TITLE)));
                movie.setOverview(movieCursor.getString(movieCursor.getColumnIndex(DataName.COLUMN_MOVIE_OVERVIEW)));
                movie.setPosterPath(movieCursor.getString(movieCursor.getColumnIndex(DataName.COLUMN_MOVIE_POSTER_PATH)));
                movie.setReleaseDate(movieCursor.getString(movieCursor.getColumnIndex(DataName.COLUMN_MOVIE_RELEASE_DATE)));
                movie.setVoteAverage(Double.parseDouble(movieCursor.getString(movieCursor.getColumnIndex(DataName.COLUMN_MOVIE_VOTE_AVERAGE))));

                String[] selectionGenreArgs = {String.valueOf(movie.getId())};

                Cursor genreMovieCursor = db.query(DataName.TABLE_GENRE_MOVIE, movieGenreColumns, selection,
                        selectionArgs, null, null, null);

                ArrayList<Integer> genreIds = new ArrayList<>();
                if (genreMovieCursor.moveToFirst()){
                    do{
                        genreIds.add(Integer.parseInt(genreMovieCursor.getString(genreMovieCursor.getColumnIndex(DataName.COLUMN_GENRE_ID))));
                    } while (genreMovieCursor.moveToNext());
                }
                genreMovieCursor.close();
                movie.setGenreIds(genreIds);

                return movie;

        }

        movieCursor.close();
        db.close();

        return null;
    }

    public void deleteMovie(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DataName.TABLE_GENRE_MOVIE, DataName.COLUMN_MOVIE_ID+ "=" + id, null);
        db.delete(DataName.TABLE_MOVIE, DataName.COLUMN_MOVIE_ID+ "=" + id, null);
        db.close();
    }
}
