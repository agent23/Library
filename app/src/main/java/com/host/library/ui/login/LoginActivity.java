package com.host.library.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.host.library.MainActivity;
import com.host.library.R;
import com.host.library.configs.Constants;
import com.host.library.ui.registration.RegistrationActivity;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity  {

    private ProgressBar loadingProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        loadingProgressBar = findViewById(R.id.login_loading);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                boolean valid = validateCredentials(username, password);
                if (valid) {
                    login(username, password);
                } else
                    Toast.makeText(LoginActivity.this, "Enter username and password", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void login(String username, String password){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = Constants.login_base_url + "username=" + username + "&password=" + password;
        loadingProgressBar.setVisibility(View.VISIBLE);
        JsonObjectRequest objReq = new JsonObjectRequest(Request.Method.POST, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            setLoggedInUserModel(response);
                            //Complete and destroy login activity once successful
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } catch (JSONException e) {
                            // If there is an error then output this to the logs.
                            loadingProgressBar.setVisibility(View.GONE);
                            //Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // If there a HTTP error then add a note to our repo list.
                        String errorResponse = new String(error.networkResponse.data);
                        Toast.makeText(LoginActivity.this, errorResponse, Toast.LENGTH_LONG).show();
                        loadingProgressBar.setVisibility(View.GONE);
                    }
                }
        );
        // Add the request we just defined to our request queue.
        // The request queue will automatically handle the request as soon as it can.
        requestQueue.add(objReq);
    }

    private void setLoggedInUserModel(JSONObject response) throws JSONException {
        new LoggedInUser(response.getString("username"), response.getString("email"),
                response.getString("phone")
        );
    }

    private boolean validateCredentials(String username, String password) {
        boolean flag = true;
        if (StringUtils.isAllBlank(username, password)) {
            flag = false;
        }
        return flag;
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
        }
        return super.onOptionsItemSelected(item);
    }
}
