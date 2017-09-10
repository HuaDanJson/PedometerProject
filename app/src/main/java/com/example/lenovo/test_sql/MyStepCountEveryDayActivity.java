package com.example.lenovo.test_sql;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.aidebar.greendaotest.gen.DBStepEveryDayCountBeanUtils;
import com.example.lenovo.test_sql.DB.DBStepEveryDayCountBean;
import com.example.lenovo.test_sql.DB.DataSaveEvent;
import com.example.lenovo.test_sql.constants.ConstKey;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.functions.Action1;

public class MyStepCountEveryDayActivity extends AppCompatActivity {

    @BindView(R.id.lvMyStepCountEveryDayActivity)
    ListView lvMyStepCountEveryDayActivity;
    private List<DBStepEveryDayCountBean> resultDaoList = new ArrayList<>();
    private MyKindAdapter myAdapter;
    private Subscription rxSubscription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_step_count_every_day);
        ButterKnife.bind(this);
        refreshMediaUpdateEvent();
        getListViewData();
    }

    public void getListViewData(){

        resultDaoList = DBStepEveryDayCountBeanUtils.getInstance().queryData();
        if (resultDaoList!=null&&resultDaoList.size()>0){
            Log.i("aaa",resultDaoList.toString());
            myAdapter = new MyKindAdapter(resultDaoList,MyStepCountEveryDayActivity.this);
            lvMyStepCountEveryDayActivity.setAdapter(myAdapter);
        }
    }

    public void refreshMediaUpdateEvent() {
        rxSubscription = RxBus.getDefault()
                .toObservable(DataSaveEvent.class)
                .subscribe(new Action1<DataSaveEvent>() {
                    @Override
                    public void call(DataSaveEvent dataSaveEvent) {
                        if (ConstKey.SAVE_DATA_SUCCESS.equals(dataSaveEvent.getDataSaveEvent())) {
                            myAdapter.notifyDataSetChanged();
                            getListViewData();
                        }
                    }
                });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterRxBus();
    }

    private void unregisterRxBus() {
        if (rxSubscription != null && !rxSubscription.isUnsubscribed()) {
            rxSubscription.unsubscribe();
        }
    }
}
