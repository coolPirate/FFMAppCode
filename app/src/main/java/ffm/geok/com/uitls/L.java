package ffm.geok.com.uitls;

import com.orhanobut.logger.Logger;

import ffm.geok.com.global.AppConfig;

public class L {

    //输出d等级log
    public static void d(String msg) {
        if (AppConfig.DEBUG) {
            Logger.d(msg);
        }
    }

    //输出d等级log
    public static void d(String tag, String msg) {
        if (AppConfig.DEBUG) {
            Logger.t(tag).d(msg);
        }
    }

    //输出e等级log
    public static void e(String msg) {
        if (AppConfig.DEBUG) {
            Logger.e(msg);
        }
    }

    //输出e等级log
    public static void e(String tag, String msg) {
        if (AppConfig.DEBUG) {
            Logger.t(tag).e(msg);
        }
    }
    //输出w等级log
    public static void w(String msg) {
        if (AppConfig.DEBUG) {
            Logger.w(msg);
        }
    }

    //输出w等级log
    public static void w(String tag, String msg) {
        if (AppConfig.DEBUG) {
            Logger.t(tag).w(msg);
        }
    }

    //输出v等级log
    public static void v(String msg) {
        if (AppConfig.DEBUG) {
            Logger.v(msg);
        }
    }

    //输出V等级log
    public static void v(String tag, String msg) {
        if (AppConfig.DEBUG) {
            Logger.t(tag).v(msg);
        }
    }

    //输出i等级log
    public static void i(String msg) {
        if (AppConfig.DEBUG) {
            Logger.i(msg);
        }
    }

    //输出i等级log
    public static void i(String tag, String msg) {
        if (AppConfig.DEBUG) {
            Logger.t(tag).i(msg);
        }
    }

    //输出wtf等级log
    public static void wtf(String msg) {
        if (AppConfig.DEBUG) {
            Logger.wtf(msg);
        }
    }

    //输出wtf等级log
    public static void wtf(String tag, String msg) {
        if (AppConfig.DEBUG) {
            Logger.t(tag).wtf(msg);
        }
    }

    //输出json等级log
    public static void json(String msg) {
        if (AppConfig.DEBUG) {
            Logger.json(msg);
        }
    }

    //输出json等级log
    public static void json(String tag, String msg) {
        if (AppConfig.DEBUG) {
            Logger.t(tag).json(msg);
        }
    }

    //输出xml等级log
    public static void xml(String msg) {
        if (AppConfig.DEBUG) {
            Logger.xml(msg);
        }
    }

    //输出xml等级log
    public static void xml(String tag, String msg) {
        if (AppConfig.DEBUG) {
            Logger.t(tag).xml(msg);
        }
    }
}
