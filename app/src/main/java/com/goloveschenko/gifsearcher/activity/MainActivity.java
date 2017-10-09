package com.goloveschenko.gifsearcher.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.goloveschenko.gifsearcher.R;
import com.goloveschenko.gifsearcher.adapter.GifsAdapter;
import com.goloveschenko.gifsearcher.data.entity.Gif;
import com.goloveschenko.gifsearcher.domain.SearchingUseCase;
import com.goloveschenko.gifsearcher.domain.TrendingUseCase;
import com.goloveschenko.gifsearcher.fragment.GifDialog;
import com.goloveschenko.gifsearcher.utils.PaginationScrollListener;
import com.goloveschenko.gifsearcher.utils.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.observers.DisposableObserver;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private static final String GIF_DIALOG_TAG = "dialog";
    private static final int COLUMN_COUNT = 2;
    private static final String RATING_SHOW_ALL = "unrated";
    private static final String RATING_Y = "y";
    private static final String RATING_G = "g";
    private static final String RATING_PG = "pg";
    private static final int OFFSET_COEFF = 25;

    private boolean isLoading = false;
    private int offset = 0;

    private RecyclerView gifsView;
    private List<Gif> gifList = new ArrayList<>();
    private SearchView searchView;
    private BottomNavigationView navigationView;
    private TrendingUseCase trendingUseCase = new TrendingUseCase();
    private SearchingUseCase searchingUseCase = new SearchingUseCase();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setActionBarTitle(getResources().getString(R.string.main_page_title));
        setBackButtonVisible(false);

        navigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(this);

        loadGifs("", RATING_SHOW_ALL, 0);
        GifsAdapter gifsAdapter = new GifsAdapter(gifList, this);
        gifsView = (RecyclerView) findViewById(R.id.gifs_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, COLUMN_COUNT);
        gifsView.setLayoutManager(gridLayoutManager);
        gifsView.setAdapter(gifsAdapter);
        gifsView.addOnScrollListener(new PaginationScrollListener(gridLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                offset = offset + OFFSET_COEFF;

                switch (navigationView.getSelectedItemId()) {
                    case R.id.action_show_all:
                        loadGifs(getSearchingQuery(), RATING_SHOW_ALL, offset);
                        break;
                    case R.id.action_rating_y:
                        loadGifs(getSearchingQuery(), RATING_Y, offset);
                        break;
                    case R.id.action_rating_g:
                        loadGifs(getSearchingQuery(), RATING_G, offset);
                        break;
                    case R.id.action_rating_pg:
                        loadGifs(getSearchingQuery(), RATING_PG, offset);
                        break;
                }
            }

            @Override
            public int getOffsetCount() {
                return offset;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
        //click for recycler view
        gifsView.addOnItemTouchListener(new RecyclerItemClickListener(this, (view, position) -> {
            GifDialog gifDialog = GifDialog.getInstance(gifList.get(position).getNormalSizeUrl());
            gifDialog.show(getSupportFragmentManager(), GIF_DIALOG_TAG);
        }));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        searchView = (SearchView) menu.findItem(R.id.menu_toolbar_search).getActionView();
        searchView.setQueryHint(getResources().getString(R.string.search_hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                loadGifs(query, RATING_SHOW_ALL, 0);
//                searchView.onActionViewCollapsed();
                invalidateOptionsMenu();
                setBackButtonVisible(true);
                setActionBarTitle(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        //set searching query
        searchView.setOnSearchClickListener(view -> searchView.setQuery(getSearchingQuery(), false));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setActionBarTitle(getResources().getString(R.string.main_page_title));
            setBackButtonVisible(false);
            navigationView.setSelectedItemId(R.id.action_show_all);
            loadGifs(getSearchingQuery(), RATING_SHOW_ALL, 0);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        trendingUseCase.dispose();
        searchingUseCase.dispose();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_show_all:
                loadGifs(getSearchingQuery(), RATING_SHOW_ALL, 0);
                break;
            case R.id.action_rating_y:
                loadGifs(getSearchingQuery(), RATING_Y, 0);
                break;
            case R.id.action_rating_g:
                loadGifs(getSearchingQuery(), RATING_G, 0);
                break;
            case R.id.action_rating_pg:
                loadGifs(getSearchingQuery(), RATING_PG, 0);
                break;
        }
        return true;
    }

    /**
     * if query is empty then load trending gifs, else load searching gifs
     */
    private void loadGifs(String query, String rating, int offset) {
        if (offset == 0) {
            this.offset = 0;
            gifList.clear();
        }
        DisposableObserver<List<Gif>> observer = new DisposableObserver<List<Gif>>() {
            @Override
            public void onNext(List<Gif> gifs) {
                gifList.addAll(gifs);
                gifsView.getAdapter().notifyDataSetChanged();
                isLoading = false;
            }

            @Override
            public void onError(Throwable e) {
                failureMessage(e);
            }

            @Override
            public void onComplete() {

            }
        };
        if (!query.isEmpty()) {
            searchingUseCase.execute(SearchingUseCase.Params.getParams(query, rating, offset), observer);
        } else {
            trendingUseCase.execute(TrendingUseCase.Params.getParams(rating, offset), observer);
        }
    }

    private void failureMessage(Throwable e) {
        Snackbar.make(gifsView, e.getMessage(), Snackbar.LENGTH_SHORT).show();
    }

    private void setActionBarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    private void setBackButtonVisible(boolean visible) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(visible);
        }
    }

    private String getSearchingQuery() {
        String result = "";
        if (getSupportActionBar() != null) {
            result = getSupportActionBar().getTitle() == getResources().getString(R.string.main_page_title) ? "" : getSupportActionBar().getTitle().toString();
        }
        return result;
    }
}
