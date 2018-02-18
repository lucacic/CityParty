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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import luigi.casciaro.cityparty.BuildConfig;
import luigi.casciaro.cityparty.R;
import luigi.casciaro.cityparty.contract.SignContract;
import luigi.casciaro.cityparty.controller.UserController;
import luigi.casciaro.cityparty.util.MyDialogUtil;
import luigi.casciaro.cityparty.util.MyUtil;

public class SignUpActivity extends BaseActivity implements SignContract {

    @BindView(R.id.editTextUsername)
    EditText editTextUsername;

    @BindView(R.id.editTextPassword)
    EditText editTextPassword;

    @BindView(R.id.imageViewTogglePassword)
    ImageView imageViewTogglePassword;

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

    @OnClick(R.id.textViewSignUp)
    public void signUp(){

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

        UserController.signUpOffline(this, editTextUsername.getText().toString(), editTextPassword.getText().toString());
    }

    ProgressDialog progressDialog;
    boolean passwordIsShow = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        // done button
        editTextPassword.setOnEditorActionListener((v, actionId, event) -> {
            if(actionId == EditorInfo.IME_ACTION_DONE){
                signUp();
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
}