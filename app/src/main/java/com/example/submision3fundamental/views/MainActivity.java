package com.example.submision3fundamental.views;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.submision3fundamental.R;
import com.example.submision3fundamental.viewmodels.MainViewModels;
import com.example.submision3fundamental.views.adapter.UserAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private UserAdapter adapter;
    private MainViewModels mainViewModels;

    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.linearNotFound) LinearLayout linearNotFound;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.main_search_username) SearchView searchUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            searchUsername.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchUsername.onActionViewExpanded();
            searchUsername.setQueryHint(getResources().getString(R.string.search_hint));
            searchUsername.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    linearNotFound.setVisibility(View.GONE);
                    showLoading(true);
                    mainViewModels.fetchUser(s);

                    return true;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    return false;
                }
            });

        }

        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new UserAdapter();
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        mainViewModels = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MainViewModels.class);
        mainViewModels.getUsers().observe(this, users -> {
            adapter.setData(users);

            if (users.size() == 0) linearNotFound.setVisibility(View.VISIBLE);
            else linearNotFound.setVisibility(View.GONE);

            showLoading(false);
        });

        adapter.setOnItemClickCallback(data -> {
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra(DetailActivity.EXTRA_USER, data);
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_bahasa:
                Intent changeLang = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(changeLang);
                break;

            case R.id.menu_favorit:
                Intent favorite = new Intent(MainActivity.this, FavoriteActivity.class);
                startActivity(favorite);
                break;

            case R.id.menu_notif:
                Intent notification = new Intent(MainActivity.this, NotificationActivity.class);
                startActivity(notification);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showLoading(Boolean state) {
        if (state) progressBar.setVisibility(View.VISIBLE);
        else progressBar.setVisibility(View.GONE);
    }
}
