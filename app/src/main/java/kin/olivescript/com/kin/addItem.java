package kin.olivescript.com.kin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class addItem extends AppCompatActivity {
private String username;
    protected Object lockT;
    ProgressDialog pDialog;
    JSONArray jsonArray;
    RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        lockT=new Object();
        jsonArray=new JSONArray();
        queue= Volley.newRequestQueue(getApplicationContext());
        Bundle extras=getIntent().getExtras();
        if(extras!=null){
            username=extras.getString("login.username");
        }
    }

    public void addInput(View v){
        EditText text= (EditText) findViewById(R.id.editText3);
        String jItem=text.getText().toString();;
        Log.d("AddInput", "ok");
        pushL(jItem);
    }



//    protected void getL(final JSONObject jItem){
//        String url="http://kinserve.herokuapp.com/list/"+username;
//
//        StringRequest strreq=new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            Log.d("getL","create array");
//                            jsonArray = new JSONArray(response);
//                            jsonArray.put(jItem);
//                            Log.d("getL", jsonArray.toString());
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        Log.d("GetL", "ok");
//                        pushL();
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        pDialog.dismiss();
//                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
//                    }
//                });
//        Log.d("getL","Assign StringRequest");
//        pDialog=new ProgressDialog(addItem.this);
//        pDialog.setMessage("Please Wait...");
//        pDialog.setCancelable(false);
//        pDialog.show();
//        queue.add(strreq);
//        Log.d("getL", "Request added to queue");
//    }

    protected void pushL(final String jitem){
        String url="http://kinserve.herokuapp.com/list/"+username+"/add";

        StringRequest jreq=new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                Log.d("PushL", "ok");
                back();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Log.d("LIST", jitem);
                HashMap<String,String> param=new HashMap();
                param.put("list","{\"item\":\""+jitem+"\"}");
                Log.d("Param",param.toString());
                return param;
            }
        };
        pDialog=new ProgressDialog(addItem.this);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        Log.d("getL", "Request added to queue");
        queue.add(jreq);
    }

    protected void back(){
        Intent intent1 = new Intent(this, List.class);
        intent1.putExtra("login.username", username);
        Log.d("Back", "ok");
        startActivity(intent1);
    }
}


