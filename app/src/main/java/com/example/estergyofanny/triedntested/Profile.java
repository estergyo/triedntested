package com.example.estergyofanny.triedntested;

import android.os.Parcel;
import android.os.Parcelable;

public class Profile implements Parcelable {
    private String textNama;
    private String textNIM;
    private String textEmail; //ganti sama no hp
    private String textTTL;
    private String image;

    public Profile(){
        textNama = "";
        textNIM = "";
        textEmail = "";
        textTTL = "";
        image = "";
    }

    public Profile(String textNama, String textNIM, String textEmail, String textTTL, String image){
        this.textNama = textNama;
        this.textNIM = textNIM;
        this.textEmail = textEmail;
        this.textTTL = textTTL;
        this.image = image;
    }
    public Profile(Parcel in){
        String[] data = new String[5];

        in.readStringArray(data);
        this.textNama = data[0];
        this.textNIM = data[1];
        this.textEmail = data[2];
        this.textTTL = data[3];
        this.image = data[4];
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {
                this.textNama,
                this.textNIM,
                this.textEmail,
                this.textTTL,
                this.image});
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Profile createFromParcel(Parcel in) {
            return new Profile(in);
        }

        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };

    public String getTextNama() {
        return textNama;
    }

    public void setTextNama(String textNama) {
        this.textNama = textNama;
    }

    public String getTextNIM() {
        return textNIM;
    }

    public void setTextNIM(String textNIM) {
        this.textNIM = textNIM;
    }

    public String getTextNoHP() {
        return textEmail;
    }

    public void setTextNoHP(String textEmail) {
        this.textEmail = textEmail;
    }

    public String getTextTTL() {
        return textTTL;
    }

    public void setTextTTL(String textTTL) {
        this.textTTL = textTTL;
    }

    public String getTextImage() {
        return image;
    }

    public void setTextImage(String image) {
        this.image = image;
    }
}
