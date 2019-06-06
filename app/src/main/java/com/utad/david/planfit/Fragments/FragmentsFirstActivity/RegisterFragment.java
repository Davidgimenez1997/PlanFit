
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
import android.widget.Toast;
import com.crashlytics.android.Crashlytics;
import com.utad.david.planfit.Utils.UtilsEncryptDecryptAES;
import com.utad.david.planfit.Data.SessionUser;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.UtilsNetwork;
import io.fabric.sdk.android.Fabric;

import java.util.regex.Pattern;

public class RegisterFragment extends Fragment {

    /******************************** VARIABLES *************************************+/
     *
     */

    private EditText emailRegister;
    private EditText passwordRegister;
    private Button buttonContinue;
    private Button buttonBack;
    private String emailUser;
    private String passwordUser;
    private Callback mListener;

    /******************************** INTERFAZ *************************************+/
     *
     */

    public interface Callback {
        void clickButtonContinue();
        void clickButtonBack();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Callback) {
            mListener = (Callback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement Callback");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /******************************** CONFIGURE UI *************************************+/
     *
     */

    @Override
    public void onStart() {
        super.onStart();
        if(!UtilsNetwork.checkConnectionInternetDevice(getContext())){
            Toast.makeText(getContext(),getString(R.string.info_network_device),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(getContext(), new Crashlytics());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_register, container, false);

        findViewById(view);
        onClickButtonBack();
        onClickButtonContinue();
        configView();

        return view;
    }

    /******************************** CONFIGURA VISTA *************************************+/
     *
     */

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

    /******************************** ONCLICK CONTINUE *************************************+/
     *
     */

    private void onClickButtonContinue(){
        buttonContinue.setOnClickListener(v -> {
            try {
                String pass = UtilsEncryptDecryptAES.encrypt(passwordRegister.getText().toString().trim());
                if (mListener!=null){
                    SessionUser.getInstance().user.setEmail(emailRegister.getText().toString().trim());
                    SessionUser.getInstance().user.setPassword(pass);
                    mListener.clickButtonContinue();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
    }

    /******************************** ONCLICK BACK *************************************+/
     *
     */

    private void onClickButtonBack(){
        buttonBack.setOnClickListener(v -> {
            if(mListener!=null){
                mListener.clickButtonBack();
            }
        });
    }

    /******************************** CONFIGURA EDITTEXT DEL REGISTRO *************************************+/
     *
     */

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
        if(UtilsNetwork.checkConnectionInternetDevice(getContext())){
            if (emailValidate(emailUser) && passValidate(passwordUser)) {
                return true;
            } else {
                return false;
            }
        }else{
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
}
