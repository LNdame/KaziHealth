package mandela.cct.ansteph.kazihealth.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

/**
 * Created by loicstephan on 2018/06/14.
 */
@Entity(tableName = "risk_item")
public class RiskItem implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name ="_id")
    private int id;

    @ColumnInfo(name ="name")
    String name;

    @ColumnInfo(name ="unit")
    String unit;

    @ColumnInfo(name ="date_created")
    String date_created;

    @Ignore
    public RiskItem(int id, String name, String unit, String date_created) {
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.date_created = date_created;
    }


    public RiskItem(String name, String unit, String date_created) {
        this.name = name;
        this.unit = unit;
        this.date_created = date_created;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }
}
