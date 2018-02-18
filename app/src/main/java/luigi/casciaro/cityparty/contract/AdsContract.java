package luigi.casciaro.cityparty.contract;

import java.util.ArrayList;

import luigi.casciaro.cityparty.model.Ad;

public interface AdsContract extends MainContract {
    void onAdsLoaded(ArrayList<Ad> items);
}