package kin.olivescript.com.kin;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class List extends AppCompatActivity {
//    private String TAG=List.class.getSimpleName();
    private ProgressDialog pDialog;
    private ListView lv;
    private RequestQueue queue;
    private boolean volleyStatus=false;
    private String username;
    ArrayList<HashMap<String,String>> itemList;
    private Object lockT=new Object();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        queue= Volley.newRequestQueue(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        username="sudaraka";
        itemList=new ArrayList<>();
        lv= (ListView) findViewById(R.id.list);
        new GetList().execute();
    }

    private class GetList extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //showing progress dialog
            pDialog=new ProgressDialog(List.this);
            pDialog.setMessage("Please Wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String url="http://kinserve.herokuapp.com/list/"+username;

            StringRequest strreq=new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                           decode(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                        }
                    });

            queue.add(strreq);
            synchronized(lockT) {
                while(!volleyStatus){
                    try {
                        lockT.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Log.d("return null", "Running");
                return null;
            }
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(pDialog.isShowing()){
                pDialog.dismiss();
                Log.d("onPostExecute", "Running");
            }
            ListAdapter adapter=new SimpleAdapter(List.this,itemList,R.layout.list_item,new String[]{"name"},new int[]{R.id.name});
            lv.setAdapter(adapter);
            Log.d("Adapter", "Set");
        }
    }



    public void decode(String input) {
        synchronized (lockT) {
            try {
                JSONObject jsonObject = new JSONObject(input);
                //getting JSON Array node
                JSONArray array = jsonObject.getJSONArray("list");
                for (int i = 0; i < array.length(); i++) {
                    try {
                        JSONObject item = array.getJSONObject(i);
                        HashMap<String, String> itemO = new HashMap<>();
                        itemO.put("name", item.getString("item"))
                        ;
                        itemList.add(itemO);
                        Log.d("ItemAdd", item.getString("item"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    volleyStatus = true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("onResponce", "Running");
            lockT.notify();
        }
    }
}
