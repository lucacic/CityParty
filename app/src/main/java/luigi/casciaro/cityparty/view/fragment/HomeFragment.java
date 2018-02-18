package luigi.casciaro.cityparty.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import luigi.casciaro.cityparty.R;
import luigi.casciaro.cityparty.adapter.AdAdapter;
import luigi.casciaro.cityparty.contract.AdsContract;
import luigi.casciaro.cityparty.controller.RealmController;
import luigi.casciaro.cityparty.model.Ad;
import luigi.casciaro.cityparty.util.MyUtil;
import luigi.casciaro.cityparty.view.MainActivity;

public class HomeFragment extends Fragment implements AdsContract {

    @BindView(R.id.root)
    RelativeLayout root;

    @BindView(R.id.progressBar)
    View progressBar;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    public static MainActivity mainActivity;
    AdAdapter adapter;
    boolean viewCreated = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        swipeRefreshLayout.setOnRefreshListener(() -> RealmController.loadAds(this));
        viewCreated = true;
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (viewCreated && isVisibleToUser) {

            MyUtil.print("USERS -> " + RealmController.getUsers().size());
            MyUtil.print("ADS   -> " + RealmController.getAds().size());

            RealmController.loadAds(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        RealmController.loadAds(this);
    }

    /**
     * AdsContract
     */
    @Override
    public void onAdsLoaded(ArrayList<Ad> items) {

        adapter = new AdAdapter(items, true, getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setRefreshing(false);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onError(String error) {
        Toast.makeText(mainActivity, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_search);

        SearchView searchView = new SearchView(mainActivity.getSupportActionBar().getThemedContext());
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setActionView(item, searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (adapter != null) adapter.getFilter().filter(query);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if (adapter != null) adapter.getFilter().filter(newText);
                return true;
            }
        });
    }
}