package luigi.casciaro.cityparty.model;

import android.graphics.Bitmap;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import luigi.casciaro.cityparty.AppController;
import luigi.casciaro.cityparty.R;
import luigi.casciaro.cityparty.util.MyDateUtil;
import luigi.casciaro.cityparty.util.MyImageUtil;

public class Ad extends RealmObject {

    public static final String EVENT_TYPE_FREE = "EVENT_TYPE_FREE";
    public static final String EVENT_TYPE_PAID = "EVENT_TYPE_PAID";

    @PrimaryKey
    public int id;

    public String name;
    public Category category;
    public String categoryName;
    public Double latitude;
    public Double longitude;
    public String address;
    public String eventType;
    public String hashTags;
    public String city;
    public Date date;
    public RealmList<AdImage> images;
    public String numberPhone;
    public String descriptionEvent;

    // default for Realm
    public Ad() {}

    public Ad(int id, String name, String city, Category category, Double latitude, Double longitude,
              String address, String eventType, Date date, String numberPhone, String
                      descriptionEvent, RealmList<AdImage> images) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.city = city;
        this.categoryName = category.getName();
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.eventType = eventType;
        this.date = date;
        this.images = images;
        this.numberPhone = numberPhone;
        this.descriptionEvent = descriptionEvent ;
    }

    public String getNumberPhone() {
        return numberPhone;
    }

    public void setNumberPhone(String numberPhone) {
        this.numberPhone = numberPhone;
    }

    public String getDescriptionEvent() {
        return descriptionEvent;
    }

    public void setDescriptionEvent(String descriptionEvent) {
        this.descriptionEvent = descriptionEvent;
    }

    public int getId() {
        return id;
    }

    public String getFirstRow() {
        String out = getName();

        out += " - " + getEventType_toString();
        out += "\n" + getCity();
        out += " - " + getCategoryName();

        return out;
    }

    public String getName() {
        return name != null ? name : "";
    }

    public Category getCategory() {
        return category;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getAddress() {
        return address;
    }

    public String getEventType() {
        return eventType;
    }

    public String getEventType_toString() {
        if(eventType.equalsIgnoreCase(EVENT_TYPE_FREE)) return AppController.getInstance().getString(R.string.text_free);
        if(eventType.equalsIgnoreCase(EVENT_TYPE_PAID)) return AppController.getInstance().getString(R.string.text_paid);
        return "";
    }

    public Date getDate() {
        return date;
    }

    public String getDate_toString() {
        String from = MyDateUtil.getddMMYYYYFromDate(date);
        return "Il: "+from;
    }

    public String getMonthName() {
        return MyDateUtil.getMonthName(date).toUpperCase();
    }

    public String getDay() {
        return MyDateUtil.getDateArrayFromDate(date)[0];
    }

    public Bitmap getImage() {
        return MyImageUtil.getBitmapFromByteArray(images.get(0).getImage());
    }

    public RealmList<AdImage> getImages() {
        return images;
    }

    public void setImages(RealmList<AdImage> images) {
        this.images = images;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getHashTags() {
        return hashTags;
    }

    public void setHashTags(String hashTags) {
        this.hashTags = hashTags;
    }

    public String getCity() {
        return city != null ? city  : "";
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "Ad{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category=" + category +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", address='" + address + '\'' +
                ", eventType='" + eventType + '\'' +
                ", dateFrom=" + date+
                '}';
    }
}