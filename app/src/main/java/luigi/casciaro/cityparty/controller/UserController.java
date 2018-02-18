package luigi.casciaro.cityparty.controller;

import android.content.Intent;

import io.realm.Realm;
import io.realm.RealmResults;
import luigi.casciaro.cityparty.AppController;
import luigi.casciaro.cityparty.contract.SignContract;
import luigi.casciaro.cityparty.model.User;
import luigi.casciaro.cityparty.util.SessionManager;
import luigi.casciaro.cityparty.view.SignInActivity;

public class UserController {

    public static void signInOffline(SignContract owner, String username, String password){

        boolean found = false;

        // write on realm
        try(Realm realm = Realm.getDefaultInstance()) {

            // persons => [U1,U2]
            RealmResults<User> usersOnRealm = realm.where(User.class)
                    .equalTo("username", username)
                    .findAll();

            for (User userMobile: usersOnRealm) {
                found = password.equalsIgnoreCase(userMobile.password);
                if (found) break;
            }

            if(found){
                // save response
                new SessionManager(AppController.getInstance()).createLoginSession(username);
                // back to owner
                owner.onUserLogged();
            }else{
                owner.onError("Utente non trovato");
            }
        }
    }

    public static void signUpOffline(SignContract owner, String username, String password){

        boolean found = false;

        // write on realm
        try(Realm realm = Realm.getDefaultInstance()) {

            // persons => [U1,U2]
            RealmResults<User> usersOnRealm = realm.where(User.class)
                    .equalTo("username", username)
                    .findAll();

            for (User userMobile: usersOnRealm) {
                found = password.equalsIgnoreCase(userMobile.password);
                if (found) break;
            }

            if(found){
                owner.onError("Utente già presente");
            }else{

                realm.executeTransaction((r) -> {
                    realm.copyToRealmOrUpdate(new User(username, password, User.USER_TYPE_CUSTOMER));
                    // save response
                    new SessionManager(AppController.getInstance()).createLoginSession(username);
                    // back
                    owner.onUserLogged();
                });
            }
        }
    }

    public static User getUserLogged(){
        // get users
        RealmResults<User> users = Realm.getDefaultInstance().where(User.class).equalTo("username", new SessionManager(AppController.getInstance()).getUsername()).findAll();
        // user present? only 1, field id is unique
        if(users.size() == 1){
            // yes
            return users.get(0);
        }else{
            // no, mmm
            return null;
        }
    }

    public static void createUser(String email) {
        // create user
        User user = new User(email, "password", User.USER_TYPE_CUSTOMER);
        // add to realm
        try(Realm r = Realm.getDefaultInstance()) {
            r.executeTransaction((realm) -> {
                // write on realm refreshed data
                realm.copyToRealmOrUpdate(user);
            });
        }
    }

    /**
     * Log out from the application
     */
    public static void logOut(){

        new SessionManager(AppController.getInstance()).clear();

        Intent i = new Intent(AppController.getInstance(), SignInActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppController.getInstance().startActivity(i);
    }
}