package equipbid.armentum.com.equip_bid;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Darshan on 10/25/2017.
 */

public class User implements Parcelable {

    private String id, name, user_email, phone, role, profile_image;

    public User(String id, String name, String user_email, String phone, String role, String profile_image) {
        id = id;
        name = name;
        user_email = user_email;
        phone = phone;
        role = role;
        profile_image = profile_image;
    }

    public User() {

    }

    public User(String id, String name) {
        id = id;
        name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    @Override
    public String toString() { return name; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.user_email);
        dest.writeString(this.phone);
        dest.writeString(this.role);
        dest.writeString(this.profile_image);
    }

    private User(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.user_email = in.readString();
        this.phone = in.readString();
        this.role = in.readString();
        this.profile_image = in.readString();

    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}

