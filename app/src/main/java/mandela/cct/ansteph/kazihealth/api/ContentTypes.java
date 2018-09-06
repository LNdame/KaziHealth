package mandela.cct.ansteph.kazihealth.api;

import android.net.Uri;

/**
 * Created by loicstephan on 2018/06/14.
 */

public class ContentTypes {



    public static final Uri USER_CONTENT_URI = Uri.parse("content://mandela.cct.ansteph.kazihealth.contentprovider.usercontentprovider/kazihealth");
    public static final Uri RISK_PROFILE_CONTENT_URI = Uri.parse("content://mandela.cct.ansteph.kazihealth.contentprovider.riskprofilecontentprovider/kazihealth");
    public static final Uri RISK_ITEM_CONTENT_URI = Uri.parse("content://mandela.cct.ansteph.kazihealth.contentprovider.riskitemcontentprovider/kazihealth");

}
