package com.dreammedia.dreammedia.adapter;

import android.os.Parcel;
import android.os.Parcelable;

public class WalletModel implements Parcelable {
    private String name;
    private int image;

    public WalletModel(String name, int image) {
        this.name = name;
        this.image = image;
    }

    protected WalletModel(Parcel in) {
        name = in.readString();
        image = in.readInt();
    }

    public static final Creator<WalletModel> CREATOR = new Creator<WalletModel>() {
        @Override
        public WalletModel createFromParcel(Parcel in) {
            return new WalletModel(in);
        }

        @Override
        public WalletModel[] newArray(int size) {
            return new WalletModel[size];
        }
    };

    public String getName() {
        return name;
    }

    public int getImage() {
        return image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(image);
    }
}