package aarnav100.developer.attendancemanager.Services;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import aarnav100.developer.attendancemanager.Database.DBUtils.DatabaseHandler;

public class BackupService extends IntentService {
    private StorageReference mStorageRef;
    public BackupService() {
        super("BackupService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            String email = preferences.getString("email",null);
            if(email==null)
                return;
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date currDate = Calendar.getInstance().getTime();
            final String backupDate = sdf.format(currDate);
            mStorageRef = FirebaseStorage.getInstance().getReference();
            Uri file = Uri.fromFile(getDatabasePath(DatabaseHandler.DB_NAME));
            StorageReference riversRef = mStorageRef.child(email+"/"+DatabaseHandler.DB_NAME);

            riversRef.putFile(file)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            preferences.edit().putString("backup",backupDate).apply();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                        }
                    });
        }
    }
}
