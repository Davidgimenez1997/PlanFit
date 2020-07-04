package com.utad.david.planfit.DialogFragment.User.EditUserProfiler;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.utad.david.planfit.Activitys.AuthenticationActivity;
import com.utad.david.planfit.Base.BaseDialogFragment;
import com.utad.david.planfit.Utils.Constants;
import com.utad.david.planfit.Utils.Utils;
import com.utad.david.planfit.Model.User.User;
import com.utad.david.planfit.R;
import io.fabric.sdk.android.Fabric;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import java.io.File;
import java.util.List;
import static android.app.Activity.RESULT_OK;
import static com.utad.david.planfit.Utils.Constants.RequestPermissions.REQUEST_GALLERY;
import static com.utad.david.planfit.Utils.Constants.RequestPermissions.REQUEST_IMAGE_PERMISSIONS;

public class EditUserProfilerDialogFragment
        extends BaseDialogFragment
        implements EditUserProfilerView,
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
    private Callback mListener;
    private ProgressDialog mProgress;
    private String photoPath;
    private EditUserProfilerPresenter editUserProfilerPresenter;

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

    /******************************** SET CALLBACK FIREBASE *************************************+/
     *
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.editUserProfilerPresenter = new EditUserProfilerPresenter(this);
        if (this.editUserProfilerPresenter.checkInternetDevice(getContext())) {
            Fabric.with(getContext(), new Crashlytics());
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.dialog_edit_user_data, container, false);
        v.setBackgroundResource(R.drawable.corner_dialog_fragment);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        this.userUpdate = this.editUserProfilerPresenter.getUser();

        if (this.userUpdate != null) {
            this.findById(v);
            this.putData();
            this.updateBottonImagen();
            this.onClickImage();
            this.onClickButtonDeletePhoto();
            this.onClickButtonUpdateNickName();
            this.onClickButtonUpdateFullName();
            this.onClickButtonDeleteAccount();
            this.onClickClose();
            this.configView();
        }
        return v;
    }


    /******************************** CONFIGURA VISTA *************************************+/
     *
     */

    private void configView(){
        this.buttonUpdatePhoto.setEnabled(false);
        this.editTextFullName.addTextChangedListener(this.textWatcherEditPesonalDataFullName);
        this.editTextNickName.addTextChangedListener(this.textWatcherEditPesonalDataNickName);
    }

    private void findById(View v){
        this.imageView = v.findViewById(R.id.imageViewEditUser);
        this.editTextFullName = v.findViewById(R.id.fullNameEditUser);
        this.editTextNickName  = v.findViewById(R.id.nickNameEditUser);
        this.buttonDeletePhoto = v.findViewById(R.id.button_delete_photo);
        this.buttonUpdateFullName = v.findViewById(R.id.button_update_fullname);
        this.buttonUpdateNickName = v.findViewById(R.id.button_nick_update);
        this.buttonUpdatePhoto = v.findViewById(R.id.button_update_photo);
        this.buttonDeleteAccount = v.findViewById(R.id.button_delete_account);
        this.buttonClose = v.findViewById(R.id.button_close_info);
    }

    private void putData(){
        User user = this.editUserProfilerPresenter.getUser();
        if (user != null) {
            this.editTextNickName.setText(user.getNickName());
            this.editTextFullName.setText(user.getFullName());
            this.editUserProfilerPresenter.checkImagenUser(user.getImgUser());
        }
    }

    private void updateBottonImagen(){
        User user = this.editUserProfilerPresenter.getUser();
        if (user != null) {
            this.editUserProfilerPresenter.updateBottonImage(user);
        }
    }

    /******************************** CONFIGURA DIALOGO *************************************+/
     *
     */

    private void showDialog(String title, String message){
        this.mProgress = new ProgressDialog(getContext());
        this.mProgress.setTitle(title);
        this.mProgress.setMessage(message);
        this.mProgress.setCancelable(false);
        this.mProgress.setIndeterminate(true);
    }

    /******************************** NAVEGAS AL LOGIN *************************************+/
     *
     */

    private void navigatedUserLoginRegister(){
        Intent intent =new Intent(getContext(), AuthenticationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        getActivity().finish();
    }


    /******************************** CIERRA LA PANTALLA *************************************+/
     *
     */

    private void onClickClose() {
        this.buttonClose.setOnClickListener(v -> dismiss());
    }

    /******************************** ONCLICK IMAGEN *************************************+/
     *
     */

    private void onClickImage(){
        this.imageView.setOnClickListener(v -> {
            String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA};
            if (EasyPermissions.hasPermissions(getContext(), perms)) {
                this.showTakePictureDialog();
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
        File photoFile = this.editUserProfilerPresenter.getFileForImage();
        // Continue only if the File was successfully created
        if (photoFile != null) {
            this.photoPath = this.editUserProfilerPresenter.getPatchForImage(photoFile);
            Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri photoURI = FileProvider.getUriForFile(getActivity(),
                    "com.utad.david.planfit.camera.fileprovider",
                    photoFile);
            intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(intentCamera, Constants.RequestPermissions.REQUEST_CAMERA);
        }
    }

    /******************************** CALLBACK GALERIA Y CAMARA *************************************+/
     *
     */

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_GALLERY) {
                this.editUserProfilerPresenter.getImagenForGallery(data);
            }
            if (requestCode ==  Constants.RequestPermissions.REQUEST_CAMERA) {
                this.editUserProfilerPresenter.getImagenForCamera(this.photoPath, getContext());
            }

        } else {
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
            if (editUserProfilerPresenter.fullNameValidate(fullName, userUpdate)) {
                buttonUpdateFullName.setEnabled(true);
                userUpdate.setFullName(fullName);
            }
        }
    };

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
            if (editUserProfilerPresenter.nickNameValidate(nickName, userUpdate)) {
                buttonUpdateNickName.setEnabled(true);
                userUpdate.setNickName(nickName);
            }
        }
    };



    /******************************** BORRA FOTO *************************************+/
     *
     */

    private void onClickButtonDeletePhoto(){
        this.buttonDeletePhoto.setOnClickListener(v -> {
            if (this.editUserProfilerPresenter.checkInternetDevice(getContext())) {
                this.showDialog(getString(R.string.title_delete_boto),getString(R.string.message_delete_photo));
                this.mProgress.show();
                this.editUserProfilerPresenter.clickDeletePhoto(this.userUpdate);
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
        this.buttonUpdatePhoto.setOnClickListener(v -> {
            if (this.editUserProfilerPresenter.checkInternetDevice(getContext())) {
                this.showDialog(getString(R.string.title_update_foto), getString(R.string.message_update_photo));
                this.mProgress.show();
                this.editUserProfilerPresenter.clickUpdatePhoto(this.userUpdate);
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
        this.buttonUpdateFullName.setOnClickListener(v -> {
            if (this.editUserProfilerPresenter.checkInternetDevice(getContext())) {
                this.showDialog(getString(R.string.title_update_name),getString(R.string.message_update_name));
                this.mProgress.show();
                this.editUserProfilerPresenter.clickUpdateFullName(this.userUpdate);
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
        this.buttonUpdateNickName.setOnClickListener(v -> {
            if (this.editUserProfilerPresenter.checkInternetDevice(getContext())) {
                this.showDialog(getString(R.string.title_update_nick), getString(R.string.message_update_nick));
                this.mProgress.show();
                this.editUserProfilerPresenter.clickUpdateNickName(this.userUpdate);
            }
        });
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
        this.buttonDeleteAccount.setOnClickListener(v -> {
            if (this.editUserProfilerPresenter.checkInternetDevice(getContext())){
                this.createAndShowAlertDialogDeleteUser(getString(R.string.info_delete_acount_1)+" "+ this.userUpdate.getNickName() + getString(R.string.info_delete_acount_2));
            }
        });
    }

    private void createAndShowAlertDialogDeleteUser(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
        builder.setMessage(message)
                .setPositiveButton(R.string.action_delete, (dialog, id) -> {
                    this.showDialog(getString(R.string.title_delete_user),getString(R.string.message_delete_user));
                    this.mProgress.show();
                    this.editUserProfilerPresenter.clickDeleteUser();
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

    /******************************** CALLBACK DE PRESENTER *************************************+/
     *
     */

    @Override
    public void deviceOfflineMessage() {
        Toast.makeText(getContext(),getString(R.string.info_network_device),Toast.LENGTH_LONG).show();
    }

    @Override
    public void putDefaultImage() {
        this.imageView.setImageResource(R.drawable.icon_gallery);
    }

    @Override
    public void putUserImage(String imgUser) {
        Utils.loadImage(imgUser, this.imageView, Utils.PLACEHOLDER_GALLERY);
    }

    @Override
    public void updateButtonImagen(boolean enabled) {
        if (enabled) {
            this.buttonUpdatePhoto.setEnabled(enabled);
            this.onClickButtonUpdatePhoto();
        } else {
            this.buttonUpdatePhoto.setEnabled(enabled);
        }
    }

    @Override
    public void getImagenGallery(String imagen) {
        this.editUserProfilerPresenter.checkImagenUser(imagen);
        this.buttonUpdatePhoto.setEnabled(true);
        if (imagen != null) {
            this.userUpdate.setImgUser(imagen);
            this.onClickButtonUpdatePhoto();
        }
    }

    @Override
    public void getErrorGallery() {
        Toast.makeText(getActivity().getApplicationContext(), getString(R.string.error_gallery_1), Toast.LENGTH_LONG).show();
    }

    @Override
    public void getImagenCamera(Uri imagen) {
        this.editUserProfilerPresenter.checkImagenUser(imagen.toString());
        this.buttonUpdatePhoto.setEnabled(true);
        if (imagen != null) {
            this.userUpdate.setImgUser(imagen.toString());
            this.onClickButtonUpdatePhoto();
        }
    }

    @Override
    public void deletePhoto() {
        if (this.mListener != null) {
            this.mProgress.dismiss();
            this.imageView.setImageResource(R.drawable.icon_gallery);
            this.userUpdate.setImgUser("");
            Toast.makeText(getContext(),getString(R.string.delete_photo_info),Toast.LENGTH_LONG).show();
            this.mListener.updateData(userUpdate);
        }
    }

    @Override
    public void errorDeletePhoto() {
        this.mProgress.dismiss();
        this.errorDeletePhoto(getString(R.string.error_delete_photo));
    }

    @Override
    public void updatePhoto() {
        if (this.mListener != null) {
            this.mProgress.dismiss();
            this.buttonUpdatePhoto.setEnabled(false);
            this.editUserProfilerPresenter.checkImagenUser(this.userUpdate.getImgUser());
            this.mListener.updateData(this.userUpdate);
            Toast.makeText(getContext(),getString(R.string.info_update_photo),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void errorUpdatePhoto() {
        this.mProgress.dismiss();
        this.errorUpdatePhoto(getString(R.string.error_update_photo));
        this.mListener.updateData(this.userUpdate);
    }

    @Override
    public void updateNickName() {
        if (this.mListener != null) {
            this.mProgress.dismiss();
            this.buttonUpdateNickName.setEnabled(false);
            this.editTextNickName.setText(this.userUpdate.getNickName());
            this.mListener.updateData(this.userUpdate);
            Toast.makeText(getContext(),getString(R.string.info_update_nickname),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void errorUpdateNickName() {
        this.mProgress.dismiss();
        this.errorUpdateNickName(getString(R.string.error_nick));
    }

    @Override
    public void updateFullName() {
        if (this.mListener != null) {
            this.mProgress.dismiss();
            this.buttonUpdateFullName.setEnabled(false);
            this.editTextFullName.setText(this.userUpdate.getFullName());
            this.mListener.updateData(this.userUpdate);
            Toast.makeText(getContext(),getString(R.string.info_update_name),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void errorUpdateFullName() {
        this.mProgress.dismiss();
        this.errorUpdateName(getString(R.string.error_name));
    }

    @Override
    public void deleteUser() {
        this.mProgress.dismiss();
        Toast.makeText(getContext(),getString(R.string.info_delete_user),Toast.LENGTH_LONG).show();
        this.navigatedUserLoginRegister();
    }

    @Override
    public void errorDeleteUser() {
        this.mProgress.dismiss();
        this.errorDeleteAccunt(getString(R.string.error_delete_user));
    }

    /******************************** EasyPermissions.PermissionCallbacks *************************************+/
     *
     */

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        try {
            this.showTakePictureDialog();
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
