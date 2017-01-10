package app.rbzeta.edcimplementationtracker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import java.util.List;

import app.rbzeta.edcimplementationtracker.R;
import app.rbzeta.edcimplementationtracker.model.EDCItem;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static app.rbzeta.edcimplementationtracker.fragment.EDCStagingFragment.BUNDLE_EDC_ITEM_KEY;
import static app.rbzeta.edcimplementationtracker.fragment.EDCStagingFragment.INTENT_EDC_ITEM_KEY;

public class SubmitActivity extends AppCompatActivity implements
        SubmitActivityFragment.SubmitEDCImplementationListener,
        SubmitActivityFragment.VerifyEDCImplementationListener,
        SubmitActivityFragment.CancelEDCImplementationListener{

    public static final String MSG_KEY_SUBMIT_EDC_IMPL_RESULT = "app.rbzeta.edcimplementationtracker.activity.MSG_KEY_SUBMIT_EDC_IMPL_RESULT";
    public static final java.lang.String MSG_KEY_SUBMIT_EDC_VER_RESULT = "app.rbzeta.edcimplementationtracker.activity.MSG_KEY_SUBMIT_EDC_VER_RESULT";
    public static final java.lang.String MSG_KEY_SUBMIT_EDC_CANCEL_RESULT = "app.rbzeta.edcimplementationtracker.activity.MSG_KEY_SUBMIT_EDC_VER_RESULT";
    public static final java.lang.String MSG_KEY_SUBMIT_EDC_DONE_RESULT = "app.rbzeta.edcimplementationtracker.activity.MSG_KEY_SUBMIT_EDC_DONE_RESULT";
    public static final String BUNDLE_EDC_TAB_TYPE = "app.rbzeta.edcimplementationtracker.activity.BUNDLE_EDC_TAB_TYPE";

    private static int TAB_TYPE;
    @BindView(R.id.toolbar_submit)
    Toolbar toolbar;
    @BindView(R.id.text_toolbar_tid)
    TextView textToolbarTid;
    @BindView(R.id.text_toolbar_mid)
    TextView textToolbarMid;
    @BindView(R.id.text_toolbar_type)
    TextView textToolbarType;

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);

        unbinder = ButterKnife.bind(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar_submit);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        EDCItem item = getIntent().getParcelableExtra(INTENT_EDC_ITEM_KEY);
        TAB_TYPE = getIntent().getIntExtra("TAB_TYPE",1);

        if (item != null) {
            textToolbarTid.setText("TID "+item.getTid());
            textToolbarMid.setText("MID "+item.getMid());
            textToolbarType.setText(item.getType());
        }

        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_EDC_ITEM_KEY,item);
        bundle.putInt(BUNDLE_EDC_TAB_TYPE,TAB_TYPE);
        SubmitActivityFragment f = SubmitActivityFragment.newInstance(bundle);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_submit_placeholder,f,"fragment_submit_tag");
        ft.commitAllowingStateLoss();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            if (fragment instanceof SubmitActivityFragment){
                fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }

    }

    @Override
    public void onSuccessSubmit(EDCItem edc) {
        Intent data = new Intent();
        data.putExtra(this.MSG_KEY_SUBMIT_EDC_IMPL_RESULT,true);
        setResult(RESULT_OK,data);
        finish();
    }

    @Override
    public void onSuccessVerify(EDCItem edc) {
        Intent data = new Intent();
        data.putExtra(this.MSG_KEY_SUBMIT_EDC_VER_RESULT,true);
        setResult(RESULT_OK,data);
        finish();
    }

    @Override
    public void onSuccessCancel(EDCItem edc) {
        Intent data = new Intent();
        data.putExtra(this.MSG_KEY_SUBMIT_EDC_CANCEL_RESULT,true);
        setResult(RESULT_OK,data);
        finish();
    }
}
