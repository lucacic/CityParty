package luigi.casciaro.cityparty.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class AdImage extends RealmObject {

    @PrimaryKey
    int id;
    String path;
    byte[] image;

    public AdImage() {}

    public AdImage(String path, byte[] image, int id) {
        this.id = id;
        this.path = path;
        this.image = image;
    }

    public AdImage(String path, byte[] image) {
        this.path = path;
        this.image = image;
    }

    public void setId(int id){this.id = id;}

    public String getPath() {
        return path;
    }

    public byte[] getImage() {
        return image;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                "path='" + path + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }
}