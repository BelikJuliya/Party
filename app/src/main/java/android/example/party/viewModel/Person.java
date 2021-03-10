package android.example.party.viewModel;

import android.graphics.Bitmap;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
public class Person {
    private String mName;
    private String mUrl;
    private boolean isInviter;
    private Bitmap mAvatar;

    public Person(String name, String url, boolean isInviter) {
        mName = name;
        mUrl = url;
        this.isInviter = isInviter;
    }

    public Person(String name, Bitmap avatar) {
        mName = name;
        mAvatar = avatar;
    }

    public String getName() {
        return mName;
    }

    public String getUrl() {
        return mUrl;
    }

    public boolean isInviter() {
        return isInviter;
    }

    public Bitmap getAvatar() {
        return mAvatar;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setAvatar(String avatar) {
        mName = avatar;
    }

    public void setInviter(boolean inviter) {
        isInviter = inviter;
    }
}
