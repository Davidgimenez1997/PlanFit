package com.utad.david.planfit.Activitys;

import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import com.utad.david.planfit.Data.Firebase.FirebaseAdmin;
import com.utad.david.planfit.Fragments.LoginFragment;
import com.utad.david.planfit.Fragments.RegisterFragment;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Data.SessionUser;

public class FirstActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener,RegisterFragment.OnFragmentInteractionListener,FirebaseAdmin.FirebaseAdminLisener {

    private LoginFragment loginFragment;
    private RegisterFragment registerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        if (findViewById(R.id.frameLayout_FirstActivity) != null) {
            if (savedInstanceState != null) {
                return;
            }
            loginFragment = new LoginFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frameLayout_FirstActivity, loginFragment).commit();
        }

       SessionUser.getInstance().firebaseAdmin.setAdminLisener(this);
    }

    @Override
    public void clickButtonLogin(String email, String password) {

    }

    @Override
    public void clickButtonRegister() {
        registerFragment = new RegisterFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout_FirstActivity, registerFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void clickButtonOk(String emailRegister, String passwordRegister) {
        new RegisterAsyncTask(emailRegister,passwordRegister).execute();
    }

    @Override
    public void clickButtonBack() {
        loginFragment = new LoginFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout_FirstActivity,loginFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void registerWithEmailAndPassword(boolean end) {
        if(end == true){
            Toast.makeText(this,"Register Completed",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this,"Register Fail",Toast.LENGTH_LONG).show();
        }
    }

    private class RegisterAsyncTask extends AsyncTask<Void,Void,Void>{

        private String emailRegister;
        private String passwordRegister;

        public RegisterAsyncTask(String emailRegister, String passwordRegister) {
            this.emailRegister = emailRegister;
            this.passwordRegister = passwordRegister;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            SessionUser.getInstance().firebaseAdmin.registerWitchEmailAndPassword(emailRegister,passwordRegister);
            return null;
        }
    }
}
