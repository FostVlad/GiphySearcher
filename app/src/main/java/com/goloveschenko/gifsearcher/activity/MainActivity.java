package com.goloveschenko.gifsearcher.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import com.goloveschenko.gifsearcher.R;
import com.goloveschenko.gifsearcher.adapter.GifsAdapter;
import com.goloveschenko.gifsearcher.data.entity.Gif;
import com.goloveschenko.gifsearcher.domain.GifListUseCase;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.observers.DisposableObserver;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private static final int COLUMN_COUNT = 2;

    private RecyclerView gifsView;
    private SearchView searchView;
    private List<Gif> gifList = new ArrayList<>();
    private List<Gif> trendingGifList = new ArrayList<>();
    private GifListUseCase gifListUseCase = new GifListUseCase();

    private View.OnClickListener searchListener = view -> {
        if (getSupportActionBar() != null) {
            CharSequence title = getSupportActionBar().getTitle() == getResources().getString(R.string.main_page_title) ? "" : getSupportActionBar().getTitle();
            searchView.setQuery(title, false);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setActionBarTitle(getResources().getString(R.string.main_page_title));

        sendQuery(null);
        GifsAdapter gifsAdapter = new GifsAdapter(gifList, this);
        gifsView = (RecyclerView) findViewById(R.id.gifs_view);
        gifsView.setLayoutManager(new GridLayoutManager(this, COLUMN_COUNT));
        gifsView.setAdapter(gifsAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        searchView = (SearchView) menu.findItem(R.id.menu_toolbar_search).getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setOnSearchClickListener(searchListener);
        searchView.setQueryHint(getResources().getString(R.string.search_hint));
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        gifListUseCase.dispose();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        //clear gifs list before searching
        gifList.clear();
        sendQuery(query);
        searchView.onActionViewCollapsed();
        setActionBarTitle(query);

        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    private void sendQuery(String query) {
        gifListUseCase.execute(query, new DisposableObserver<List<Gif>>() {
            @Override
            public void onNext(List<Gif> gifs) {
                gifList.addAll(gifs);
                gifsView.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable e) {
                failureMessage(e);
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void failureMessage(Throwable e) {
        Snackbar.make(gifsView, e.getMessage(), Snackbar.LENGTH_SHORT).show();
    }

    private void setActionBarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }
}
