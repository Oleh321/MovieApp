package balychev.oleh.blch.movieapp.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import balychev.oleh.blch.movieapp.R;
import balychev.oleh.blch.movieapp.adapter.MoviesAdapter;
import balychev.oleh.blch.movieapp.adapter.PeopleAdapter;
import balychev.oleh.blch.movieapp.api.Client;
import balychev.oleh.blch.movieapp.api.Service;
import balychev.oleh.blch.movieapp.data.PeopleLab;
import balychev.oleh.blch.movieapp.model.Person;
import balychev.oleh.blch.movieapp.model.response.GenresResponse;
import balychev.oleh.blch.movieapp.model.response.PersonsResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchPeopleByNameFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private TextView mAmountText;
    private EditText mNameEditText;
    private ImageButton mSearchButton;
    private TextView mNotFoundText;
    private ImageButton mLeftButton;
    private ImageButton mRightButton;
    private TextView mCurrentPageText;
    private ScrollView mScrollView;

    private int mCurrentPage;
    private int mAmount;



    private ProgressDialog mDialog;
    private Service mApiService;

    private String mCurrentQuery;
    private boolean needToReload;

    private PeopleLab mPeopleLab;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mApiService = Client.getClient().create(Service.class);
        mDialog = new ProgressDialog(getContext());
        mDialog.setTitle("Загрузка...");
        if (savedInstanceState != null){
            mCurrentPage = savedInstanceState.getInt("current_page");
            mAmount = savedInstanceState.getInt("pages_amount");
            mCurrentQuery = savedInstanceState.getString("edit_text");
            needToReload = false;
        } else  {
            needToReload = true;
            mCurrentPage = 1;
            mCurrentQuery = "";


        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("current_page", mCurrentPage);
        outState.putInt("pages_amount", mAmount);
        outState.putString("edit_text", mNameEditText.getText().toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search_people_by_name, container, false);

        mLeftButton = view.findViewById(R.id.layout_pager_list_left_btn);
        mRightButton = view.findViewById(R.id.layout_pager_list_right_btn);
        mAmountText = view.findViewById(R.id.layout_pager_list_pages_amount);
        mCurrentPageText = view.findViewById(R.id.layout_pager_list_current_page);
        mRecyclerView = view.findViewById(R.id.layout_pager_list_recycler_view);
        mNameEditText = view.findViewById(R.id.fg_searchpeople_edit_text_person);
        mSearchButton = view.findViewById(R.id.fg_searchpeople_search_btn);
        mNotFoundText = view.findViewById(R.id.fg_searchpeople_not_found_text);
        mScrollView = view.findViewById(R.id.fg_searchpeople_scroll_view);


        mCurrentPageText.setVisibility(View.GONE);
        mLeftButton.setVisibility(View.GONE);
        mRightButton.setVisibility(View.GONE);
        mNameEditText.setText(mCurrentQuery);


        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        } else {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        mPeopleLab = PeopleLab.get();


        mSearchButton.setOnClickListener(v -> {
            mNameEditText.setSelected(false);
            InputMethodManager imm = (InputMethodManager) getContext().
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            if("".equals(mNameEditText.getText().toString()))
                return;
            mCurrentPage=1;
            mDialog.show();
            loadPeople();
        });

        if(!needToReload){
            mDialog.show();
            loadGUI();
        }

        mLeftButton.setOnClickListener(v->{
            if(mCurrentPage==1) {
                return;
            }
            mCurrentPage--;
            loadPeople();
        });
        mRightButton.setOnClickListener(v->{
            if(mCurrentPage==mAmount) {
                return;
            }
            mCurrentPage++;
            loadPeople();
        });

        return view;
    }

    private void loadPeople() {
        try {
            Call<PersonsResponse> call = mApiService.getPeopleByName(getString(R.string.API_KEY),
                    mNameEditText.getText().toString(), mCurrentPage);
            call.enqueue(new Callback<PersonsResponse>() {
                @Override
                public void onResponse(Call<PersonsResponse> call, Response<PersonsResponse> response) {
                    mPeopleLab.setPeople(response.body().getResults());
                    mAmount = response.body().getTotalPages();
                    loadGUI();
                }
                @Override
                public void onFailure(Call<PersonsResponse> call, Throwable t) {
                    Toast.makeText(getContext(), t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void loadGUI() {
        mDialog.dismiss();
        mScrollView.scrollTo(0,0);

        //Если ничего не найдено
        if (mPeopleLab.getPeople().size() == 0) {
            mPeopleLab.setPeople(new ArrayList<Person>());
            mAmountText.setVisibility(View.GONE);
            mNotFoundText.setVisibility(View.VISIBLE);

         //   return;
        }else {
            mNotFoundText.setVisibility(View.GONE);
        }

        PeopleAdapter adapter = new PeopleAdapter(getContext());
        adapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(adapter);

        // Если одна страница, то сообщаем о количестве записей и прячем кнопки перехода
        if(mAmount == 1){
            mCurrentPageText.setVisibility(View.GONE);
            mAmountText.setText(getString(R.string.people_amount) + " "
                    + mPeopleLab.getPeople().size());
            mLeftButton.setVisibility(View.GONE);
            mRightButton.setVisibility(View.GONE);
            return;
        }

        if (mCurrentPage==1){
            mLeftButton.setVisibility(View.INVISIBLE);
        }else {
            mLeftButton.setVisibility(View.VISIBLE);
        }
        if (mCurrentPage == mAmount){
            mRightButton.setVisibility(View.INVISIBLE);
        } else {
            mRightButton.setVisibility(View.VISIBLE);
        }


        mCurrentPageText.setVisibility(View.VISIBLE);
        mCurrentPageText.setText(String.valueOf(mCurrentPage));
        mAmountText.setVisibility(View.VISIBLE);
        mAmountText.setText(getString(R.string.pages_amount) + " " + mAmount);
        mCurrentPageText.setVisibility(View.VISIBLE);

    }


}
