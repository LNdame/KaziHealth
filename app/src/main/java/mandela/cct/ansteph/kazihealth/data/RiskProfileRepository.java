package mandela.cct.ansteph.kazihealth.data;

import android.content.Context;

import java.util.List;

import mandela.cct.ansteph.kazihealth.model.RiskProfileItem;

public class RiskProfileRepository {

    private static RiskProfileRepository instance;
    private KaziDatabase kDB;
    private RiskProfileRepository(Context context){
        kDB = KaziDatabase.getInstance(context);
    }

    public static synchronized RiskProfileRepository getInstance(Context context) {
        if(instance==null){
            instance = new RiskProfileRepository(context);
        }
        return instance;
    }


    public List<RiskProfileItem> retrieveProfileItems(int userID) {
       return kDB.riskProfileDao().getAllRiskProfileItem(userID);

    }

}
