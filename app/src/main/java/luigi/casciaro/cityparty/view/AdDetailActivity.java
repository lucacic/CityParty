package luigi.casciaro.cityparty.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.MenuItem;
import android.widget.TextView;

import luigi.casciaro.cityparty.AppController;
import luigi.casciaro.cityparty.R;
import luigi.casciaro.cityparty.model.Ad;
import luigi.casciaro.cityparty.util.MyUtil;

public class AdDetailActivity extends BaseActivity {

    Ad ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_detail);
        ad = AppController.getInstance().getCurrentAd();
        MyUtil.setToolbar(this, ad.getName());

        String text = getString(R.string.details_text).replace("#PRICE", ad.getEventType_toString());

        // set
        findViewById(R.id.imageView).setBackground(ContextCompat.getDrawable(this, ad.getImage()));
        ((TextView) findViewById(R.id.textViewTextDetails)).setText(text);
        ((TextView) findViewById(R.id.textView9)).setText(ad.getAddress());
        ((TextView) findViewById(R.id.textView8)).setText(ad.getDate_toString());

        // listener
        findViewById(R.id.viewLocation).setOnClickListener(v -> goToMaps());
        findViewById(R.id.imageViewMap).setOnClickListener(v -> goToMaps());
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