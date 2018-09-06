package mandela.cct.ansteph.kazihealth.api.columns;

/**
 * Created by loicstephan on 2018/06/14.
 */

public interface UserColumns extends DataColumns {

    String NAME  = "name";
    String EMAIL  = "email";
    String DOB  = "dob";
    String GENDER  = "gender";
    String PROFILE_IMAGE = "profile_image";
    String KH_NUMBER = "kh_number";
    String PASSWORD = "password";


    String[]PROJECTION = new String[]{_ID,NAME,EMAIL,PROFILE_IMAGE,KH_NUMBER,DOB,GENDER};
}
