package com.vishnu.madirectory;

import android.Manifest;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Main3Activity extends AppCompatActivity {
    Intent i1;
    String value[]=new String[6],mail1,sms1;
    TextView detail,phone1,phone2,mail;
    Button s,w;
    String url = "http://172.18.114.203:5678/sendit?filenam=";
    ImageView image;
    int type,id;
    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        i1=getIntent();
        image=findViewById(R.id.imageView);
        type=i1.getIntExtra("type",0);
        id=i1.getIntExtra("id",100);
        value=Main6Activity.sql.selectAll(id);
        setTitle(value[0]);
        GetXMLTask task = new GetXMLTask();
        // Execute the task
        if(isNetworkAvailable())
        task.execute(new String[] { url +value[7]});
        detail=findViewById(R.id.textView3);
        phone1=findViewById(R.id.textView2);
        phone2=findViewById(R.id.textView8);
        mail=findViewById(R.id.textView5);
        s=findViewById(R.id.button3);
        w=findViewById(R.id.button5);
        builder = new AlertDialog.Builder(this);
        if(Integer.parseInt(value[6])==1)
            detail.setText("Current Staff\n"+value[4]+"-"+value[5]);
        else
            detail.setText("Retired Staff\n"+value[4]+"-"+value[5]);
        SpannableString content=new SpannableString(value[3]);
        content.setSpan(new UnderlineSpan(),0,content.length(),0);
        mail.setText(content);
        if((value[1].equalsIgnoreCase("nil"))) {
        phone1.setVisibility(View.INVISIBLE);
        }
        else {
            phone1.setText(value[1]);
            sms1 = value[1];
        }
        if(value[2].equalsIgnoreCase("nil")) {
            phone2.setVisibility(View.INVISIBLE);
        }
        else
            phone2.setText(value[2]);
        if(value[3].equalsIgnoreCase("nil")) {
            mail.setVisibility(View.INVISIBLE);
        }
        if((value[1].equalsIgnoreCase("nil")&&(value[2].equalsIgnoreCase("nil"))))
        {
            s.setVisibility(View.INVISIBLE);
            w.setVisibility(View.INVISIBLE);
        }
        Toast.makeText(getApplicationContext(),"Tap on a Number to CALL",Toast.LENGTH_LONG).show();
        phone1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone1.setTextColor(getResources().getColor(R.color.colorAccent));
                phone2.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                Toast.makeText(getApplicationContext(), "Initiating Call...", Toast.LENGTH_LONG).show();
                if(ActivityCompat.checkSelfPermission(Main3Activity.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED)
                    ActivityCompat.requestPermissions(Main3Activity.this,new String[]{Manifest.permission.CALL_PHONE},1);
                try {
                    i1 = new Intent(Intent.ACTION_CALL, Uri.parse("tel:+91"+value[1]));
                    startActivity(i1);
                } catch (Exception E) {
                    Toast.makeText(getApplicationContext(),E.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        phone2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone1.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                phone2.setTextColor(getResources().getColor(R.color.colorAccent));
                mail.setTextColor(getResources().getColor(R.color.blue));
                Toast.makeText(getApplicationContext(), "Initiating Call...", Toast.LENGTH_LONG).show();
                if(ActivityCompat.checkSelfPermission(Main3Activity.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED)
                    ActivityCompat.requestPermissions(Main3Activity.this,new String[]{Manifest.permission.CALL_PHONE},1);

                try{
                    i1=new Intent(Intent.ACTION_CALL,Uri.parse("tel:+91"+value[2]));
                    startActivity(i1);}
                catch (Exception E){
                }


            }
        });
        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Uri uri = Uri.parse("smsto:+91" + sms1);
                    Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                    intent.putExtra("sms_body", "");
                    startActivity(intent);
                }
                catch (Exception e){
                    Toast.makeText(getApplicationContext(),e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        w.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    i1 = new Intent(Intent.ACTION_VIEW);
                    i1.setData(Uri.parse("http://api.whatsapp.com/send?phone=91" + sms1));
                    startActivity(i1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mail.setTextColor(getResources().getColor(R.color.green));
                try{
                    i1 = new Intent(Intent.ACTION_SENDTO);
                    mail1="mailto:"+value[3]+Uri.encode("")+Uri.encode("")+Uri.encode("");
                    i1.setData(Uri.parse(mail1));
                    startActivity(i1);
                }
                catch (Exception E) {
                }
            }
        });
    }
    public void onBackPressed(){
        if(type==2) {
            i1 = new Intent(getApplicationContext(), Main2Activity.class);
            i1.putExtra("dep", value[4]);
        }
        else{
            i1=new Intent(getApplicationContext(),MainActivity.class);
        i1.putExtra("load",0);}
        startActivity(i1);

    }

    public boolean onCreateOptionsMenu(Menu men) {
        getMenuInflater().inflate(R.menu.help, men);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item2) {
            builder.setMessage(">You can TAP on any number to initiate call.\n>You can TAP on E-mail send mail.\n>You can select a number using selection button to send SMS/WhatsApp message.\n>Data updated automatically when you connected internet.\n")
                    .setCancelable(false)
                    .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            //Creating dialog box
            AlertDialog alert = builder.create();
            //Setting the title manually
            alert.setTitle("Help");
            alert.show();
        }
        if (item.getItemId() == R.id.item4) {
            builder.setMessage("Online Phonebook designed for MA College of Engineering Kothamangalam\n\nMacebook\nVersion : 2.7.5\nUpdated On 07/12/2020\nReleased On 02/06/2020\n\nDeveloped by\n Vishnu P U\n builtfromnull@gmail.com\n")
                    .setCancelable(false)
                    .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            //Creating dialog box
            AlertDialog alert = builder.create();
            //Setting the title manually
            alert.setTitle("About");
            alert.show();
        }
        if (item.getItemId() == R.id.item3) {
            Toast.makeText(getApplicationContext(),"Redirected to Mail...",Toast.LENGTH_SHORT).show();
            i1 = new Intent(Intent.ACTION_SEND);
            i1.putExtra(Intent.EXTRA_EMAIL, new String[]{"builtfromnull@gmail.com"});
            i1.putExtra(Intent.EXTRA_SUBJECT, "Macebook_Feedback");
            i1.putExtra(Intent.EXTRA_TEXT, "Hi \n");
            i1.setType("message/rfc822");
            startActivity(Intent.createChooser(i1, "Send Mail"));
        }
        return true;
    }
    private class GetXMLTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap map = null;
            for (String url : urls) {
                map = downloadImage(url);
            }
            return map;
        }

        // Sets the Bitmap returned by doInBackground
        @Override
        protected void onPostExecute(Bitmap result) {
            image.setImageBitmap(result);
        }

        // Creates Bitmap from InputStream and returns it
        private Bitmap downloadImage(String url) {
            Bitmap bitmap = null;
            InputStream stream = null;
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inSampleSize = 1;

            try {
                stream = getHttpConnection(url);
                bitmap = BitmapFactory.
                        decodeStream(stream, null, bmOptions);
                stream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return bitmap;
        }

        // Makes HttpURLConnection and returns InputStream
        private InputStream getHttpConnection(String urlString)
                throws IOException {
            InputStream stream = null;
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();

            try {
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                httpConnection.setRequestMethod("GET");
                httpConnection.connect();

                if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    stream = httpConnection.getInputStream();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return stream;
        }
    }
    public boolean isNetworkAvailable() {
      ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
