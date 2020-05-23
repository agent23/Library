package com.host.library.ui.registration;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
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

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_username, et_email, et_phone, et_password, et_re_password;
    private String username, email, phone, password, rePassword;
    private ProgressBar loadingProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initViews();
    }

    private void initViews(){
        et_username = findViewById(R.id.et_username);
        et_email = findViewById(R.id.et_email);
        et_phone = findViewById(R.id.et_phone);
        et_password = findViewById(R.id.et_password);
        et_re_password = findViewById(R.id.et_re_password);
        loadingProgressBar = findViewById(R.id.reg_loading);
        Button btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);
    }

    private void initValues(){
        username = et_username.getText().toString().trim();
        email = et_email.getText().toString().trim();
        phone = et_phone.getText().toString().trim();
        password = et_password.getText().toString().trim();
        rePassword = et_re_password.getText().toString().trim();
    }

    private boolean passwordMatches(String password, String rePassword){
        boolean flag = true;
        for (int i = 0; i < password.length(); i++) {
            if (password.charAt(i) != rePassword.charAt(i)){
                flag = false;
                break;
            }
        }
        return flag;
    }

    private boolean fieldsNotEmpty(){
        boolean flag = true;
        if (StringUtils.isAllBlank(username, email, phone, password, rePassword)){
            flag = false;
        }
        return flag;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_register){
            loadingProgressBar.setVisibility(View.VISIBLE);
            initValues();
            boolean notEmpty = fieldsNotEmpty();

            if (notEmpty){
                emailPasswordValidate();
            }
            else {
                loadingProgressBar.setVisibility(View.GONE);
                Toast.makeText(this, "Fields MUST not be empty", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void emailPasswordValidate() {
        boolean passwordMatches;
        passwordMatches = passwordMatches(password, rePassword);

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            loadingProgressBar.setVisibility(View.GONE);
            et_email.setError("Not a valid email!");
            et_email.requestFocus();
        }

        if (!validatePassword(password)){
            loadingProgressBar.setVisibility(View.GONE);
            et_password.setError("Password must be 8 or more characters and " +
                    "contain a-zA-Z, 0-9, !@#$%&*()_+=|<>?{}");
            et_password.requestFocus();
        }

        if (!passwordMatches){
            loadingProgressBar.setVisibility(View.GONE);
            et_re_password.setError("Passwords mismatch!");
            et_re_password.requestFocus();
        }

        if (!email.equals("") &&
                et_password.getText().toString().length() >= 8 &&
                !et_password.getText().toString().trim().equals("")
                && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            registerUser();
        }
    }


    private JSONObject buildRequest(RegisterUserView userView) throws JSONException {
        JSONObject requestBody = new JSONObject();
        requestBody.put("username", userView.getUsername());
        requestBody.put("email", userView.getEmail());
        requestBody.put("phone", userView.getPhone());
        requestBody.put("password", userView.getPassword());
        return requestBody;
    }

    private void registerUser()  {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        String url = Constants.registration_base_url;
        loadingProgressBar.setVisibility(View.VISIBLE);

        RegisterUserView userView = new RegisterUserView(username, email, phone, password);
        JSONObject requestBody;
        JsonObjectRequest objReq = null;
        try {
            requestBody = buildRequest(userView);
            objReq = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                Toast.makeText(RegistrationActivity.this,
                                        "Hello, "+response.getString("username"), Toast.LENGTH_LONG).show();
                            } catch (JSONException e) {
                                loadingProgressBar.setVisibility(View.GONE);
                                e.printStackTrace();
                            }
                            //Complete and destroy registration activity once successful
                            startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                            finish();
                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // If there a HTTP error then add a note to our repo list.
                            String response = new String(error.networkResponse.data);
                            Toast.makeText(RegistrationActivity.this, response,
                                    Toast.LENGTH_LONG).show();
                            loadingProgressBar.setVisibility(View.GONE);
                        }
                    }
            );
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(RegistrationActivity.this, "Something went wrong, try again!!",
                    Toast.LENGTH_LONG).show();
        }
        // Add the request we just defined to our request queue.
        // The request queue will automatically handle the request as soon as it can.
        requestQueue.add(Objects.requireNonNull(objReq));
    }

    boolean validatePassword(String password) {
        if (password.length() >= 8) {
            Pattern letter = Pattern.compile("[a-zA-Z]");
            Pattern digit = Pattern.compile("[0-9]");
            Pattern special = Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~-]");

            Matcher hasLetter = letter.matcher(password);
            Matcher hasDigit = digit.matcher(password);
            Matcher hasSpecial = special.matcher(password);

            return hasLetter.find() && hasDigit.find() && hasSpecial.find();
        } else
            return false;
    }
}
