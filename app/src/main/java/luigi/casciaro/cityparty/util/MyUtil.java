package luigi.casciaro.cityparty.util;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import luigi.casciaro.cityparty.AppController;
import luigi.casciaro.cityparty.BuildConfig;
import luigi.casciaro.cityparty.R;

public class MyUtil {

    /**
     * Set toolbar and back button disable
     * @param owner
     */
    public static void setToolbarNoBackButton(AppCompatActivity owner) {
        // toolbar
        Toolbar toolbar = (Toolbar) owner.findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(owner, R.color.colorTitleToolbar));
        owner.setSupportActionBar(toolbar);
        owner.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    /**
     * Set toolbar and back button enable
     * @param owner
     */
    public static void setToolbar(AppCompatActivity owner) {
        // toolbar
        Toolbar toolbar = (Toolbar) owner.findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(owner, R.color.colorTitleToolbar));
        owner.setSupportActionBar(toolbar);
        owner.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Set toolbar with color of back button and a title
     * @param owner
     * @param title
     */
    public static void setToolbar(AppCompatActivity owner, String title) {
        setToolbar(owner);
        owner.getSupportActionBar().setTitle(title);
    }

    /**
     * Set toolbar with color of back button and a title
     * @param owner
     * @param title
     */
    public static void setToolbar(AppCompatActivity owner, String title, String subTitle) {
        setToolbar(owner);
        owner.getSupportActionBar().setTitle(title);
        owner.getSupportActionBar().setSubtitle(subTitle);
    }

    /**
     * Set toolbar with color of back button and a title
     * @param owner
     * @param subTitle
     */
    public static void setToolbarSubtitle(AppCompatActivity owner, String subTitle) {
        setToolbar(owner);
        owner.getSupportActionBar().setSubtitle(subTitle);
    }

    /**
     * Check for tablet datepicker bug of Samsung device and Lollipop android version
     * @return
     */
    public static boolean isBrokenSamsungDevice() {
        return (Build.MANUFACTURER.equalsIgnoreCase("samsung") && isBetweenAndroidVersions(Build.VERSION_CODES.LOLLIPOP, Build.VERSION_CODES.LOLLIPOP_MR1));
    }

    /**
     * Check if current android version is between two versions
     * @param min
     * @param max
     * @return
     */
    public static boolean isBetweenAndroidVersions(int min, int max) {
        return Build.VERSION.SDK_INT >= min && Build.VERSION.SDK_INT <= max;
    }

    /**
     * Print in console if app is in DEBUG mode
     * @param context
     * @param message
     */
    public static void print(Context context, String message) {
        if (BuildConfig.DEBUG) {
            String class_name = context.getClass().getSimpleName();
            if (message.length() > 4000) {
                Log.d(AppController.TAG, class_name + " -> " + message.substring(0, 4000));
                longInfo(message.substring(4000));
            } else
                Log.d(AppController.TAG, class_name + " -> " + message);
        }
    }

    /**
     * Print in console if app is in DEBUG mode
     * @param message
     */
    public static void print(String message) {
        if (BuildConfig.DEBUG) {
            if (message.length() > 4000) {
                Log.d(AppController.TAG, " -> " + message.substring(0, 4000));
                longInfo(message.substring(4000));
            } else
                Log.d(AppController.TAG, " -> " + message);
        }
    }

    /**
     * Used from print() methods for long messages
     * @param str
     */
    private static void longInfo(String str) {
        if (str.length() > 4000) {
            System.out.print(str.substring(0, 4000));
            longInfo(str.substring(4000));
        } else
            System.out.print(str);
    }

    /**
     * Hide soft keyboard
     * @param owner
     */
    public static void hideKeyboard(AppCompatActivity owner){
        View view = owner.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) owner.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * Set first char uppercase
     * @param text
     * @return
     */
    public static String firstCharUpperCase(String text){
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }
}