package luigi.casciaro.cityparty.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import luigi.casciaro.cityparty.AppController;
import luigi.casciaro.cityparty.R;
import luigi.casciaro.cityparty.model.Ad;
import luigi.casciaro.cityparty.util.MyUtil;

public class AdDetailActivity extends BaseActivity {

    Ad ad;
    String phone;
    String details;
    ImageView imgphone;
    ImageView imgshare;
    private static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_detail);

        ad = AppController.getInstance().getCurrentAd();
        phone = ad.getNumberPhone();
        details = ad.getDescriptionEvent();

        MyUtil.setToolbar(this, ad.getName());

        String text = getString(R.string.details_text).replace("#PRICE", ad.getEventType_toString());

        // set
        findViewById(R.id.imageView).setBackground(ContextCompat.getDrawable(this, ad.getImage()));
        ((TextView) findViewById(R.id.textViewTextDetails)).setText(details);
        ((TextView) findViewById(R.id.textView9)).setText(ad.getAddress());
        ((TextView) findViewById(R.id.textView8)).setText(ad.getDate_toString());

        // listener
        findViewById(R.id.viewLocation).setOnClickListener(v -> goToMaps());
        findViewById(R.id.imageViewMap).setOnClickListener(v -> goToMaps());





        // functionality phone
        imgphone = (ImageView) findViewById(R.id.imagePhone);
        imgphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("click", "phone");
                if (checkPermission(Manifest.permission.CALL_PHONE)) {
                    String dial = "tel:" + phone;
                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                } else {
                    ActivityCompat.requestPermissions(AdDetailActivity.this, new String[]{Manifest.permission.CALL_PHONE}, MAKE_CALL_PERMISSION_REQUEST_CODE);

                }
            }
        });

        // functionality share
        imgshare = (ImageView) findViewById(R.id.imageShare);
        imgshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("click", "share");
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String shareBody = details;
                String shareSub ="Subject here";
                myIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
                myIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(myIntent, "Share using"));
            }
        });
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MAKE_CALL_PERMISSION_REQUEST_CODE:
                //String numberPhone = ad.getPhone();
                Toast.makeText(this, "Adesso puoi effettuare la chiamata!", Toast.LENGTH_SHORT).show();
                return;
        }
    }

    private boolean checkPermission(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
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
}