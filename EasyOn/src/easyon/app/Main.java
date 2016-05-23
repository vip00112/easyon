package easyon.app;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;

import easyon.config.Config;
import easyon.db.ChatTable;
import easyon.db.UsersTable;
import easyon.server.netty.Server;
import easyon.util.DBFactory;
import easyon.util.GeneralThreadPool;

/** 주 진입점 및 라이프사이클 관리 **/
public class Main {
    private static final String LOG_PROP = "config/log.properties"; //로그 설정 파일

    public static void main(String[] args) {
        // 로그 설정
        File logFolder = new File("log");
        logFolder.mkdir();
        try {
            InputStream is = new BufferedInputStream(new FileInputStream(LOG_PROP));
            LogManager.getLogManager().readConfiguration(is);
            is.close();
        } catch (IOException e) {
            System.exit(0);
        }

        // config 파일 로드
        Config.load();

        // DB 설정
        DBFactory.getInstance().setting(Config.DRIVER, Config.URL, Config.USER, Config.PASSWORD);

        // db load
        UsersTable.getInstance().getAllUsers();
        ChatTable.getInstance().getAllChats();

        // socket server bind
        Server.getInstance().start();

        // TODO manager open
    }

    /** 프로그램의 모든 리소스를 해제하고 종료 한다. **/
    public void destroy() {
        GeneralThreadPool.getInstance().destroy();
        DBFactory.getInstance().destroy();
        System.exit(0);
    }

}
