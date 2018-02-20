package luigi.casciaro.cityparty.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class User extends RealmObject {

    public static final String USER_TYPE_ADVERTISER = "USER_TYPE_ADVERTISER";
    public static final String USER_TYPE_CUSTOMER = "USER_TYPE_CUSTOMER";

    @PrimaryKey
    public String username;

    public String password;
    public String type;
    public RealmList<Ad> liked;
    public RealmList<Ad> published;

    // default for Realm
    public User() {}

    public User(String username, String password, String type) {
        this.username = username;
        this.password = password;
        this.type = type;
    }

    public RealmList<Ad> getLiked() {
        return liked;
    }

    public boolean isInLiked(Ad ad1) {
        for (Ad ad : liked) {
            if(ad.getId() == ad1.getId()) return true;
        }
        return false;
    }

    public void removeFromLiked(Ad ad1) {
        if(liked != null && liked.size() > 0){

            Ad adToRemove = null;

            for (Ad ad : liked) {
                if(ad.getId() == ad1.getId()){
                    adToRemove = ad;
                }
            }

            if(adToRemove != null) liked.remove(adToRemove);
        }
    }

    public RealmList<Ad> getPublished() {
        return published;
    }

    public void setPublished(RealmList<Ad> published) {
        this.published = published;
    }

    public String getUsername() {
        return username;
    }

    public boolean isAdvertiser(){
        return type.equals(USER_TYPE_ADVERTISER);
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", type='" + type + '\'' +
                ", liked=" + liked +
                ", published=" + published +
                '}';
    }
}