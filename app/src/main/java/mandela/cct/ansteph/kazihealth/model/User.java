package mandela.cct.ansteph.kazihealth.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

/**
 * Created by loicstephan on 2018/06/14.
 */
@Entity(tableName = "user")
public class User implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name ="_id")
    private int id;

    @ColumnInfo(name="uid")
    private String uid;

    @ColumnInfo(name ="name")
    private String  name;

    @ColumnInfo(name ="email")
    private String email;

    @ColumnInfo(name ="kh_number")
    private String kh_number;

    @ColumnInfo(name ="password")
    private String password;

    @ColumnInfo(name ="dob")
    private String dob ;

    @ColumnInfo(name ="gender")
    private String gender;

    @ColumnInfo(name ="profile_image")
    private byte [] profilePic;

    @Ignore
    public User(){}

    public User(String uid,String name, String email, String kh_number, String password, String dob, String gender) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.kh_number = kh_number;
        this.password = password;
        this.dob = dob;
        this.gender = gender;
    }

    @Ignore
    public User(int id, String uid, String name, String email, String kh_number, String password, String dob, String gender) {
        this.id = id;
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.kh_number = kh_number;
        this.password = password;
        this.dob = dob;
        this.gender = gender;
    }

    @Ignore
    public User(int id, String name, String email, String kh_number, String password, String dob, String gender) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.kh_number = kh_number;
        this.password = password;
        this.dob = dob;
        this.gender = gender;
    }

     @Ignore
     public User (String name, String email,String password){
         this.name = name;
         this.email = email;
         this.password = password;
     }

    @Ignore
    public User (String name, String email,String password, String dob){
        this.name = name;
        this.email = email;
        this.password = password;
        this.dob = dob;
    }

    @Ignore
    public User (int id, String name, String email, String dob, String password, String gender ){
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.dob = dob;
        this.gender = gender;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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
