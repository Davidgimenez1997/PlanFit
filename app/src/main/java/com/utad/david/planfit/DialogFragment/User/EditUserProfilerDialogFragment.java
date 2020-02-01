package com.utad.david.planfit.DialogFragment.User;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.utad.david.planfit.Activitys.FirstActivity;
import com.utad.david.planfit.Base.BaseDialogFragment;
import com.utad.david.planfit.Utils.Constants;
import com.utad.david.planfit.Utils.Utils;
import com.utad.david.planfit.Data.Firebase.FirebaseAdmin;
import com.utad.david.planfit.Data.SessionUser;
import com.utad.david.planfit.Model.User;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.UtilsNetwork;
import io.fabric.sdk.android.Fabric;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import java.io.File;
import java.io.IOException;
import java.util.List;
import static android.app.Activity.RESULT_OK;
import static com.utad.david.planfit.Utils.Constants.RequestPermisos.REQUEST_GALLERY;
import static com.utad.david.planfit.Utils.Constants.RequestPermisos.REQUEST_IMAGE_PERMISSIONS;

public class EditUserProfilerDialogFragment extends BaseDialogFragment
        implements FirebaseAdmin.FirebaseAdminUpdateAndDeleteUserListener,
        EasyPermissions.PermissionCallbacks {


    /******************************** VARIABLES *************************************+/
     *
     */

    private ImageView imageView;
    private EditText editTextFullName;
    private EditText editTextNickName;
    private Button buttonUpdateNickName;
    private Button buttonUpdateFullName;
    private Button buttonDeletePhoto;
    private Button buttonUpdatePhoto;
    private Button buttonDeleteAccount;
    private Button buttonClose;
    private User userUpdate;
    private String oldPassword;
    private Callback mListener;
    private ProgressDialog mProgress;
    private String photoPath;

    /******************************** INTERFAZ *************************************+/
     *
     */

    public interface Callback {
        void updateData(User user);
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

    /******************************** SET CALLBACK FIREBASE *************************************+/
     *
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(UtilsNetwork.checkConnectionInternetDevice(getContext())){
            SessionUser.getInstance().firebaseAdmin.setFirebaseAdminUpdateUserListener(this);
            Fabric.with(getContext(), new Crashlytics());
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.dialog_edit_user_data, container, false);
        v.setBackgroundResource(R.drawable.corner_dialog_fragment);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        userUpdate = SessionUser.getInstance().firebaseAdmin.userDataFirebase;

        if(userUpdate!=null){

            oldPassword = userUpdate.getPassword();
            findById(v);
            putData();
            checkImageUser();
            onClickImage();
            onClickButtonDeletePhoto();
            onClickButtonUpdateNickName();
            onClickButtonUpdateFullName();
            onClickButtonDeleteAccount();
            onClickClose();
            configView();
        }
        return v;
    }


    /******************************** CONFIGURA VISTA *************************************+/
     *
     */

    private void configView(){
        buttonUpdatePhoto.setEnabled(false);
        editTextFullName.addTextChangedListener(textWatcherEditPesonalDataFullName);
        editTextNickName.addTextChangedListener(textWatcherEditPesonalDataNickName);
    }

    private void findById(View v){
        imageView = v.findViewById(R.id.imageViewEditUser);
        editTextFullName = v.findViewById(R.id.fullNameEditUser);
        editTextNickName  = v.findViewById(R.id.nickNameEditUser);
        buttonDeletePhoto = v.findViewById(R.id.button_delete_photo);
        buttonUpdateFullName = v.findViewById(R.id.button_update_fullname);
        buttonUpdateNickName = v.findViewById(R.id.button_nick_update);
        buttonUpdatePhoto = v.findViewById(R.id.button_update_photo);
        buttonDeleteAccount = v.findViewById(R.id.button_delete_account);
        buttonClose = v.findViewById(R.id.button_close_info);
    }

    private void putData(){
        User user = SessionUser.getInstance().firebaseAdmin.userDataFirebase;
        if(user !=null){
            editTextNickName.setText(user.getNickName());
            editTextFullName.setText(user.getFullName());
            checkAndPhotoUser(user);
        }
    }

    private void checkAndPhotoUser(User user){
        if(user.getImgUser()==null){
            imageView.setImageResource(R.drawable.icon_gallery);
        }else{
            putPhotoUser(user.getImgUser());
        }
    }

    public void putPhotoUser(String stringUri) {
        Utils.loadImage(stringUri,imageView,Utils.PLACEHOLDER_GALLERY);
    }

    private void checkImageUser(){
        if(userUpdate.getImgUser()==null || userUpdate.getImgUser().equals("")){
            buttonUpdatePhoto.setEnabled(false);
        }else{
            onClickButtonUpdatePhoto();
            buttonUpdatePhoto.setEnabled(true);
        }
    }

    /******************************** CONFIGURA DIALOGO *************************************+/
     *
     */

    private void showDialog(String title, String message){
        mProgress = new ProgressDialog(getContext());
        mProgress.setTitle(title);
        mProgress.setMessage(message);
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);
    }

    /******************************** NAVEGAS AL LOGIN *************************************+/
     *
     */

    private void navigatedUserLoginRegister(){
        Intent intent =new Intent(getContext(),FirstActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        getActivity().finish();
    }


    /******************************** CIERRA LA PANTALLA *************************************+/
     *
     */

    private void onClickClose() {
        buttonClose.setOnClickListener(v -> dismiss());
    }

    /******************************** ONCLICK IMAGEN *************************************+/
     *
     */

    private void onClickImage(){
        imageView.setOnClickListener(v -> {
            String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA};
            if (EasyPermissions.hasPermissions(getContext(), perms)) {
                showTakePictureDialog();
            } else {
                EasyPermissions.requestPermissions(getActivity(), getString(R.string.permissions_picture_rationale), REQUEST_IMAGE_PERMISSIONS, perms);
            }
        });
    }

    /******************************** DIALOGO DE IMAGEN *************************************+/
     *
     */

    private void showTakePictureDialog() {
        final CharSequence[] items = {getString(R.string.galeria),getString(R.string.camara), getString(R.string.action_cancel)};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.menuopengalery);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        openGallery();
                        break;
                    case 1:
                        openCamara();
                        break;
                    case 2:
                        dialog.dismiss();
                        break;
                }
            }
        });
        builder.show();
    }

    /******************************** ABRIR GALERIA *************************************+/
     *
     */

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

    /******************************** ABRIR CAMARA *************************************+/
     *
     */

    private void openCamara() {
        File photoFile = null;
        try {
            photoFile = Utils.createImageFile();
            photoPath = photoFile.getAbsolutePath();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if (photoFile != null) {
            Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri photoURI = FileProvider.getUriForFile(getActivity(),
                    "com.utad.david.planfit.camera.fileprovider",
                    photoFile);
            intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(intentCamera, Constants.RequestPermisos.REQUEST_CAMERA);
        }
    }

    /******************************** CALLBACK GALERIA Y CAMARA *************************************+/
     *
     */

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri imageUri;
        if (resultCode == RESULT_OK) {
            if(requestCode == REQUEST_GALLERY){
                try {
                    imageUri = data.getData();
                    putPhotoUser(imageUri.toString());
                    buttonUpdatePhoto.setEnabled(true);
                    if(imageUri!=null){
                        userUpdate.setImgUser(imageUri.toString());
                        onClickButtonUpdatePhoto();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity().getApplicationContext(), getString(R.string.error_gallery_1), Toast.LENGTH_LONG).show();
                }
            }
            if(requestCode ==  Constants.RequestPermisos.REQUEST_CAMERA){
                Bitmap photo = Utils.getBitmapFromPath(photoPath);
                if (photo != null) {
                    Uri tempUri = Utils.getImageUri(getContext(), photo);
                    putPhotoUser(tempUri.toString());
                    buttonUpdatePhoto.setEnabled(true);
                    if(tempUri!=null){
                        userUpdate.setImgUser(tempUri.toString());
                        onClickButtonUpdatePhoto();
                    }
                }
            }

        }else {
            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.error_gallery_2),Toast.LENGTH_LONG).show();
        }
    }

    /******************************** CONFIGURA EDITTEXT DEL NOMBRE *************************************+/
     *
     */

    private TextWatcher textWatcherEditPesonalDataFullName = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            String fullName = editTextFullName.getText().toString().trim();
            if(fullNameValidate(fullName)){
                buttonUpdateFullName.setEnabled(true);
                userUpdate.setFullName(fullName);
            }
        }
    };

    public boolean fullNameValidate(String fullName){
        if(fullName.equals(userUpdate.getFullName())){
            return false;
        }else{
            return true;
        }
    }

    /******************************** CONFIGURA EDITTEXT DEL NICK *************************************+/
     *
     */

    private TextWatcher textWatcherEditPesonalDataNickName = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            String nickName = editTextNickName.getText().toString().trim();
            if(nickNameValidate(nickName)){
                buttonUpdateNickName.setEnabled(true);
                userUpdate.setNickName(nickName);
            }
        }
    };

    public boolean nickNameValidate(String nickName){
        if(nickName.equals(userUpdate.getNickName())){
            return false;
        }else{
            return true;
        }
    }

    /******************************** BORRA FOTO *************************************+/
     *
     */

    private void onClickButtonDeletePhoto(){
        buttonDeletePhoto.setOnClickListener(v -> {
            if(UtilsNetwork.checkConnectionInternetDevice(getContext())){
                showDialog(getString(R.string.title_delete_boto),getString(R.string.message_delete_photo));
                mProgress.show();
                SessionUser.getInstance().firebaseAdmin.userDataFirebase = userUpdate;
                SessionUser.getInstance().firebaseAdmin.deletePhoto();
            }else{
                Toast.makeText(getContext(),getString(R.string.info_network_device),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void errorDeletePhoto(String messageError) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
        builder.setMessage(messageError)
                .setPositiveButton(R.string.info_dialog_err, (dialog, id) -> dialog.dismiss());
        builder.create();
        builder.show();
    }

    /******************************** ACTUALIZA FOTO *************************************+/
     *
     */

    private void onClickButtonUpdatePhoto(){
        buttonUpdatePhoto.setOnClickListener(v -> {
            if(UtilsNetwork.checkConnectionInternetDevice(getContext())){
                showDialog(getString(R.string.title_update_foto),getString(R.string.message_update_photo));
                mProgress.show();
                SessionUser.getInstance().firebaseAdmin.userDataFirebase = userUpdate;
                SessionUser.getInstance().firebaseAdmin.updatePhotoUserInFirebase();
            }else{
                Toast.makeText(getContext(),getString(R.string.info_network_device),Toast.LENGTH_LONG).show();
            }
        });

    }

    private void errorUpdatePhoto(String messageError) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
        builder.setMessage(messageError)
                .setPositiveButton(R.string.info_dialog_err, (dialog, id) -> dialog.dismiss());
        builder.create();
        builder.show();
    }

    /******************************** ACTUALIZA NOMBRE *************************************+/
     *
     */

    private void onClickButtonUpdateFullName(){
        buttonUpdateFullName.setOnClickListener(v -> {
            if(UtilsNetwork.checkConnectionInternetDevice(getContext())){
                showDialog(getString(R.string.title_update_name),getString(R.string.message_update_name));
                mProgress.show();
                SessionUser.getInstance().firebaseAdmin.userDataFirebase = userUpdate;
                SessionUser.getInstance().firebaseAdmin.updateFullNameUserInFirebase();
            }else{
                Toast.makeText(getContext(),getString(R.string.info_network_device),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void errorUpdateName(String message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
        builder.setMessage(message)
                .setPositiveButton(R.string.info_dialog_err, (dialog, id) -> dialog.dismiss());
        builder.create();
        builder.show();
    }

    /******************************** ACTUALIZA NICK *************************************+/
     *
     */

    private void onClickButtonUpdateNickName(){
        if(UtilsNetwork.checkConnectionInternetDevice(getContext())){
            buttonUpdateNickName.setOnClickListener(v -> {
                if(UtilsNetwork.checkConnectionInternetDevice(getContext())){
                    showDialog(getString(R.string.title_update_nick),getString(R.string.message_update_nick));
                    mProgress.show();
                    SessionUser.getInstance().firebaseAdmin.userDataFirebase = userUpdate;
                    SessionUser.getInstance().firebaseAdmin.updateNickNameUserInFirebase();
                }else{
                    Toast.makeText(getContext(),getString(R.string.info_network_device),Toast.LENGTH_LONG).show();
                }

            });
        }
    }

    private void errorUpdateNickName(String message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
        builder.setMessage(message)
                .setPositiveButton(R.string.info_dialog_err, (dialog, id) -> dialog.dismiss());
        builder.create();
        builder.show();
    }

    /******************************** BORRA CUENTA *************************************+/
     *
     */

    private void onClickButtonDeleteAccount(){
        buttonDeleteAccount.setOnClickListener(v -> {
            if(UtilsNetwork.checkConnectionInternetDevice(getContext())){
                createAndShowAlertDialogDeleteUser(getString(R.string.info_delete_acount_1)+" "+userUpdate.getNickName()+
                        getString(R.string.info_delete_acount_2));
            }else{
                Toast.makeText(getContext(),getString(R.string.info_network_device),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void createAndShowAlertDialogDeleteUser(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
        builder.setMessage(message)
                .setPositiveButton(R.string.action_delete, (dialog, id) -> {
                    showDialog(getString(R.string.title_delete_user),getString(R.string.message_delete_user));
                    mProgress.show();
                    SessionUser.getInstance().firebaseAdmin.deleteAccountInFirebase();
                })
                .setNegativeButton(R.string.action_cancel, (dialog, which) -> dialog.dismiss());
        builder.create();
        builder.show();
    }

    private void errorDeleteAccunt(String message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
        builder.setMessage(message)
                .setPositiveButton(R.string.info_dialog_err, (dialog, id) -> dialog.dismiss());
        builder.create();
        builder.show();
    }

    /******************************** CALLBACK DE FIREBASE *************************************+/
     *
     */

    @Override
    public void updatePhotoInFirebase(boolean end) {
        if(end){
            if(mListener!=null){
                mProgress.dismiss();
                buttonUpdatePhoto.setEnabled(false);
                putPhotoUser(userUpdate.getImgUser());
                mListener.updateData(userUpdate);
                Toast.makeText(getContext(),getString(R.string.info_update_photo),Toast.LENGTH_LONG).show();
            }
        }
        else{
            mProgress.dismiss();
            errorUpdatePhoto(getString(R.string.error_update_photo));
            mListener.updateData(userUpdate);
        }
    }

    @Override
    public void deletePhotoInFirebase(boolean end) {
        if(end){
            if(mListener!=null){
                mProgress.dismiss();
                imageView.setImageResource(R.drawable.icon_gallery);
                SessionUser.getInstance().user.setImgUser(null);
                userUpdate.setImgUser(null);
                mListener.updateData(userUpdate);
                Toast.makeText(getContext(),getString(R.string.delete_photo_info),Toast.LENGTH_LONG).show();
            }
        }else{
            mProgress.dismiss();
            errorDeletePhoto(getString(R.string.error_delete_photo));
        }
    }

    @Override
    public void updateNickNameInFirebase(boolean end) {
        if(end){
            if(mListener!=null){
                mProgress.dismiss();
                buttonUpdateNickName.setEnabled(false);
                editTextNickName.setText(userUpdate.getNickName());
                mListener.updateData(userUpdate);
                Toast.makeText(getContext(),getString(R.string.info_update_nickname),Toast.LENGTH_LONG).show();
            }
        }else{
            mProgress.dismiss();
            errorUpdateNickName(getString(R.string.error_nick));
        }
    }

    @Override
    public void updateFullNameInFirebase(boolean end) {
        if(end){
            if(mListener!=null){
                mProgress.dismiss();
                buttonUpdateFullName.setEnabled(false);
                editTextFullName.setText(userUpdate.getFullName());
                mListener.updateData(userUpdate);
                Toast.makeText(getContext(),getString(R.string.info_update_name),Toast.LENGTH_LONG).show();
            }
        }else{
            mProgress.dismiss();
            errorUpdateName(getString(R.string.error_name));
        }
    }

    @Override
    public void deleteUserInFirebase(boolean end) {
        if(end){
            mProgress.dismiss();
            Toast.makeText(getContext(),getString(R.string.info_delete_user),Toast.LENGTH_LONG).show();
            navigatedUserLoginRegister();
        }else{
            mProgress.dismiss();
            errorDeleteAccunt(getString(R.string.error_delete_user));
        }
    }

    /******************************** EasyPermissions.PermissionCallbacks *************************************+/
     *
     */

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
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
