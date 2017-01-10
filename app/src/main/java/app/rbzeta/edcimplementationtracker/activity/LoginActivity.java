package app.rbzeta.edcimplementationtracker.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;

import app.rbzeta.edcimplementationtracker.R;
import app.rbzeta.edcimplementationtracker.application.MyApplication;
import app.rbzeta.edcimplementationtracker.database.MyDBHandler;
import app.rbzeta.edcimplementationtracker.helper.SessionManager;
import app.rbzeta.edcimplementationtracker.helper.UIHelper;
import app.rbzeta.edcimplementationtracker.helper.ValidationHelper;
import app.rbzeta.edcimplementationtracker.model.User;
import app.rbzeta.edcimplementationtracker.network.NetworkService;
import app.rbzeta.edcimplementationtracker.network.response.LoginResponseMessage;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Observable;
import rx.Observer;
import rx.Subscription;

import static android.Manifest.permission.READ_CONTACTS;

public class LoginActivity extends AppCompatActivity {

    // UI references.
    @BindView(R.id.edit_login_personal_number)
    AutoCompleteTextView editLoginPN;
    @BindView(R.id.edit_login_password)
    EditText editLoginPassword;
    @BindView(R.id.progress_login)
    ProgressBar progressBar;
    @BindView(R.id.scroll_login_container)
    View mLoginFormView;
    @BindView(R.id.text_login_help)
    TextView textLoginHelp;
    @OnClick(R.id.text_login_help)
    public void helpOnClick(){
        showHelpDialog();
    }

    @BindView(R.id.button_login_submit)
    Button buttonLoginSubmit;
    @BindView(R.id.text_login_app_name)
    TextView textLoginAppName;

    private NetworkService service;
    private SessionManager session;
    private MyDBHandler dbHandler;
    private Unbinder unbinder;
    private Subscription subscription;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        unbinder = ButterKnife.bind(this);

        service = MyApplication.getInstance().getNetworkService();
        session = MyApplication.getInstance().getSessionManager();
        dbHandler = MyApplication.getInstance().getDBHandler();

        Typeface mFace = Typeface.createFromAsset(getAssets(),"fonts/Cornerstone.ttf");
        textLoginAppName.setTypeface(mFace);

        editLoginPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.editPasswordLoginIme || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        buttonLoginSubmit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void attemptLogin() {
        if (!validateRequiredField(editLoginPN)) return;
        if (!validatePassword()) return;
        else loginProcess();

    }

    private void loginProcess() {
        showProgress(true);

        Observable<LoginResponseMessage> observable = (Observable<LoginResponseMessage>)
                service.getPreparedObservable(service.getNetworkAPI().userLogin(
                        editLoginPN.getText().toString().trim(), editLoginPassword.getText().toString().trim()),
                        LoginResponseMessage.class,
                        false, false);
        subscription = observable.subscribe(new Observer<LoginResponseMessage>() {
            @Override
            public void onCompleted() {
                showProgress(false);
            }

            @Override
            public void onError(Throwable e) {
                showProgress(false);
                rxUnSubscribe();
                UIHelper.showCustomSnackBar(mLoginFormView,
                        getString(R.string.dialog_msg_error_login),
                        ContextCompat.getColor(LoginActivity.this, R.color.white));
            }

            @Override
            public void onNext(LoginResponseMessage response) {

                if (response.isSuccess()) {
                    User user = response.getUser();

                    if (user != null) {
                        session.deleteSharedPreference();
                        session.setUserLogin(true);
                        session.setUserSharedPreferences(user);

                        goToHomeActivity();
                        finish();
                    }
                } else {
                    UIHelper.showCustomSnackBar(mLoginFormView,
                            response.getMessage(),
                            ContextCompat.getColor(LoginActivity.this, R.color.white));
                }
            }
        });

    }

    private void goToHomeActivity() {
        Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private boolean validatePassword() {
        if (TextUtils.isEmpty(editLoginPassword.getText().toString().trim())) {
            editLoginPassword.requestFocus();
            editLoginPassword.setError(getString(R.string.error_field_required));
            return false;
        } else editLoginPassword.setError(null);

        return true;
    }

    private boolean validateRequiredField(EditText editText) {
        editText.setError(null);

        if (editText.getText().toString().trim().isEmpty()) {
            editText.requestFocus();
            editText.setError(getString(R.string.error_field_required));
            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.button_transparent_with_border, 0);
            return false;
        }

        return true;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            progressBar.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void rxUnSubscribe() {
        if (subscription != null && !subscription.isUnsubscribed())
            subscription.unsubscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        rxUnSubscribe();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Login Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
    private void showHelpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.dialog_msg_help_login));

        builder.setPositiveButton(getString(R.string.action_ok),
                (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

