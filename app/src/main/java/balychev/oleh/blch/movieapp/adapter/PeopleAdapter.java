package balychev.oleh.blch.movieapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import balychev.oleh.blch.movieapp.R;
import balychev.oleh.blch.movieapp.activity.MoviePagerActivity;
import balychev.oleh.blch.movieapp.activity.PersonActivity;
import balychev.oleh.blch.movieapp.data.PeopleLab;
import balychev.oleh.blch.movieapp.model.Person;

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.PersonViewHolder>{

    private Context mContext;
    private PeopleLab mPeopleLab;
    private ArrayList<Person> mPeople;

    public PeopleAdapter(Context context) {
        mContext = context;
        mPeopleLab = PeopleLab.get();
        mPeople = mPeopleLab.getPeople();
    }

    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_person_layout, parent, false);
        return new PersonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonViewHolder holder, int position) {
        Person person = mPeople.get(position);
        holder.mNameText.setText(person.getName());
        if (person.getProfilePath()!=null) {
            Picasso.get().load(mContext.getString(R.string.IMAGE_PATH) + person.getProfilePath())
                     .into(holder.mPhotoImage);
        } else {
            Picasso.get().load(R.drawable.man_user).into(holder.mPhotoImage);
        }

        View.OnClickListener listener = v-> {
            Intent intent = new Intent(mContext, PersonActivity.class);
            intent.putExtra(PersonActivity.PERSON_POSITION, position);
            mContext.startActivity(intent);
        };

        holder.mPhotoImage.setOnClickListener(listener);
        holder.mView.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return mPeople.size();
    }

    public class PersonViewHolder extends RecyclerView.ViewHolder{
        private ImageView mPhotoImage;
        private TextView mNameText;

        private View mView;

        public PersonViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mPhotoImage = itemView.findViewById(R.id.single_person_layout_photo);
            mNameText = itemView.findViewById(R.id.single_person_layout_name_text);
        }

    }
}
