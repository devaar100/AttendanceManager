package aarnav100.developer.attendancemanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import aarnav100.developer.attendancemanager.Database.DBUtils.DatabaseHandler;
import aarnav100.developer.attendancemanager.Database.Tables.TableDates;
import aarnav100.developer.attendancemanager.Generic.FileUtils;
import aarnav100.developer.attendancemanager.Generic.TypeWriter;

public class SplashActivity extends AppCompatActivity {
    private String startDate = "25/01/2018";
    private SharedPreferences spref;
    private int delay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        spref = PreferenceManager.getDefaultSharedPreferences(SplashActivity.this);
        String teacherName = spref.getString("name",null);
        TypeWriter tvName = findViewById(R.id.name);
        if(teacherName!=null) {
            teacherName = teacherName.split(" ")[0];
            tvName.animateText("Hi, " + teacherName);
        }

        delay = tvName.getTypeText().length()+2;
        Log.i("TAG",String.valueOf(delay));
    }

    @Override
    protected void onStart() {
        super.onStart();
        final ProgressBar progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                            DatabaseHandler dbhandler = new DatabaseHandler(SplashActivity.this);
                            String savedDate = spref.getString("date",null);
                            if(savedDate!=null)
                                startDate = savedDate;
                            Date strDate = sdf.parse(startDate);
                            Date currDate = new Date();
                            Date toSave = strDate;
                            while ((currDate.after(strDate))) {
                                TableDates.addDate(dbhandler.getWritableDatabase(), new java.sql.Date(strDate.getTime()), "U");
                                toSave = strDate;
                                strDate = new Date(strDate.getTime() + 24 * 60 * 60 * 1000);
                            }
                            spref.edit().putString("date",sdf.format(toSave)).apply();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if(PreferenceManager.getDefaultSharedPreferences(SplashActivity.this).getBoolean("first",true)) {
                            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            try {
                                File outFile = new File(Environment.getExternalStorageDirectory()+FileUtils.DB_SD_FILEPATH);
                                if(!outFile.exists())
                                    outFile.createNewFile();
                                FileUtils.copyFile(getDatabasePath(DatabaseHandler.DB_NAME).getPath(),outFile.getPath());
                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.e("TAG", "File could not be copied");
                            } finally {
                                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                                finish();
                            }
                        }
                    }
                },2500);
            }
        },delay*100);
    }
}
