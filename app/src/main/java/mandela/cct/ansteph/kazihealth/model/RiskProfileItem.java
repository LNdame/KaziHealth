package mandela.cct.ansteph.kazihealth.model;

import java.io.Serializable;

/**
 * Created by loicstephan on 2018/06/14.
 */

public class RiskProfileItem implements Serializable {


    int id, user_id, risk_item_id;
    String measurement, comment;


    public RiskProfileItem() {
    }

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
