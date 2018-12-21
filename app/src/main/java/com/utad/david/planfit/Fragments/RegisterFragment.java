
package com.utad.david.planfit.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.utad.david.planfit.Data.SessionUser;
import com.utad.david.planfit.R;


public class RegisterFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private EditText emailRegister;
    private EditText passwordRegister;
    private Button buttonContinue;
    private Button buttonBack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_register, container, false);

        findViewById(view);
        onClickButtonBack();
        onClickButtonContinue();

        return view;
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
                if (mListener!=null){
                    SessionUser.getInstance().user.setEmail(emailRegister.getText().toString());
                    mListener.clickButtonContinue(emailRegister.getText().toString(),passwordRegister.getText().toString());
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
        // TODO: Update argument type and name
        void clickButtonContinue(String emailRegister,String passwordRegister);
        void clickButtonBack();
    }
}
