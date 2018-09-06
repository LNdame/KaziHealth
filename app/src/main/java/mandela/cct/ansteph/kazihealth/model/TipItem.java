package mandela.cct.ansteph.kazihealth.model;

import java.io.Serializable;

/**
 * Created by loicstephan on 2018/05/18.
 */

public class TipItem implements Serializable {

    int id;
    String position, name;

    public TipItem(int id, String position, String name) {
        this.id = id;
        this.position = position;
        this.name = name;
    }

    public TipItem(String position, String name) {
        this.position = position;
        this.name = name;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
