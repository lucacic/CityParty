package luigi.casciaro.cityparty.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class AdImage extends RealmObject {

    @PrimaryKey
    String path;

    byte[] image;
    int index;

    public AdImage() {}

    public AdImage(String path, byte[] image, int index) {
        this.path = path;
        this.image = image;
        this.index = index;
    }

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
                "index='" + index + '\'' +
                "path='" + path + '\'' +
                '}';
    }

    public int getIndex() {
        return index;
    }
}