package xiaoma.com.bomb;

import android.content.Context;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

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

    public <T extends BmobObject> void saveToServer(T user, SaveListener<String> listener) {
        user.save(listener);
    }
}
