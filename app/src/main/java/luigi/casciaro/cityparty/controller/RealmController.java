package luigi.casciaro.cityparty.controller;

import android.graphics.BitmapFactory;
import android.media.Image;

import java.util.ArrayList;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import luigi.casciaro.cityparty.AppController;
import luigi.casciaro.cityparty.R;
import luigi.casciaro.cityparty.contract.AdCreateContract;
import luigi.casciaro.cityparty.contract.AdsContract;
import luigi.casciaro.cityparty.model.Ad;
import luigi.casciaro.cityparty.model.AdImage;
import luigi.casciaro.cityparty.model.Category;
import luigi.casciaro.cityparty.model.User;
import luigi.casciaro.cityparty.util.MyDateUtil;
import luigi.casciaro.cityparty.util.MyImageUtil;

public class RealmController {

    static Realm realm;

    public static void initRealm(){

        realm = Realm.getDefaultInstance();

        realm.executeTransaction((realm) -> {

            //addUsers();
            addCategories();
            //addAds();
        });

    }

    public static void addUsers(){

        ArrayList<User> users = new ArrayList();
        users.add(new User("inserzionista@gmail.com", "password", User.USER_TYPE_ADVERTISER));
        users.add(new User("mattia.arietti@gmail.com", "password", User.USER_TYPE_ADVERTISER));
        users.add(new User("luigi.casciaro91@gmail.com", "password", User.USER_TYPE_CUSTOMER));

        // write on realm refreshed data
        realm.copyToRealmOrUpdate(users);

    }

    public static void addCategories(){

        ArrayList<Category> categories = new ArrayList();
        categories.add(new Category("Discoteca", R.drawable.background_discoteca));
        categories.add(new Category("Concerto", R.drawable.background_concerto));
        categories.add(new Category("Apericena", R.drawable.background_apericena));

        // write on realm refreshed data
        realm.copyToRealmOrUpdate(categories);
    }


    public static void addAds(){

        ArrayList<Category> categories = getCategories();

        ArrayList<Ad> ads = new ArrayList();
        ads.add(new Ad(1, "AD 1", "Torino", categories.get(0), 45.0651384, 7.65677679, "Via Pier Carlo Boggio, 59 Torino (TO)", Ad.EVENT_TYPE_FREE, new Date(), "34859652", "Descrizione serata 1", new RealmList(new AdImage(MyImageUtil.getURLForResource(R.drawable.serata), MyImageUtil.convertBitmapToByte(BitmapFactory.decodeResource(AppController.getInstance().getResources(), R.drawable.serata)), 1))));
        ads.add(new Ad(2, "AD 2", "Torino", categories.get(1), 45.06572584470129, 7.655252894873001, "Corso Francesco Ferrucci, 101, Torino (TO)", Ad.EVENT_TYPE_FREE, new Date(), "34859652", "Descrizione serata 2", new RealmList(new AdImage(MyImageUtil.getURLForResource(R.drawable.concerto), MyImageUtil.convertBitmapToByte(BitmapFactory.decodeResource(AppController.getInstance().getResources(), R.drawable.concerto)), 1))));
        ads.add(new Ad(3, "AD 3", "Milano", categories.get(2), 45.47195911140795, 9.187788963317871, "Via Brera, 28, Milano (MI)", Ad.EVENT_TYPE_PAID, new Date(), "34859652", "Descrizione serata 2", new RealmList(new AdImage(MyImageUtil.getURLForResource(R.drawable.apericena), MyImageUtil.convertBitmapToByte(BitmapFactory.decodeResource(AppController.getInstance().getResources(), R.drawable.apericena)), 1))));

        // write on realm refreshed data
        realm.copyToRealmOrUpdate(ads);
    }


    public static void addAdToPublisherUser(AdCreateContract owner, Ad ad){

        User user = UserController.getUserLogged();

        try(Realm r = Realm.getDefaultInstance()) {
            r.executeTransaction((realm) -> {
                // write on realm
                Number currentIdNum = realm.where(Ad.class).max("id");

                ad.setId(currentIdNum==null ? 0 : currentIdNum.intValue()+1);

                Number max = realm.where(AdImage.class).max("id");
                int maxIdIndex = max == null ? 0 : max.intValue()+1;
                for(AdImage ai:ad.getImages())
                    ai.setId(maxIdIndex++);

                user.getPublished().add(ad);
                realm.copyToRealmOrUpdate(user);
                owner.onAdCreated();
            });
        }
    }



    public static ArrayList<Category> getCategories() {

        RealmQuery<Category> query = Realm.getDefaultInstance().where(Category.class);
        RealmResults<Category> result = query.findAll();

        ArrayList<Category> items = new ArrayList();
        for (int i = 0; i < result.size(); i++) {
            items.add(Realm.getDefaultInstance().copyFromRealm(result.get(i)));
        }

        return items;
    }

    public static ArrayList<Ad> getAds() {

        RealmQuery<Ad> query = Realm.getDefaultInstance().where(Ad.class);
        RealmResults<Ad> result = query.findAll();

        ArrayList<Ad> items = new ArrayList();
        for (int i = 0; i < result.size(); i++) {
            items.add(Realm.getDefaultInstance().copyFromRealm(result.get(i)));
        }

        return items;
    }

    public static ArrayList<String> getCityOfAds() {

        RealmQuery<Ad> query = Realm.getDefaultInstance().where(Ad.class);
        RealmResults<Ad> result = query.findAll();

        ArrayList<String> items = new ArrayList();
        for (int i = 0; i < result.size(); i++) {
            String city = result.get(i).getCity();
            if(!items.contains(city)) items.add(city);
        }

        return items;
    }

    public static ArrayList<User> getUsers() {

        RealmQuery<User> query = Realm.getDefaultInstance().where(User.class);
        RealmResults<User> result = query.findAll();

        ArrayList<User> items = new ArrayList();
        for (int i = 0; i < result.size(); i++) {
            items.add(Realm.getDefaultInstance().copyFromRealm(result.get(i)));
        }

        return items;
    }

    public static void loadAds(AdsContract owner){

        RealmQuery<Ad> query = Realm.getDefaultInstance().where(Ad.class);
        // filters ?
        if(AppController.getInstance().getFiltersActiveNumber() > 0) {
            // yes
            // event type?
            if(AppController.getInstance().isFilterByEventTypeSetted())
               query = query.equalTo("eventType", AppController.getInstance().getFilterByEventType());
            // category?
            if(AppController.getInstance().isFilterByCategorySetted())
               query = query.equalTo("categoryName", AppController.getInstance().getFilterByCategoryName());
            // date?
            if(AppController.getInstance().isFilterByDateSetted())
                query = query.equalTo("date", AppController.getInstance().getFilterByDate());
            // city?
            if(AppController.getInstance().isFilterByCitySetted())
                query = query.equalTo("city", AppController.getInstance().getFilterByCity());
        }

        RealmResults<Ad> result = query.findAll();

        ArrayList<Ad> items = new ArrayList();
        for (int i = 0; i < result.size(); i++) {
            items.add(Realm.getDefaultInstance().copyFromRealm(result.get(i)));
        }

        owner.onAdsLoaded(items);
    }

    public static int getAdsLikedInCounter(Ad ad) {

        int count = 0;
        RealmQuery<User> query = Realm.getDefaultInstance().where(User.class);
        RealmResults<User> result = query.findAll();

        for (int i = 0; i < result.size(); i++) {
            if(result.get(i).isInLiked(ad))
                count = count + 1;
        }

        return count;
    }
}