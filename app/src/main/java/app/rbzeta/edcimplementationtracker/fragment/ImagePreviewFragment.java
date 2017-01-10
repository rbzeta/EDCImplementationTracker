package app.rbzeta.edcimplementationtracker.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import app.rbzeta.edcimplementationtracker.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Robyn on 1/9/2017.
 */

public class ImagePreviewFragment extends DialogFragment {

    @BindView(R.id.image_preview)
    ImageView imagePreview;
    @BindView(R.id.progress_image_preview)
    ProgressBar progress;

    private String url;
    private Unbinder unbinder;

    public static ImagePreviewFragment newInstance(Bundle bundle){
        ImagePreviewFragment frag = new ImagePreviewFragment();
        frag.setArguments(bundle);
        return frag;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle args = getArguments();
        url = args.getString("picture_url");

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.MyDialogFullScreen);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_preview,container,false);
        unbinder = ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        loadImage();
    }

    private void loadImage() {
        progress.setVisibility(View.VISIBLE);

        if(url != null){
            Glide.with(getContext()).load(url)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            progress.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            progress.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .crossFade()
                    .thumbnail(0.4f)
                    .fitCenter()
                    .error(R.drawable.ic_error_outline_black_24dp)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imagePreview);
        }
    }
}
