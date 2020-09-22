package com.crp.infokajianislami;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;

import java.io.InputStream;
import java.io.OutputStream;

import androidx.annotation.RequiresApi;

import static android.content.Context.JOB_SCHEDULER_SERVICE;

public class Utils {

    private static final String TAG = Utils.class.getSimpleName();
    /**
     * schedule job once
     *
     * @param context
     * @param cls
     */


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void scheduleJob(Context context, Class<?> cls) {
        ComponentName serviceComponent = new ComponentName(context, cls);

        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
        builder.setMinimumLatency(1 * 1000); // wait at least
        builder.setOverrideDeadline(3 * 1000); // maximum delay
//        builder.setPeriodic(5 * 1000);
        builder.setRequiresCharging(false); // we don't care if the device is charging or not
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(JOB_SCHEDULER_SERVICE);

        jobScheduler.schedule(builder.build());
    }


    /**
     * schedule job always in every 5 seconds, can be parametrized time - ie. pass time in parameter
     *
     * @param context
     * @param cls
     */

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void scheduleJob1(Context context, Class<?> cls) {
        ComponentName serviceComponent = new ComponentName(context, cls);
        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
        builder.setPeriodic(5 * 1000);
        builder.setRequiresCharging(false); // we don't care if the device is charging or not
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(builder.build());
    }

    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (;;) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        }catch (Exception ex) {

        }

    }




}
