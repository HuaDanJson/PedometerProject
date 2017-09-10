package com.aidebar.greendaotest.gen;

import android.content.Context;

import com.example.lenovo.test_sql.DB.DBStepEveryDayCountBean;

import java.util.List;

/**
 * Created by Json on 2017/3/30.
 */

public class DBStepEveryDayCountBeanUtils {

    private DBStepEveryDayCountBeanDao dbStepEveryDayCountBeanDao ;
    private static DBStepEveryDayCountBeanUtils dbStepEveryDayCountBeanUtils=null;

    public DBStepEveryDayCountBeanUtils  (Context context){
        dbStepEveryDayCountBeanDao= DaoManager.getInstance(context).getNewSession().getDBStepEveryDayCountBeanDao();
    }

    public static DBStepEveryDayCountBeanUtils getInstance(){
        return dbStepEveryDayCountBeanUtils;
    }
    public static void Init(Context context){
        if(dbStepEveryDayCountBeanUtils == null){
            dbStepEveryDayCountBeanUtils=new  DBStepEveryDayCountBeanUtils(context);
        }
    }

    /**
     * 完成对数据库中插入一条数据操作
     * @param dbStepEveryDayCountBean
     * @return
     */
    public void insertOneData(DBStepEveryDayCountBean dbStepEveryDayCountBean){
        dbStepEveryDayCountBeanDao.insertOrReplace(dbStepEveryDayCountBean);
    }

    /**
     * 完成对数据库中插入多条数据操作
     * @param dbStepEveryDayCountBeanList
     * @return
     */
    public boolean insertManyData(List<DBStepEveryDayCountBean> dbStepEveryDayCountBeanList){
        boolean flag = false;
        try{
            dbStepEveryDayCountBeanDao.insertOrReplaceInTx(dbStepEveryDayCountBeanList);
            flag = true;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 完成对数据库中删除一条数据操作
     * @param dbStepEveryDayCountBean
     * @return
     */
    public boolean deleteOneData(DBStepEveryDayCountBean dbStepEveryDayCountBean){
        boolean flag = false;
        try{
            dbStepEveryDayCountBeanDao.delete(dbStepEveryDayCountBean);
            flag = true;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 完成对数据库中删除一条数据 ByKey操作
     * @return
     */
    public boolean deleteOneDataByKey(long id){
        boolean flag = false;
        try{
            dbStepEveryDayCountBeanDao.deleteByKey(id);
            flag = true;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 完成对数据库中批量删除数据操作
     * @return
     */
    public boolean deleteManData(List<DBStepEveryDayCountBean> dbStepEveryDayCountBeanList){
        boolean flag = false;
        try{
            dbStepEveryDayCountBeanDao.deleteInTx(dbStepEveryDayCountBeanList);
            flag = true;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 完成对数据库更新数据操作
     * @return
     */
    public boolean updateData(DBStepEveryDayCountBean dbStepEveryDayCountBean){
        boolean flag = false;
        try{
            dbStepEveryDayCountBeanDao.update(dbStepEveryDayCountBean);
            flag = true;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 完成对数据库批量更新数据操作
     * @return
     */
    public boolean updateManData(List<DBStepEveryDayCountBean> dbStepEveryDayCountBeanList){
        boolean flag = false;
        try{
            dbStepEveryDayCountBeanDao.updateInTx(dbStepEveryDayCountBeanList);
            flag = true;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 完成对数据库查询数据操作
     * @return
     */
    public DBStepEveryDayCountBean queryData(long id) {
        return dbStepEveryDayCountBeanDao.load(id);
    }

    /**
     * 完成对数据库查询所有数据操作
     * @return
     */
    public List<DBStepEveryDayCountBean> queryData() {
        return dbStepEveryDayCountBeanDao.loadAll();
    }


}
