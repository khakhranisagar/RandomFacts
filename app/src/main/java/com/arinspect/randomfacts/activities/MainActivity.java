package com.arinspect.randomfacts.activities;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.idling.CountingIdlingResource;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.arinspect.randomfacts.R;
import com.arinspect.randomfacts.adapters.FactsAdapter;
import com.arinspect.randomfacts.entities.CountryFact;
import com.arinspect.randomfacts.entities.Fact;
import com.arinspect.randomfacts.rest.RestClient;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private FactsAdapter factsAdapter;
    private List<Fact> allFacts;
    private List<Fact> recyclerItems;
    private RestClient restClient;
    private boolean isScrolling = false;
    private ProgressBar progressBar;
    private final String TAG="Error :"+ this.getClass().getCanonicalName();
    private CountingIdlingResource countingIdlingResource= new CountingIdlingResource("Recycler");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeInstanceComponents();
        fetchData();
    }

    private void fetchData() {
        countingIdlingResource.increment();
        Call<CountryFact> countryFactCall = restClient.getFactsService().getCountryFacts();
        countryFactCall.enqueue(countryFactCallback);
    }

    /**
     * Initialization of initial Views and instance variables
     */
    private void initializeInstanceComponents(){
        recyclerView = findViewById(R.id.rvFacts);
        progressBar  = findViewById(R.id.pbFactList);
        layoutManager = new LinearLayoutManager(this);
        restClient= new RestClient();
        recyclerView.setOnScrollListener(onScrollListener);
    }

    /**
     * callback used to handle web api response
     */
    private Callback<CountryFact> countryFactCallback = new Callback<CountryFact>() {
        @Override
        public void onResponse(Call<CountryFact> call, Response<CountryFact> response) {
            hideProgressBar();
            setAllFacts(response.body().getFacts());
            recyclerItems = new ArrayList<>();
            recyclerItems.addAll(allFacts.subList(0, 10));
            factsAdapter = new FactsAdapter(recyclerItems, getApplicationContext());
            recyclerView.setAdapter(factsAdapter);
            recyclerView.setLayoutManager(layoutManager);
            getSupportActionBar().setTitle(response.body().getTitle());
            countingIdlingResource.decrement();

        }

        @Override
        public void onFailure(Call<CountryFact> call, Throwable t) {

        }
    };

    /**
     * shows the progressbar
     * Used to show progressBar when data is being fetched
     */
    private void showProgressBar(){
        progressBar.setVisibility(View.VISIBLE);
    }

    /**
     * hides the progressbar
     * Used to hide progressBar when data is fetched
     */
    private void hideProgressBar(){
        progressBar.setVisibility(View.GONE);
    }


    /**
     * onScrollListener used to know if user has scrolled to end, and if yes,
     * next data will be loaded
     */
    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            LinearLayoutManager layoutManager = ((LinearLayoutManager)recyclerView.getLayoutManager());

            if (isScrolling && recyclerItems.size()< allFacts.size() && layoutManager.findLastVisibleItemPosition()==9) {
                showProgressBar();
                isScrolling = false;
                loadMoreData();
            }
        }

        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true;
            }
        }
    };

    /**
     * Mocks the scenario of paging i.e Lazy loads more items in the list
     */
    private void loadMoreData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                HashSet<Fact> facts= new HashSet<>();
                facts.addAll(allFacts);
                for (int i =10;i<allFacts.size();i++){
                    if(recyclerItems.indexOf(allFacts.get(i))==-1){
                        recyclerItems.add(allFacts.get(i));
                    }
                }

//                recyclerItems.addAll(allFacts.subList(10, allFacts.size()));

                factsAdapter.notifyDataSetChanged();
                hideProgressBar();
            }
        }, 3000);
    }

    /**
     * Populates an instance of arraylist used in recycler
     * @param allFacts
     */
    public void setAllFacts(List<Fact> allFacts) {
        this.allFacts = allFacts;
    }

    /**
     * Created a getter of Idling resource for test environment
     *
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        return countingIdlingResource;
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
