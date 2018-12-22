package com.utad.david.planfit.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.utad.david.planfit.Activitys.MainMenuActivity;
import com.utad.david.planfit.Data.Firebase.FirebaseAdmin;
import com.utad.david.planfit.Data.SessionUser;
import com.utad.david.planfit.R;

import java.util.regex.Pattern;


public class LoginFragment extends Fragment implements FirebaseAdmin.FirebaseAdminLisener {

    private OnFragmentInteractionListener mListener;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SessionUser.getInstance().firebaseAdmin.setAdminLisener(this);
    }

    private EditText emailLogin;
    private EditText passwordLogin;
    private Button buttonLogin;
    private Button buttonRegister;
    private String emailUser;
    private String passwordUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_login, container, false);

       findViewById(view);
       onClickButtonLogin();
       onClickButtonRegister();
       configView();

       return view;
    }

    private void configView(){
        emailLogin.setText("");
        passwordLogin.setText("");
        emailLogin.addTextChangedListener(textWatcherLoginFragment);
        passwordLogin.addTextChangedListener(textWatcherLoginFragment);
    }

    private void findViewById(View view){
        emailLogin = view.findViewById(R.id.emailLogin);
        passwordLogin = view.findViewById(R.id.passwordLogin);
        buttonLogin = view.findViewById(R.id.buttonLogin);
        buttonRegister = view.findViewById(R.id.buttonRegister);
    }

    private void onClickButtonLogin(){
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener!=null){
                    SessionUser.getInstance().user.setEmail(emailLogin.getText().toString());
                    SessionUser.getInstance().user.setPassword(passwordLogin.getText().toString());
                    mListener.clickButtonLogin(emailLogin.getText().toString(),passwordLogin.getText().toString());
                }
            }
        });
    }

    private void onClickButtonRegister(){
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!=null){
                    mListener.clickButtonRegister();
                }
            }
        });
    }

    private TextWatcher textWatcherLoginFragment = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            emailUser = emailLogin.getText().toString().trim();
            passwordUser = passwordLogin.getText().toString().trim();
            buttonLogin.setEnabled(enableButton());
            if (!enableButton()) {
                if (!emailValidate(emailUser)) {
                    emailLogin.setError(getString(R.string.err_email));
                }
                if (!passValidate(emailUser)) {
                    passwordLogin.setError(getString(R.string.err_password));
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

    @Override
    public void singInWithEmailAndPassword(boolean end) {
        if (end == true) {
            Toast.makeText(getContext(), "Login Completed", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getContext(),MainMenuActivity.class);
            startActivity(intent);
            getActivity().finish();
        } else {
            Toast.makeText(getContext(), "Login Fail", Toast.LENGTH_LONG).show();
            errorSingInRegister("Login Fail");
        }
        Log.d("InfoUser", "Email: " + SessionUser.getInstance().user.getEmail() +
                " FullName: " + SessionUser.getInstance().user.getFullName() +
                " NickName: " + SessionUser.getInstance().user.getNickName() +
                " StringImg: " + SessionUser.getInstance().user.getImgUser());
    }

    private void errorSingInRegister(String title){
        if(mListener!=null){
            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage(title)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            emailLogin.setText("");
                            passwordLogin.setText("");
                        }
                    });
            // Create the AlertDialog object and return it
            builder.create();
            builder.show();
        }
    }

    @Override
    public void registerWithEmailAndPassword(boolean end) {

    }

    public interface OnFragmentInteractionListener {
        void clickButtonLogin(String email,String password);
        void clickButtonRegister();
    }
}
