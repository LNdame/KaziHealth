package mandela.cct.ansteph.kazihealth.data;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

import mandela.cct.ansteph.kazihealth.model.RiskItem;
import mandela.cct.ansteph.kazihealth.model.User;

@Dao
public interface RiskItemDao {
    @Query("SELECT * FROM risk_item")
    List<RiskItem> getAllRiskItem();

    @Query("SELECT * FROM risk_item WHERE _id LIKE :id LIMIT 1")
    User findById(int id);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll (RiskItem... riskItems);

    @Update
    void updateRiskItem(RiskItem riskItem);

    @Delete
    void delete(RiskItem riskItem);

    @Query("DELETE FROM risk_item ")
    void deleteAll();
}
