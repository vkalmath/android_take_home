package com.foursquare.takehome.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.foursquare.takehome.*;
import com.foursquare.takehome.model.PeopleHereModel;
import com.foursquare.takehome.model.PeopleHereModelImpl;
import com.foursquare.takehome.model.Person;
import com.foursquare.takehome.presenter.PeopleHerePresenter;
import com.foursquare.takehome.presenter.PeopleHerePresenterImpl;

import java.util.List;

/**
 * MainActivity acts as dumb view implementing PeopleHereView
 */
public class MainActivity extends AppCompatActivity implements PeopleHereView{

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView rvRecyclerView;
    private PersonAdapter personAdapter;
    private PeopleHereModel model;
    private PeopleHerePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvRecyclerView = (RecyclerView) findViewById(R.id.rvRecyclerView);
        rvRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        final DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                LinearLayout.VERTICAL);
        rvRecyclerView.addItemDecoration(dividerItemDecoration);

        model = new PeopleHereModelImpl(getApplicationContext());
        presenter = new PeopleHerePresenterImpl(this, model);
        model.setVisitorFetchListener((PeopleHerePresenterImpl)presenter);

    }


    @Override
    protected void onResume() {
        super.onResume();

        //delegate visitor fetching action to presenter
        presenter.fetchVisitors();
    }

    @Override
    public void showProgress() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "Fetch started..", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void stopProgress() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "Fetch complete..", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void showVisitors(final List<Person> list) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(list != null) {
                    personAdapter = new PersonAdapter(list);
                    rvRecyclerView.setAdapter(personAdapter);
                }
            }
        });
    }

    @Override
    public void showError(final Exception e) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
