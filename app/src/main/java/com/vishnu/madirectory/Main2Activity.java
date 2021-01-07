package com.vishnu.madirectory;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Toast;

public class Main2Activity extends AppCompatActivity {
    Intent i1;
    String dep;
    ListView l;
    AutoCompleteTextView at;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        i1=getIntent();
        dep=i1.getStringExtra("dep");
        setTitle(dep);
        l=findViewById(R.id.list);
        at=findViewById(R.id.autoCompleteTextView);
        if(Main6Activity.sql.dsiplayNamDep(dep)!=null)
        setData();
        else
            custToast("No data Found");
        at.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goTo( Main6Activity.sql.idFind(at.getText().toString()));
            }
        });
        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goTo(Main6Activity.sql.idFind(l.getItemAtPosition(position).toString()));

            }
        });

    }
    public  void setData(){
        ArrayAdapter<String> arrayAdapter1=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,Main6Activity.sql.dsiplayNamDep(String.valueOf(dep)));
        at.setAdapter(arrayAdapter1);
        at.setThreshold(1);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.activity_list_item, R.id.textViewLIST,Main6Activity.sql.dsiplayNamDep(String.valueOf(dep)));
        l.setAdapter(arrayAdapter);
    }
    public void onBackPressed(){
        i1=new Intent(getApplicationContext(),MainActivity.class);
        i1.putExtra("load",0);
        startActivity(i1);
    }
    public void goTo(int id){
        if (id==-1)
              Toast.makeText(getApplicationContext(),"Problem Contact Admin", Toast.LENGTH_SHORT).show();
         else {
            i1 = new Intent(getApplicationContext(), Main3Activity.class);
            i1.putExtra("id", id);
            i1.putExtra("type", 2);
            startActivity(i1);
        }
    }
    public void custToast(String ki){
        Toast.makeText(getApplicationContext(),ki,Toast.LENGTH_SHORT).show();
    }

}
