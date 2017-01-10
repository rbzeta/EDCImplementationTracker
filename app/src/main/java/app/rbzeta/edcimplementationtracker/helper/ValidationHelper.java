package app.rbzeta.edcimplementationtracker.helper;

/**
 * Created by Robyn on 1/8/2017.
 */

public class ValidationHelper {

    public static boolean isValidEmail(String email){
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
