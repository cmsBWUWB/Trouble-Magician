package xiaoma.com.bomb;

import android.content.Context;

import com.hfut.utils.callbacks.DefaultCallback;
import com.hfut.utils.utils.FileUtils;
import com.socks.library.KLog;

import org.json.JSONArray;

import java.io.File;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by wzt on 2019/5/21
 */
public class BmobManager {

    public static BmobManager getInstance() {
        return InnerClass.INSTANCE;
    }

    private static class InnerClass{
        private static BmobManager INSTANCE = new BmobManager();
    }

    public void init(Context context) {
        Bmob.initialize(context, "b4a8cfc0bc8503850e8adad8e6134899");
    }

    public <T extends BmobUser> void registerToServer(T user, SaveListener<T> listener) {
        user.signUp(listener);
    }

    public <T extends BmobObject> void saveToServer(T user, final DefaultCallback<String> callback) {
        user.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    callback.onSuccess(s);
                } else {
                    e.printStackTrace();
                    callback.onFail(e.getErrorCode(), e.getMessage());
                }
            }
        });
    }

    public <T extends BmobObject> void update(T t, final DefaultCallback<Object> callback) {
        t.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    callback.onSuccess(new Object());
                } else {
                    e.printStackTrace();
                    callback.onFail(e.getErrorCode(), e.getMessage());
                }
            }
        });
    }

    public void uploadFile(File file, final DefaultCallback<String> callback) {
        if (!FileUtils.isExist(file)) {
            callback.onFail(-1,"upload a null local file!");
            return;
        }
        final BmobFile bmobFile = new BmobFile(file);
        bmobFile.upload(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    callback.onSuccess(bmobFile.getFileUrl());
                } else {
                    e.printStackTrace();
                    callback.onFail(e.getErrorCode(), e.getMessage());
                }
            }
        });
    }

    public void queryData(String tableName, String[] keys, String[] values,final DefaultCallback<String> defaultCallback) {
        BmobQuery query = new BmobQuery(tableName);
        if (keys.length != values.length) {
            throw new IllegalArgumentException("keys.length is different with values length!");
        }
        for (int i = 0; i < keys.length; i++) {
            String key = keys[i];
            String value = values[i];
            query.addWhereEqualTo(key, value);
        }
        query.findObjectsByTable(new QueryListener<JSONArray>() {
            @Override
            public void done(JSONArray jsonArray, BmobException e) {
                if (e == null) {
                    final String str = jsonArray.toString();
                    KLog.i("query data success" + "jsonArray = " + str);
                    defaultCallback.onSuccess(str);
                } else {
                    KLog.e("errorCode = " + e.getErrorCode() + "; msg = " + e.getMessage());
                    defaultCallback.onFail(e.getErrorCode(), e.getMessage());
                }
            }
        });
    }
}
