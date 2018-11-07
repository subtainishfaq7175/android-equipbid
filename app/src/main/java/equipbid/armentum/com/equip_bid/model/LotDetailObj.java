package equipbid.armentum.com.equip_bid.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import equipbid.armentum.com.equip_bid.Gallary;

/**
 * Created by darshan on 29-08-2017.
 */

public class LotDetailObj implements Parcelable {
    private String items;
    private String ean;
    private String title;
    private String description;
    private String upc;
    private String brand;
    private String model;
    private String color;
    private String dimension;
    private String size;
    private String weight;
    private String currency;
    private String lowest_recorded_price;
    private String highest_recorded_price;
    private String elid;
    private ArrayList<String> images;
    private ArrayList<LotDetailsOffer> offers;
    private ArrayList<Gallary> LotimgList;

    protected LotDetailObj(Parcel in) {
        items = in.readString();
        ean = in.readString();
        title = in.readString();
        description = in.readString();
        upc = in.readString();
        brand = in.readString();
        model = in.readString();
        color = in.readString();
        dimension = in.readString();
        size = in.readString();
        weight = in.readString();
        currency = in.readString();
        lowest_recorded_price = in.readString();
        highest_recorded_price = in.readString();
        elid = in.readString();
        images = in.createStringArrayList();
        offers = in.createTypedArrayList(LotDetailsOffer.CREATOR);
        LotimgList = in.createTypedArrayList(Gallary.CREATOR);
    }
    public LotDetailObj(){};
    public static final Creator<LotDetailObj> CREATOR = new Creator<LotDetailObj>() {
        @Override
        public LotDetailObj createFromParcel(Parcel in) {
            return new LotDetailObj(in);
        }

        @Override
        public LotDetailObj[] newArray(int size) {
            return new LotDetailObj[size];
        }
    };

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getEan() {
        return ean;
    }

    public void setEan(String ean) {
        this.ean = ean;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getLowest_recorded_price() {
        return lowest_recorded_price;
    }

    public void setLowest_recorded_price(String lowest_recorded_price) {
        this.lowest_recorded_price = lowest_recorded_price;
    }

    public String getHighest_recorded_price() {
        return highest_recorded_price;
    }

    public void setHighest_recorded_price(String highest_recorded_price) {
        this.highest_recorded_price = highest_recorded_price;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public String getElid() {
        return elid;
    }

    public void setElid(String elid) {
        this.elid = elid;
    }

    public ArrayList<LotDetailsOffer> getOffers() {
        return offers;
    }

    public void setOffers(ArrayList<LotDetailsOffer> offers) {
        this.offers = offers;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(items);
        dest.writeString(ean);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(upc);
        dest.writeString(brand);
        dest.writeString(model);
        dest.writeString(color);
        dest.writeString(dimension);
        dest.writeString(size);
        dest.writeString(weight);
        dest.writeString(currency);
        dest.writeString(lowest_recorded_price);
        dest.writeString(highest_recorded_price);
        dest.writeString(elid);
        dest.writeStringList(images);
        dest.writeTypedList(offers);
        dest.writeTypedList(LotimgList);
    }

    public ArrayList<Gallary> getLotimgList() {
        return LotimgList;
    }

    public void setLotimgList(ArrayList<Gallary> lotimgList) {
        LotimgList = lotimgList;
    }
}
