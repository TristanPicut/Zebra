package com.totris.zebra.users.auth;

import android.content.Intent;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.squareup.otto.Subscribe;
import com.totris.zebra.conversations.ConversationsListActivity;
import com.totris.zebra.R;
import com.totris.zebra.groups.Group;
import com.totris.zebra.users.User;
import com.totris.zebra.users.events.UserRegistrationFailedEvent;
import com.totris.zebra.users.events.UserSignInFailedEvent;
import com.totris.zebra.utils.Authentication;
import com.totris.zebra.utils.EventBus;
import com.totris.zebra.utils.WithErrorView;

public class LoginActivity extends AppCompatActivity implements LoginFragment.LoginListener, RegisterFragment.RegisterListener {
    private static final String TAG = "LoginActivity";

    private Authentication auth;

    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = Authentication.getInstance();

        EventBus.register(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorFullscreenStatusBar));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }

        currentFragment = new LoginFragment();

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.activity_main, currentFragment)
                    .commit();
        }
    }

    @Subscribe
    public void onUserSignInEvent(User user) {
        user.updatePublicKey().commit();
        User.getAll();
        Group.getAll();
        Intent intent = new Intent(this, ConversationsListActivity.class);
        startActivity(intent);
    }

    @Subscribe
    public void onUserSignInFailedEvent(UserSignInFailedEvent event) {
        ((WithErrorView) currentFragment).setError(event.getMessage());
    }

    @Subscribe
    public void onUserRegistrationFailedEvent(UserRegistrationFailedEvent event) {
        ((WithErrorView) currentFragment).setError(event.getMessage());
    }

    @Override
    public void onLogin(String mail, String password) {
        auth.signIn(mail, password, getApplicationContext());
    }

    @Override
    public void onGotoRegister() {
        currentFragment = new RegisterFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_main, currentFragment)
                .commit();
    }

    @Override
    public void onRegister(String username, String mail, String password) {
        auth.register(username, mail, password, getApplicationContext());
    }

    @Override
    public void onGotoLogin() {
        currentFragment = new LoginFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_main, currentFragment)
                .commit();
    }
}
