package luigi.casciaro.cityparty.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.method.PasswordTransformationMethod;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import luigi.casciaro.cityparty.BuildConfig;
import luigi.casciaro.cityparty.R;
import luigi.casciaro.cityparty.contract.SignContract;
import luigi.casciaro.cityparty.controller.UserController;
import luigi.casciaro.cityparty.util.MyDialogUtil;
import luigi.casciaro.cityparty.util.MyUtil;
import luigi.casciaro.cityparty.util.SessionManager;

public class SignInActivity extends BaseActivity implements SignContract {

    private static final int RC_SIGN_IN = 1;

    @BindView(R.id.editTextUsername)
    EditText editTextUsername;

    @BindView(R.id.editTextPassword)
    EditText editTextPassword;

    @BindView(R.id.imageViewTogglePassword)
    ImageView imageViewTogglePassword;

    @BindView(R.id.signInButton)
    SignInButton signInButton;

    @OnClick(R.id.imageViewTogglePassword)
    public void viewShowPassword(){
        if(passwordIsShow){
            // hide
            passwordIsShow = false;
            editTextPassword.setTransformationMethod(new PasswordTransformationMethod());
            imageViewTogglePassword.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_hide_password));
        }else{
            // show
            passwordIsShow = true;
            editTextPassword.setTransformationMethod(null);
            imageViewTogglePassword.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_show_password));
        }
    }

    @OnClick(R.id.textViewSignIn)
    public void signIn(){

        MyUtil.hideKeyboard(this);

        if (editTextUsername.getText().toString().isEmpty()) {
            editTextUsername.setError(getString(R.string.text_required));
            editTextUsername.requestFocus();
            return;
        }

        if (editTextPassword.getText().toString().isEmpty()) {
            editTextPassword.setError(getString(R.string.text_required));
            editTextPassword.requestFocus();
            return;
        }

        progressDialog = MyDialogUtil.showProgressDialog(this, getString(R.string.progress_dialog_sign_in));

        UserController.signInOffline(this, editTextUsername.getText().toString(), editTextPassword.getText().toString());
    }

    @OnClick(R.id.signInButton)
    public void signInButtonListener() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @OnClick(R.id.viewSignUp)
    public void signUpButtonListener() {
        startActivity(new Intent(this, SignUpActivity.class));
    }

    ProgressDialog progressDialog;
    boolean passwordIsShow = false;
    GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);

        signInButton.setSize(SignInButton.SIZE_STANDARD);

        // Configure sign-in to request the user's ID, email address, and basic profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        // done button
        editTextPassword.setOnEditorActionListener((v, actionId, event) -> {
            if(actionId == EditorInfo.IME_ACTION_DONE){
                signIn();
            }
            return false;
        });

        // debug?
        if(BuildConfig.DEBUG){
            // yes, set default login for test
            editTextUsername.setText(BuildConfig.DEFAULT_USER);
            editTextPassword.setText(BuildConfig.DEFAULT_PASSWORD);
        }
    }

    /**
     * SignContract
     */
    @Override
    public void onUserLogged() {
        // dismiss
        if(progressDialog != null) progressDialog.dismiss();
        // go to
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void onError(String error) {
        // dismiss
        progressDialog.dismiss();
        // show error on toast
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    /**
     * onActivityResult
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach a listener
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            // try to get account
            try {
                // get account
                GoogleSignInAccount account = task.getResult(ApiException.class);
                // create or update user
                UserController.createUser(account.getEmail());
                // set session
                new SessionManager(this).createLoginSession(account.getEmail());
                // start activity
                onUserLogged();

            } catch (ApiException e) {
                // The ApiException status code indicates the detailed failure reason.
                // Please refer to the GoogleSignInStatusCodes class reference for more information.
                MyUtil.print("signInResult:failed code=" + e.getStatusCode());
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // exit
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}