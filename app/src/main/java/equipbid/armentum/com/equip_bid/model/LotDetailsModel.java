package equipbid.armentum.com.equip_bid.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;
import java.security.PrivateKey;
import java.util.ArrayList;

/**
 * Created by darshan on 29-08-2017.
 */

public class LotDetailsModel implements Parcelable {
    private String code, total, offset, Auction_name, lot_no;
    private ArrayList<LotDetailObj> items;
    private LotDetailServer lotdetailsserver;
    private ArrayList<String> Cam_Images;
    private String MFile;
    private String ScanResult, Lot_S ,Lot_S_o,LocalVar, Auction_n_D, Lot_n_D;

    protected LotDetailsModel(Parcel in) {
        code = in.readString();
        total = in.readString();
        offset = in.readString();
        Auction_name = in.readString();
        lot_no = in.readString();
        items = in.createTypedArrayList(LotDetailObj.CREATOR);
        lotdetailsserver = in.readParcelable(LotDetailServer.class.getClassLoader());
        Cam_Images = in.createStringArrayList();
        MFile = in.readString();
        ScanResult = in.readString();
        Lot_S = in.readString();
        Lot_S_o = in.readString();
        LocalVar = in.readString();
        Auction_n_D = in.readString();
        Lot_n_D = in.readString();
    }

    public static final Creator<LotDetailsModel> CREATOR = new Creator<LotDetailsModel>() {
        @Override
        public LotDetailsModel createFromParcel(Parcel in) {
            return new LotDetailsModel(in);
        }

        @Override
        public LotDetailsModel[] newArray(int size) {
            return new LotDetailsModel[size];
        }
    };

    public String getScanResult() {
        return ScanResult;
    }

    public void setScanResult(String scanResult) {
        ScanResult = scanResult;
    }

    public String getLot_S() {
        return Lot_S;
    }

    public void setLot_S(String lot_S) {
        Lot_S = lot_S;
    }

    public String getLot_S_o() {
        return Lot_S_o;
    }

    public void setLot_S_o(String lot_S_o) {
        Lot_S_o = lot_S_o;
    }

    public String getLocalVar() {
        return LocalVar;
    }

    public void setLocalVar(String localVar) {
        LocalVar = localVar;
    }

    public String getAuction_n_D() {
        return Auction_n_D;
    }

    public void setAuction_n_D(String auction_n_D) {
        Auction_n_D = auction_n_D;
    }

    public String getLot_n_D() {
        return Lot_n_D;
    }

    public void setLot_n_D(String lot_n_D) {
        Lot_n_D = lot_n_D;
    }

    public LotDetailsModel(){};

    public void setCode(String code) {
        this.code = code;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public String getCode() {
        return code;
    }

    public ArrayList<LotDetailObj> getItems() {
        return items;
    }

    public void setItems(ArrayList<LotDetailObj> items) {
        this.items = items;
    }

    public String getAuction_name() {
        return Auction_name;
    }

    public void setAuction_name(String auction_name) {
        Auction_name = auction_name;
    }

    public String getLot_no() {
        return lot_no;
    }

    public void setLot_no(String lot_no) {
        this.lot_no = lot_no;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(code);
        dest.writeString(total);
        dest.writeString(offset);
        dest.writeString(Auction_name);
        dest.writeString(lot_no);
        dest.writeTypedList(items);
        dest.writeParcelable(lotdetailsserver, flags);
        dest.writeStringList(Cam_Images);
        dest.writeString(MFile);
        dest.writeString(ScanResult);
        dest.writeString(Lot_S);
        dest.writeString(Lot_S_o);
        dest.writeString(LocalVar);
        dest.writeString(Auction_n_D);
        dest.writeString(Lot_n_D);
    }


    public LotDetailServer getLotdetailsserver() {
        return lotdetailsserver;
    }

    public void setLotdetailsserver(LotDetailServer lotdetailsserver) {
        this.lotdetailsserver = lotdetailsserver;
    }

    public ArrayList<String> getCam_Images() {
        return Cam_Images;
    }

    public void setCam_Images(ArrayList<String> cam_Images) {
        Cam_Images = cam_Images;
    }

    public String getMFile() {
        return MFile;
    }

    public void setMFile(String MFile) {
        this.MFile = MFile;
    }
}
