package luigi.casciaro.cityparty.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.synnapps.carouselview.CarouselView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import io.realm.Realm;
import luigi.casciaro.cityparty.AppController;
import luigi.casciaro.cityparty.R;
import luigi.casciaro.cityparty.contract.AdActionsContract;
import luigi.casciaro.cityparty.controller.RealmController;
import luigi.casciaro.cityparty.model.Ad;
import luigi.casciaro.cityparty.util.MyImageUtil;
import luigi.casciaro.cityparty.util.MyUtil;

public class AdDetailActivity extends BaseActivity implements AdActionsContract {

    private static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;

    Ad ad;
    boolean isLiked = false;
    CarouselView carouselView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_detail);

        ad = AppController.getInstance().getCurrentAd();

        MyUtil.setToolbar(this, ad.getName());

        carouselView = findViewById(R.id.carouselView);
        carouselView.setPageCount(ad.getImages().size());
        carouselView.setImageListener((position, imageView) -> imageView.setImageBitmap(MyImageUtil.getBitmapFromByteArray(ad.getImages().get(position).getImage())));

        // set
        ((TextView) findViewById(R.id.textViewTitle)).setText(ad.getName());
        ((TextView) findViewById(R.id.textViewTitle2)).setText(ad.getHashTags());
        ((TextView) findViewById(R.id.textViewTextDetails)).setText(ad.getDescriptionEvent());
        ((TextView) findViewById(R.id.textViewAddress)).setText(ad.getAddress());
        ((TextView) findViewById(R.id.textViewAddress2)).setText(ad.getAddress());
        ((TextView) findViewById(R.id.textViewPhone)).setText(ad.getNumberPhone());
        ((TextView) findViewById(R.id.textViewDate)).setText(ad.getDate_toString());
        ((TextView) findViewById(R.id.textViewMese)).setText(ad.getMonthName());
        ((TextView) findViewById(R.id.textViewGiorno)).setText(ad.getDay());
        ((TextView) findViewById(R.id.textViewLikedBy)).setText(String.valueOf(RealmController.getAdsLikedInCounter(ad)));

        // favourite?
        if(MainActivity.userLogged.isInLiked(ad)){
            // yes
            ((ImageView) findViewById(R.id.imageViewLike)).setImageDrawable(ContextCompat.getDrawable(this, R.drawable.like_active_24dp));
        } else{
            // no
            ((ImageView) findViewById(R.id.imageViewLike)).setImageDrawable(ContextCompat.getDrawable(this, R.drawable.like_24dp));
        }

        // listener
        findViewById(R.id.viewLocation).setOnClickListener(v -> goToMaps());
        findViewById(R.id.imageViewMap).setOnClickListener(v -> goToMaps());
        findViewById(R.id.imageViewLike).setOnClickListener(v -> onSmilePressed(ad));
        findViewById(R.id.imagePhone).setOnClickListener(view -> onCallPressed(ad));
        findViewById(R.id.imageShare).setOnClickListener(view -> onSharePressed(ad));
    }

    void goToMaps(){
        Uri gmmIntentUri = Uri.parse("google.streetview:cbll="+ad.getLatitude()+","+ad.getLongitude());
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    /**
     * AdActionsContract
     */
    @Override
    public void onCallPressed(Ad ad) {
        if (checkPermission(Manifest.permission.CALL_PHONE)) {
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + ad.getNumberPhone())));
        } else {
            ActivityCompat.requestPermissions(AdDetailActivity.this, new String[]{Manifest.permission.CALL_PHONE}, MAKE_CALL_PERMISSION_REQUEST_CODE);

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
        File file = new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
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
        isLiked = MainActivity.userLogged.isInLiked(ad);
        // favourite?
        if(isLiked){
            // yes
            ((ImageView) findViewById(R.id.imageViewLike)).setImageDrawable(ContextCompat.getDrawable(this, R.drawable.like_24dp));
            isLiked = false;
            // remove from liked
            try(Realm r = Realm.getDefaultInstance()) {
                r.executeTransaction((realm) -> {
                    // write on realm refreshed data
                    MainActivity.userLogged.removeFromLiked(ad);
                });
            }
        } else{
            // no
            ((ImageView) findViewById(R.id.imageViewLike)).setImageDrawable(ContextCompat.getDrawable(this, R.drawable.like_active_24dp));
            isLiked = true;
            // add to favourite
            try(Realm r = Realm.getDefaultInstance()) {
                r.executeTransaction((realm) -> {
                    // write on realm refreshed data
                    MainActivity.userLogged.getLiked().add(ad);
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
                Toast.makeText(this, "Adesso puoi effettuare la chiamata!", Toast.LENGTH_SHORT).show();
                return;
        }
    }

    private boolean checkPermission(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }
}