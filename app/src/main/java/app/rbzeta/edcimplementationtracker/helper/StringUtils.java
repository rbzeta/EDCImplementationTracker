package app.rbzeta.edcimplementationtracker.helper;

import java.text.SimpleDateFormat;
import java.util.Date;

import app.rbzeta.edcimplementationtracker.application.AppConfig;

/**
 * Created by Robyn on 11/29/2016.
 */

public class StringUtils {

    public static final String getStringTimeStamp(){
        return new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    }

    public static String getImageFileName() {
        return AppConfig.PREFIX_IMG_FILE_NAME + getStringTimeStamp();
    }
}
