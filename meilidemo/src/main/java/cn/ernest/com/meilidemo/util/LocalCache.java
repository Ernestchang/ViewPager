package cn.ernest.com.meilidemo.util;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.List;

import cn.ernest.com.meilidemo.DApplication;

public class LocalCache {

    private static String dataCachePath = null;

    public static File getDataCachePath() {
        if (dataCachePath == null) {
            //path: /data/data/com.mypackage.path/app_local_cache
            dataCachePath = DApplication.getInstance().getDir("local_cache", Context.MODE_PRIVATE).getAbsolutePath();
        }
        File file = new File(dataCachePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static void writeData(Context context, Object obj, String cacheFileName) {
        File file = null;
        FileOutputStream fileout = null;
        ObjectOutputStream objOutstream = null;
        try {
            file = new File(getDataCachePath(), cacheFileName);
            if (!file.exists()) {
                file.createNewFile();
            }

            fileout = new FileOutputStream(file);
            objOutstream = new ObjectOutputStream(fileout);
            objOutstream.writeObject(obj);
            objOutstream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (objOutstream != null) {
                    objOutstream.close();
                    objOutstream = null;
                }
                if (fileout != null) {
                    fileout.close();
                    fileout = null;
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static Object readData(Context context, String cacheFileName) {
        FileInputStream filein = null;
        ObjectInputStream objInstream = null;
        try {
            File file = new File(getDataCachePath(), cacheFileName);
            if (!file.exists()) {
                return null;
            }
            filein = new FileInputStream(file);
            objInstream = new ObjectInputStream(filein);
            Object object = objInstream.readObject();
            return object;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (filein != null) {
                    filein.close();
                    filein = null;
                }
                if (objInstream != null) {
                    objInstream.close();
                    objInstream = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param context
     * @param assetFileName 必须是"path/filename"或"filename"
     * @return
     */
    public static Object readAsset(Context context, String assetFileName) {
        ObjectInputStream objInstream = null;
        InputStream is = null;
        try {
            is = context.getResources().getAssets().open(assetFileName);
            if (is == null) {
                return null;
            }

            objInstream = new ObjectInputStream(is);
            return objInstream.readObject();
        } catch (Exception e) {
            return null;
        } finally {
            try {
                if (objInstream != null) {
                    objInstream.close();
                    objInstream = null;
                }
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
            }
        }
    }

//    @SuppressWarnings("unchecked")
//    public static List<ProvinceModel> getJsonDataFromAssets(AssetManager assetManager) {
//        List<ProvinceModel> provinceList = null;
//        try {
//            InputStream inputStream = assetManager.open("provinceList.dat");
//            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
//            provinceList = (List<ProvinceModel>) objectInputStream.readObject();
//            objectInputStream.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return provinceList;
//    }

    /**
     * @param context
     * @param path    如果是assets目录下的，就传""
     * @param name
     * @return
     */
    public static boolean isInAsset(Context context, String path, String name) {
        try {
            String[] files = context.getAssets().list(path);
            if (files != null) {
                List<String> list = Arrays.asList(files);
                return list.contains(name);
            }
        } catch (IOException e) {
        }
        return false;
    }


}
