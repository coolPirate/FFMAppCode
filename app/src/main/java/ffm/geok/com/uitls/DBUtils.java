package ffm.geok.com.uitls;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ffm.geok.com.javagen.BaseDao;
import ffm.geok.com.javagen.DaoSession;
import ffm.geok.com.javagen.FireAddEntityDao;
import ffm.geok.com.javagen.FireCheckEntityDao;
import ffm.geok.com.javagen.FireDateEntityDao;
import ffm.geok.com.javagen.FireMediaEntityDao;
import ffm.geok.com.model.FireAddEntity;
import ffm.geok.com.model.FireCheckEntity;
import ffm.geok.com.model.FireDateEntity;
import ffm.geok.com.model.FireMediaEntity;
import ffm.geok.com.presenter.DBDao;

public class DBUtils<T> implements DBDao<T> {
    private static DBUtils dbUtils;
    private DaoSession mDaoSession;
    private FireDateEntityDao fireDateEntityDao;
    private FireCheckEntityDao fireCheckEntityDao;
    private FireMediaEntityDao fireMediaEntityDao;
    private FireAddEntityDao fireAddEntityDao;

    private static Map<String, AbstractDao> daoMap = null;
    public DBUtils() {

    }

    public static DBUtils getInstance() {
        if (null == dbUtils) {
            synchronized (DBUtils.class) {
                if (null == dbUtils) {
                    dbUtils = new DBUtils();
                    dbUtils.mDaoSession = BaseDao.getDaoSession();
                    dbUtils.fireDateEntityDao = dbUtils.mDaoSession.getFireDateEntityDao();
                    dbUtils.fireCheckEntityDao = dbUtils.mDaoSession.getFireCheckEntityDao();
                    dbUtils.fireMediaEntityDao = dbUtils.mDaoSession.getFireMediaEntityDao();
                    dbUtils.fireAddEntityDao = dbUtils.mDaoSession.getFireAddEntityDao();

                    /*将Dao信息存入Map*/
                    daoMap = new HashMap<String, AbstractDao>();
                    daoMap.put(FireDateEntityDao.class.getName(), dbUtils.fireDateEntityDao);
                    daoMap.put(FireCheckEntityDao.class.getName(), dbUtils.fireCheckEntityDao);
                    daoMap.put(FireMediaEntity.class.getName(), dbUtils.fireMediaEntityDao);
                    daoMap.put(FireAddEntity.class.getName(), dbUtils.fireAddEntityDao);

                }
            }
        }
        return dbUtils;
    }

    public DaoSession getmDaoSession() {
        return mDaoSession;
    }

    public void setmDaoSession(DaoSession mDaoSession) {
        this.mDaoSession = mDaoSession;
    }

    public AbstractDao getDao(T t) {
        AbstractDao abstractDao = null;
        if (null != daoMap) {
            String entityClass = "";
            if (t instanceof Class) {
                entityClass = ((Class) t).getName();
            } else {
                entityClass = t.getClass().getName();
            }
            String entityName = entityClass.substring(entityClass.lastIndexOf("."));
            String daoName = "";
            for (String key : daoMap.keySet()) {
                daoName = key.substring(key.lastIndexOf("."));
                if (daoName.indexOf(entityName) != -1) {
                    abstractDao = daoMap.get(key);
                    break;
                }
            }
        } else {
            throw new RuntimeException("数据异常请重启!");
        }
        return abstractDao;
    }

    @Override
    public void insertOrReplace(T t) {
        getDao(t).insertOrReplace(t);
    }

    @Override
    public void delete(T t) {
        getDao(t).delete(t);
    }

    @Override
    public void deleteAll(T t) {
        getDao(t).deleteAll();
    }

    @Override
    public void update(T t) {
        getDao(t).update(t);
    }

    @Override
    public List<T> queryAll(T t) {
        List<T> list = getDao(t).loadAll();
        return list;

    }

    @Override
    public List<T> queryAllSynchroData(T t) {
        QueryBuilder qb = getDao(t).queryBuilder();
        /*if (FireDateEntity.class.getName().equals(((Class) t).getName())) {
            qb.whereOr(FireDateEntityDao.Properties.DataStatus.eq(ConstantUtils.global.IS_SYNCHRO_NO),FireDateEntityDao.Properties.DataStatus.eq(ConstantUtils.global.IS_SYNCHRO_UPDATE));
        } else if (IaCMediaEntity.class.getName().equals(((Class) t).getName())) {
            qb.whereOr(IaCMediaEntityDao.Properties.IsSynchro.eq(ConstantUtils.global.IS_SYNCHRO_NO),IaCMediaEntityDao.Properties.IsSynchro.eq(ConstantUtils.global.IS_SYNCHRO_UPDATE));
        }*/
        return qb.list();
    }

    @Override
    public List<T> queryAllBySingleWhereConditions(T t, WhereCondition condition) {
        QueryBuilder qb = getDao(t).queryBuilder();
        qb.where(condition);
        return qb.list();
    }

    public List<T> queryAllBySingleWhereConditionsMore(T t, WhereCondition condition1,WhereCondition condition2) {
        QueryBuilder qb = getDao(t).queryBuilder();
        qb.where(condition1,condition2);
        return qb.list();
    }
}
