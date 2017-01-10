package app.rbzeta.edcimplementationtracker.application;

/**
 * Created by Robyn on 12/30/2016.
 */

public class AppConfig {

    //URL Services related constants
    public static final String BASE_URL = "http://202.169.39.114/";
    public static final String GET_EDC_LIST_STAGING_URL = "jakers/edc/ws/ws_get_edc_staging_list.php";
    public static final String SUBMIT_EDC_IMPLEMENTATION_URL = "jakers/edc/ws/ws_submit_edc_implementation.php";
    public static final String GET_EDC_LIST_COMPLETED_URL = "jakers/edc/ws/ws_get_edc_implemented_list.php";
    public static final String GET_EDC_LIST_VERIFIED_URL = "jakers/edc/ws/ws_get_edc_verified_list.php";
    public static final String USER_LOGIN_URL = "jakers/edc/ws/ws_user_login.php";
    public static final String VERIFIED_EDC_IMPLEMENTATION_URL = "jakers/edc/ws/ws_verify_edc_implementation.php";
    public static final String CANCELED_EDC_IMPLEMENTATION_URL = "jakers/edc/ws/ws_cancel_edc_implementation.php";
    public static final String CANCELED_EDC_VERIFICATION_URL = "jakers/edc/ws/ws_cancel_edc_verification.php";

    //preference related constants
    public static final String PREF_NAME = "edcimptrack_pref_name";
    public static final String PREF_KEY_IS_LOGGED_IN = "pref_key_user_is_logged_in";
    public static final String PREF_KEY_BRANCH_ID = "pref_key_user_branch_id";
    public static final String PREF_KEY_USER_BRANCH_NAME = "pref_key_user_branch_name";
    public static final String PREF_KEY_USER_EMAIL = "pref_key_user_email";
    public static final String PREF_KEY_USER_UKER = "pref_key_user_uker";
    public static final String PREF_KEY_USER_LEVEL_ID = "pref_key_user_level_id";
    public static final String PREF_KEY_USER_PN = "pref_key_user_pn";
    public static final String PREF_KEY_USER_NAME = "pref_key_user_name";

    public static final String STR_FILE_PROVIDER = "app.rbzeta.edcimplementationtracker.fileprovider";
    public static final String DIR_IMG_FILE_NAME = "/EDC";
    public static final String PREFIX_IMG_FILE_NAME = "EDC_";
    public static final java.lang.String FORMAT_JPG = ".jpg";
}
