package mandela.cct.ansteph.kazihealth.api.columns;

/**
 * Created by loicstephan on 2018/06/14.
 */

public interface RiskItemColumns extends DataColumns {


    String NAME  = "name";
    String UNIT  = "unit";

    String[]PROJECTION = new String[]{_ID,NAME,UNIT};
}
