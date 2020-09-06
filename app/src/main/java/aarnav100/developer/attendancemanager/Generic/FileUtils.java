package aarnav100.developer.attendancemanager.Generic;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Created by aarnavjindal on 27/01/18.
 */
public class FileUtils {
    public static String DB_SD_FILEPATH = "/AttendanceManager/attendance.db";
    public static File sd = Environment.getExternalStorageDirectory();
    public static File data  = Environment.getDataDirectory();
    /**
     * Creates the specified <code>toFile</code> as a byte for byte copy of the
     * <code>fromFile</code>. If <code>toFile</code> already exists, then it
     * will be replaced with a copy of <code>fromFile</code>. The name and path
     * of <code>toFile</code> will be that of <code>toFile</code>.<br/>
     * <br/>
     * <i> Note: <code>fromFile</code> and <code>toFile</code> will be closed by
     * this function.</i>
     *
     * @param fromFile
     *            - FileInputStream for the file to copy from.
     * @param toFile
     *            - FileInputStream for the file to copy to.
     */
    public static void copyFile(String fromFile, String toFile) throws IOException {
        FileChannel fromChannel = null;
        FileChannel toChannel = null;
        try {
            fromChannel = new FileInputStream(fromFile).getChannel();
            toChannel = new FileOutputStream(toFile).getChannel();
            fromChannel.transferTo(0, fromChannel.size(), toChannel);
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            try {
                if (fromChannel != null) {
                    fromChannel.close();
                }
            } finally {
                if (toChannel != null) {
                    toChannel.close();
                }
            }
        }
    }
}