package app.rbzeta.edcimplementationtracker.helper;

import android.content.Context;
import android.content.SharedPreferences;

import app.rbzeta.edcimplementationtracker.application.AppConfig;
import app.rbzeta.edcimplementationtracker.model.User;

/**
 * Created by Robyn on 12/30/2016.
 */

public class SessionManager {

    private String TAG = SessionManager.class.getSimpleName();

    SharedPreferences preferences;

    SharedPreferences.Editor editor;

    Context mContext;

    public SessionManager(Context context, int mode){
        mContext = context;
        preferences = mContext.getSharedPreferences(AppConfig.PREF_NAME,mode);
        editor = preferences.edit();
    }

    public void setUserLogin(boolean isLoggedIn){

        editor.putBoolean(AppConfig.PREF_KEY_IS_LOGGED_IN,isLoggedIn);

        editor.commit();
    }

    public boolean isUserLogin(){
        return preferences.getBoolean(AppConfig.PREF_KEY_IS_LOGGED_IN,false);
    }

    public void setUserBranchId(String branchId) {
        editor.putInt(AppConfig.PREF_KEY_BRANCH_ID,Integer.valueOf(branchId));
        editor.commit();
    }

    public int getUserBranchId(){
        return preferences.getInt(AppConfig.PREF_KEY_BRANCH_ID,0);
    }

    public void deleteSharedPreference(){
        editor.clear();
        editor.commit();
    }

    public void setUserName(String userName){
        editor.putString(AppConfig.PREF_KEY_USER_NAME,userName);
        editor.commit();
    }

    public String getUserName(){
        return preferences.getString(AppConfig.PREF_KEY_USER_NAME,null);
    }

    public void setUserPN(String userPN){
        editor.putString(AppConfig.PREF_KEY_USER_PN,userPN);
        editor.commit();
    }

    public String getUserPN(){
        return preferences.getString(AppConfig.PREF_KEY_USER_PN,null);
    }

    public void setUserLevelId(int levelId) {
        editor.putInt(AppConfig.PREF_KEY_USER_LEVEL_ID,levelId);
        editor.commit();
    }

    public int getUserLevelId(){
        return preferences.getInt(AppConfig.PREF_KEY_USER_LEVEL_ID,0);
    }

    public void setUserUker(int userUker) {
        editor.putInt(AppConfig.PREF_KEY_USER_UKER,userUker);
        editor.commit();
    }

    public int getUserUker(){
        return preferences.getInt(AppConfig.PREF_KEY_USER_UKER,0);
    }

    public void setUserEmail(String userEmail){
        editor.putString(AppConfig.PREF_KEY_USER_EMAIL,userEmail);
        editor.commit();
    }

    public String getUserEmail(){
        return preferences.getString(AppConfig.PREF_KEY_USER_EMAIL,null);
    }

    public void setUserBranchName(String branchName){
        editor.putString(AppConfig.PREF_KEY_USER_BRANCH_NAME,branchName);
        editor.commit();
    }

    public String getUserBranchName(){
        return preferences.getString(AppConfig.PREF_KEY_USER_BRANCH_NAME,null);
    }

    public void setUserSharedPreferences(User user) {
        setUserBranchId(user.getUserBranchCode());
        setUserBranchName(user.getUserBranchName());
        setUserEmail(user.getUserEmail());
        setUserLevelId(user.getUserLevelId());
        setUserName(user.getUserName());
        setUserPN(user.getUserPN());
        setUserUker(user.getUserUker());
    }
}
