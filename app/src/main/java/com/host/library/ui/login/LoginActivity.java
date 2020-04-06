package com.host.library.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import com.host.library.R;
import com.host.library.configs.Constants;
import com.host.library.ui.registration.RegistrationActivity;
import com.host.library.utils.HttpUtil;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                boolean valid = validateCredentials(username, password);
                if (valid) {
                    loginClicked(username, password);
                } else
                    Toast.makeText(LoginActivity.this, "username/password is empty!", Toast.LENGTH_LONG).show();
                loadingProgressBar.setVisibility(View.VISIBLE);
                //Complete and destroy login activity once successful
                finish();
            }
        });
    }

    private boolean validateCredentials(String username, String password) {
        boolean flag = true;
        if (username.trim().equals("") || username.isEmpty() || password.trim().equals("") || password.isEmpty()) {
            flag = false;
        }
        return flag;
    }

    private void loginClicked(String username, String password) {
        RequestParams rp = new RequestParams();
        rp.add("username", username);
        rp.add("password", password);

        HttpUtil.post(Constants.login_base_url, rp, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                Log.d("asd", "---------------- this is response : " + response);
                try {
                    JSONObject serverResp = new JSONObject(response.toString());

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
    }

//    private void updateUiWithUser(LoginUserView model) {
//        String welcome = getString(R.string.welcome) + model.getDisplayName();
//        // TODO : initiate successful logged in experience
//        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
//    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.login_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.reg_menu_item) {
            this.startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
