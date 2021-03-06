package luigi.casciaro.cityparty.view.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import luigi.casciaro.cityparty.R;
import luigi.casciaro.cityparty.adapter.AdAdapter;
import luigi.casciaro.cityparty.contract.AdActionsContract;
import luigi.casciaro.cityparty.contract.AdsContract;
import luigi.casciaro.cityparty.controller.RealmController;
import luigi.casciaro.cityparty.model.Ad;
import luigi.casciaro.cityparty.util.MyUtil;
import luigi.casciaro.cityparty.view.MainActivity;

public class HomeFragment extends Fragment implements AdsContract, AdActionsContract {

    private static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;

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

        adapter = new AdAdapter(items, true, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setRefreshing(false);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onError(String error) {
        Toast.makeText(mainActivity, error, Toast.LENGTH_SHORT).show();
    }

    /**
     * AdActionsContract
     */
    @Override
    public void onCallPressed(Ad ad) {
        if (checkPermission(Manifest.permission.CALL_PHONE)) {
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + ad.getNumberPhone())));
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, MAKE_CALL_PERMISSION_REQUEST_CODE);

        }
    }

    @Override
    public void onSharePressed(Ad ad) {
        Uri bmpUri = getLocalBitmapUri(ad.getImage()); // see previous remote images section
        // Construct share intent as described above based on bitmap

        Intent shareIntent = new Intent();
        //shareIntent.setPackage("com.whatsapp");
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, ad.getDescriptionEvent() );
        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
        shareIntent.setType("image/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "Share Opportunity"));

    }

    private Uri getLocalBitmapUri(Bitmap bmp) {
        Uri bmpUri = null;
        File file = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            bmpUri = Uri.fromFile(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    @Override
    public void onSmilePressed(Ad ad) {
        // favourite?
        if(MainActivity.userLogged.isInLiked(ad)){
            // yes
            // remove from liked
            try(Realm r = Realm.getDefaultInstance()) {
                r.executeTransaction((realm) -> {
                    // write on realm refreshed data
                    MainActivity.userLogged.removeFromLiked(ad);
                    adapter.notifyDataSetChanged();
                });
            }
        } else{
            // no
            // add to favourite
            try(Realm r = Realm.getDefaultInstance()) {
                r.executeTransaction((realm) -> {
                    // write on realm refreshed data
                    MainActivity.userLogged.getLiked().add(ad);
                    adapter.notifyDataSetChanged();
                });
            }
        }
    }

    /**
     * Permissions
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MAKE_CALL_PERMISSION_REQUEST_CODE:
                Toast.makeText(getActivity(), "Adesso puoi effettuare la chiamata!", Toast.LENGTH_SHORT).show();
                return;
        }
    }

    private boolean checkPermission(String permission) {
        return ContextCompat.checkSelfPermission(getActivity(), permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Menu
     */
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