package com.example.lenovo.test_sql.DB;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;


/**
 * Created by Json on 2017/3/30.
 */
@Entity // 标识实体类，greenDAO会映射成sqlite的一个表，表名为实体类名的大写形式
public class DBStepEveryDayCountBean {
    @Id(autoincrement = false)
    public long dateAsId;  //把时间作为表的ID
    @Property(nameInDb = "DBStepEveryDayCountBean")
    public String stepCount;  // 每天的步数
    public String XiaoHaokaLiLuCount;//每天消耗的卡里路
    @Generated(hash = 127688107)
    public DBStepEveryDayCountBean(long dateAsId, String stepCount,
            String XiaoHaokaLiLuCount) {
        this.dateAsId = dateAsId;
        this.stepCount = stepCount;
        this.XiaoHaokaLiLuCount = XiaoHaokaLiLuCount;
    }
    @Generated(hash = 29388388)
    public DBStepEveryDayCountBean() {
    }
    public long getDateAsId() {
        return this.dateAsId;
    }
    public void setDateAsId(long dateAsId) {
        this.dateAsId = dateAsId;
    }
    public String getStepCount() {
        return this.stepCount;
    }
    public void setStepCount(String stepCount) {
        this.stepCount = stepCount;
    }
    public String getXiaoHaokaLiLuCount() {
        return this.XiaoHaokaLiLuCount;
    }
    public void setXiaoHaokaLiLuCount(String XiaoHaokaLiLuCount) {
        this.XiaoHaokaLiLuCount = XiaoHaokaLiLuCount;
    }

    @Override
    public String toString() {
        return "DBStepEveryDayCountBean{" +
                "dateAsId=" + dateAsId +
                ", stepCount='" + stepCount + '\'' +
                ", XiaoHaokaLiLuCount='" + XiaoHaokaLiLuCount + '\'' +
                '}';
    }
}
