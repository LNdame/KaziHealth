package mandela.cct.ansteph.kazihealth.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import mandela.cct.ansteph.kazihealth.model.User;

@Dao
public interface UserDao {
    @Query("SELECT * From user")
    List<User> getall();

    @Query("SELECT * FROM user where _id IN (:userIds)")
    List<User> loadAllbyIds(int [] userIds);

    @Query("SELECT * FROM user WHERE email LIKE :email LIMIT 1")
    User findByUsername(String email);

    @Query("SELECT * FROM user where uid= :uid LIMIT 1")
    User findUserByUID(String uid);

    @Query("SELECT * FROM user where uid = :uid AND password = :password")
    List<User> checkPassword(String uid, String password);

    @Insert
    void insertAll (User... users);

    @Update
    void updateUser(User user);

    @Delete
    void delete(User user);

    @Query("UPDATE user SET profile_image = :image WHERE _id= :id")
    void updateUserImage( byte [] image , int id);

    @Query("UPDATE user SET password = :password WHERE uid= :uid")
    void updateUserPwd( String password, String uid);
}
