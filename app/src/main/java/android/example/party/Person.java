package android.example.party;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
public class Person {
    private String name;
    private String avatar;

    public Person(String name, String avatar) {
        this.name = name;
        this.avatar = avatar;
    }

    public Person() {
    }

    public String getName() {
        return name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
