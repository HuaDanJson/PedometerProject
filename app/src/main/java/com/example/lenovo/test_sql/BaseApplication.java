package com.example.lenovo.test_sql;

import android.app.Application;

import com.aidebar.greendaotest.gen.DBStepEveryDayCountBeanUtils;


/**
 * Created by Json on 2017/3/29.
 */

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DBStepEveryDayCountBeanUtils.Init(getApplicationContext());
    }
}
