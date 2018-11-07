package equipbid.armentum.com.equip_bid.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import equipbid.armentum.com.equip_bid.Add_Detail;
import equipbid.armentum.com.equip_bid.CustomGallery;

/**
 * Created by darshan on 29-08-2017.
 */

public class LotDetailsData implements Parcelable{
    private String lot_no, post_id, lot_status, auction_no, item_title, seller_id, upc, description;
    private String item_class, condition,add_detalils, location,start_price,reverse_price, quantity,textable, msrp,make,model, width;
    private String depth, height,appears_new,damage, open_box, factory_sealed,incomplete, plugged_in_and_works,used,parts_only,powers_on;
    private String cools_to_temperature;
    private String brand;
    private String color;
    private String category;
    private String additional_details_checklist;
    private ArrayList<String> SpinList;
    private ArrayList<String> AdditionalList;
    private int Spinselect;
    private ArrayList<Add_Detail> AddDetailList;
    private ArrayList<CustomGallery> dataT;
    private ArrayList<CustomGallery> dataG;
    private ArrayList<CustomGallery> dataC;

    public LotDetailsData(){};

    protected LotDetailsData(Parcel in) {
        lot_no = in.readString();
        post_id = in.readString();
        lot_status = in.readString();
        auction_no = in.readString();
        item_title = in.readString();
        seller_id = in.readString();
        upc = in.readString();
        description = in.readString();
        item_class = in.readString();
        condition = in.readString();
        add_detalils = in.readString();
        location = in.readString();
        start_price = in.readString();
        reverse_price = in.readString();
        quantity = in.readString();
        textable = in.readString();
        msrp = in.readString();
        make = in.readString();
        model = in.readString();
        width = in.readString();
        depth = in.readString();
        height = in.readString();
        appears_new = in.readString();
        damage = in.readString();
        open_box = in.readString();
        factory_sealed = in.readString();
        incomplete = in.readString();
        plugged_in_and_works = in.readString();
        used = in.readString();
        parts_only = in.readString();
        powers_on = in.readString();
        cools_to_temperature = in.readString();
        brand = in.readString();
        color = in.readString();
        category = in.readString();
        additional_details_checklist = in.readString();
        SpinList = in.createStringArrayList();
        AdditionalList = in.createStringArrayList();
        Spinselect = in.readInt();
        AddDetailList = in.createTypedArrayList(Add_Detail.CREATOR);
        dataT = in.createTypedArrayList(CustomGallery.CREATOR);
        dataG = in.createTypedArrayList(CustomGallery.CREATOR);
        dataC = in.createTypedArrayList(CustomGallery.CREATOR);
        thumbnail = in.readString();
    }

    public static final Creator<LotDetailsData> CREATOR = new Creator<LotDetailsData>() {
        @Override
        public LotDetailsData createFromParcel(Parcel in) {
            return new LotDetailsData(in);
        }

        @Override
        public LotDetailsData[] newArray(int size) {
            return new LotDetailsData[size];
        }
    };

    public String getLot_no() {
        return lot_no;
    }

    public void setLot_no(String lot_no) {
        this.lot_no = lot_no;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getLot_status() {
        return lot_status;
    }

    public void setLot_status(String lot_status) {
        this.lot_status = lot_status;
    }

    public String getAuction_no() {
        return auction_no;
    }

    public void setAuction_no(String auction_no) {
        this.auction_no = auction_no;
    }

    public String getItem_title() {
        return item_title;
    }

    public void setItem_title(String item_title) {
        this.item_title = item_title;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getItem_class() {
        return item_class;
    }

    public void setItem_class(String item_class) {
        this.item_class = item_class;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getAdd_detalils() {
        return add_detalils;
    }

    public void setAdd_detalils(String add_detalils) {
        this.add_detalils = add_detalils;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStart_price() {
        return start_price;
    }

    public void setStart_price(String start_price) {
        this.start_price = start_price;
    }

    public String getReverse_price() {
        return reverse_price;
    }

    public void setReverse_price(String reverse_price) {
        this.reverse_price = reverse_price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTextable() {
        return textable;
    }

    public void setTextable(String textable) {
        this.textable = textable;
    }

    public String getMsrp() {
        return msrp;
    }

    public void setMsrp(String msrp) {
        this.msrp = msrp;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getDepth() {
        return depth;
    }

    public void setDepth(String depth) {
        this.depth = depth;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getAppears_new() {
        return appears_new;
    }

    public void setAppears_new(String appears_new) {
        this.appears_new = appears_new;
    }

    public String getDamage() {
        return damage;
    }

    public void setDamage(String damage) {
        this.damage = damage;
    }

    public String getOpen_box() {
        return open_box;
    }

    public void setOpen_box(String open_box) {
        this.open_box = open_box;
    }

    public String getFactory_sealed() {
        return factory_sealed;
    }

    public void setFactory_sealed(String factory_sealed) {
        this.factory_sealed = factory_sealed;
    }

    public String getIncomplete() {
        return incomplete;
    }

    public void setIncomplete(String incomplete) {
        this.incomplete = incomplete;
    }

    public String getPlugged_in_and_works() {
        return plugged_in_and_works;
    }

    public void setPlugged_in_and_works(String plugged_in_and_works) {
        this.plugged_in_and_works = plugged_in_and_works;
    }

    public String getUsed() {
        return used;
    }

    public void setUsed(String used) {
        this.used = used;
    }

    public String getParts_only() {
        return parts_only;
    }

    public void setParts_only(String parts_only) {
        this.parts_only = parts_only;
    }

    public String getPowers_on() {
        return powers_on;
    }

    public void setPowers_on(String powers_on) {
        this.powers_on = powers_on;
    }

    public String getCools_to_temperature() {
        return cools_to_temperature;
    }

    public void setCools_to_temperature(String cools_to_temperature) {
        this.cools_to_temperature = cools_to_temperature;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAdditional_details_checklist() {
        return additional_details_checklist;
    }

    public void setAdditional_details_checklist(String additional_details_checklist) {
        this.additional_details_checklist = additional_details_checklist;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    private String thumbnail;

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(lot_no);
        dest.writeString(post_id);
        dest.writeString(lot_status);
        dest.writeString(auction_no);
        dest.writeString(item_title);
        dest.writeString(seller_id);
        dest.writeString(upc);
        dest.writeString(description);
        dest.writeString(item_class);
        dest.writeString(condition);
        dest.writeString(add_detalils);
        dest.writeString(location);
        dest.writeString(start_price);
        dest.writeString(reverse_price);
        dest.writeString(quantity);
        dest.writeString(textable);
        dest.writeString(msrp);
        dest.writeString(make);
        dest.writeString(model);
        dest.writeString(width);
        dest.writeString(depth);
        dest.writeString(height);
        dest.writeString(appears_new);
        dest.writeString(damage);
        dest.writeString(open_box);
        dest.writeString(factory_sealed);
        dest.writeString(incomplete);
        dest.writeString(plugged_in_and_works);
        dest.writeString(used);
        dest.writeString(parts_only);
        dest.writeString(powers_on);
        dest.writeString(cools_to_temperature);
        dest.writeString(brand);
        dest.writeString(color);
        dest.writeString(category);
        dest.writeString(additional_details_checklist);
        dest.writeStringList(SpinList);
        dest.writeStringList(AdditionalList);
        dest.writeInt(Spinselect);
        dest.writeTypedList(AddDetailList);
        dest.writeTypedList(dataT);
        dest.writeTypedList(dataG);
        dest.writeTypedList(dataC);
        dest.writeString(thumbnail);
    }


    public ArrayList<String> getSpinList() {
        return SpinList;
    }

    public void setSpinList(ArrayList<String> spinList) {
        SpinList = spinList;
    }

    public int getSpinselect() {
        return Spinselect;
    }

    public void setSpinselect(int spinselect) {
        Spinselect = spinselect;
    }

    public ArrayList<Add_Detail> getAddDetailList() {
        return AddDetailList;
    }

    public void setAddDetailList(ArrayList<Add_Detail> addDetailList) {
        AddDetailList = addDetailList;
    }

    public ArrayList<CustomGallery> getDataT() {
        return dataT;
    }

    public void setDataT(ArrayList<CustomGallery> dataT) {
        this.dataT = dataT;
    }

    public ArrayList<CustomGallery> getDataG() {
        return dataG;
    }

    public void setDataG(ArrayList<CustomGallery> dataG) {
        this.dataG = dataG;
    }

    public ArrayList<CustomGallery> getDataC() {
        return dataC;
    }

    public void setDataC(ArrayList<CustomGallery> dataC) {
        this.dataC = dataC;
    }

    public ArrayList<String> getAdditionalList() {
        return AdditionalList;
    }

    public void setAdditionalList(ArrayList<String> additionalList) {
        AdditionalList = additionalList;
    }
}
