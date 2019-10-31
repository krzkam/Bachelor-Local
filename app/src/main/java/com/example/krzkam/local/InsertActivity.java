package com.example.krzkam.local;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import io.realm.Realm;


public class InsertActivity extends AppCompatActivity {

    private String inLat = "";
    private String inLng = "";
    private EditText et_name;
    private EditText et_address;
    private EditText et_phone;
    private EditText et_web;
    private String newPtype = "";
    private BaseApplication base = new BaseApplication();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        inLat = getIntent().getStringExtra("inLat");
        inLng = getIntent().getStringExtra("inLng");

        Spinner spinner = (Spinner) findViewById(R.id.spin_Type);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(InsertActivity.this,
                R.layout.spinner_add_layout, getResources().getStringArray(R.array.spinadd));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.spinner_add_layout);
        spinner.setAdapter(adapter);

        et_name = (EditText) findViewById(R.id.et_inName);
        et_name.setHorizontallyScrolling(false);
        et_address = (EditText) findViewById(R.id.et_inAddress);
        et_address.setHorizontallyScrolling(false);
        et_phone = (EditText) findViewById(R.id.et_inPhone);
        et_phone.setHorizontallyScrolling(false);
        et_web = (EditText) findViewById(R.id.et_inWeb);
        et_web.setHorizontallyScrolling(false);
        et_name.setTextSize(TypedValue.COMPLEX_UNIT_DIP,13);

        et_address.setTextSize(TypedValue.COMPLEX_UNIT_DIP,13);
        et_phone.setTextSize(TypedValue.COMPLEX_UNIT_DIP,13);
        et_web.setTextSize(TypedValue.COMPLEX_UNIT_DIP,13);

        base.realm = Realm.getDefaultInstance();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==1)
                {
                    newPtype = "hotel";
                }
                if (i==2)
                {
                    newPtype = "cafe";
                }
                if (i==3)
                {
                    newPtype = "restaurant";
                }
                if (i==4)
                {
                    newPtype = "bar";
                }
                if (i==5)
                {
                    newPtype = "supermarket";
                }
                if (i==6)
                {
                    newPtype = "gas_station";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                newPtype = "";
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        base.realm.close();
    }

    public void onSendClick(View v)
    {
        if (v.getId() == R.id.b_SendData)
        {
            String newP_name = "";
            newP_name = et_name.getText().toString();

            String newP_address = "";
            if (et_address.getText().toString() != "")
            {
                newP_address = et_address.getText().toString();
            }

            String newP_web = "";
            if (et_web.getText().toString() != "")
            {
                newP_web = et_web.getText().toString();
            }

            String newP_phone = "";
            if (et_phone.getText().toString() != "")
            {
                newP_phone = et_phone.getText().toString();
            }

            if (isEmpty(et_name) || newPtype == "")
            {
                Toast.makeText(InsertActivity.this,"Fill mandatory fields, please", Toast.LENGTH_SHORT).show();
            }
            else
            {
                base.save_into_database(newP_name,inLat,inLng,newPtype,newP_address,newP_phone,newP_web);
            }
            Toast.makeText(InsertActivity.this,"Place has been added", Toast.LENGTH_SHORT).show();
        }
        finish();
    }


    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }


}
