package ffm.geok.com.uitls;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.TypedValue;
import android.view.inputmethod.InputMethodManager;

import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.db.DBUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.UUID;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class ToolUtils {

    /**
     * 隐藏软件盘
     */
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    public static void hideSoftInput(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * 显示软键盘
     */
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    public static void showInputMethod(Activity activity) {
        if (activity.getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
            imm.showSoftInputFromInputMethod(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    //解压gzip文件
    public static boolean extractZip(File file, File parent) {
        ZipFile zf = null;
        try {
            zf = new ZipFile(file);
            Enumeration<? extends ZipEntry> entries = zf.entries();
            if (entries == null)
                return false;

            final byte[] buf = new byte[256];

            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (entry == null)
                    continue;

                if (entry.isDirectory()) {
                    File dir = new File(parent, entry.getName());
                    dir.mkdirs();
                    continue;
                }

                File dstFile = new File(parent, entry.getName());
                if (!dstFile.exists()) {
                    dstFile.getParentFile().mkdirs();
                }

                InputStream fis = zf.getInputStream(entry);
                BufferedInputStream bis = new BufferedInputStream(fis);
                FileOutputStream fos = new FileOutputStream(dstFile);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                int read = 0;
                while ((read = bis.read(buf)) > 0) {
                    bos.write(buf, 0, read);
                }
                fis.close();
                bis.close();
                bos.close();
                fos.close();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                zf.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 对将单个文件进行压缩
     *
     * @param source
     *            源文件
     * @param target
     *            目标文件
     * @throws IOException
     */
    public static boolean zipFile(String source, String target){
        boolean zipResult = false;
        FileInputStream fin = null;
        FileOutputStream fout = null;
        GZIPOutputStream gzout = null;
        try {
            fin = new FileInputStream(source);
            fout = new FileOutputStream(target);
            gzout = new GZIPOutputStream(fout);
            byte[] buf = new byte[1024];
            int num;
            while ((num = fin.read(buf)) != -1) {
                gzout.write(buf, 0, num);
            }
            if (gzout != null)
                gzout.close();
            if (fout != null)
                fout.close();
            if (fin != null)
                fin.close();
            zipResult = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            L.e(e.toString());
            zipResult = false;
        } catch (IOException e) {
            e.printStackTrace();
            L.e(e.toString());
            zipResult = false;
        }
        return zipResult;
    }
    //多个文件压缩成gzip文件
    public static boolean mutileFileToGzip(ArrayList<String> filePaths, String targetFileName) {
        boolean mutileZipResult = false;
        try {
            File file = new File(targetFileName);
            FileOutputStream fout = new FileOutputStream(file);
            BufferedInputStream bin = null;
            ZipOutputStream zout = new ZipOutputStream(fout);
            for (String fileSource : filePaths) {
                String[] fileNames = fileSource.split("/");
                zout.putNextEntry(new ZipEntry(fileNames[fileNames.length - 1]));
                int c;
                bin = new BufferedInputStream(new FileInputStream(fileSource));
                while ((c = bin.read()) != -1) {
                    zout.write(c);
                }
                bin.close();
            }
            zout.close();
            mutileZipResult = true;
            System.out.println("压缩成功！");
        } catch (Exception e) {
            e.printStackTrace();
            mutileZipResult = false;
        }
        return mutileZipResult;
    }

    /**
     * 压缩文件夹
     * @param srcFilePath
     * @param zipFilePath
     * @throws Exception
     */
    public static void zipFolder(String srcFilePath, String zipFilePath) throws Exception {
        // 创建Zip包
        java.util.zip.ZipOutputStream outZip = new java.util.zip.ZipOutputStream(new java.io.FileOutputStream(zipFilePath));
        // 打开要输出的文件
        java.io.File file = new java.io.File(srcFilePath);
        // 压缩
        zipFiles(file.getParent() + java.io.File.separator, file.getName(), outZip);
        // 完成,关闭
        outZip.finish();
        outZip.close();
    }


    private static void zipFiles(String folderPath, String filePath, java.util.zip.ZipOutputStream zipOut) throws Exception {
        if (zipOut == null) {
            return;
        }
        java.io.File file = new java.io.File(folderPath + filePath);
        // 判断是不是文件
        if (file.isFile()) {
            java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry(filePath);
            java.io.FileInputStream inputStream = new java.io.FileInputStream(file);
            zipOut.putNextEntry(zipEntry);
            int len;
            byte[] buffer = new byte[100000];
            while ((len = inputStream.read(buffer)) != -1) {
                zipOut.write(buffer, 0, len);
            }
            inputStream.close();
            zipOut.closeEntry();
        } else {
            // 文件夹的方式,获取文件夹下的子文件
            String fileList[] = file.list();
            // 如果没有子文件, 则添加进去即可
            if (fileList.length <= 0) {
                java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry(filePath + java.io.File.separator);
                zipOut.putNextEntry(zipEntry);
                zipOut.closeEntry();
            }
            // 如果有子文件, 遍历子文件
            for (int i = 0; i < fileList.length; i++) {
                zipFiles(folderPath, filePath + java.io.File.separator + fileList[i], zipOut);
            }
        }
    }

    /**
     * 生成UUID
     *
     * @return
     */
    public static String generateUUID() {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return uuid;
    }

    public static String getSystemDate(){
        SimpleDateFormat sDateFormat    =   new SimpleDateFormat("yyyy-MM-dd");
        String    date    =    sDateFormat.format(new    java.util.Date());
        return date;
    }

    /**
     * 复制单个文件
     *
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public static boolean copyFile(String oldPath, String newPath) {
        boolean result = false;
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
                result = true;
            } else {
                L.e("文件不存在");
                result = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            L.e("复制单个文件操作出错");
            result = false;
        }
        return result;
    }

    /**
     * 复制整个文件夹内容
     *
     * @param oldPath String 原文件路径 如：c:/fqf
     * @param newPath String 复制后路径 如：f:/fqf/ff
     * @return boolean
     */
    public static boolean copyFolder(String oldPath, String newPath) {
        boolean result = false;
        try {
            (new File(newPath)).mkdirs(); //如果文件夹不存在 则建立新文件夹
            File a = new File(oldPath);
            String[] file = a.list();
            File temp = null;
            for (int i = 0; i < file.length; i++) {
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + file[i]);
                } else {
                    temp = new File(oldPath + File.separator + file[i]);
                }

                if (temp.isFile()) {
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(newPath + "/" +
                            (temp.getName()).toString());
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if (temp.isDirectory()) {//如果是子文件夹
                    copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
                }
            }
            result = true;
        } catch (Exception e) {
            System.out.println("复制整个文件夹内容操作出错");
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    public static int dp(Context context, int dp) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, context.getResources().getDisplayMetrics()) + 0.5F);
    }

    public static long mLastClickTime;
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - mLastClickTime;
        if (0 < timeD && timeD < 500) {
            return true;
        }
        mLastClickTime = time;
        return false;
    }

    /*导入本地行政区划数据*/
    /*public static boolean importAdcdData(Context mContext) {
        boolean result = false;
        try {
            JSONObject jsonObject = new JSONObject(Convert.parseJsonFile(mContext, "ia_z_adinfo.json"));
            JSONArray jsonArray = jsonObject.getJSONArray("RECORDS");
            Collection<IaZAdinfoEntity> list = Convert.fromJsonFormat(jsonArray.toString().toLowerCase(), new TypeToken<Collection<IaZAdinfoEntity>>() {
            }.getType());
            //插入操作耗时
            DBUtils.getInstance().getmDaoSession().runInTx(() -> {
                for (IaZAdinfoEntity entity : list) {
                    DBUtils.getInstance().getmDaoSession().insertOrReplace(entity);
                }
            });
            result = true;
        } catch (JSONException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }*/

    /**
     * 插入机电井枚举数据
     */
    /*public static boolean importSysEuntlangData(Context mContext) {
        boolean result = false;
        try {
            JSONObject jsonObject = new JSONObject(Convert.parseJsonFile(mContext, "sys_euntlang.json"));
            JSONArray jsonArray = jsonObject.getJSONArray("RECORDS");
            Collection<SysEuntlangEntity> list = Convert.fromJson(jsonArray.toString(), new TypeToken<Collection<SysEuntlangEntity>>() {
            }.getType());
            //插入操作耗时
            DBUtils.getInstance().getmDaoSession().runInTx(() -> {
                for (SysEuntlangEntity entity : list) {
                    DBUtils.getInstance().getmDaoSession().insertOrReplace(entity);
                }
            });
            result = true;
        } catch (JSONException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }*/

    public static void main(String[] args) {
        generateUUID();
    }
}
