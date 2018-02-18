package luigi.casciaro.cityparty.contract;

import luigi.casciaro.cityparty.model.User;

public interface SplashContract extends MainContract {
    void onUserLogged();
    void onUserNotLogged();
}