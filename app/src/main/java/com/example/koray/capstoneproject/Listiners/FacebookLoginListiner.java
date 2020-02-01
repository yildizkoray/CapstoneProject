package com.example.koray.capstoneproject.Listiners;

import com.facebook.FacebookException;
import com.facebook.login.LoginResult;

/**
 * Created by Koray on 12.12.2017.
 */

public interface FacebookLoginListiner {
    public void onSuccess(LoginResult loginResult);

    public void onCancel();

    public void onFailed(FacebookException e);
}
