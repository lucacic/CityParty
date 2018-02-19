package luigi.casciaro.cityparty.contract;

import luigi.casciaro.cityparty.model.Ad;

public interface AdActionsContract {
    void onCallPressed(Ad ad);
    void onSharePressed(Ad ad);
    void onSmilePressed(Ad ad);
}