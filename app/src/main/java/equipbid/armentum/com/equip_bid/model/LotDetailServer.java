package equipbid.armentum.com.equip_bid.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by darshan on 29-08-2017.
 */

public class LotDetailServer implements Parcelable {
    private  String id, code;
    private  LotDetailsData lotdetailsdata;

    protected LotDetailServer(Parcel in) {
        id = in.readString();
        code = in.readString();
        lotdetailsdata = in.readParcelable(LotDetailsData.class.getClassLoader());
    }
    public LotDetailServer(){};

    public static final Creator<LotDetailServer> CREATOR = new Creator<LotDetailServer>() {
        @Override
        public LotDetailServer createFromParcel(Parcel in) {
            return new LotDetailServer(in);
        }

        @Override
        public LotDetailServer[] newArray(int size) {
            return new LotDetailServer[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LotDetailsData getLotdetailsdata() {
        return lotdetailsdata;
    }

    public void setLotdetailsdata(LotDetailsData lotdetailsdata) {
        this.lotdetailsdata = lotdetailsdata;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(code);
        dest.writeParcelable(lotdetailsdata, flags);
    }
}
