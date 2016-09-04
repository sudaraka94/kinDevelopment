package kin.olivescript.com.kin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;




public class Login extends AppCompatActivity {
    RequestQueue queue;
    ProgressDialog pDialog;
    private String username;
    private String password;
    public Object lock1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        queue = Volley.newRequestQueue(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        lock1=new Object();
        Button loginBtn = (Button) findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText user = (EditText) findViewById(R.id.editText);
                EditText pass = (EditText) findViewById(R.id.editText2);
                username = user.getText().toString();
                password = pass.getText().toString();
                loginAuth(username,password);

            }
        });
    }

    public void switchAct(String username) {
        Intent intent1 = new Intent(this, List.class);
        intent1.putExtra("login.username", username);
        startActivity(intent1);
    }

//    private class loginAuthn extends AsyncTask<Void, Void, Void> {
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            //showing progress dialog
//            pDialog = new ProgressDialog(Login.this);
//            pDialog.setMessage("Please Wait...");
//            pDialog.setCancelable(false);
//            pDialog.show();
//
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            loginAuth(username, password);
//            synchronized (lock1) {
//                while (!volleyStatus) {
//                    try {
//                        lock1.wait();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//                return null;
//            }
//        }
//
//        @Override
//        protected void onPostExecute(Void responce) {
//            super.onPostExecute(responce);
//            if (pDialog.isShowing()) {
//                pDialog.dismiss();
//                Log.d("onPostExecute", "Running");
//            }
//        }
//
//    }


    public void loginAuth(final String username,final String password){
        String url="http://kinserve.herokuapp.com/auth/login/"+username;

        StringRequest strreq=new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(pDialog.isShowing()){
                                pDialog.dismiss();
                            }
                            if (response.equals("true ")) {
                                switchAct(username);
                                Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_LONG).show();
                            } else {
                                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                            }
                        }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("pass", password);
                return params;
            }
        };
        pDialog = new ProgressDialog(Login.this);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        queue.add(strreq);
    }
}
