package com.example.lenovo.test_sql;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.aidebar.greendaotest.gen.DBStepEveryDayCountBeanUtils;
import com.example.lenovo.test_sql.DB.DBStepEveryDayCountBean;
import com.example.lenovo.test_sql.DB.DataSaveEvent;
import com.example.lenovo.test_sql.StepCount.UI.RoundProgressView;
import com.example.lenovo.test_sql.StepCount.config.Constant;
import com.example.lenovo.test_sql.StepCount.service.StepService;
import com.example.lenovo.test_sql.constants.ConstKey;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Json on 2017/3/29.
 */

public class StepCountActivity extends AppCompatActivity  implements Handler.Callback, View.OnClickListener{
    //循环取当前时刻的步数中间的间隔时间
    long TIME_INTERVAL = 500;
    private TextView steps,dayGoal;
    ImageButton UserMain,imageButtonStepCountMain;
    Button navigate;
    TextView distance, kcal;
    private RoundProgressView roundProgressView;
    private Messenger messenger;
    private Messenger mGetReplyMessenger = new Messenger(new Handler(this));
    private Handler delayHandler;
    private int weight, daysteps;
    private List<DBStepEveryDayCountBean> dbStepEveryDayCountBeanList = new ArrayList<>();
    DBStepEveryDayCountBean dbStepEveryDayCountBean = new DBStepEveryDayCountBean();
    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                messenger = new Messenger(service);
                Message msg = Message.obtain(null, Constant.MSG_FROM_CLIENT);
                msg.replyTo = mGetReplyMessenger;
                messenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case Constant.MSG_FROM_SERVER:
                // 更新界面上的步数
                steps.setText(msg.getData().getInt("step") + "");
                //Log.i("MyDebug","msg.step: "+msg.getData().getInt("step") + "");
                roundProgressView.setMax(daysteps);
                dayGoal.setText("每日目标"+daysteps+"步");
                roundProgressView.setProgress(msg.getData().getInt("step"));
                double distances = msg.getData().getInt("step") * 0.5 / 1000;
                double kcals = distances * weight * 1.036; //体重（kg）×距离（公里）×1.036
                distance.setText("距离："+ Math.round(distances*100)/100.0 +"公里");
                kcal.setText("消耗卡路里："+ Math.round(kcals*100)/100.0 +"kcal");
                delayHandler.sendEmptyMessageDelayed(Constant.REQUEST_SERVER, TIME_INTERVAL);
                dbStepEveryDayCountBean.setDateAsId(getTime());
                dbStepEveryDayCountBean.setStepCount(msg.getData().getInt("step") + "");
                dbStepEveryDayCountBean.setXiaoHaokaLiLuCount(Math.round(kcals*100)/100.0 +"kcal");
               if (dbStepEveryDayCountBeanList!=null&&dbStepEveryDayCountBeanList.size()>0){
                   dbStepEveryDayCountBeanList.clear();
               }
               boolean flag = false;
                dbStepEveryDayCountBeanList = DBStepEveryDayCountBeanUtils.getInstance().queryData();
                for (int i=0;i<dbStepEveryDayCountBeanList.size();i++){
                        if ( (getTimeString(dbStepEveryDayCountBean.getDateAsId())).equals(getTimeString(dbStepEveryDayCountBeanList.get(i).getDateAsId()))){
                            flag = true;
                        }
                  }
                if (flag){
                  boolean f = DBStepEveryDayCountBeanUtils.getInstance().updateData(dbStepEveryDayCountBean);
                    Log.i("aaa","是够更新成功"+f);
                    flag = false;
                    RxBus.getDefault().post(new DataSaveEvent(ConstKey.SAVE_DATA_SUCCESS));
                }else {
                    DBStepEveryDayCountBeanUtils.getInstance().insertOneData(dbStepEveryDayCountBean);
                    RxBus.getDefault().post(new DataSaveEvent(ConstKey.SAVE_DATA_SUCCESS));
                }



                break;
            case Constant.REQUEST_SERVER:
                try {
                    Message msg1 = Message.obtain(null, Constant.MSG_FROM_CLIENT);
                    msg1.replyTo = mGetReplyMessenger;
                    messenger.send(msg1);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
        }
        return false;
    }
    public long getTime() {
        return System.currentTimeMillis();//获取系统时间戳
    }

    public String getTimeString(long time){
        SimpleDateFormat sdr2 = new SimpleDateFormat("MM月dd日");
        String CreatedTime2 = sdr2.format(new Date(time));//获取视频创建时间 格式：MM月dd日
        return CreatedTime2;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_count);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            callCamera();
        }
        init();
    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.UserMain:
                Intent intent = new Intent(StepCountActivity.this, UserActivity.class);
                startActivity(intent);
                break;
            case R.id.navigate:
                startActivity(new Intent(StepCountActivity.this, NavigateActivity.class));
                break;
            case R.id.imageButtonStepCountMain:
                startActivity(new Intent(StepCountActivity.this, MyStepCountEveryDayActivity.class));
                break;
            default: break;
        }
    }


    private void init() {
        steps = (TextView) findViewById(R.id.steps);
        roundProgressView = (RoundProgressView) findViewById(R.id.stepProgress);
        dayGoal = (TextView) findViewById(R.id.dayGoal);
        distance = (TextView) findViewById(R.id.distance);
        kcal = (TextView) findViewById(R.id.kcal);
        UserMain = (ImageButton) findViewById(R.id.UserMain);
        imageButtonStepCountMain = (ImageButton) findViewById(R.id.imageButtonStepCountMain);
        imageButtonStepCountMain.setOnClickListener(this);
        UserMain.setOnClickListener(this);
        navigate = (Button) findViewById(R.id.navigate);
        navigate.setOnClickListener(this);
        delayHandler = new Handler(this);
        //读取用户信息，包括当日步数、每日目标等
        SharedPreferences saveUser = getSharedPreferences("User",MODE_PRIVATE);
        String username = saveUser.getString("username",null);
        MainActivity.myDB = new MyDB(getApplicationContext());
        Cursor cursor = MainActivity.myDB.getUserbyName(username, new String[]{"weight","daysteps"});
        cursor.moveToFirst();
        weight = cursor.getInt(cursor.getColumnIndex("weight"));
        daysteps = cursor.getInt(cursor.getColumnIndex("daysteps"));
    }
    @Override
    protected void onStart() {
        super.onStart();
        setupService();
    }

    private void setupService() {
        Intent intent = new Intent(this, StepService.class);
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
        startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause(){
        //请求更新步数
        delayHandler.sendEmptyMessage(Constant.REQUEST_SERVER);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
    }
    //处理申请权限的结果
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                int gsize = grantResults.length;
                int grantResult = grantResults[0];
                int flag = 0;
                for (int i = 0; i < gsize; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        flag = 1;
                    }
                }
                if (flag == 0) {
                    Toast.makeText(StepCountActivity.this, "申请权限成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(StepCountActivity.this, "you refused the camera function", Toast.LENGTH_SHORT).show();
                    this.finish();
                }
                break;
        }
    }

    public void callCamera() {
        String callPhone = Manifest.permission.CAMERA;
        String writestorage = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        String writeContacts = Manifest.permission.WRITE_CONTACTS;
        String readContacts = Manifest.permission.READ_CONTACTS;
        String callTel = Manifest.permission.CALL_PHONE;
        String[] permissions = new String[]{callPhone, writestorage, writeContacts, readContacts, callTel};
        int selfPermission = ActivityCompat.checkSelfPermission(this, callPhone);
        int selfwrite = ActivityCompat.checkSelfPermission(this, writestorage);
        int selfwritecon = ActivityCompat.checkSelfPermission(this, writeContacts);
        int sefreadcon = ActivityCompat.checkSelfPermission(this, readContacts);
        int sefcallTel = ActivityCompat.checkSelfPermission(this, callTel);
        if (selfPermission != PackageManager.PERMISSION_GRANTED || selfwrite != PackageManager.PERMISSION_GRANTED || selfwritecon != PackageManager.PERMISSION_GRANTED || sefreadcon != PackageManager.PERMISSION_GRANTED || sefcallTel != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, 1);
        } else {

        }
    }

    public void startphoto() {
        Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Environment.getExternalStorageDirectory().toString() + "/DCIM" + "head0.jpg");
        if (file.exists())
            file.delete();
        intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(intent2, 2);// 采用ForResult打开
    }

}
