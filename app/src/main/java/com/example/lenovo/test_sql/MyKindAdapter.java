package com.example.lenovo.test_sql;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lenovo.test_sql.DB.DBStepEveryDayCountBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Json on 2017/3/29.
 */

public class MyKindAdapter extends BaseAdapter {

    private List<DBStepEveryDayCountBean> dbUserInvestmentList;
    private LayoutInflater inflater;
    private MyVidewHolder myViewHolder;

    public MyKindAdapter(List<DBStepEveryDayCountBean> dbUserInvestmentList, Context context) {
        this.dbUserInvestmentList = dbUserInvestmentList;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return dbUserInvestmentList.size();
    }

    @Override
    public Object getItem(int position) {
        return dbUserInvestmentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_db_step_activity, null);
            myViewHolder = new MyVidewHolder(convertView);
            convertView.setTag(myViewHolder);
        } else {
            myViewHolder = (MyVidewHolder) convertView.getTag();
        }
        SimpleDateFormat sdr1 = new SimpleDateFormat("yyyy年MM月dd日");
        String CreatedTime1 = sdr1.format(new Date(dbUserInvestmentList.get(position).getDateAsId()));
        myViewHolder.tvItemTime.setText("日期:" + CreatedTime1);
        myViewHolder.tvItemStep.setText("步数:" + dbUserInvestmentList.get(position).getStepCount());
        myViewHolder.tvItemKaLuli.setText("消耗卡路里：" + dbUserInvestmentList.get(position).getXiaoHaokaLiLuCount());
        return convertView;
    }



     class MyVidewHolder {
        @BindView(R.id.tvItemTime)
        TextView tvItemTime;
        @BindView(R.id.tvItemStep)
        TextView tvItemStep;
        @BindView(R.id.tvItemKaLuli)
        TextView tvItemKaLuli;
         MyVidewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}


