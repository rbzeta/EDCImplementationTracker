package app.rbzeta.edcimplementationtracker.application;

import android.app.Application;

import app.rbzeta.edcimplementationtracker.database.MyDBHandler;
import app.rbzeta.edcimplementationtracker.helper.SessionManager;
import app.rbzeta.edcimplementationtracker.network.NetworkService;

/**
 * Created by Robyn on 12/30/2016.
 */

public class MyApplication extends Application {
    private static MyApplication mInstance;
    private NetworkService mNetworkService;
    private MyDBHandler myDBHandler;
    private SessionManager mSessionManager;

    @Override
    public void onCreate(){
        super.onCreate();
        mInstance = this;
        mNetworkService = new NetworkService();
        myDBHandler = MyDBHandler.getInstance(this);
        mSessionManager = new SessionManager(this,MODE_PRIVATE);
    }

    public static synchronized MyApplication getInstance(){return mInstance;}

    public NetworkService getNetworkService(){
        return mNetworkService;
    }

    public MyDBHandler getDBHandler(){return myDBHandler;}

    public SessionManager getSessionManager(){
        return mSessionManager;
    }
}
