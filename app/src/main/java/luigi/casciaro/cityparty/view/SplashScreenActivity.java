package luigi.casciaro.cityparty.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import luigi.casciaro.cityparty.BuildConfig;
import luigi.casciaro.cityparty.R;
import luigi.casciaro.cityparty.contract.SplashContract;
import luigi.casciaro.cityparty.controller.SplashController;
import luigi.casciaro.cityparty.controller.UserController;

public class SplashScreenActivity extends BaseActivity implements SplashContract {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(() -> SplashController.checkUser(this), BuildConfig.SPLASH_TIME_OUT);
    }

    @Override
    public void onUserLogged() {
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void onUserNotLogged() {
        startActivity(new Intent(this, SignInActivity.class));
    }

    @Override
    public void onError(String error) {
        // show error
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        // go to sign in
        UserController.logOut();
    }
}