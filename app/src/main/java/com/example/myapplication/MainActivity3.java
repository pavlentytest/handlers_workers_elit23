package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MainActivity3 extends AppCompatActivity {

    private OneTimeWorkRequest workRequest, workRequest2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        Data data = new Data.Builder().putString("key","Hello").build();
        workRequest = new OneTimeWorkRequest.Builder(MyWorker.class)
                 .setInputData(data)
                 .build();
        workRequest2 = new OneTimeWorkRequest.Builder(MyWorker.class).build();
        List<OneTimeWorkRequest> list = new ArrayList<>();
        list.add(workRequest);
        list.add(workRequest2);

        // парал-о
        WorkManager.getInstance(this).enqueue(list);
        // последователь
        WorkManager.getInstance(this).beginWith(list).enqueue();
        WorkManager.getInstance(this).beginWith(workRequest).then(workRequest2).enqueue();


        WorkManager.getInstance(this).enqueue(workRequest);
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(workRequest.getId()).observe(
                this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        Log.d("RRR","State = " + workInfo.getState());
                        String message = workInfo.getOutputData().getString("key1");
                        int x = workInfo.getOutputData().getInt("key2",0);
                        Log.d("RRR","message = "+message+", x = "+x);
                    }
                }
        );

    }
}