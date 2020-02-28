package com.utad.david.planfit.DialogFragment.User;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.utad.david.planfit.Base.BaseDialogFragment;
import com.utad.david.planfit.Model.User.User;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.Constants;
import com.utad.david.planfit.Utils.Utils;

public class UserDetailDialogFragments extends BaseDialogFragment {

    /******************************** VARIABLES *************************************+/
     *
     */

    public static final String EXTRA_USER = Constants.ConfigChat.EXTRA_USER;

    private User userDetail;
    private Toolbar toolbar;

    private ImageView imageUser;
    private TextView nameUser;
    private TextView nickUser;


    public static UserDetailDialogFragments newInstance(User user) {
        Bundle args = new Bundle();
        UserDetailDialogFragments fragment = new UserDetailDialogFragments();
        args.putParcelable(EXTRA_USER,user);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userDetail = getArguments().getParcelable(EXTRA_USER);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_fragment_user_detail, container, false);

        toolbar = view.findViewById(R.id.toolbar);
        imageUser = view.findViewById(R.id.imageUser);
        nameUser = view.findViewById(R.id.name);
        nickUser = view.findViewById(R.id.nick);

        setUI();
        return view;

    }

    private void setUI() {
        showLoading();
        Utils.loadImage(userDetail.getImgUser(),imageUser,Utils.PLACEHOLDER_USER);
        nameUser.setText(userDetail.getFullName());
        nickUser.setText(userDetail.getNickName());
        hideLoading();
    }
}
