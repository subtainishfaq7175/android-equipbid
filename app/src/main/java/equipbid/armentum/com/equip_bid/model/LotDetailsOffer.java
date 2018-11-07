package equipbid.armentum.com.equip_bid.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by darshan on 29-08-2017.
 */

public class LotDetailsOffer implements Parcelable {

    protected LotDetailsOffer(Parcel in) {
        merchant = in.readString();
        domain = in.readString();
        title = in.readString();
        currency = in.readString();
        list_price = in.readString();
        price = in.readString();
        shipping = in.readString();
        condition = in.readString();
        availability = in.readString();
        link = in.readString();
        updated_t = in.readString();
    }

    public static final Creator<LotDetailsOffer> CREATOR = new Creator<LotDetailsOffer>() {
        @Override
        public LotDetailsOffer createFromParcel(Parcel in) {
            return new LotDetailsOffer(in);
        }

        @Override
        public LotDetailsOffer[] newArray(int size) {
            return new LotDetailsOffer[size];
        }
    };

    public LotDetailsOffer(){};
    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getList_price() {
        return list_price;
    }

    public void setList_price(String list_price) {
        this.list_price = list_price;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getShipping() {
        return shipping;
    }

    public void setShipping(String shipping) {
        this.shipping = shipping;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getUpdated_t() {
        return updated_t;
    }

    public void setUpdated_t(String updated_t) {
        this.updated_t = updated_t;
    }

    private String merchant,domain,title,currency,list_price,price,shipping,condition,availability,link,updated_t;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(merchant);
        dest.writeString(domain);
        dest.writeString(title);
        dest.writeString(currency);
        dest.writeString(list_price);
        dest.writeString(price);
        dest.writeString(shipping);
        dest.writeString(condition);
        dest.writeString(availability);
        dest.writeString(link);
        dest.writeString(updated_t);
    }
}
