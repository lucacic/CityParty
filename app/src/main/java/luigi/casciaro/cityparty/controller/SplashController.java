package luigi.casciaro.cityparty.controller;

import luigi.casciaro.cityparty.AppController;
import luigi.casciaro.cityparty.contract.SplashContract;
import luigi.casciaro.cityparty.util.SessionManager;

public class SplashController {

    public static void checkUser(SplashContract owner){


        if(new SessionManager(AppController.getInstance()).isLoggedIn()) {
            owner.onUserLogged();
        }
        else {
            // init Realm
            // RealmController.initRealm();
            owner.onUserNotLogged();
        }
    }
}