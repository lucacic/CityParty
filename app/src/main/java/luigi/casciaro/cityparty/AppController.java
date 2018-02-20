package luigi.casciaro.cityparty;

import android.app.Application;
import android.os.StrictMode;

import java.util.Date;

import io.realm.Realm;
import io.realm.SyncConfiguration;
import io.realm.SyncCredentials;
import io.realm.SyncUser;
import luigi.casciaro.cityparty.model.Ad;
import luigi.casciaro.cityparty.model.Category;
import luigi.casciaro.cityparty.util.MyDateUtil;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Note:
 * - https://developers.google.com/identity/sign-in/android/start-integrating
 */
public class AppController extends Application {

    public static final String TAG = "CITYPARTY_DEBUG";
    public static final String ISO_FORMAT_INPUT = "yyyy-MM-dd'T'HH:mm:ssZ";
    public static final String ISO_FORMAT_BASE = "yyyy-MM-dd'T'HH:mm";
    public static final String ISO_FORMAT_INPUT_AMERICAN = "yyyy-MM-dd";
    public static final String ISO_FORMAT_OUTPUT = "dd/MM/yyyy";

    private static AppController mInstance;

    private String address = "192.168.1.121:9080";
    private String realmUrl = "realm://"+address+"/~/cityparty";
    private String serverUrl = "http://"+address;


    // global temp values
    private static String filterByEventType;
    private static Category filterByCategory;
    private static Date filterByDate;
    private static String filterByCity;
    private static Ad currentAd;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        // custom font
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/RobotoMono-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        // realm database
        Realm.init(this);

        SyncCredentials myCredentials = SyncCredentials.usernamePassword("Luigi", "luigi", false);
        SyncUser user = SyncUser.login(myCredentials, serverUrl);
        SyncConfiguration config = new SyncConfiguration.Builder(user, realmUrl)
                .build();
        Realm.setDefaultConfiguration(config);
    }

    public static void resetFilter(){
        filterByEventType = null;
        filterByDate = null;
        filterByCategory = null;
        filterByCity = null;
    }

    /**
     * [START getter]
     */
    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public static Ad getCurrentAd() {
        return currentAd;
    }

    public static int getFiltersActiveNumber() {
        int out = 0;

        if(isFilterByEventTypeSetted()) out = out + 1;
        if(isFilterByDateSetted()) out = out + 1;
        if(isFilterByCategorySetted()) out = out + 1;
        if(isFilterByCitySetted()) out = out + 1;

        return out;
    }

    /**
     * [|_ getter filter Event Type]
     */
    public static boolean isFilterByEventTypeSetted() {
        return filterByEventType != null;
    }

    public static String getFilterByEventType() {
        return isFilterByEventTypeSetted() ? filterByEventType : "";
    }

    public static void setFilterByEventType(String filterByEventType) {
        AppController.filterByEventType = filterByEventType;
    }

    /**
     * [|_ getter filter Date]
     */
    public static boolean isFilterByDateSetted() {
        return filterByDate != null;
    }

    public static Date getFilterByDate() {
        return isFilterByDateSetted() ? filterByDate : null;
    }

    public static String getFilterByDateString() {
        return isFilterByDateSetted() ? MyDateUtil.getddMMYYYYFromDate(filterByDate) : "";
    }

    public static void setFilterByDate(Date filterByDate) {
        AppController.filterByDate = filterByDate;
    }

    /**
     * [|_ getter filter Category]
     */
    public static boolean isFilterByCategorySetted() {
        return (filterByCategory != null && !filterByCategory.getName().equalsIgnoreCase("Nessuna"));
    }

    public static Category getFilterByCategory() {
        return filterByCategory;
    }

    public static String getFilterByCategoryName() {
        return isFilterByCategorySetted() ? filterByCategory.getName() : "";
    }

    public static void setFilterByCategory(Category filterByCategory) {
        AppController.filterByCategory = filterByCategory;
    }

    /**
     * [|_ getter filter City]
     */
    public static boolean isFilterByCitySetted() {
        return (filterByCity != null && !filterByCity.equalsIgnoreCase("Nessuna"));
    }

    public static String getFilterByCity() {
        return isFilterByCitySetted() ? filterByCity : "";
    }

    public static void setFilterByCity(String filterByCity) {
        AppController.filterByCity = filterByCity;
    }

    /**
     * [START setter]
     */
    public static void setCurrentAd(Ad currentAd) {
        AppController.currentAd = currentAd;
    }
}