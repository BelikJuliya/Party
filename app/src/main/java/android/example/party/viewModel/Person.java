package android.example.party.viewModel;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
public class Person {
    private String mName;
    private String mAvatar;
    private boolean isInviter;

    public Person(String name, String avatar, boolean isInviter) {
        mName = name;
        mAvatar = avatar;
        this.isInviter = isInviter;
    }

    public Person() {
    }

    public String getName() {
        return mName;
    }

    public String getAvatar() {
        return mAvatar;
    }

    public boolean isInviter() {
        return isInviter;
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
