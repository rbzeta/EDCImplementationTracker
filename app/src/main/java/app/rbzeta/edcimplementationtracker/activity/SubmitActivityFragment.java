package app.rbzeta.edcimplementationtracker.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import app.rbzeta.edcimplementationtracker.R;
import app.rbzeta.edcimplementationtracker.application.AppConfig;
import app.rbzeta.edcimplementationtracker.application.MyApplication;
import app.rbzeta.edcimplementationtracker.fragment.EditTextDialogFragment;
import app.rbzeta.edcimplementationtracker.fragment.ImagePreviewFragment;
import app.rbzeta.edcimplementationtracker.helper.FileUtils;
import app.rbzeta.edcimplementationtracker.helper.PermissionHelper;
import app.rbzeta.edcimplementationtracker.helper.SessionManager;
import app.rbzeta.edcimplementationtracker.helper.UIHelper;
import app.rbzeta.edcimplementationtracker.model.EDCItem;
import app.rbzeta.edcimplementationtracker.network.NetworkService;
import app.rbzeta.edcimplementationtracker.network.response.EDCResponseMessage;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Observer;
import rx.Subscription;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static app.rbzeta.edcimplementationtracker.activity.SubmitActivity.BUNDLE_EDC_TAB_TYPE;
import static app.rbzeta.edcimplementationtracker.fragment.EDCStagingFragment.BUNDLE_EDC_ITEM_KEY;

/**
 * A placeholder fragment containing a simple view.
 */
public class SubmitActivityFragment extends Fragment implements EditTextDialogFragment.ButtonOKDialogListener{

    private EDCItem item;
    private Unbinder unbinder;

    private SessionManager session;
    private PermissionHelper permissionHelper;
    private Subscription subscription;
    private NetworkService service;

    private static final int REQUEST_IMAGE_FROM_CAMERA = 10;
    private static final int REQUEST_IMAGE_FROM_GALLERY = 11;
    public static final int REQUEST_USE_CAMERA = 1;
    public static final int REQUEST_WRITE_EXTERNAL_STORAGE = 2;
    private static final int MAX_IMAGE_WIDTH = 500;
    private static final int MAX_IMAGE_HEIGHT = 500;

    private String imgPhotoClick = "";
    private int tabType = 0;

    private Uri photoURI;
    private File imageFromCamera = null;
    private File filePhotoToUpload = null;
    private File fileBAToUpload = null;
    private String currentPhotoPath;
    private String currentPhotoBAPath;

    @BindView(R.id.text_edc_detail_name_value)
    TextView textEDCDetailNameValue;
    @BindView(R.id.text_edc_detail_address_value)
    TextView textEDCDetailAddressValue;
    @BindView(R.id.text_edc_detail_sn_edc_value)
    TextView textEDCDetailSnEdcValue;
    @BindView(R.id.text_edc_detail_sn_sim_value)
    TextView textEDCDetailSnSimValue;
    @BindView(R.id.text_edc_detail_desc_value)
    TextView textEDCDetailDescValue;
    @BindView(R.id.progress_picture_photo)
    ProgressBar progressPhoto;
    @BindView(R.id.progress_picture_ba)
    ProgressBar progressBa;
    @BindView(R.id.scroll_edc_detail)
    NestedScrollView scrollView;
    @BindView(R.id.text_edc_detail_phone_value)
    TextView textEDCDetailPhoneValue;

    @BindView(R.id.button_edc_detail_verify)
    Button buttonEdcDetailVerify;
    @OnClick(R.id.button_edc_detail_verify)
    public void verifyOnClick(){
        showAlertVerifyConfirmation();
    }

    @BindView(R.id.button_edc_detail_submit)
    Button buttonEdcDetailSubmit;
    @OnClick(R.id.button_edc_detail_submit)
    public void submitOnClick(){
        if(filePhotoToUpload != null && fileBAToUpload != null){
            showAlertSubmitConfirmation();
        }else
            UIHelper.showCustomSnackBar(buttonEdcDetailSubmit,
                    getString(R.string.err_edc_detail_image_null),
                    Color.WHITE);

    }

    @BindView(R.id.button_edc_detail_cancel)
    Button buttonEdcDetailCancel;
    @OnClick(R.id.button_edc_detail_cancel)
    public void cancelOnClick(){
        if(session.getUserUker() != 0){
            showAlertCancelConfirmation("submit");
        }else if(session.getUserUker() == 0)
            showAlertCancelConfirmation("verify");
    }

    @BindView(R.id.img_edc_detail_ba)
    ImageView imageEdcDetailBa;
    @OnClick(R.id.img_edc_detail_ba)
    public void baOnClick(){
        if (session.getUserUker() == 1 && tabType == 0){
            showDialogImagePicker(imageEdcDetailBa);
            imgPhotoClick = "BA";
        }else if(session.getUserUker() == 0 && tabType == 1){
            openBAImage();
        }else if(session.getUserUker() == 0 && tabType == 2){
            openBAImage();
        }

    }

    private void openBAImage() {
        Bundle bundle = new Bundle();
        bundle.putString("picture_url",item.getUrlBa());
        ImagePreviewFragment f = ImagePreviewFragment.newInstance(bundle);
        f.show(getChildFragmentManager(),"image_preview_fragment");
    }

    @BindView(R.id.img_edc_detail_photo)
    ImageView imageEdcDetailPhoto;
    @OnClick(R.id.img_edc_detail_photo)
    public void photoOnClick(){
        if (session.getUserUker() == 1 && tabType == 0){
            showDialogImagePicker(imageEdcDetailPhoto);
            imgPhotoClick = "PHOTO";
        }else if(session.getUserUker() == 0 && tabType == 1){
            openPhotoImage();
        }else if(session.getUserUker() == 0 && tabType == 2){
            openPhotoImage();
        }
        
    }

    private void openPhotoImage() {
        Bundle bundle = new Bundle();
        bundle.putString("picture_url",item.getUrlPicture());
        ImagePreviewFragment f = ImagePreviewFragment.newInstance(bundle);
        f.show(getChildFragmentManager(),"image_preview_fragment");
    }

    @BindView(R.id.container_edc_detail_name)
    LinearLayout containerEdcDetailName;
    @OnClick(R.id.container_edc_detail_name)
    public void nameOnClick(){

    }
    @BindView(R.id.container_edc_detail_address)
    LinearLayout containerEdcDetailAddress;
    @OnClick(R.id.container_edc_detail_address)
    public void addressOnClick(){
        //Toast.makeText(getContext(), "Click Address", Toast.LENGTH_SHORT).show();
    }
    @BindView(R.id.container_edc_detail_sn_edc)
    LinearLayout containerEdcDetailSnEdc;
    @OnClick(R.id.container_edc_detail_sn_edc)
    public void snEdcOnClick(){
        if (session.getUserUker() == 1 && tabType == 0){
            showEditTextFragmentSNEdc();

        }

    }

    private void showEditTextFragmentSNEdc() {
        EditTextDialogFragment dialog = new EditTextDialogFragment().newInstance(
                getString(R.string.dialog_title_input_sn_edc),
                getString(R.string.dialog_title_input_sn_edc_hint),
                textEDCDetailSnEdcValue.getText().toString().trim(),
                getString(R.string.dialog_title_input_sn_edc_hint),
                getString(R.string.input_type_text));
        dialog.show(getChildFragmentManager(),"edit_text_dialog");
    }

    @BindView(R.id.container_edc_detail_sn_sim)
    LinearLayout containerEdcDetailSnSim;
    @OnClick(R.id.container_edc_detail_sn_sim)
    public void snSimOnClick(){
        if (session.getUserUker() == 1 && tabType == 0){
            showEditTextFragmentSNSim();

        }

    }

    private void showEditTextFragmentSNSim() {
        EditTextDialogFragment dialog = new EditTextDialogFragment().newInstance(
                getString(R.string.dialog_title_input_sn_sim),
                getString(R.string.dialog_title_input_sn_sim_hint),
                textEDCDetailSnSimValue.getText().toString().trim(),
                getString(R.string.dialog_title_input_sn_sim_hint),
                getString(R.string.input_type_text));
        dialog.show(getChildFragmentManager(),"edit_text_dialog");
    }

    @BindView(R.id.container_edc_detail_desc)
    LinearLayout containerEdcDetailDesc;
    @OnClick(R.id.container_edc_detail_desc)
    public void descOnClick(){
        if (session.getUserUker() == 1 && tabType == 0){
            showEditTextFragmentDesc();

        }
    }

    private void showEditTextFragmentDesc() {
        EditTextDialogFragment dialog = new EditTextDialogFragment().newInstance(
                getString(R.string.dialog_title_input_edc_desc),
                getString(R.string.dialog_title_input_edc_desc_hint),
                textEDCDetailDescValue.getText().toString().trim(),
                getString(R.string.dialog_title_input_edc_desc_hint),
                getString(R.string.input_type_text));
        dialog.show(getChildFragmentManager(),"edit_text_dialog");
    }

    @BindView(R.id.container_edc_detail_phone)
    LinearLayout containerEdcDetailPhone;
    @OnClick(R.id.container_edc_detail_phone)
    public void phoneOnClick(){
        if (session.getUserUker() == 1 && tabType == 0){
            showEditTextFragmentPhone();

        }
    }

    private void showEditTextFragmentPhone() {
        EditTextDialogFragment dialog = new EditTextDialogFragment().newInstance(
                getString(R.string.dialog_title_input_edc_phone),
                getString(R.string.dialog_title_input_edc_phone_hint),
                textEDCDetailPhoneValue.getText().toString().trim(),
                getString(R.string.dialog_title_input_edc_phone_hint),
                getString(R.string.input_type_phone));
        dialog.show(getChildFragmentManager(),"edit_text_dialog");
    }

    public static SubmitActivityFragment newInstance(Bundle bundle) {
        SubmitActivityFragment f =  new SubmitActivityFragment();
        f.setArguments(bundle);
        return f;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle args = getArguments();
        item = args.getParcelable(BUNDLE_EDC_ITEM_KEY);
        tabType = args.getInt(BUNDLE_EDC_TAB_TYPE);
        session = MyApplication.getInstance().getSessionManager();
        permissionHelper = new PermissionHelper((AppCompatActivity)getActivity());
        service = MyApplication.getInstance().getNetworkService();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_submit, container, false);
        unbinder = ButterKnife.bind(this,view);

        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setTextViewValue();
        setButtonViewValue();
        setImageViewValue();
    }

    private void setButtonViewValue() {
        if (tabType == 0){
            //if (session.getUserUker() == 0 && session.getUserLevelId() == 99){
            if (session.getUserUker() == 0){
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)scrollView.getLayoutParams();
                params.bottomMargin = 0;
                scrollView.setLayoutParams(params);
                buttonEdcDetailSubmit.setVisibility(View.GONE);
                buttonEdcDetailVerify.setVisibility(View.GONE);
            }else{
                buttonEdcDetailSubmit.setVisibility(View.VISIBLE);
                buttonEdcDetailVerify.setVisibility(View.GONE);
            }
        }else if(tabType == 1){
            //if (session.getUserUker() == 0 && session.getUserLevelId() == 99){
            if (session.getUserUker() == 0){
                buttonEdcDetailSubmit.setVisibility(View.GONE);
                buttonEdcDetailVerify.setVisibility(View.VISIBLE);
            }else{
                /*ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)scrollView.getLayoutParams();
                params.bottomMargin = 0;
                scrollView.setLayoutParams(params);*/
                buttonEdcDetailSubmit.setVisibility(View.GONE);
                buttonEdcDetailVerify.setVisibility(View.GONE);
                buttonEdcDetailCancel.setVisibility(View.VISIBLE);

            }

        }else if(tabType == 2){
            if(session.getUserUker() == 0){
                buttonEdcDetailSubmit.setVisibility(View.GONE);
                buttonEdcDetailVerify.setVisibility(View.GONE);
                buttonEdcDetailCancel.setVisibility(View.VISIBLE);
            }else{
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)scrollView.getLayoutParams();
                params.bottomMargin = 0;
                scrollView.setLayoutParams(params);
                buttonEdcDetailSubmit.setVisibility(View.GONE);
                buttonEdcDetailVerify.setVisibility(View.GONE);
                buttonEdcDetailCancel.setVisibility(View.GONE);
            }

        }
    }

    private void setImageViewValue() {
        if (item.getUrlBa() != null)
            progressBa.setVisibility(View.VISIBLE);

            Glide.with(getContext()).load(item.getUrlBa())
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            progressBa.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            progressBa.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .crossFade()
                    .thumbnail(0.2f)
                    .fitCenter()
                    .error(R.drawable.ic_menu_camera)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(imageEdcDetailBa);

        if (item.getUrlPicture() != null)
            progressPhoto.setVisibility(View.VISIBLE);

            Glide.with(getContext()).load(item.getUrlPicture())
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            progressPhoto.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            progressPhoto.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .crossFade()
                    .thumbnail(0.2f)
                    .fitCenter()
                    .error(R.drawable.ic_menu_camera)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(imageEdcDetailPhoto);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        deleteFilePhoto();
        deleteFilePhotoBA();
        rxUnSubscribe();
    }

    private void setTextViewValue() {
        textEDCDetailNameValue.setText(item.getName());
        textEDCDetailAddressValue.setText((item.getAddress() != null)?item.getAddress():"-");
        textEDCDetailSnEdcValue.setText((item.getSnEdc() != null)?item.getSnEdc():"-");
        textEDCDetailSnSimValue.setText((item.getSnSim() != null)?item.getSnSim():"-");
        textEDCDetailDescValue.setText((item.getDescription() != null)?item.getDescription():"-");
        textEDCDetailPhoneValue.setText((item.getPhoneNumber() != null)?item.getPhoneNumber():"-");
    }

    @Override
    public void onButtonOKPressed(String text, String editTextType) {
        text = text.trim();
        if (!text.equals("")){
            if (editTextType.equals(getString(R.string.dialog_title_input_sn_edc_hint))) {
                if (!textEDCDetailSnEdcValue.getText().toString().equals(text)){
                    if (text.length() > getResources().getInteger(R.integer.max_sn_edc_length)){
                        UIHelper.showCustomSnackBar(buttonEdcDetailSubmit,
                                getString(R.string.err_edc_detail_sn_edc_length), Color.WHITE);
                        return;
                    }
                    textEDCDetailSnEdcValue.setText(text);
                }

            }else if(editTextType.equals(getString(R.string.dialog_title_input_sn_sim_hint))){
                if (!textEDCDetailSnSimValue.getText().toString().equals(text)){
                    if (text.length() > getResources().getInteger(R.integer.max_sn_sim_length)){
                        UIHelper.showCustomSnackBar(buttonEdcDetailSubmit,
                                getString(R.string.err_edc_detail_sn_sim_length), Color.WHITE);
                        return;
                    }
                    textEDCDetailSnSimValue.setText(text);
                }
            }else if(editTextType.equals(getString(R.string.dialog_title_input_edc_desc_hint))) {
                if (!textEDCDetailDescValue.getText().toString().equals(text)) {
                    if (text.length() > getResources().getInteger(R.integer.max_edc_desc_length)) {
                        UIHelper.showCustomSnackBar(buttonEdcDetailSubmit,
                                getString(R.string.err_edc_detail_edc_desc_length), Color.WHITE);
                        return;
                    }
                    textEDCDetailDescValue.setText(text);
                }
            }else if(editTextType.equals(getString(R.string.dialog_title_input_edc_phone_hint))) {
                if (!textEDCDetailPhoneValue.getText().toString().equals(text)) {
                    if (text.length() < getResources().getInteger(R.integer.max_edc_phone_length)) {
                        UIHelper.showCustomSnackBar(buttonEdcDetailSubmit,
                                getString(R.string.err_edc_detail_edc_phone_length), Color.WHITE);
                        return;
                    }
                    textEDCDetailPhoneValue.setText(text);
                }
            }
        }

    }

    private void showDialogImagePicker(final View view) {
        if(!permissionHelper.mayRequestCamera(view))
            return;
        if(!permissionHelper.mayRequestWriteExternalStorage(view))
            return;

        final CharSequence[] items = {getString(R.string.text_camera),getString(R.string.text_gallery),
                getString(R.string.action_cancel)};
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
        alertBuilder.setTitle(getString(R.string.dialog_title_select_image_from));
        alertBuilder.setItems(items, (dialog, which) -> {

            if (items[which].equals(getString(R.string.text_camera))){
                takePictureFromCamera(view);
            }else if (items[which].equals(getString(R.string.text_gallery))){
                takePictureFromGallery(view);
            }else
                dialog.dismiss();
        });
        alertBuilder.show();
    }

    private void takePictureFromGallery(View view) {
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.dialog_title_select_picture))
                , REQUEST_IMAGE_FROM_GALLERY);
    }

    private void takePictureFromCamera(View view) {
        if (getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA))
        {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {

                try{
                    imageFromCamera = FileUtils.createImageFile();
                    currentPhotoPath = imageFromCamera.getAbsolutePath();
                }catch (IOException ex){
                    Log.e("ERROR : ",ex.getLocalizedMessage());
                }

                if (imageFromCamera != null) {

                    photoURI = FileProvider.getUriForFile(getContext(),
                            AppConfig.STR_FILE_PROVIDER,
                            imageFromCamera);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent,REQUEST_IMAGE_FROM_CAMERA);

                }

            }
        }else {
            Snackbar.make(view,R.string.msg_device_no_camera,Snackbar.LENGTH_LONG).show();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            if(imgPhotoClick.equals("PHOTO"))
                deleteFilePhoto();
            else if(imgPhotoClick.equals("BA"))
                deleteFilePhotoBA();

            if (requestCode == REQUEST_IMAGE_FROM_CAMERA){

                if(imgPhotoClick.equals("PHOTO")){
                    filePhotoToUpload = prepareImageCameraForUpload();
                    if (filePhotoToUpload != null) {
                        setPhotoToView(requestCode);
                    }
                }else if(imgPhotoClick.equals("BA")){
                    fileBAToUpload = prepareImageCameraForUpload();
                    if (fileBAToUpload != null) {
                        setPhotoToView(requestCode);
                    }
                }


            }else if(requestCode == REQUEST_IMAGE_FROM_GALLERY){

                if(imgPhotoClick.equals("PHOTO")){
                    filePhotoToUpload = prepareImageGalleryForUpload(data);
                    if (filePhotoToUpload != null) {
                        setPhotoToView(requestCode);
                    }
                }else if(imgPhotoClick.equals("BA")){
                    fileBAToUpload = prepareImageGalleryForUpload(data);
                    if (fileBAToUpload != null) {
                        setPhotoToView(requestCode);
                    }
                }

            }
        }else if (resultCode == RESULT_CANCELED){
            if (requestCode == REQUEST_IMAGE_FROM_CAMERA) {
                imageFromCamera.delete();
            }
        }

    }

    private File prepareImageGalleryForUpload(Intent data) {

        File file = null;
        OutputStream os;
        if (data != null){

            try {

                Uri selectedImage = data.getData();
                currentPhotoBAPath = getRealPathFromURI(getContext(),selectedImage);

                // Get the dimensions of the View
                int targetW;
                int targetH;

                // Get the dimensions of the bitmap
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(currentPhotoBAPath, bmOptions);
                int photoW = bmOptions.outWidth;
                int photoH = bmOptions.outHeight;

                // Decode the image file into a Bitmap sized to fill the View
                bmOptions.inJustDecodeBounds = false;

                if (photoH > 1000 && photoW > 1000){
                    int divider;
                    if(photoH > photoW){
                        divider = photoH/MAX_IMAGE_HEIGHT;
                        targetH = MAX_IMAGE_HEIGHT;
                        targetW = photoW/divider;
                    }else {
                        divider = photoW/MAX_IMAGE_WIDTH;
                        targetW = MAX_IMAGE_WIDTH;
                        targetH = photoH/divider;
                    }
                    // Determine how much to scale down the image
                    int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
                    bmOptions.inSampleSize = scaleFactor;

                    //bmOptions.inSampleSize = mScaleFactor;
                }

                bmOptions.inPurgeable = true;

                Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoBAPath, bmOptions);

                bitmap = FileUtils.getCorrectedOrientationBitmap(bitmap,currentPhotoBAPath);

                file = FileUtils.createImageFile();

                os = new FileOutputStream(file);

                bitmap.compress(Bitmap.CompressFormat.JPEG,100,os);
                os.flush();
                os.close();

                currentPhotoBAPath = file.getAbsolutePath();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                //Log.d(TAG,"File not found : " + file.getAbsolutePath());

            } catch (IOException e) {
                e.printStackTrace();
                //Log.d(TAG,e.getLocalizedMessage());
            }
        }

        return file;
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] projection = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  projection, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void deleteFilePhotoBA() {
        if (fileBAToUpload != null)
            fileBAToUpload.delete();
    }

    private void deleteFilePhoto() {
        if (filePhotoToUpload != null)
            filePhotoToUpload.delete();
    }

    private File prepareImageCameraForUpload() {
        // Get the dimensions of the View
        int targetW;
        int targetH;

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;

        if (photoH > 1000 && photoW > 1000){
            int divider;
            if(photoH > photoW){
                divider = photoH/MAX_IMAGE_HEIGHT;
                targetH = MAX_IMAGE_HEIGHT;
                targetW = photoW/divider;
            }else {
                divider = photoW/MAX_IMAGE_WIDTH;
                targetW = MAX_IMAGE_WIDTH;
                targetH = photoH/divider;
            }
            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
            bmOptions.inSampleSize = scaleFactor;

            //bmOptions.inSampleSize = mScaleFactor;
        }
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);

        bitmap = FileUtils.getCorrectedOrientationBitmap(bitmap,currentPhotoPath);

        imageFromCamera.delete();

        OutputStream os;

        File file = null;
        try {
            file = FileUtils.createImageFile();

            os = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,os);
            os.flush();
            os.close();

            currentPhotoPath = file.getAbsolutePath();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            //Log.d(TAG,"File not found : " + file.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
            //Log.d(TAG,e.getLocalizedMessage());
        }

        return file;
    }

    private void setPhotoToView(int requestCode) {
        if(imgPhotoClick.equals("PHOTO")){
            Glide.with(this).load(filePhotoToUpload.getAbsolutePath())
                    .crossFade()
                    .thumbnail(0.2f)
                    .fitCenter()
                    .error(R.drawable.ic_error_outline_black_24dp)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(imageEdcDetailPhoto);
        }else if(imgPhotoClick.equals("BA")){
            Glide.with(this).load(fileBAToUpload.getAbsolutePath())
                    .crossFade()
                    .thumbnail(0.2f)
                    .fitCenter()
                    .error(R.drawable.ic_error_outline_black_24dp)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(imageEdcDetailBa);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_USE_CAMERA) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showDialogImagePicker(textEDCDetailNameValue);
            }
        }

        if (requestCode == REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showDialogImagePicker(textEDCDetailNameValue);
            }
        }
    }
    private void submitDataEdc() {
        final ProgressDialog progress = ProgressDialog.show(getContext(),
                getString(R.string.txt_title_upload_data_edc),
                getString(R.string.txt_dialog_please_wait),true,false);

        item.setSnEdc(textEDCDetailSnEdcValue.getText().toString());
        item.setSnSim(textEDCDetailSnSimValue.getText().toString());
        item.setDescription(textEDCDetailDescValue.getText().toString());
        item.setPhoneNumber(textEDCDetailPhoneValue.getText().toString());

        MultipartBody.Part photo = null;
        MultipartBody.Part photoBA = null;

        RequestBody requestFilePhoto = RequestBody.create(MediaType.parse("multipart/form-data"),filePhotoToUpload);

        photo = MultipartBody.Part
                .createFormData("picture_photo",
                        filePhotoToUpload.getName(),
                        requestFilePhoto);

        RequestBody requestFilePhotoBA = RequestBody.create(MediaType.parse("multipart/form-data"),fileBAToUpload);

        photoBA = MultipartBody.Part
                .createFormData("picture_ba",
                        fileBAToUpload.getName(),
                        requestFilePhotoBA);

        Observable<EDCResponseMessage> observable = (Observable<EDCResponseMessage>)
                service.getPreparedObservable(service.getNetworkAPI().submitEDCImplementation(item,photo,photoBA),
                        EDCResponseMessage.class,false,false);

        subscription = observable.subscribe(new Observer<EDCResponseMessage>() {
            @Override
            public void onCompleted() {
                progress.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                progress.dismiss();
                rxUnSubscribe();
                UIHelper.showCustomSnackBar(buttonEdcDetailSubmit,
                        getString(R.string.dialog_msg_error_upload_implementation_edc),
                        ContextCompat.getColor(getContext(),R.color.white));
            }

            @Override
            public void onNext(EDCResponseMessage response) {
                if (response.isSuccess()){

                    EDCItem edc = response.getEdc();

                    if(currentPhotoPath != null && currentPhotoBAPath != null)
                        addPictureToGallery();

                    SubmitEDCImplementationListener listener = (SubmitEDCImplementationListener)getActivity();
                    listener.onSuccessSubmit(edc);

                }else{
                    UIHelper.showCustomSnackBar(buttonEdcDetailSubmit,
                            response.getMessage(),
                            ContextCompat.getColor(getContext(),R.color.white));

                }
            }
        });
    }

    private void addPictureToGallery() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        File f2 = new File(currentPhotoBAPath);
        Uri contentUri = Uri.fromFile(f);
        Uri contentUri2 = Uri.fromFile(f2);
        mediaScanIntent.setData(contentUri);
        mediaScanIntent.setData(contentUri2);
        getActivity().sendBroadcast(mediaScanIntent);
    }

    private void rxUnSubscribe() {
        if(subscription!=null && !subscription.isUnsubscribed())
            subscription.unsubscribe();
    }

    public interface SubmitEDCImplementationListener{
        void onSuccessSubmit(EDCItem edc);
    }

    public interface VerifyEDCImplementationListener{
        void onSuccessVerify(EDCItem edc);
    }

    public interface CancelEDCImplementationListener{
        void onSuccessCancel(EDCItem edc);
    }

    private void verifyDataEdc() {
        final ProgressDialog progress = ProgressDialog.show(getContext(),
                getString(R.string.txt_title_verify_data_edc),
                getString(R.string.txt_dialog_please_wait),true,false);

        Observable<EDCResponseMessage> observable = (Observable<EDCResponseMessage>)
                service.getPreparedObservable(service.getNetworkAPI().verifyEDCImplementation(item),
                        EDCResponseMessage.class,false,false);

        subscription = observable.subscribe(new Observer<EDCResponseMessage>() {
            @Override
            public void onCompleted() {
                progress.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                progress.dismiss();
                rxUnSubscribe();
                UIHelper.showCustomSnackBar(buttonEdcDetailSubmit,
                        getString(R.string.dialog_msg_error_upload_implementation_edc),
                        ContextCompat.getColor(getContext(),R.color.white));
            }

            @Override
            public void onNext(EDCResponseMessage response) {
                if (response.isSuccess()){

                    EDCItem edc = response.getEdc();

                    VerifyEDCImplementationListener listener = (VerifyEDCImplementationListener)getActivity();
                    listener.onSuccessVerify(edc);

                }else{
                    UIHelper.showCustomSnackBar(buttonEdcDetailSubmit,
                            response.getMessage(),
                            ContextCompat.getColor(getContext(),R.color.white));

                }
            }
        });
    }

    private void cancelDataEdc(String cancelType) {
        final ProgressDialog progress = ProgressDialog.show(getContext(),
                getString(R.string.txt_title_upload_data_edc),
                getString(R.string.txt_dialog_please_wait),true,false);

        Observable<EDCResponseMessage> observable;

        if (cancelType.equals("submit")){
            observable = (Observable<EDCResponseMessage>)
                    service.getPreparedObservable(service.getNetworkAPI().cancelEDCImplementation(item),
                            EDCResponseMessage.class,false,false);
        }else {
            observable = (Observable<EDCResponseMessage>)
                    service.getPreparedObservable(service.getNetworkAPI().cancelEDCVerification(item),
                            EDCResponseMessage.class,false,false);
        }

        subscription = observable.subscribe(new Observer<EDCResponseMessage>() {
            @Override
            public void onCompleted() {
                progress.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                progress.dismiss();
                rxUnSubscribe();
                UIHelper.showCustomSnackBar(buttonEdcDetailSubmit,
                        getString(R.string.dialog_msg_error_upload_implementation_edc),
                        ContextCompat.getColor(getContext(),R.color.white));
            }

            @Override
            public void onNext(EDCResponseMessage response) {
                if (response.isSuccess()){

                    EDCItem edc = response.getEdc();

                    CancelEDCImplementationListener listener = (CancelEDCImplementationListener)getActivity();
                    listener.onSuccessCancel(edc);

                }else{
                    UIHelper.showCustomSnackBar(buttonEdcDetailSubmit,
                            response.getMessage(),
                            ContextCompat.getColor(getContext(),R.color.white));

                }
            }
        });
    }

    private void showAlertSubmitConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(getString(R.string.dialog_msg_submit_confirmation));

        builder.setPositiveButton(getString(R.string.action_yes),
                (dialog, which) -> submitDataEdc());
        builder.setNegativeButton(getString(R.string.action_no), (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void showAlertVerifyConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(getString(R.string.dialog_msg_verify_confirmation));

        builder.setPositiveButton(getString(R.string.action_yes),
                (dialog, which) -> verifyDataEdc());
        builder.setNegativeButton(getString(R.string.action_no), (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void showAlertCancelConfirmation(String cancelType) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(getString(R.string.dialog_msg_cancel_confirmation));

        builder.setPositiveButton(getString(R.string.action_yes),
                (dialog, which) -> cancelDataEdc(cancelType));
        builder.setNegativeButton(getString(R.string.action_no), (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }


}
