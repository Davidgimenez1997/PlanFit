package com.utad.david.planfit.Fragments.Authentication.Register.Details;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.utad.david.planfit.Activitys.MainMenu.MainMenuActivity;
import com.utad.david.planfit.Base.BaseFragment;
import com.utad.david.planfit.Data.User.SessionUser;
import com.utad.david.planfit.Fragments.Authentication.Register.RegisterFragment;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.Constants;
import com.utad.david.planfit.Utils.Utils;

import io.fabric.sdk.android.Fabric;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import java.io.File;
import java.util.List;
import static android.app.Activity.RESULT_OK;
import static com.utad.david.planfit.Utils.Constants.RequestPermissions.REQUEST_GALLERY;
import static com.utad.david.planfit.Utils.Constants.RequestPermissions.REQUEST_IMAGE_PERMISSIONS;

public class RegisterDetailsFragmet extends BaseFragment
        implements EasyPermissions.PermissionCallbacks, RegisterDetailsView {

    /******************************** VARIABLES *************************************+/
     *
     */

    private Callback mListener;
    private EditText fullName;
    private EditText nickName;
    private ImageView imageViewUser;
    private Button buttonOk;
    private Button buttonBackDetails;
    private String photoPath;
    private RegisterDetailsPresenter registerDetailsPresenter;

    /******************************** INTERFAZ *************************************+/
     *
     */

    public interface Callback {
        void clickButtonOk();
        void clickButtonBackDetails();
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

        this.registerDetailsPresenter = new RegisterDetailsPresenter(this);

        if (this.registerDetailsPresenter.checkInternetDevice(getContext())) {
            Fabric.with(getContext(), new Crashlytics());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_register_details, container, false);

        this.findViewById(view);
        this.onClickButtonOk();
        this.onClickButtonBackDetails();
        this.onClickImage();
        this.configView();
        this.createRegisterDetailsDialog();

        return view;
    }

    /******************************** CONFIGURA VISTA *************************************+/
     *
     */

    private void findViewById(View view){
        this.fullName = view.findViewById(R.id.fullName);
        this.nickName = view.findViewById(R.id.nickName);
        this.imageViewUser = view.findViewById(R.id.imageViewUser);
        this.buttonOk = view.findViewById(R.id.buttonOk);
        this.buttonBackDetails = view.findViewById(R.id.buttonBackDetails);
    }

    private void configView(){
        this.fullName.setText("");
        this.nickName.setText("");
        this.imageViewUser.setImageResource(Utils.PLACEHOLDER_GALLERY);
        this.fullName.addTextChangedListener(this.textWatcherRegistreDetailsFragment);
        this.nickName.addTextChangedListener(this.textWatcherRegistreDetailsFragment);
    }


    /******************************** ONCLICK IMAGEN *************************************+/
     *
     */

    private void onClickImage() {
        this.imageViewUser.setOnClickListener(v -> {
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
        builder.setItems(items, (dialog, item) -> {
            switch (item) {
                case 0:
                    this.openGallery();
                    break;
                case 1:
                    this.openCamera();
                    break;
                case 2:
                    dialog.dismiss();
                    break;
            }
        });
        builder.show();
    }

    /******************************** ABRIR GALERIA *************************************+/
     *
     */

    private void openGallery() {
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

    private void openCamera() {
        File photoFile = this.registerDetailsPresenter.getFileForImage();
        // Continue only if the File was successfully created
        if (photoFile != null) {
            this.photoPath = this.registerDetailsPresenter.getPatchForImage(photoFile);
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
            if(requestCode == REQUEST_GALLERY){
                this.registerDetailsPresenter.getImagenForGallery(data, getContext());
            }
            if(requestCode ==  Constants.RequestPermissions.REQUEST_CAMERA) {
                this.registerDetailsPresenter.getImagenForCamera(this.photoPath, getContext());
            }
        } else {
            Toast.makeText(getContext(), getString(R.string.error_gallery_2),Toast.LENGTH_LONG).show();
        }
    }

    /******************************** CONFIGURA EDITTEXT DE LOS DETALLES DEL REGISTRO *************************************+/
     *
     */

    private TextWatcher textWatcherRegistreDetailsFragment = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            buttonOk.setEnabled(registerDetailsPresenter.enableButtonOk(getContext(), nickName.getText().toString().trim(), fullName.getText().toString().trim()));
        }
    };

    /******************************** ONCLICK OK *************************************+/
     *
     */

    private void onClickButtonOk(){
        this.buttonOk.setOnClickListener(v -> {
            if (this.mListener != null) {
                this.registerDetailsPresenter.setDataUser();
                this.showRegisterDetailsDialog();
                this.mListener.clickButtonOk();
            } else {
                this.dismissRegisterDetailsDialog();
            }
        });
    }

    /******************************** ONCLICK BACK *************************************+/
     *
     */

    private void onClickButtonBackDetails(){
        this.buttonBackDetails.setOnClickListener(v -> {
            if (this.mListener != null) {
                this.mListener.clickButtonBackDetails();
            }
        });
    }

    /******************************** ERROR AL REGISTRAR *************************************+/
     *
     */

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


    /******************************** CALLBACK DEL PRESENTER *************************************+/
     *
     */

    @Override
    public void setImagenGallery(Bitmap selectedImage) {
        this.imageViewUser.setImageBitmap(selectedImage);
    }

    @Override
    public void setImagenCamera(Bitmap photo) {
        this.imageViewUser.setImageBitmap(photo);
    }

    @Override
    public void errorGallery() {
        Toast.makeText(getContext(), getString(R.string.error_gallery_1), Toast.LENGTH_LONG).show();
    }

    @Override
    public void registerUser() {
        Toast.makeText(getContext(), getString(R.string.info_register)+" "+ SessionUser.getInstance().getUser().getFullName().trim(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void errorRegister() {
        this.dismissRegisterDetailsDialog();
        this.errorSingInRegister(getString(R.string.err_register_fail));
        Toast.makeText(getContext(), getString(R.string.err_register), Toast.LENGTH_LONG).show();
    }

    @Override
    public void navigateToMainMenu() {
        this.dismissRegisterDetailsDialog();
        Intent intent = new Intent(getContext(), MainMenuActivity.class);
        startActivity(intent);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        getActivity().finish();
    }

    @Override
    public void errorAddUserData() {
        this.dismissRegisterDetailsDialog();
    }

    @Override
    public void deviceOffline() {
        Toast.makeText(getContext(),getString(R.string.info_network_device),Toast.LENGTH_LONG).show();
    }
}
