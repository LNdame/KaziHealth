package mandela.cct.ansteph.kazihealth.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

/**
 * Created by loicstephan on 2018/06/14.
 */
@Entity(tableName = "risk_profile")
public class RiskProfileItem implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    private int id;

    @ColumnInfo(name = "user_id")
    private int user_id;

    @ColumnInfo(name = "risk_item_id")
    private int risk_item_id;

    @ColumnInfo(name = "measurement")
    private String measurement;

    @ColumnInfo(name = "comment")
    private String comment;

    @Ignore
    public RiskProfileItem() {
    }

    @Ignore
    public RiskProfileItem(int id, int user_id, int risk_item_id, String measurement, String comment) {
        this.id = id;
        this.user_id = user_id;
        this.risk_item_id = risk_item_id;
        this.measurement = measurement;
        this.comment = comment;
    }

    public RiskProfileItem(int user_id, int risk_item_id, String measurement, String comment) {
        this.user_id = user_id;
        this.risk_item_id = risk_item_id;
        this.measurement = measurement;
        this.comment = comment;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getRisk_item_id() {
        return risk_item_id;
    }

    public void setRisk_item_id(int risk_item_id) {
        this.risk_item_id = risk_item_id;
    }

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


}
