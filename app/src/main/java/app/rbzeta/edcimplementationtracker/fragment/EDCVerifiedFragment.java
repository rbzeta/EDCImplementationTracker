package app.rbzeta.edcimplementationtracker.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.rbzeta.edcimplementationtracker.R;
import app.rbzeta.edcimplementationtracker.activity.SubmitActivity;
import app.rbzeta.edcimplementationtracker.adapter.EDCRecyclerAdapter;
import app.rbzeta.edcimplementationtracker.application.MyApplication;
import app.rbzeta.edcimplementationtracker.database.MyDBHandler;
import app.rbzeta.edcimplementationtracker.helper.SessionManager;
import app.rbzeta.edcimplementationtracker.helper.UIHelper;
import app.rbzeta.edcimplementationtracker.listener.EndlessRecyclerViewScrollListener;
import app.rbzeta.edcimplementationtracker.listener.RecyclerTouchListener;
import app.rbzeta.edcimplementationtracker.model.EDCItem;
import app.rbzeta.edcimplementationtracker.network.NetworkService;
import app.rbzeta.edcimplementationtracker.network.response.EDCResponseMessage;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observable;
import rx.Observer;
import rx.Subscription;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Robyn on 1/4/2017.
 */

public class EDCVerifiedFragment extends Fragment implements RecyclerTouchListener.OnItemClickListener{

    public static final String INTENT_EDC_ITEM_KEY = "app.rbzeta.edcimplementationtracker.fragment.INTENT_EDC_ITEM_KEY";
    private static final int TAB_VERIFIED = 2;
    private static final int REQUEST_CODE_SUBMIT_DONE = 30;

    @BindView(R.id.recycler_home)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_home)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.text_tab_no_data_found)
    TextView textNoDataFound;

    private List<EDCItem> edcItems;
    private EDCRecyclerAdapter adapter;
    private EndlessRecyclerViewScrollListener scrollListener;

    private Subscription subscription;
    private NetworkService service;
    private SessionManager session;
    private MyDBHandler dbHandler;
    private Unbinder unbinder;

    private static String edcType;
    String currentType = "";


    public static EDCVerifiedFragment newInstance(Bundle bundle){
        EDCVerifiedFragment frag = new EDCVerifiedFragment();
        frag.setArguments(bundle);
        edcType = bundle.getString("edc_type");
        return frag;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        edcItems = new ArrayList<>();
        adapter = new EDCRecyclerAdapter(getContext(),edcItems);
        service = MyApplication.getInstance().getNetworkService();
        dbHandler = MyApplication.getInstance().getDBHandler();
        session = MyApplication.getInstance().getSessionManager();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_tab_onprogress,container,false);
        unbinder = ButterKnife.bind(this,view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public int getFooterViewType(int defaultNoFooterViewType) {
                return -1;
            }

            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                fetchEDCDataFromServer(page);
            }
        };
        recyclerView.addOnScrollListener(scrollListener);
        DefaultItemAnimator animator = new DefaultItemAnimator();
        recyclerView.setItemAnimator(animator);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), LinearLayout.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(),this));
        recyclerView.setAdapter(adapter);

        refreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(),R.color.colorPrimary));

        textNoDataFound.setVisibility(View.GONE);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refreshLayout.setOnRefreshListener(() -> fetchEDCDataFromServer(0)
        );
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadInitEDCDataFromServer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        rxUnSubscribe();
    }

    private void loadInitEDCDataFromServer() {
        refreshLayout.setRefreshing(true);
        fetchEDCDataFromServer(0);
    }

    private void fetchEDCDataFromServer(int page) {
        if (page != 0)
            showFooterProgressBar();

        if (!currentType.equals(edcType)){
            edcItems.clear();
            adapter.notifyDataSetChanged();
        }
        currentType = edcType;

        Observable<EDCResponseMessage> observable = (Observable<EDCResponseMessage>)
                service.getPreparedObservable(
                        service.getNetworkAPI().getEDCListVerifiedFromServer(page,session.getUserBranchId(),currentType),
                        EDCResponseMessage.class,
                        false,false);
        subscription = observable.subscribe(new Observer<EDCResponseMessage>() {
            @Override
            public void onCompleted() {
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(Throwable e) {
                refreshLayout.setRefreshing(false);
                if (page != 0)
                    dismissFooterProgressBar();
                else {
                    if (edcItems.size() == 0)
                        textNoDataFound.setVisibility(View.VISIBLE);
                }

                rxUnSubscribe();
                UIHelper.showCustomSnackBar(recyclerView,
                        getString(R.string.dialog_msg_error_fetch_edc),
                        ContextCompat.getColor(getContext(),R.color.white));

            }

            @Override
            public void onNext(EDCResponseMessage response) {
                if (page != 0) {
                    dismissFooterProgressBar();

                }
                textNoDataFound.setVisibility(View.GONE);
                if (response.isSuccess()){
                    List<EDCItem> items = response.getEdcList();
                    if (items.size() > 0){
                        if(page == 0){
                            edcItems.clear();
                            scrollListener.resetState();
                        }

                        edcItems.addAll(items);

                        if(page == 0)
                            adapter.notifyDataSetChanged();
                        else adapter.notifyItemRangeInserted(adapter.getItemCount()+1,items.size());
                    }else{
                        if (page == 0) {
                            edcItems.clear();
                            adapter.notifyDataSetChanged();
                        }

                        if (edcItems.size() == 0)
                            textNoDataFound.setVisibility(View.VISIBLE);
                    }
                }else {
                    UIHelper.showCustomSnackBar(recyclerView,
                            response.getMessage(),
                            ContextCompat.getColor(getContext(),R.color.white));
                }
            }
        });
    }

    private void dismissFooterProgressBar() {
        edcItems.remove(edcItems.size() - 1);
        adapter.notifyItemRemoved(edcItems.size());

    }

    private void showFooterProgressBar() {
        edcItems.add(null);
        adapter.notifyItemInserted(edcItems.size() - 1);
    }

    private void rxUnSubscribe() {
        if(subscription!=null && !subscription.isUnsubscribed())
            subscription.unsubscribe();
    }

    @Override
    public void onItemClick(View view, int position) {
        openSubmitActivity(position);
    }

    private void openSubmitActivity(int position) {
        EDCItem item = edcItems.get(position);
        Intent intent = new Intent(getActivity(), SubmitActivity.class);
        intent.putExtra(INTENT_EDC_ITEM_KEY,item);
        intent.putExtra("TAB_TYPE", TAB_VERIFIED);
        startActivityForResult(intent,REQUEST_CODE_SUBMIT_DONE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_SUBMIT_DONE){
            if (resultCode == RESULT_OK){
                if (data.getExtras().getBoolean(SubmitActivity.MSG_KEY_SUBMIT_EDC_CANCEL_RESULT)){
                    loadInitEDCDataFromServer();
                }
            }
        }
    }
}
