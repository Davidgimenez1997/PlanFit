package com.utad.david.planfit.Fragments.FragmentsFirstActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.*;
import com.utad.david.planfit.Activitys.MainMenuActivity;
import com.utad.david.planfit.Data.Firebase.FirebaseAdmin;
import com.utad.david.planfit.Data.SessionUser;
import com.utad.david.planfit.R;

import java.util.regex.Pattern;

public class LoginFragment extends Fragment implements FirebaseAdmin.FirebaseAdminLoginAndRegisterListener {

    private OnFragmentInteractionListener mListener;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SessionUser.getInstance().firebaseAdmin.setFirebaseAdminLoginAndRegisterListener(this);
        SessionUser.getInstance().firebaseAdmin.mAuth = FirebaseAuth.getInstance();
    }

    private EditText emailLogin;
    private EditText passwordLogin;
    private Button buttonLogin;
    private Button buttonRegister;
    private String emailUser;
    private String passwordUser;
    private ProgressDialog mProgress;
    public Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_login, container, false);

       findViewById(view);
       onClickButtonLogin();
       onClickButtonRegister();
       configView();
       checkStatusUserFirebase();
       showDialog();

       return view;
    }

    private void checkStatusUserFirebase(){
        SessionUser.getInstance().firebaseAdmin.authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                SessionUser.getInstance().firebaseAdmin.currentUser = firebaseAuth.getCurrentUser();
                if (SessionUser.getInstance().firebaseAdmin.currentUser != null) {
                    Activity activity = getActivity();
                    if(activity!=null){
                        Intent intent = new Intent(context, MainMenuActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        activity.startActivity(intent);
                        activity.finish();
                    }
                }
            }
        };
    }

    private void showDialog(){
        mProgress = new ProgressDialog(getContext());
        mProgress.setTitle(getString(R.string.title_login));
        mProgress.setMessage(getString(R.string.message_login));
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        SessionUser.getInstance().firebaseAdmin.mAuth.addAuthStateListener(SessionUser.getInstance().firebaseAdmin.authStateListener);
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
                    mProgress.show();
                    SessionUser.getInstance().user.setEmail(emailLogin.getText().toString().trim());
                    SessionUser.getInstance().user.setPassword(passwordLogin.getText().toString().trim());
                    mListener.clickButtonLogin();
                }else{
                    mProgress.dismiss();
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
            this.context = context;
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
            mProgress.dismiss();
            Intent intent = new Intent(getContext(),MainMenuActivity.class);
            startActivity(intent);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            getActivity().finish();
        } else {
            mProgress.dismiss();
            errorSingInRegister(getString(R.string.err_login_fail));
        }
    }

    //Metodo que crea un dialogo de alerta cuando falla el inicio de Sesion con Firebase
    private void errorSingInRegister(String title){
        if(mListener!=null){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
            builder.setMessage(title)
                    .setPositiveButton(R.string.info_dialog_err, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            RegisterFragment registerFragment = new RegisterFragment();
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.replace(R.id.frameLayout_FirstActivity, registerFragment);
                            transaction.addToBackStack(null);
                            transaction.commit();
                        }
                    });
            builder.create();
            builder.show();
        }
    }

    @Override
    public void registerWithEmailAndPassword(boolean end) {
        //Metodo implementado pero no se usa
    }

    public interface OnFragmentInteractionListener {
        void clickButtonLogin();
        void clickButtonRegister();
    }
}
