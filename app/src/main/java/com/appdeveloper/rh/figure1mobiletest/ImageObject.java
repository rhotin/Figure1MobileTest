package com.appdeveloper.rh.figure1mobiletest;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Roman on 2/18/2018.
 */

public class ImageObject implements Parcelable {
    String id;
    String title;

    ImageObject() {
    }

    ImageObject(String id, String title) {
        this.id = id;
        this.title = title;
    }

    protected ImageObject(Parcel in) {
        id = in.readString();
        title = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ImageObject> CREATOR = new Creator<ImageObject>() {
        @Override
        public ImageObject createFromParcel(Parcel in) {
            return new ImageObject(in);
        }

        @Override
        public ImageObject[] newArray(int size) {
            return new ImageObject[size];
        }
    };
}
