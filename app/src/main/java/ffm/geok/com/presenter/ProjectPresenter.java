package ffm.geok.com.presenter;

import android.app.Activity;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.Collection;
import java.util.List;

import ffm.geok.com.R;
import ffm.geok.com.javagen.FireDateEntityDao;
import ffm.geok.com.manager.DialogCallback;
import ffm.geok.com.model.FireDateEntity;
import ffm.geok.com.model.InputInfoModel;
import ffm.geok.com.model.ResponseModel;
import ffm.geok.com.uitls.ConstantUtils;
import ffm.geok.com.uitls.Convert;
import ffm.geok.com.uitls.DBUtils;
import ffm.geok.com.uitls.L;
import ffm.geok.com.uitls.ServerUrl;

/**
 * Created by zhanghs on 2017/9/3.
 */

public class ProjectPresenter implements IProjectPresenter {
    private Activity mActivity;
    private ProjectCallback mCallbace;
    public ProjectPresenter(Activity activity, ProjectCallback callback) {
        this.mActivity = activity;
        this.mCallbace = callback;
    }

    @Override
    public void getFiresList(String adcd) {
        final String paramsTag = "adcd";
        OkGo.<String>get(ServerUrl.Fires_list)
                .tag(this)
                .params(ConstantUtils.RequestTag.ADCD, adcd)
                .execute(new DialogCallback(mActivity) {

                    @Override
                    public void onSuccess(Response<String> response) {
                        String responseString = response.body();
                        L.d("test", "请求成功：" + responseString);
                        Collection<FireDateEntity> list = Convert.fromJson(responseString, new TypeToken<List<FireDateEntity>>() {
                        }.getType()); // line 6
                        L.json(new Gson().toJson(list));
                        mCallbace.onFiresListSuccess(responseString);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        L.e("test", "请求失败：" + response.message());
                        mCallbace.onFiresListFail(response.message());
                    }
                });


    }

    @Override
    public void getFiresList(String startTime, String endTime) {
        OkGo.<String>get(ServerUrl.Fires_list)
                .tag(this)
                .params(ConstantUtils.RequestTag.ST, startTime)
                .params(ConstantUtils.RequestTag.ET, endTime)
                .execute(new DialogCallback(mActivity) {

                    @Override
                    public void onSuccess(Response<String> response) {
                        String responseString = response.body();
                        L.i("response",responseString);
                        try {
                            ResponseModel responseModel = Convert.fromJson(responseString, ResponseModel.class);
                            if (null != responseModel && responseModel.isStatus()) {
                                String responseData = responseModel.getData();
                                Collection<FireDateEntity> list = Convert.fromJsonFormat(responseData,
                                        new TypeToken<Collection<FireDateEntity>>() {}.getType());
                                mCallbace.onFiresListSuccess((List<FireDateEntity>) list);
                            } else {
                                mCallbace.onFiresListFail(mActivity.getString(R.string.requestdata_error));
                            }
                        } catch (JsonIOException e) {
                            e.printStackTrace();
                            mCallbace.onFiresListFail(mActivity.getString(R.string.parsedata_error));
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                            mCallbace.onFiresListFail(mActivity.getString(R.string.parsedata_error));
                        }
                        L.d("test", "请求成功：" + responseString);
                        /*List<FireDateEntity> list = Convert.fromJson(responseString, new TypeToken<List<FireDateEntity>>() {
                        }.getType());// line 6
                        L.json(new Gson().toJson(list));
                        mCallbace.onFiresListSuccess(responseString);*/
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        L.e("test", "请求失败：" + response.message());
                        mCallbace.onFiresListFail(response.message());
                    }
                });

    }

    @Override
    public void getFiresList(String adcd, String projectName, int pageSize, int pageNumber) {
        //测试从本地查询
        try {
            List<FireDateEntity> fireDateEntityList = null;
            FireDateEntityDao fireDateEntityDao = (FireDateEntityDao) DBUtils.getInstance().getDao(FireDateEntity.class);
            if (null != fireDateEntityDao) {
                QueryBuilder queryBuilder = fireDateEntityDao.queryBuilder();
                if (!TextUtils.isEmpty(adcd) && TextUtils.isEmpty(projectName)) {
                    //如果是查询乡镇数据，需要截取后3位做模糊查询
                    /*if (adcd.endsWith("000")) {
                        adcd = adcd.substring(0, adcd.length() - 3);
                    }
                    queryBuilder.where(FireDateEntityDao.Properties.Adcd.like("%" + adcd + "%"));*/
                }
                if (TextUtils.isEmpty(adcd) && !TextUtils.isEmpty(projectName)) {
                    queryBuilder.where(FireDateEntityDao.Properties.County.like("%" + projectName + "%"));
                }
                fireDateEntityList = queryBuilder.offset(pageNumber * 20).limit(pageSize).orderDesc(FireDateEntityDao.Properties.CreateTime).list();
                //fireDateEntityList=DBUtils.getInstance().queryAll(FireDateEntity.class);
                L.i("List1",String.valueOf(fireDateEntityList.size()));

                if (null != fireDateEntityList) {
                    mCallbace.onFiresListSuccess(fireDateEntityList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            L.e("查询失败："+e.toString());
            mCallbace.onFiresListFail(e.toString());
        }
    }

    @Override
    public void getFiresList(String time, String adcd, String projectName, int pageSize, int pageNumber) {
        //测试从本地查询
        try {
            List<FireDateEntity> fireDateEntityList = null;
            FireDateEntityDao fireDateEntityDao = (FireDateEntityDao) DBUtils.getInstance().getDao(FireDateEntity.class);
            if (null != fireDateEntityDao) {
                QueryBuilder queryBuilder = fireDateEntityDao.queryBuilder();
                queryBuilder.where(FireDateEntityDao.Properties.CreateTime.ge(time));
                if (!TextUtils.isEmpty(adcd) && TextUtils.isEmpty(projectName)) {
                    //如果是查询乡镇数据，需要截取后3位做模糊查询
                    /*if (adcd.endsWith("000")) {
                        adcd = adcd.substring(0, adcd.length() - 3);
                    }
                    queryBuilder.where(FireDateEntityDao.Properties.Adcd.like("%" + adcd + "%"));*/
                }
                if (TextUtils.isEmpty(adcd) && !TextUtils.isEmpty(projectName)) {
                    queryBuilder.where(FireDateEntityDao.Properties.County.like("%" + projectName + "%"));
                }
                fireDateEntityList = queryBuilder.offset(pageNumber * 20).limit(pageSize).orderDesc(FireDateEntityDao.Properties.CreateTime).list();
                //fireDateEntityList=DBUtils.getInstance().queryAll(FireDateEntity.class);
                L.i("List1",String.valueOf(fireDateEntityList.size()));

                if (null != fireDateEntityList) {
                    mCallbace.onFiresListSuccess(fireDateEntityList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            L.e("查询失败："+e.toString());
            mCallbace.onFiresListFail(e.toString());
        }
    }

    @Override
    public void saveSampleProjectInfo(InputInfoModel infomodel) {

    }
}
