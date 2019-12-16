package mandela.cct.ansteph.kazihealth.api.columns;

/**
 * Created by loicstephan on 2018/06/14.
 */

public interface RiskProfileColumns extends DataColumns {
    String USER_ID  = "user_id";
    String RISK_ITEM_ID  = "risk_item_id";
    String MEASUREMENT = "measurement";
    String COMMENT = "comment";

    String[]PROJECTION = new String[]{_ID,USER_ID,RISK_ITEM_ID,MEASUREMENT, COMMENT};
}
