package com.ngbj.browse.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ngbj.browse.bean.AdBean;
import com.ngbj.browse.bean.BigModelCountData;
import com.ngbj.browse.bean.BigModelCountDataDao;
import com.ngbj.browse.bean.BookMarkData;
import com.ngbj.browse.bean.BookMarkDataDao;
import com.ngbj.browse.bean.KeyBean;
import com.ngbj.browse.bean.KeyBeanDao;
import com.ngbj.browse.bean.CountData;
import com.ngbj.browse.bean.CountDataDao;
import com.ngbj.browse.bean.DaoMaster;
import com.ngbj.browse.bean.DaoSession;
import com.ngbj.browse.bean.HistoryData;
import com.ngbj.browse.bean.HistoryDataDao;
import com.ngbj.browse.bean.ModelBean;
import com.ngbj.browse.bean.ModelBeanDao;
import com.ngbj.browse.bean.StatisticsBean;
import com.ngbj.browse.bean.StatisticsBeanDao;
import com.ngbj.browse.bean.UserInfoBean;
import com.ngbj.browse.bean.UserInfoBeanDao;
import com.ngbj.browse.bean.WeatherSaveBean;
import com.ngbj.browse.bean.WeatherSaveBeanDao;
import com.ngbj.browse.util.AppUtil;
import com.ngbj.browse.util.SPHelper;
import com.socks.library.KLog;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class DBManager {
    private final static String dbName = "ngbj_db";
    private static DBManager mInstance;
    private DaoMaster.DevOpenHelper openHelper;
    private Context context;

    public DBManager(Context context) {
        this.context = context;
        openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
    }

    /**
     * 获取单例引用
     *
     * @param context
     * @return
     */
    public static DBManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (DBManager.class) {
                if (mInstance == null) {
                    mInstance = new DBManager(context);
                }
            }
        }
        return mInstance;
    }

    /**
     * 获取可读数据库
     */
    private SQLiteDatabase getReadableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        SQLiteDatabase db = openHelper.getReadableDatabase();
        return db;
    }

    /**
     * 获取可写数据库
     */
    private SQLiteDatabase getWritableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        SQLiteDatabase db = openHelper.getWritableDatabase();
        return db;
    }


    /** -------------  用户 开始部分  ------------   */
    /**
     * 插入一位用户
     *
     * @param userInfoBean
     */
    public void insertUserInfo(UserInfoBean userInfoBean) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        UserInfoBeanDao userDao = daoSession.getUserInfoBeanDao();
        userDao.insert(userInfoBean);
    }

    /**
     * 删除一位用户
     */
    public void deleteUserInfo() {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        UserInfoBeanDao userInfoBeanDao = daoSession.getUserInfoBeanDao();
        userInfoBeanDao.deleteAll();
    }


    /**
     * 更新一位用户
     *
     * @param userInfoBean
     */
    public void updateUserInfo(UserInfoBean userInfoBean) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        UserInfoBeanDao userDao = daoSession.getUserInfoBeanDao();
        userDao.update(userInfoBean);
    }

    /**
     * 查询一位用户
     */
    public List<UserInfoBean> queryUserInfo() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        UserInfoBeanDao userDao = daoSession.getUserInfoBeanDao();
        QueryBuilder<UserInfoBean> qb = userDao.queryBuilder();
        List<UserInfoBean> list = qb.list();
        return list;
    }


    /** -------------  用户 结束部分  ------------   */

    /** -------------  书签 开始部分  ------------   */

    /**
     * 插入一书签
     *
     * @param bookMarkData
     */
    public void insertBookMark(BookMarkData bookMarkData) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        BookMarkDataDao userDao = daoSession.getBookMarkDataDao();
        userDao.insert(bookMarkData);
    }

    /**
     * 删除一书签
     */
    public void deleteBookMark() {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        BookMarkDataDao userInfoBeanDao = daoSession.getBookMarkDataDao();
        userInfoBeanDao.deleteAll();
    }


    /**
     * 更新一书签
     *
     * @param bookMarkData
     */
    public void updateBookMark(BookMarkData bookMarkData) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        BookMarkDataDao userDao = daoSession.getBookMarkDataDao();
        userDao.update(bookMarkData);
    }

    /**
     * 查询一书签
     */
    public List<BookMarkData> queryBookMark() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        BookMarkDataDao userDao = daoSession.getBookMarkDataDao();
        QueryBuilder<BookMarkData> qb = userDao.queryBuilder();
        List<BookMarkData> list = qb.list();
        return list;
    }


    /** -------------  书签 结束部分  ------------   */


    /** =========================================================================== */

    /**
     * 插入一条浏览
     *
     * @param countData
     */
    public void insertUser(CountData countData) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        CountDataDao userDao = daoSession.getCountDataDao();
        userDao.insert(countData);
    }

    /**
     * 插入浏览集合
     *
     * @param countDatas
     */
    public void insertUserList(List<CountData> countDatas) {
        if (countDatas == null || countDatas.isEmpty()) {
            return;
        }
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        CountDataDao countDataDao = daoSession.getCountDataDao();
        countDataDao.insertInTx(countDatas);
    }


    /**
     * 更新一条浏览广告
     *
     * @param countData
     */
    public void updateUser(CountData countData) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        CountDataDao userDao = daoSession.getCountDataDao();
        userDao.update(countData);
    }

    /**
     * 查询一条浏览广告
     */
    public CountData queryUser(String adId) {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        CountDataDao userDao = daoSession.getCountDataDao();
        QueryBuilder<CountData> qb = userDao.queryBuilder();
        CountData countData= userDao.queryBuilder().where(CountDataDao.Properties.Ad_id.eq(adId)).unique();
        return countData;
    }

    /**
     * 查询广告浏览列表
     */
    public List<CountData>queryCountsListAll () {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        CountDataDao userDao = daoSession.getCountDataDao();
        QueryBuilder<CountData> qb = userDao.queryBuilder();
        List<CountData> list = qb.list();
        return list;
    }


    /**
     * 查询广告浏览列表 -- 通过type - 0
     */
    public List<CountData> queryCountsListByType() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        CountDataDao userDao = daoSession.getCountDataDao();
        QueryBuilder<CountData> qb = userDao.queryBuilder();
        qb.where(CountDataDao.Properties.Type.eq("0"));
        List<CountData> list = qb.list();
        return list;
    }

    /**
     * 删除广告浏览集合
     */
    public void deleteAllCountData() {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        CountDataDao countDataDao = daoSession.getCountDataDao();
        countDataDao.deleteAll();
    }

    /**
     * 更新广告集合 -- 删除show_数量
     */
    public void updateAllCountData() {

        List<CountData> list = queryCountsListByType();
        if(list != null && list.size() != 0){
            for (CountData countData : list) {
                countData.setShow_num(0);
                countData.setClick_num(0);
                countData.setClick_user_num(0);
                updateUser(countData);
            }
        }
    }

    /**
     * 删除广告集合
     */
    public void deleteAllCounts() {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        CountDataDao countDataDao = daoSession.getCountDataDao();
        countDataDao.deleteAll();
    }

    /** -------------  大模块 开始部分  ------------   */

    /**
     * 查询单个大模块
     */
    public BigModelCountData queryBigModel(int type ) {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        BigModelCountDataDao userDao = daoSession.getBigModelCountDataDao();
        QueryBuilder<BigModelCountData> qb = userDao.queryBuilder();
        BigModelCountData countData= qb.where(BigModelCountDataDao.Properties.Type.eq(type)).unique();
        return countData;
    }


    /**
     * 查询大模块列表 -- 包含不同
     */
    public List<BigModelCountData> queryBigModelCountDataList() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        BigModelCountDataDao userDao = daoSession.getBigModelCountDataDao();
        QueryBuilder<BigModelCountData> qb = userDao.queryBuilder();
        List<BigModelCountData> list = qb.list();
        return list;
    }


    /**
     * 插入一条大模块
     *
     * @param bigModelCountData
     */
    public void insertBigModel(BigModelCountData bigModelCountData) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        BigModelCountDataDao userDao = daoSession.getBigModelCountDataDao();
        userDao.insert(bigModelCountData);
    }

    /**
     * 更新一条大模块集合
     *
     * @param bigModelCountData
     */
    public void updateBigModel(BigModelCountData bigModelCountData) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        BigModelCountDataDao userDao = daoSession.getBigModelCountDataDao();
        userDao.update(bigModelCountData);
    }

    /**
     * 删除大模块集合
     */
    public void deleteAllBigModelCountData() {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        BigModelCountDataDao countDataDao = daoSession.getBigModelCountDataDao();
        countDataDao.deleteAll();
    }
    /** -------------  大模块 结束部分  ------------   */


    /** ------------- 搜索界面关键字 开始部分  ------------   */

    /**
     * 查询关键字记录 -- 5条
     */
    public List<KeyBean> queryKeyList() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        KeyBeanDao userDao = daoSession.getKeyBeanDao();
        QueryBuilder<KeyBean> qb = userDao.queryBuilder();
        qb.limit(5).orderDesc(KeyBeanDao.Properties.CurrentTime);
        List<KeyBean> list = qb.list();
        return list;
    }

    /**
     * 删除关键字记录
     */
    public void deleteAllKeyData() {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        KeyBeanDao keyBeanDao = daoSession.getKeyBeanDao();
        keyBeanDao.deleteAll();
    }

    /**
     * 插入一条关键字记录
     */
    public void insertKey(KeyBean keyBean) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        KeyBeanDao keyBeanDao = daoSession.getKeyBeanDao();
        keyBeanDao.insert(keyBean);
    }

    /**
     * 查询一条关键字记录
     */
    public KeyBean queryKey(String content) {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        KeyBeanDao keyBeanDao = daoSession.getKeyBeanDao();
        KeyBean countData= keyBeanDao.queryBuilder().where(KeyBeanDao.Properties.KeyName.eq(content)).unique();
        return countData;
    }

    /**
     * 更新一条关键字记录
     *
     * @param keyBean
     */
    public void updateKeyBean(KeyBean keyBean) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        KeyBeanDao userDao = daoSession.getKeyBeanDao();
        userDao.update(keyBean);
    }




    /** ------------- 搜索界面关键字 结束部分  ------------   */

/** =========================================================================== */




    /**
     * 插入一条历史记录
     */
    public void insertHistrory(HistoryData historyData) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        HistoryDataDao userDao = daoSession.getHistoryDataDao();
        userDao.insert(historyData);
    }



    /**
     * 删除一条记录
     *
     * @param countData
     */
    public void deleteUser(CountData countData) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        CountDataDao userDao = daoSession.getCountDataDao();
        userDao.delete(countData);
    }

    /**
     * 删除历史记录
     */
    public void deleteAllHistoryData() {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        HistoryDataDao historyDataDao = daoSession.getHistoryDataDao();
        historyDataDao.deleteAll();
    }





    /**
     * 查询历史记录
     */
    public List<HistoryData> queryHistoryList() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        HistoryDataDao userDao = daoSession.getHistoryDataDao();
        QueryBuilder<HistoryData> qb = userDao.queryBuilder();
        List<HistoryData> list = qb.list();
        return list;
    }



    /**
     * 查询用户列表 >某一数量
     */
    public List<CountData> queryUserList(int adShowCount) {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        CountDataDao userDao = daoSession.getCountDataDao();
        QueryBuilder<CountData> qb = userDao.queryBuilder();
        qb.where(CountDataDao.Properties.Show_num.gt(adShowCount)).orderAsc(CountDataDao.Properties.Show_num);
        List<CountData> list = qb.list();
        return list;
    }

    /**
     * 查询历史列表 >某一数量
     */
    public List<HistoryData> queryHistoryLimitList() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        HistoryDataDao userDao = daoSession.getHistoryDataDao();
        QueryBuilder<HistoryData> qb = userDao.queryBuilder();
        qb.orderDesc(HistoryDataDao.Properties.CurrentTime);
        List<HistoryData> list = qb.list();
        return list;
    }


    /** ----------------------------------------------------------  */
    /**
     * 插入一条天气
     */
    public void insertWeather(WeatherSaveBean weatherSaveBean) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        WeatherSaveBeanDao userDao = daoSession.getWeatherSaveBeanDao();
        userDao.insert(weatherSaveBean);
    }


    /**
     * 查询天气集合
     */
    public List<WeatherSaveBean> queryWeatherList() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        WeatherSaveBeanDao userDao = daoSession.getWeatherSaveBeanDao();
        QueryBuilder<WeatherSaveBean> qb = userDao.queryBuilder();
        qb.limit(1);
        List<WeatherSaveBean> list = qb.list();
        return list;
    }




    /** -------------  广告用户点击数 开始部分  ------------   */

    public void insertAdUser(StatisticsBean statisticsBean) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        StatisticsBeanDao statisticsBeanDao = daoSession.getStatisticsBeanDao();
        statisticsBeanDao.insert(statisticsBean);
    }

    public void updateAdUser(StatisticsBean statisticsBean) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        StatisticsBeanDao userDao = daoSession.getStatisticsBeanDao();
        userDao.update(statisticsBean);
    }

    public List<StatisticsBean> queryAdUser() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        StatisticsBeanDao userDao = daoSession.getStatisticsBeanDao();
        QueryBuilder<StatisticsBean> qb = userDao.queryBuilder();
        List<StatisticsBean> list = qb.list();
        return list;
    }

    public StatisticsBean queryAdUser(String adId) {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        StatisticsBeanDao userDao = daoSession.getStatisticsBeanDao();
        QueryBuilder<StatisticsBean> qb = userDao.queryBuilder();
        StatisticsBean statisticsBean = qb.where(StatisticsBeanDao.Properties.Ad_id.eq(adId)).unique();
        return statisticsBean;
    }


    /** -------------  广告用户点击数 结束部分  ------------   */

    /** -------------  模块用户点击数 开始部分  ------------   */

    public void insertModelUser(ModelBean modelBean) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        ModelBeanDao modelBeanDao = daoSession.getModelBeanDao();
        modelBeanDao.insert(modelBean);
    }

    public void updateModelUser(ModelBean modelBean) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        ModelBeanDao userDao = daoSession.getModelBeanDao();
        userDao.update(modelBean);
    }

    public List<ModelBean> queryModelUser() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        ModelBeanDao userDao = daoSession.getModelBeanDao();
        QueryBuilder<ModelBean> qb = userDao.queryBuilder();
        List<ModelBean> list = qb.list();
        return list;
    }

    public ModelBean queryModelUser(String modelId) {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        ModelBeanDao userDao = daoSession.getModelBeanDao();
        QueryBuilder<ModelBean> qb = userDao.queryBuilder();
        ModelBean modelBean = qb.where(ModelBeanDao.Properties.Model_id.eq(modelId)).unique();
        return modelBean;
    }


    /** -------------   模块用户点击数 结束部分  ------------   */


}