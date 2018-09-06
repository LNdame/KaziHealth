package mandela.cct.ansteph.kazihealth.model;

import java.io.Serializable;

/**
 * Created by loicstephan on 2018/06/14.
 */

public class User implements Serializable {

    int id;
    String  name, email,kh_number, password, dob , gender;

    byte [] profilePic;

    public User() {
    }

    public User(int id, String name, String email, String kh_number, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.kh_number = kh_number;
        this.password = password;
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getKh_number() {
        return kh_number;
    }

    public void setKh_number(String kh_number) {
        this.kh_number = kh_number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public byte[] getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(byte[] profilePic) {
        this.profilePic = profilePic;
    }
}
