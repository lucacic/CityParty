package luigi.casciaro.cityparty.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

import luigi.casciaro.cityparty.R;
import luigi.casciaro.cityparty.controller.UserController;

public class MyDialogUtil {

    /**
     * AlertDialog for generic onError contract method, one button
     * @param owner
     */
    public static void showDialog_onError(Activity owner){
        // display message
        AlertDialog.Builder builder;
        AlertDialog alert;
        builder = new AlertDialog.Builder(owner);
        builder.setCancelable(false);
        builder.setTitle(R.string.text_on_error_title);
        builder.setMessage(R.string.text_on_error_message);
        builder.setNeutralButton(owner.getString(R.string.text_on_error_button), (dialog, id) -> {
            // close
            dialog.dismiss();
        });

        alert = builder.create();
        alert.show();
    }

    /**
     * AlertDialog for generic onError contract method with message, one button
     * @param owner
     * @param message
     */
    public static void showDialog_onError(Activity owner, String message){

        if (message == null) message = owner.getString(R.string.text_on_error_message);

        // display message
        AlertDialog.Builder builder;
        AlertDialog alert;
        builder = new AlertDialog.Builder(owner);
        builder.setCancelable(false);
        builder.setTitle(R.string.text_on_error_title);
        builder.setMessage(message);
        builder.setNeutralButton(owner.getString(R.string.text_on_error_button), (dialog, id) -> {
            // close
            dialog.dismiss();
        });

        alert = builder.create();
        alert.show();
    }

    /**
     * AlertDialog with title and message, one button
     * @param owner
     * @param title
     * @param message
     */
    public static void showDialog(Activity owner, String title, String message){

        // display message
        AlertDialog.Builder builder;
        AlertDialog alert;
        builder = new AlertDialog.Builder(owner);
        builder.setCancelable(false);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNeutralButton(owner.getString(R.string.text_on_error_button), (dialog, id) -> {
            // close
            dialog.dismiss();
        });

        alert = builder.create();
        alert.show();
    }

    /**
     * Progress dialog with a message
     * @param owner
     * @param message
     * @return
     */
    public static ProgressDialog showProgressDialog(Activity owner, String message) {
        ProgressDialog dialog;
        dialog = new ProgressDialog(owner);
        dialog.setMessage(message);
        dialog.setCancelable(false);
        dialog.show();
        return dialog;
    }

    /**
     * Show AlertDialog to open gps setting
     * @param owner
     */
    public static void showDialog_enablingGPS(Activity owner) {

        final AlertDialog.Builder builder =  new AlertDialog.Builder(owner);
        final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
        final String message = owner.getString(R.string.text_open_gps_settings);

        builder.setMessage(message)
                .setPositiveButton(owner.getString(R.string.text_yes), (d, id) -> {
                            owner.startActivity(new Intent(action));
                            d.dismiss();
                        })
                .setNegativeButton(owner.getString(R.string.text_cancel), (d, id) -> d.cancel());
        builder.create().show();
    }

    /**
     * Show log out AlertDialog
     * @param owner
     */
    public static void showDialog_logout(Activity owner) {
        AlertDialog.Builder builder = new AlertDialog.Builder(owner);
        builder.setCancelable(false);
        builder.setTitle(owner.getString(R.string.text_logout_title));
        builder.setMessage(owner.getString(R.string.text_logout_message));
        builder.setPositiveButton(owner.getString(R.string.text_yes), (dialog, id) -> UserController.logOut()).setNegativeButton(owner.getString(R.string.text_no), (dialog, id) -> dialog.cancel());

        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void showDialog_onGuiThread(Activity owner, String message){
        owner.runOnUiThread(() -> showDialog_onError(owner, message));
    }

    public static void showDialog_finishActivity(Activity owner, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(owner);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(owner.getString(R.string.text_yes), (dialog, id) -> owner.finish()).setNegativeButton(owner.getString(R.string.text_no), (dialog, id) -> dialog.cancel());

        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void showDialog_finishActivityOneButton(Activity owner, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(owner);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNeutralButton(owner.getString(R.string.text_on_error_button), (dialog, id) -> owner.finish());

        AlertDialog alert = builder.create();
        alert.show();
    }


    public static void showDialog_closeAppOneButton(Activity owner, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(owner);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNeutralButton(owner.getString(R.string.text_on_error_button), (dialog, id) -> {
            // exit
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            owner.startActivity(intent);
            // finish
            owner.finish();
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
}