package mandela.cct.ansteph.kazihealth.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import mandela.cct.ansteph.kazihealth.model.RiskProfileItem;

@Dao
public interface RiskProfileDao {

    @Query("SELECT * FROM risk_profile ORDER BY _id ASC")
    List<RiskProfileItem> getAllRiskProfileItem();

    @Query("SELECT * FROM risk_profile WHERE user_id = :userId ORDER BY _id DESC")
    List<RiskProfileItem> getAllRiskProfileItem(int userId);

    @Query("SELECT * FROM risk_profile WHERE user_id = :userId AND risk_item_id = :riskItemId ")
    RiskProfileItem getRiskProfileItem(int userId, int riskItemId );

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(RiskProfileItem riskProfileItem);

    @Insert
    void insertAll(RiskProfileItem... riskProfileItems);

    @Query("DELETE FROM risk_profile")
    void deleteAll();

    @Update
    void updateRiskProfileItem(RiskProfileItem riskProfileItem);

    @Delete
    void delete(RiskProfileItem riskProfileItem);
}
