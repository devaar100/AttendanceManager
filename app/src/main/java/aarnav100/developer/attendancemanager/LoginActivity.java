package aarnav100.developer.attendancemanager;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.Calendar;

import aarnav100.developer.attendancemanager.Database.DBUtils.DatabaseHandler;
import aarnav100.developer.attendancemanager.Generic.FileUtils;
import aarnav100.developer.attendancemanager.Services.BackupService;

public class LoginActivity extends AppCompatActivity {
    private int PERM_REQ_CODE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final EditText etname = findViewById(R.id.name);
        final EditText etemail = findViewById(R.id.email);
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etname.getText().toString();
                String email = etemail.getText().toString();
                if(name.equals("")||email.equals("")){
                    Snackbar snack = Snackbar.make(view,"Fields can not be empty",Snackbar.LENGTH_LONG);
                    snack.getView().setBackgroundResource(R.color.colorYellow);
                    TextView textView = (TextView)snack.getView().findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.BLACK);
                    snack.show();
                    return;
                }
                preferences.edit().putString("name",name).putString("email",email).putBoolean("first",false).apply();
                if(checkPermission())
                    recoverDB();
            }
        });

        Calendar cal = Calendar.getInstance();
        Intent intent = new Intent(this, BackupService.class);
        PendingIntent pintent = PendingIntent
                .getService(this, 0, intent, 0);

        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                6*60*60*1000, pintent);
        preferences.edit().putString("backup","01/01/2018").apply();

        checkPermission();
    }

    private void recoverDB(){
        try {
            File folder = new File(FileUtils.sd,"AttendanceManager");
            if(!folder.exists()) {
                if (!folder.mkdir()) {
                    Toast.makeText(this, "Error. Please try again later", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            File sdfile = new File(FileUtils.sd,FileUtils.DB_SD_FILEPATH);
            FileUtils.copyFile(sdfile.getPath(), getDatabasePath(DatabaseHandler.DB_NAME).getPath());
            Snackbar.make(findViewById(R.id.linpar),"Previous data recovered",Snackbar.LENGTH_LONG);
        } catch (Exception e){
            e.printStackTrace();
            Snackbar.make(findViewById(R.id.linpar),"Previous data could not be recovered",Snackbar.LENGTH_LONG);
        } finally {
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
        }
    }

    private boolean checkPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED) {
            Log.i("TAG","Permission granted");
            return true;
        }
        else {
            Log.i("TAG","Permission not granted");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,  Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "App requires this permission", Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, PERM_REQ_CODE);
            return false;
        }
    }
}
