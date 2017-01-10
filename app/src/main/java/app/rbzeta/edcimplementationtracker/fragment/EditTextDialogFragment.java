package app.rbzeta.edcimplementationtracker.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import app.rbzeta.edcimplementationtracker.R;
import app.rbzeta.edcimplementationtracker.helper.UIHelper;

/**
 * Created by Robyn on 1/6/2017.
 */

public class EditTextDialogFragment extends DialogFragment implements View.OnClickListener{

    private String mTitle,mHint,mValue,mTextType,mInputType;
    private EditText editTextInput;

    public EditTextDialogFragment() {
        super();
    }

    public static EditTextDialogFragment newInstance(String title, String hint, String value
            ,String textType,String inputType){
        EditTextDialogFragment fragment = new EditTextDialogFragment();
        Bundle args = new Bundle();
        args.putString("title",title);
        args.putString("hint",hint);
        args.putString("value",value);
        args.putString("type",textType);
        args.putString("inputType",inputType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.MyDialogFullScreen);

        Bundle args = getArguments();
        mTitle = args.getString("title");
        mHint = args.getString("hint");
        mValue = args.getString("value");
        mTextType = args.getString("type");
        mInputType = args.getString("inputType");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_edittext,container);
        Toolbar toolbar = (Toolbar)view.findViewById(R.id.toolbarDialogEditText) ;
        toolbar.setTitle(mTitle);
        toolbar.setTitleTextColor(ContextCompat.getColor(getContext(),R.color.white));

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextInput = (EditText)view.findViewById(R.id.editTextDialogFragment);
        editTextInput.setHint(mHint);
        editTextInput.setText(mValue);
        editTextInput.setSelectAllOnFocus(true);
        if (mInputType.equals(getString(R.string.input_type_text)))
            editTextInput.setInputType(InputType.TYPE_CLASS_TEXT);
            //editTextInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        /*if (mInputType.equals(getString(R.string.number)))
            editTextInput.setInputType(InputType.TYPE_CLASS_NUMBER);*/
        if (mInputType.equals(getString(R.string.input_type_phone)))
            editTextInput.setInputType(InputType.TYPE_CLASS_PHONE);

        UIHelper.requestFocus(getDialog().getWindow(),editTextInput);

        Button buttonCancel = (Button)view.findViewById(R.id.buttonDialogEditTextCancel);
        Button buttonOk = (Button)view.findViewById(R.id.buttonDialogEditTextOK);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        buttonCancel.setOnClickListener(v -> dismiss());

        buttonOk.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View v) {
        ButtonOKDialogListener listener = (ButtonOKDialogListener)getParentFragment();
        listener.onButtonOKPressed(editTextInput.getText().toString().trim(),mTextType);
        dismiss();

    }

    public interface ButtonOKDialogListener{
        void onButtonOKPressed(String text,String editTextType);
    }
}