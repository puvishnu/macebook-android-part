package com.vishnu.madirectory;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
public class MainActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    SharedPreferences sh;
    Spinner sp1;
    Button b;
    TextView t,t2;
    AutoCompleteTextView at;
    Intent i1;
    int depId = 0, pos;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Home");
        b = findViewById(R.id.update);
        sp1 = findViewById(R.id.depspin);
        at = findViewById(R.id.searchall);
        t=findViewById(R.id.textView9);
        t2=findViewById(R.id.textView10);
        sh = getSharedPreferences("macebook", 0);
        t.setSelected(true);
        i1=getIntent();
        pos = sh.getInt("check", 9);
        if (pos == 9) {
            at.setVisibility(View.INVISIBLE);
            t2.setVisibility(View.INVISIBLE);
            sp1.setVisibility(View.INVISIBLE);
            t.setText("Not Updated Please Click \"Download data\"");
           if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CALL_PHONE},1);
                b.setText("Download data");
            Toast.makeText(getApplicationContext(), "Turn On Internet and Click \"Download data\"", Toast.LENGTH_LONG).show();
        } else if (pos == 5) {
        if(i1.getIntExtra("load",0)==1) {
               progressDialog = new ProgressDialog(MainActivity.this);
               progressDialog.setMessage("Please wait.....");
               progressDialog.setTitle("Downloading");
               progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
               progressDialog.setCancelable(false);
               progressDialog.show();
               new Thread(new Runnable() {
                   public void run() {
                       try {
                           Thread.sleep(2000);
                       } catch (Exception e) {
                           e.printStackTrace();
                       }
                       progressDialog.dismiss();
                   }
               }).start();
           }
            t2.setSelected(true);
            if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CALL_PHONE},1);
            ArrayAdapter<String> arr = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Main6Activity.sql.dsiplayDepNam());
            sp1.setAdapter(arr);
            ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Main6Activity.sql.dsiplayNam());
            at.setAdapter(arrayAdapter1);
            at.setThreshold(1);
            t.setText(sh.getString("notice",""));
            b.setVisibility(View.INVISIBLE);
            sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    depId = position;
                    if (depId != 0) {
                        i1 = new Intent(getApplicationContext(), Main2Activity.class);
                        i1.putExtra("dep", sp1.getSelectedItem().toString());
                        startActivity(i1);
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            at.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    goTo(Main6Activity.sql.idFind(at.getText().toString()));
                }
            });
        }
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable()) {
                    i1=new Intent(getApplicationContext(),Main6Activity.class);
                    startActivity(i1);
                } else
                    Toast.makeText(getApplicationContext(), "Internet Connection required", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onBackPressed() {
        this.finishAffinity();
    }

    public boolean isNetworkAvailable() {
     ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

    public void goTo(int id) {
        if (id == -1)
            Toast.makeText(getApplicationContext(), "Problem Contact Admin", Toast.LENGTH_SHORT).show();
        else {
            i1 = new Intent(getApplicationContext(), Main3Activity.class);
            i1.putExtra("id", id);
            i1.putExtra("type", 1);
            startActivity(i1);
        }
    }
}






