package com.app.seniorproject.mainseniorprojectpart;

import android.content.Context;

/**
 * Created by Blue on 12/4/2016.
 */

public interface LoginDialogComm {

    void registerUser(String name1, String email1, String password1, Context context);
    void loginUser(String email1, String password1, Context context);
}
