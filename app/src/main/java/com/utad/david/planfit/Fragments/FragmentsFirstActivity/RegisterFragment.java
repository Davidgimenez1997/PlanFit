
package com.utad.david.planfit.Fragments.FragmentsFirstActivity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.utad.david.planfit.Data.EncryptDecrypt;
import com.utad.david.planfit.Data.SessionUser;
import com.utad.david.planfit.R;

import java.util.regex.Pattern;

public class RegisterFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public RegisterFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private EditText emailRegister;
    private EditText passwordRegister;
    private Button buttonContinue;
    private Button buttonBack;
    private String emailUser;
    private String passwordUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_register, container, false);
        findViewById(view);
        onClickButtonBack();
        onClickButtonContinue();
        configView();
        return view;
    }

    private void configView(){
        emailRegister.setText("");
        passwordRegister.setText("");
        emailRegister.addTextChangedListener(textWatcherRegisterFragment);
        passwordRegister.addTextChangedListener(textWatcherRegisterFragment);
    }

    private void findViewById(View view){
        emailRegister = view.findViewById(R.id.emailRegister);
        passwordRegister = view.findViewById(R.id.passwordRegister);
        buttonContinue = view.findViewById(R.id.buttonContinue);
        buttonBack = view.findViewById(R.id.buttonBack);
    }

    private void onClickButtonContinue(){
        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String pass = EncryptDecrypt.encrypt(passwordRegister.getText().toString().trim());
                    if (mListener!=null){
                        SessionUser.getInstance().user.setEmail(emailRegister.getText().toString().trim());
                        SessionUser.getInstance().user.setPassword(pass);
                        mListener.clickButtonContinue();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void onClickButtonBack(){
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!=null){
                    mListener.clickButtonBack();
                }
            }
        });
    }

    private TextWatcher textWatcherRegisterFragment = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            emailUser = emailRegister.getText().toString().trim();
            passwordUser = passwordRegister.getText().toString().trim();
            buttonContinue.setEnabled(enableButton());
            if (!enableButton()) {
                if (!emailValidate(emailUser)) {
                    emailRegister.setError(getString(R.string.err_email));
                }
                if (!passValidate(emailUser)) {
                    passwordRegister.setError(getString(R.string.err_password));
                }
            }
        }
    };

    private boolean enableButton() {
        if (emailValidate(emailUser) && passValidate(passwordUser)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean emailValidate(String str_email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        if (pattern.matcher(str_email).matches()) {
            return true;
        } else {
            return false;

        }
    }

    public boolean passValidate(String str_password) {
        if (str_password.length() >= 6) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void clickButtonContinue();
        void clickButtonBack();
    }
}
