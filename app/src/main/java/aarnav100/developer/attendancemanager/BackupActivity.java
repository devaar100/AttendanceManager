package aarnav100.developer.attendancemanager;//40

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import aarnav100.developer.attendancemanager.Database.DBUtils.DatabaseHandler;

public class BackupActivity extends AppCompatActivity {
    private Button btnBackUp,sendMail;
    private ProgressBar progressBar;
    private TextView tvProgress,tvBackUp;
    private LinearLayout linpar2,linpar;
    private SharedPreferences preferences;
    private String backUpText,localDate,serverDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvBackUp = findViewById(R.id.backup_text);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        localDate = preferences.getString("date",null);
        serverDate = preferences.getString("backup",null);
        setBackUpText();
        TextView tvHelp = findViewById(R.id.help_text);
        String helpText = "In case of issues drop an email at \ndeveloper.aarav100@gmail.com";
        tvHelp.setText(helpText);
        btnBackUp = findViewById(R.id.backup_btn);
        progressBar = findViewById(R.id.progress);
        tvProgress = findViewById(R.id.tv_progress);
        linpar = findViewById(R.id.linpar);
        linpar2 = findViewById(R.id.linpar2);
        btnBackUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new BackUpTask().execute();
            }
        });
        sendMail = findViewById(R.id.mail_btn);
        sendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Intent.ACTION_SENDTO);
                i.setType("message/rfc822");
                i.setData(Uri.parse("mailto:"));
                i.putExtra(Intent.EXTRA_EMAIL , new String[]{"developer.aarnav100@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "Attendance Manager App Query\n");
            }
        });
    }

    private void setBackUpText(){
        backUpText = "Local  : " + localDate + '\n' +
                "Online : " + serverDate + '\n' +
                "The local and online back ups help restore" + '\n' +
                "data in case of application uninstall or" + '\n' +
                "in certain cases loss of device or data" + '\n' +
                "corruption. Frequent back ups ensure data safety";
        tvBackUp.setText(backUpText);
    }

    private class BackUpTask extends AsyncTask<Void,Double,Void>{
        @Override
        protected void onPreExecute() {
            flipVisibility();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date currDate = Calendar.getInstance().getTime();
            final String backupDate = sdf.format(currDate);
            String email = preferences.getString("email",null);
            if(email==null)
                return null;
            StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
            Uri file = Uri.fromFile(getDatabasePath(DatabaseHandler.DB_NAME));
            StorageReference riversRef = mStorageRef.child(email+"/"+DatabaseHandler.DB_NAME);

            riversRef.putFile(file)
                    .addOnSuccessListener(new OnSuccessListener<com.google.firebase.storage.UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(com.google.firebase.storage.UploadTask.TaskSnapshot taskSnapshot) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    preferences.edit().putString("backup",backupDate).apply();
                                    flipVisibility();
                                    serverDate = backupDate;
                                    setBackUpText();
                                    Snackbar.make(linpar,"Upload Successful",Snackbar.LENGTH_SHORT).show();
                                }
                            },1000);
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<com.google.firebase.storage.UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(com.google.firebase.storage.UploadTask.TaskSnapshot taskSnapshot) {
                            Double progress = (100*(double)taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                            publishProgress(progress);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(BackupActivity.this, "There was an error. Please try again later", Toast.LENGTH_SHORT).show();
                            flipVisibility();
                        }
                    });
            return null;
        }

        @Override
        protected void onProgressUpdate(Double... values) {
            progressBar.setProgress((int) Math.round(values[0]));
            tvProgress.setText(values[0]+"%");
        }
    }

    private void flipVisibility(){
        if(btnBackUp.getVisibility() == View.VISIBLE){
            btnBackUp.setVisibility(View.INVISIBLE);
            linpar2.setVisibility(View.VISIBLE);
            tvProgress.setText("0%");
            progressBar.setProgress(0);
        } else{
            btnBackUp.setVisibility(View.VISIBLE);
            linpar2.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            onBackPressed();
        }
        return true;
    }
}
