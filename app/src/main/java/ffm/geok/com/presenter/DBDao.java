package ffm.geok.com.presenter;

import org.greenrobot.greendao.query.WhereCondition;

import java.util.List;

public interface DBDao<T> {
    void insertOrReplace(T t);
    void delete(T t);
    void deleteAll(T t);
    void update(T t);
    List<T> queryAll(T t);
    List<T> queryAllSynchroData(T t);
    /*单条件查询*/
    List<T> queryAllBySingleWhereConditions(T t,WhereCondition conditions);
}
