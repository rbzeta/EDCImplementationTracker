package app.rbzeta.edcimplementationtracker.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import app.rbzeta.edcimplementationtracker.R;
import app.rbzeta.edcimplementationtracker.model.EDCItem;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Robyn on 1/4/2017.
 */

public class EDCRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<EDCItem> dataList;
    public static final int VIEW_TYPE_ITEM = 1;
    public static final int VIEW_TYPE_LOADING = 2;

    public EDCRecyclerAdapter(Context context, List<EDCItem> dataList) {
        mContext = context;
        this.dataList = dataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if(viewType == VIEW_TYPE_ITEM) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_edc, parent, false);

            return new ItemViewHolder(itemView);
        }else{
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.footer_loading, parent, false);

            return new LoadingViewHolder(itemView);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ItemViewHolder){
            setItemViewHolder(holder,position);
        }else if(holder instanceof LoadingViewHolder){
            setLoadingViewHolder(holder);
        }
    }

    private void setLoadingViewHolder(RecyclerView.ViewHolder viewHolder) {
        LoadingViewHolder holder = (LoadingViewHolder)viewHolder;
        holder.progress.setIndeterminate(true);
    }

    private void setItemViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ItemViewHolder holder = (ItemViewHolder)viewHolder;

        EDCItem item = dataList.get(position);

        holder.textItemTid.setText(item.getTid());
        holder.textItemName.setText(item.getName());
        holder.textItemType.setText(item.getType());
        holder.textItemAddress.setText(item.getAddress());
        holder.textItemBranch.setText((item.getImplBranchName()));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
    @Override
    public int getItemViewType(int position) {
        return (dataList.get(position) == null) ? VIEW_TYPE_LOADING
                : VIEW_TYPE_ITEM;
    }

    class LoadingViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.progress_footer)
        ProgressBar progress;
        private LoadingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_item_edc_tid)
        TextView textItemTid;
        @BindView(R.id.text_item_edc_type)
        TextView textItemType;
        @BindView(R.id.text_item_edc_name)
        TextView textItemName;
        @BindView(R.id.text_item_edc_address)
        TextView textItemAddress;
        @BindView(R.id.text_item_edc_impl_branch)
        TextView textItemBranch;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
