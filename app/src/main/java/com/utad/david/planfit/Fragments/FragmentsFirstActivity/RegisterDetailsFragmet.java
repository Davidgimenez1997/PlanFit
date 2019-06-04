package com.utad.david.planfit.Fragments.FragmentsFirstActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.crashlytics.android.Crashlytics;
import com.utad.david.planfit.Activitys.MainMenuActivity;
import com.utad.david.planfit.Data.Firebase.FirebaseAdmin;
import com.utad.david.planfit.Data.SessionUser;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.Constants;
import com.utad.david.planfit.Utils.Utils;
import com.utad.david.planfit.Utils.UtilsNetwork;
import io.fabric.sdk.android.Fabric;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.utad.david.planfit.Utils.Constants.RequestPermisos.REQUEST_GALLERY;
import static com.utad.david.planfit.Utils.Constants.RequestPermisos.REQUEST_IMAGE_PERMISSIONS;

public class RegisterDetailsFragmet extends Fragment implements FirebaseAdmin.FirebaseAdminLoginAndRegisterListener,FirebaseAdmin.FirebaseAdminInsertAndDownloandListener, EasyPermissions.PermissionCallbacks {

    private OnFragmentInteractionListener mListener;

    public RegisterDetailsFragmet() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(UtilsNetwork.checkConnectionInternetDevice(getContext())){
            Fabric.with(getContext(), new Crashlytics());
            SessionUser.getInstance().firebaseAdmin.setFirebaseAdminLoginAndRegisterListener(this);
            SessionUser.getInstance().firebaseAdmin.setFirebaseAdminInsertAndDownloandListener(this);
        }else{
            Toast.makeText(getContext(),getString(R.string.info_network_device),Toast.LENGTH_LONG).show();
        }
    }

    private EditText fullName;
    private EditText nickName;
    private ImageView imageViewUser;
    private Button buttonOk;
    private Button buttonBackDetails;
    private Uri imageUri;
    private ProgressDialog mProgress;
    private String photoPath;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_details, container, false);
        findViewById(view);
        onClickButtonOk();
        onClickButtonBackDetails();
        onClickImage();
        configView();
        showDialog();
        return view;
    }

    private void onClickImage() {
        imageViewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA};

                if (EasyPermissions.hasPermissions(getContext(), perms)) {
                    showTakePictureDialog();
                } else {
                    // Do not have permissions, request them now
                    EasyPermissions.requestPermissions(getActivity(), getString(R.string.permissions_picture_rationale), REQUEST_IMAGE_PERMISSIONS, perms);
                }
            }
        });
    }

    private void configView(){
        fullName.setText("");
        nickName.setText("");
        imageViewUser.setImageResource(R.drawable.icon_gallery);
        fullName.addTextChangedListener(textWatcherRegistreDetailsFragment);
        nickName.addTextChangedListener(textWatcherRegistreDetailsFragment);
    }

    private void showDialog(){
        mProgress = new ProgressDialog(getContext());
        mProgress.setTitle(getString(R.string.title_register));
        mProgress.setMessage(getString(R.string.message_register));
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);
    }

    private TextWatcher textWatcherRegistreDetailsFragment = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            buttonOk.setEnabled(enableButton());
        }
    };

    private boolean enableButton(){
        if(UtilsNetwork.checkConnectionInternetDevice(getContext())){
            if(!checkEditText()){
                return true;
            }else {
                return false;
            }
        }else{
            return false;
        }
    }

    private boolean checkEditText(){
        if(nickName.getText().toString().isEmpty() || fullName.getText().toString().isEmpty()){
            return true;
        }else{
            return false;
        }
    }

    private void findViewById(View view){
        fullName = view.findViewById(R.id.fullName);
        nickName = view.findViewById(R.id.nickName);
        imageViewUser = view.findViewById(R.id.imageViewUser);
        buttonOk = view.findViewById(R.id.buttonOk);
        buttonBackDetails = view.findViewById(R.id.buttonBackDetails);
    }

    private void onClickButtonOk(){
        buttonOk.setOnClickListener(v -> {
            if (mListener!=null){
                setDataUser();
                mProgress.show();
                mListener.clickButtonOk();
            }else{
                mProgress.dismiss();
            }
        });
    }

    private void onClickButtonBackDetails(){
        buttonBackDetails.setOnClickListener(v -> {
            if(mListener!=null){
                mListener.clickButtonBackDetails();
            }
        });
    }

    private void openGallery(){
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            intent.setAction(Intent.ACTION_GET_CONTENT);
        } else {
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
        }
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, getString(R.string.menuopengalery)),REQUEST_GALLERY);
    }

    private void showTakePictureDialog() {
        final CharSequence[] items = {"Galeria","Camara", "Cancelar"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Escoja una foto");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        openGallery();
                        break;
                    case 1:
                        openCamera();
                        break;
                    case 2:
                        dialog.dismiss();
                        break;
                }
            }
        });
        builder.show();
    }

    private void openCamera() {
        File photoFile = null;
        try {
            photoFile = Utils.createImageFile();
            photoPath = photoFile.getAbsolutePath();
        } catch (IOException ex) {
            // TODO: Set error occurred while creating the File
            ex.printStackTrace();
        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri photoURI = FileProvider.getUriForFile(getActivity(),
                    "com.utad.david.planfit.camera.fileprovider",
                    photoFile);
            intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(intentCamera, Constants.RequestPermisos.REQUEST_CAMERA);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if(requestCode == REQUEST_GALLERY){
                try {
                    imageUri = data.getData();
                    final InputStream imageStream = getActivity().getApplicationContext().getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    imageViewUser.setImageBitmap(selectedImage);
                    if(imageUri!=null){
                        SessionUser.getInstance().user.setImgUser(imageUri.toString());
                    }else{
                        SessionUser.getInstance().user.setImgUser(null);
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity().getApplicationContext(), getString(R.string.error_gallery_1), Toast.LENGTH_LONG).show();
                }
            }
            if(requestCode ==  Constants.RequestPermisos.REQUEST_CAMERA){
                Bitmap photo = Utils.getBitmapFromPath(photoPath);
                imageViewUser.setImageBitmap(photo);
                imageUri = Utils.getImageUri(getContext(), photo);
                if(imageUri!=null){
                    SessionUser.getInstance().user.setImgUser(imageUri.toString());
                }else{
                    SessionUser.getInstance().user.setImgUser(null);
                }
            }
        }else {
            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.error_gallery_2),Toast.LENGTH_LONG).show();
        }
    }

    private void setDataUser(){
        SessionUser.getInstance().user.setFullName(fullName.getText().toString());
        SessionUser.getInstance().user.setNickName(nickName.getText().toString());
        if(SessionUser.getInstance().user.getImgUser()!=null){
            SessionUser.getInstance().user.setImgUser(imageUri.toString());
        }else{
            SessionUser.getInstance().user.setImgUser(null);
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

    private boolean endRegister=false;

    @Override
    public void registerWithEmailAndPassword(boolean end) {
        if (end == true) {
            endRegister=true;
            Toast.makeText(getContext(), getString(R.string.info_register)+" "+SessionUser.getInstance().user.getFullName().trim(), Toast.LENGTH_LONG).show();
            SessionUser.getInstance().firebaseAdmin.addDataUserCouldFirestore();
        } else {
            mProgress.dismiss();
            Toast.makeText(getContext(), getString(R.string.err_register), Toast.LENGTH_LONG).show();
            errorSingInRegister(getString(R.string.err_register_fail));
            endRegister=false;
        }
    }

    @Override
    public void insertUserDataInFirebase(boolean end) {
        if(endRegister==true){
            if(end==true){
                mProgress.dismiss();
                Intent intent = new Intent(getContext(), MainMenuActivity.class);
                startActivity(intent);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getActivity().finish();
            }else{
                mProgress.dismiss();
                Toast.makeText(getContext(), getString(R.string.err_register), Toast.LENGTH_LONG).show();
                errorSingInRegister(getString(R.string.err_register_fail));
            }
        }else{
            mProgress.dismiss();
        }
    }

    private void errorSingInRegister(String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.AlertDialogTheme);
        builder.setMessage(title)
                .setPositiveButton(R.string.info_dialog_err, (dialog, id) -> {
                    RegisterFragment registerFragment = new RegisterFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.frameLayout_FirstActivity, registerFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                });
        builder.create();
        builder.show();
    }

    @Override
    public void singInWithEmailAndPassword(boolean end) {}

    @Override
    public void downloadUserDataInFirebase(boolean end) {}

    @Override
    public void downloadInfotDeveloper(boolean end) {}

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void clickButtonOk();
        void clickButtonBackDetails();
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        try {
            showTakePictureDialog();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this)
                    .setTitle(getString(R.string.permissions_denied_title))
                    .setRationale(getString(R.string.permissions_denied_body)).build().show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

}
