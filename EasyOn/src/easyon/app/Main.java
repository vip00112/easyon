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

/** �� ������ �� ����������Ŭ ���� **/
public class Main {
    private static final String LOG_PROP = "config/log.properties"; //�α� ���� ����

    public static void main(String[] args) {
        // �α� ����
        File logFolder = new File("log");
        logFolder.mkdir();
        try {
            InputStream is = new BufferedInputStream(new FileInputStream(LOG_PROP));
            LogManager.getLogManager().readConfiguration(is);
            is.close();
        } catch (IOException e) {
            System.exit(0);
        }

        // config ���� �ε�
        Config.load();

        // DB ����
        DBFactory.getInstance().setting(Config.DRIVER, Config.URL, Config.USER, Config.PASSWORD);

        // db load
        UsersTable.getInstance().getAllUsers();
        ChatTable.getInstance().getAllChats();

        // socket server bind
        Server.getInstance().start();

        // TODO manager open
    }

    /** ���α׷��� ��� ���ҽ��� �����ϰ� ���� �Ѵ�. **/
    public void destroy() {
        GeneralThreadPool.getInstance().destroy();
        DBFactory.getInstance().destroy();
        System.exit(0);
    }

}
