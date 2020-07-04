package com.utad.david.planfit.Fragments.Authentication.Register;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.crashlytics.android.Crashlytics;
import com.utad.david.planfit.Base.BaseFragment;
import com.utad.david.planfit.R;
import io.fabric.sdk.android.Fabric;

public class RegisterFragment extends BaseFragment implements RegisterView {

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
    private RegisterPresenter registerPresenter;

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
            this.mListener = (Callback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement Callback");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mListener = null;
    }

    /******************************** CONFIGURE UI *************************************+/
     *
     */

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.registerPresenter = new RegisterPresenter(this);
        if (this.registerPresenter.checkInternetDevice(getContext())) {
            Fabric.with(getContext(), new Crashlytics());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_register, container, false);

        this.findViewById(view);
        this.onClickButtonBack();
        this.onClickButtonContinue();
        this.configView();

        return view;
    }

    /******************************** CONFIGURA VISTA *************************************+/
     *
     */

    private void configView(){
        this.emailRegister.setText("");
        this.passwordRegister.setText("");
        this.emailRegister.addTextChangedListener(this.textWatcherRegisterFragment);
        this.passwordRegister.addTextChangedListener(this.textWatcherRegisterFragment);
    }

    private void findViewById(View view){
        this.emailRegister = view.findViewById(R.id.emailRegister);
        this.passwordRegister = view.findViewById(R.id.passwordRegister);
        this.buttonContinue = view.findViewById(R.id.buttonContinue);
        this.buttonBack = view.findViewById(R.id.buttonBack);
    }

    /******************************** ONCLICK CONTINUE *************************************+/
     *
     */

    private void onClickButtonContinue(){
        this.buttonContinue.setOnClickListener(v -> {
            this.registerPresenter.setCredentials(this.emailRegister.getText().toString().trim(), this.passwordRegister.getText().toString().trim());
        });
    }

    /******************************** ONCLICK BACK *************************************+/
     *
     */

    private void onClickButtonBack(){
        this.buttonBack.setOnClickListener(v -> {
            if (this.mListener != null) {
                this.mListener.clickButtonBack();
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
            if (!registerPresenter.emailValidate(emailUser) && registerPresenter.passwordValidate(passwordUser)) {
                emailRegister.setError(getString(R.string.err_email));
            }
            if (!registerPresenter.passwordValidate(passwordUser) && registerPresenter.emailValidate(emailUser)) {
                passwordRegister.setError(getString(R.string.err_password));
            }
            if (!registerPresenter.emailValidate(emailUser) && !registerPresenter.passwordValidate(passwordUser)) {
                emailRegister.setError(getString(R.string.err_email));
                passwordRegister.setError(getString(R.string.err_password));
            }
            boolean validated = registerPresenter.enableButtonContinuee(getContext(), emailUser, passwordUser);
            buttonContinue.setEnabled(validated);
        }
    };

    /******************************** CALLBACK DEL PRESENTER *************************************+/
     *
     */

    @Override
    public void onClickContinue() {
        this.mListener.clickButtonContinue();
    }

    @Override
    public void deviceOfflineMessage() {
        Toast.makeText(getContext(),getString(R.string.info_network_device),Toast.LENGTH_LONG).show();
    }
}
