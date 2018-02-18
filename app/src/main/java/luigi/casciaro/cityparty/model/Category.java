package luigi.casciaro.cityparty.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Category extends RealmObject {

    @PrimaryKey
    public String name;

    private int image;

    // default for Realm
    public Category() {}

    public Category(String name, int image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name != null ? name : "";
    }

    public int getImage() {
        return image;
    }

    @Override
    public String toString() {
        return name;
    }
}