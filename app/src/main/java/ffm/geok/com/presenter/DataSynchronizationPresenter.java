package ffm.geok.com.presenter;

import android.app.Activity;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ffm.geok.com.manager.DialogCallback;
import ffm.geok.com.model.FireCheckEntity;
import ffm.geok.com.model.FireMediaEntity;
import ffm.geok.com.uitls.ConstantUtils;
import ffm.geok.com.uitls.Convert;
import ffm.geok.com.uitls.DBUtils;
import ffm.geok.com.uitls.DateUtils;
import ffm.geok.com.uitls.L;
import ffm.geok.com.uitls.ServerUrl;
import ffm.geok.com.uitls.ToolUtils;

import static ffm.geok.com.uitls.DateUtils.Date2String;

public class DataSynchronizationPresenter implements IDataSynchronizationPresenter {
    private Activity mActivity;
    private final DataSynchronizationCallback mSynchronizationCallback;
    private static final String savePath = "ntsl/synData";  //默认目录路径
    private static String zipName = "";  //默认压缩文件名

    public DataSynchronizationPresenter(Activity mActivity, DataSynchronizationCallback synchronizationCallback) {
        this.mActivity = mActivity;
        this.mSynchronizationCallback = synchronizationCallback;
    }

    @Override
    public void dataSynchronization() {
        /**分类同步*/

        /**目前示例只有机电井数据
         * 1、查询所有未同步机电井数据
         * 2、机电井关联的多媒体数据及关联的多媒体文件
         * */

        boolean result = false;
        zipName = Date2String(new Date(), DateUtils.pattern_timestamp) + "_synData.zip";
        File foder = getDirFile();
        try {
            StringBuffer sbMsg = new StringBuffer();
            JSONObject jsonObject = new JSONObject();
            List<FireCheckEntity> fireCheckEntityList = DBUtils.getInstance().queryAllSynchroData(FireCheckEntity.class);
            if (null != fireCheckEntityList && fireCheckEntityList.size() > 0) {
                String fire_Check = Convert.toJson(fireCheckEntityList);
                jsonObject.put("fire_check", fire_Check);
            }
            List<FireMediaEntity> fireMediaEntityList = DBUtils.getInstance().queryAllSynchroData(FireMediaEntity.class);
            if (null != fireMediaEntityList && fireMediaEntityList.size() > 0) {
                String fire_media = Convert.toJson(fireMediaEntityList);
                jsonObject.put("fire_media", fire_media);
                File mediaFile = null;
                //遍历多媒体数据，拷贝到同步目录下压缩
                boolean copyResult = copyMdeiaFiles(fireMediaEntityList, foder);
                if (!copyResult) {
                    mSynchronizationCallback.onSynchronizationFail(sbMsg.toString());
                    return;
                }
            }
            /*List<IAChannelEntity> iaChannelEntityList=DBUtils.getInstance().queryAll(IAChannelEntity.class);
            if(null!=iaChannelEntityList&&iaChannelEntityList.size()>0){
                String ia_c_channel=Convert.toJson(iaChannelEntityList);
                jsonObject.put("ia_c_channel",ia_c_channel);
            }*/
            if (TextUtils.isEmpty(jsonObject.toString()) || "{}".equals(jsonObject.toString())) {
                mSynchronizationCallback.onNotNecessarySyn();
                return;
            } else {
                boolean exportJsonresult = exportJsonDataToFile(jsonObject, foder);
                //如果json导出成功
                if (exportJsonresult) {
                    //进行图片/json文件压缩
                    result = zipSynData(foder);
                } else {
                    result = false;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            L.e(e.toString());
            result = false;

        } catch (IOException e) {
            L.e(e.toString());
            e.printStackTrace();
            result = false;
        }
        if (result) {
            File uploadZip = new File(foder.getParent(), zipName);
            if (uploadZip.exists()) {
                OkGo.<String>post(ServerUrl.File_mobileUpload)
                        .tag(this)
                        .params("zipFile", uploadZip)
                        .execute(new DialogCallback(mActivity) {

                            @Override
                            public void onSuccess(Response<String> response) {
                                String responseString = response.body();
                                L.d("dataSynchronization", "请求成功：" + responseString);
                                //更新本地数据状态为已同步
                                synchronizedLocalData();
                                //通知界面已成功
                                mSynchronizationCallback.onSynchronizationSuccess();
                            }

                            @Override
                            public void onError(Response<String> response) {
                                super.onError(response);
                                L.e("dataSynchronization", "请求失败：" + response.message());
                                mSynchronizationCallback.onSynchronizationFail("");
                            }
                        });

            } else {
                mSynchronizationCallback.onSynchronizationFail("同步文件丢失");
            }
        } else {
            mSynchronizationCallback.onSynchronizationFail("");
        }
    }

    /**
     * 更新本地数据状态为已同步
     */
    private void synchronizedLocalData() {
        /*List<IaCEwellsEntity> iaCEwellsEntityList = DBUtils.getInstance().queryAllSynchroData(IaCEwellsEntity.class);
        if (null != iaCEwellsEntityList && iaCEwellsEntityList.size() > 0) {
            DBUtils.getInstance().getmDaoSession().runInTx(() -> {
                for (IaCEwellsEntity iaCEwellsEntity : iaCEwellsEntityList) {
                    iaCEwellsEntity.setDataStatus(ConstantUtils.global.IS_SYNCHRO_YES);
                    DBUtils.getInstance().getmDaoSession().insertOrReplace(iaCEwellsEntity);
                }
            });
        }
        List<IaCMediaEntity> iaCMediaEntityList = DBUtils.getInstance().queryAllSynchroData(IaCMediaEntity.class);
        if (null != iaCMediaEntityList && iaCMediaEntityList.size() > 0) {
            DBUtils.getInstance().getmDaoSession().runInTx(() -> {
                for (IaCMediaEntity iaCMediaEntity : iaCMediaEntityList) {
                    iaCMediaEntity.setIsSynchro(ConstantUtils.global.IS_SYNCHRO_YES);
                    DBUtils.getInstance().getmDaoSession().insertOrReplace(iaCMediaEntity);
                }
            });
        }*/
    }

    /**
     * 将json数据导出到本地文件中
     *
     * @param jsonObject 待导出json数据
     * @param foder      导出文件保存目录
     * @return
     * @throws IOException
     */
    private boolean exportJsonDataToFile(JSONObject jsonObject, File foder) throws IOException {
        boolean result;
        String fileName = "synData_" + Date2String(new Date(), DateUtils.pattern_timestamp) + ".json";
        L.json(jsonObject.toString());
        result = Convert.saveToSDCard(savePath, fileName, jsonObject.toString());
        L.d("json数据保存文件状态：" + result);
        return result;
    }

    /**
     * 压缩同步目录下的所有文件
     *
     * @param foder 待压缩文件夹
     * @throws IOException
     */
    private boolean zipSynData(File foder) {
        boolean zipResult = false;
        try {
            /*压缩文件*/
            File zipFile = new File(foder.getParent(), zipName);
            if (zipFile.exists()) {
                zipFile.delete();
                zipFile.createNewFile();
            }
            ArrayList<String> fileList = null;
            File[] foderList = foder.listFiles();
            for (File itemFile : foderList) {
                if (null == fileList) {
                    fileList = new ArrayList<String>();
                }
                fileList.add(itemFile.getAbsolutePath());
            }
            try {
                ToolUtils.zipFolder(foder.getAbsolutePath(), zipFile.getAbsolutePath());
                zipResult = true;
            } catch (Exception e) {
                e.printStackTrace();
                L.e(e.toString());
                zipResult = false;
            }
            L.d("最终同步上传的文件的压缩状态：" + zipResult);
        } catch (IOException e) {
            zipResult = false;
            e.printStackTrace();
        }
        return zipResult;
    }

    /**
     * 拷贝数据库中记录的多媒体文件到制定目录
     *
     * @param
     * @return
     */
    private boolean copyMdeiaFiles(List<FireMediaEntity> iaCMediaEntityList, File foder) {
        boolean result = false;
        StringBuffer sbMsg = new StringBuffer();
        File mediaFile;
        for (FireMediaEntity iaCMediaEntity : iaCMediaEntityList) {
            if (!TextUtils.isEmpty(iaCMediaEntity.getFpath())) {
                mediaFile = new File(iaCMediaEntity.getFpath());
                if (mediaFile.exists()) {
                    try {
                        String oldPath = iaCMediaEntity.getFpath();
                        File newDir = new File(foder.getPath(),"files");
                        if (!newDir.exists()) {
                            newDir.mkdirs();
                        } else {
                            //newDir.delete();
                        }
                        File newFile = new File(newDir.getPath(), iaCMediaEntity.getFname());
                        if (!newFile.exists()) {
                            newFile.createNewFile();
                        }
                        String newPath = newFile.getPath();
                        result = ToolUtils.copyFile(iaCMediaEntity.getFpath(), newPath);
                        L.d("oldPath = " + oldPath);
                        L.d("newPath = " + newPath);
                        L.d("多媒体文件拷贝状态：" + result);
                    } catch (IOException e) {
                        e.printStackTrace();
                        L.e("多媒体文件拷贝错误," + e.toString());
                        result = false;
                    }
                } else {
                    result = false;
                    sbMsg.append(iaCMediaEntity.getFpath() + "文件不存在");
                }
            } else {
                result = false;
                L.e("多媒体文件不存在");
            }
        }
        return result;
    }

    /*获取操作根目录文件*/
    @NonNull
    private File getDirFile() {
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        File foder = null;
        if (sdCardExist) {
            foder = new File(Environment.getExternalStorageDirectory(), savePath);
        } else {
            foder = new File(Environment.getDownloadCacheDirectory(), savePath);
        }
        if (!foder.exists()) {
            foder.mkdirs();
        } else {
            /*清空目录下文件*/
            DeleteFile(foder);
        }
        return foder;
    }

    /**
     * 清空目录下文件
     * @param foder
     */
    private void DeleteFile(File foder) {
        if (foder.listFiles().length > 0) {
            for (File item : foder.listFiles()) {
                if (item.isFile()) {
                    item.delete();
                } else if (item.isDirectory()) {
                    DeleteFile(item);
                }
            }
        }
    }
}
