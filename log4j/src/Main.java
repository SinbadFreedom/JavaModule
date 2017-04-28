/**
 * Created by bj on 2017/4/15.
 */
public class Main {

//    private static final String log4jConfig = "conf/log4j.properties";

    public static void main(String[] args) {
//        PropertyConfigurator.configure(log4jConfig);

        LogUtil.infoLogger.debug("info debug");
        LogUtil.infoLogger.info("info info");
        LogUtil.infoLogger.warn("info warn");
        LogUtil.infoLogger.error("info error");
        LogUtil.infoLogger.fatal("info fatal");

        LogUtil.confErrorLogger.debug("info debug");
        LogUtil.confErrorLogger.info("info info");
        LogUtil.confErrorLogger.warn("info warn");
        LogUtil.confErrorLogger.error("info error");
        LogUtil.confErrorLogger.fatal("info fatal");
    }
}
