package com.utad.david.planfit.DialogFragment.User.UserDetailChat;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
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

public class UserDetailChatDialogFragments extends BaseDialogFragment {

    /******************************** VARIABLES *************************************+/
     *
     */

    public static final String EXTRA_USER = Constants.ConfigChat.EXTRA_USER;

    private User userDetail;
    private ImageView imageUser;
    private TextView nameUser;
    private TextView nickUser;


    public static UserDetailChatDialogFragments newInstance(User user) {
        Bundle args = new Bundle();
        UserDetailChatDialogFragments fragment = new UserDetailChatDialogFragments();
        args.putParcelable(EXTRA_USER,user);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.userDetail = getArguments().getParcelable(EXTRA_USER);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_fragment_user_detail, container, false);

        this.imageUser = view.findViewById(R.id.imageUser);
        this.nameUser = view.findViewById(R.id.name);
        this.nickUser = view.findViewById(R.id.nick);

        this.setUI();
        return view;

    }

    private void setUI() {
        this.showLoading();
        Utils.loadImage(this.userDetail.getImgUser(), this.imageUser, Utils.PLACEHOLDER_USER);
        this.nameUser.setText(this.userDetail.getFullName());
        this.nickUser.setText(this.userDetail.getNickName());
        this.hideLoading();
    }
}
