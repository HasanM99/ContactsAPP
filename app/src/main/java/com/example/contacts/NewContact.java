package com.example.contacts;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class NewContact extends AppCompatActivity {

    Button btnNewContact;
    EditText etName, etNumber, etMail;

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact);

        etName = findViewById(R.id.etName);
        etNumber = findViewById(R.id.etNumber);
        etMail = findViewById(R.id.etMail);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);


        btnNewContact = findViewById(R.id.btnNewContact);

        btnNewContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (etMail.getText().toString().isEmpty() || etName.getText().toString().isEmpty() || etName.getText().toString().isEmpty()) {
                    Toast.makeText(NewContact.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                } else {

                    String name = etName.getText().toString().trim();
                    String email = etMail.getText().toString().trim();
                    String number = etNumber.getText().toString().trim();

                    Contacts contact = new Contacts();
                    contact.setName(name);
                    contact.setEmail(email);
                    contact.setNumber(number);
                    contact.setUserEmail(ApplicationClass.user.getEmail());

                    showProgress(true);
                    tvLoad.setText("Creating new contact...");

                    Backendless.Persistence.save(contact, new AsyncCallback<Contacts>() {
                        @Override
                        public void handleResponse(Contacts response) {
                            Toast.makeText(NewContact.this, "New contact saved successfully!", Toast.LENGTH_SHORT).show();
                            showProgress(false);
                            etName.setText("");
                            etMail.setText("");
                            etNumber.setText("");
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {

                            Toast.makeText(NewContact.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                            showProgress(false);

                        }
                    });

                }

            }
        });
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });

            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }



    //Notification function, taking in title and message strings
    public void Notify(String title, String msg){

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("Notifications","Notifications", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"Notifications");
        builder.setContentTitle(title);
        builder.setContentText(msg);
        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
        builder.setAutoCancel(true);

        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        manager.notify(0,builder.build());
    }



    //Notification on leave while adding
    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        Notify("Contact not saved!","Don't forget to save your contact.");

        finish();
    }

}