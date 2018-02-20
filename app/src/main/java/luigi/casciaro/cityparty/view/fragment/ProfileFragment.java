package luigi.casciaro.cityparty.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import luigi.casciaro.cityparty.R;
import luigi.casciaro.cityparty.adapter.AdAdapter;
import luigi.casciaro.cityparty.contract.AdActionsContract;
import luigi.casciaro.cityparty.model.Ad;
import luigi.casciaro.cityparty.util.MyUtil;
import luigi.casciaro.cityparty.view.AdCreateActivity;
import luigi.casciaro.cityparty.view.MainActivity;

public class ProfileFragment extends Fragment implements AdActionsContract {

    @BindView(R.id.root)
    RelativeLayout root;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.viewAdPublished)
    View viewAdPublished;

    @BindView(R.id.viewAdFavourites)
    View viewAdFavourites;

    @BindView(R.id.viewAdLiked)
    View viewAdLiked;

    @BindView(R.id.textViewEmail)
    TextView textViewEmail;

    @BindView(R.id.recyclerViewAdPublished)
    RecyclerView recyclerViewAdPublished;

    @BindView(R.id.recyclerViewAdLiked)
    RecyclerView recyclerViewAdLiked;

    @OnClick(R.id.fab)
    public void addNewAd(){
        startActivity(new Intent(getActivity(), AdCreateActivity.class));
    }

    public static MainActivity mainActivity;
    AdAdapter adapterPublished, adapterFavourites, adapterLiked;
    boolean viewCreated = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        // Annotate fields with @BindView and a view ID for Butter Knife to find and automatically
        // cast the corresponding view in your layout
        ButterKnife.bind(this, view);
        viewCreated = true;
        return view;
    }

    private void loadAdsPublished(){
        adapterPublished = new AdAdapter(new ArrayList(MainActivity.userLogged.getPublished()), false, this);
        recyclerViewAdPublished.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerViewAdPublished.setAdapter(adapterPublished);
    }

    private void loadAdsLiked(){
        adapterLiked = new AdAdapter(new ArrayList(MainActivity.userLogged.getLiked()), false, this);
        recyclerViewAdLiked.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerViewAdLiked.setAdapter(adapterLiked);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (viewCreated && isVisibleToUser) {
            loadData();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    void loadData(){
        MyUtil.print("USER_LOGGED -> " + MainActivity.userLogged.toString());

        textViewEmail.setText(MainActivity.userLogged.getUsername());

        // user is advertiser?
        if(MainActivity.userLogged.isAdvertiser()){
            // yes
            viewAdPublished.setVisibility(View.VISIBLE);
            viewAdLiked.setVisibility(View.VISIBLE);
            fab.setVisibility(View.VISIBLE);

            MyUtil.print("USER_LOGGED PUB -> " + MainActivity.userLogged.getPublished().toString());

            loadAdsPublished();

        }else{
            // no
            viewAdPublished.setVisibility(View.GONE);
            viewAdLiked.setVisibility(View.VISIBLE);
            fab.setVisibility(View.GONE);

            MyUtil.print("USER_LOGGED IS A CUSTOMER");
        }

        loadAdsLiked();

        MyUtil.print("USER_LOGGED LIK -> " + MainActivity.userLogged.getLiked().toString());
    }

    @Override
    public void onCallPressed(Ad ad) {
        // do nothing
    }

    @Override
    public void onSharePressed(Ad ad) {
        // do nothing
    }

    @Override
    public void onSmilePressed(Ad ad) {
        // do nothing
    }
}