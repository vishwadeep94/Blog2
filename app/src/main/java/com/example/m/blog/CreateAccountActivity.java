package com.example.m.blog;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CreateAccountActivity extends AppCompatActivity {


    private EditText firstname;
    private EditText lastname;
    private EditText email;
    private EditText password;
    private EditText city;
    private Button createaccount;
    private DatabaseReference mdatabaserefernce;
    private FirebaseDatabase mdatabse;
    private FirebaseAuth mAuth;
    private ProgressDialog mprogessdialog;
    private EditText date;
    private DatePickerDialog.OnDateSetListener datePickerListener;

    private CountryCodePicker cpp;
    private CountryCodePicker cpp1;
    private EditText phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        mdatabse = FirebaseDatabase.getInstance();
        mdatabaserefernce = mdatabse.getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();

        mprogessdialog = new ProgressDialog(CreateAccountActivity.this);

        firstname = (EditText) findViewById(R.id.firstnameid);
        lastname = (EditText) findViewById(R.id.lastnameid);
        email = (EditText) findViewById(R.id.emailid);
        password = (EditText) findViewById(R.id.passwordid);
        city = (EditText) findViewById(R.id.cityid);
        createaccount = (Button) findViewById(R.id.createaccountid);

        cpp = (CountryCodePicker) findViewById(R.id.countryid);
        cpp1 = (CountryCodePicker) findViewById(R.id.codeid);
        phone = (EditText) findViewById(R.id.phoneid);

        date = (EditText) findViewById(R.id.dob);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar mycalendar = Calendar.getInstance();
                int day = mycalendar.get(Calendar.DAY_OF_MONTH) ;
                int month = mycalendar.get(Calendar.MONTH);
                int year = mycalendar.get(Calendar.YEAR);

                DatePickerDialog dialog = new DatePickerDialog(CreateAccountActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        datePickerListener, day,month,year);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();


                //setting minimum day,month & year to start inside the dialog

                mycalendar.add(Calendar.YEAR, -58);
                mycalendar.add(Calendar.MONTH , -5);
                mycalendar.add(Calendar.DAY_OF_MONTH, -36);

                long mindate = mycalendar.getTime().getTime();

                dialog.getDatePicker().setMinDate(mindate);

            }
        });


        datePickerListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                int selectedmonth = month+1;

                String myformat = "Date of Birth:   "
                        +dayOfMonth  +"/" + (selectedmonth<10?("0" +selectedmonth) : (selectedmonth))  +"/" +year;
                date.setText(myformat);

            }
        };

        //attach phone number to edittext to ccp1
        cpp1.registerCarrierNumberEditText(phone);


        cpp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                //String selected_country = cpp.getSelectedCountryName();
            }
        });

        cpp1.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {

                //String code = cpp1.getFullNumberWithPlus();
            }
        });


        createaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!cpp1.isValidFullNumber()){
                    Toast.makeText(CreateAccountActivity.this, "Check your phone number", Toast.LENGTH_LONG).show();
                }

                createNewAccount();
            }
        });


    }



    private void createNewAccount() {

        final String name = firstname.getText().toString().trim();
        final String last_name = lastname.getText().toString().trim();
        final String em = email.getText().toString().trim();
        final String pwd = password.getText().toString().trim();
        final String dob = date.getText().toString().trim();
        final String cities = city.getText().toString().trim();
        final String country_selected = cpp.getSelectedCountryName().toString().trim();
        final String phone_number = cpp1.getFullNumberWithPlus().toString().trim();


        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(last_name) && !TextUtils.isEmpty(em)
                && !TextUtils.isEmpty(pwd) && !TextUtils.isEmpty(dob) && !TextUtils.isEmpty(cities)
                && !TextUtils.isEmpty(country_selected) && !TextUtils.isEmpty(phone_number) ){

            mprogessdialog.setMessage("Creating Accounting.....");
            mprogessdialog.show();

            mAuth.createUserWithEmailAndPassword(em, pwd).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    if(authResult != null){

                        String userid = mAuth.getCurrentUser().getUid();
                        DatabaseReference currentUserDb = mdatabaserefernce.child(userid);
                        currentUserDb.child("First Name").setValue(name);
                        currentUserDb.child("Last Name").setValue(last_name);
                        currentUserDb.child("Email").setValue(em);
                        currentUserDb.child("Date of Birth").setValue(dob);
                        currentUserDb.child("City").setValue(cities);
                        currentUserDb.child("Country").setValue(country_selected);
                        currentUserDb.child("Phone Number").setValue(phone_number);


                        mprogessdialog.dismiss();
                        Toast.makeText(CreateAccountActivity.this,"Account Created", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(CreateAccountActivity.this, PostListActivity.class);
                        startActivity(intent);
                    }
                }
            });
        }

    }


}

