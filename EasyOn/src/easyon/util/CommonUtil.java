package easyon.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CommonUtil {

    /** cmd �α� ���
     *  @param log cmd ����� �α� **/
    public static void writeCommandLine(String log) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(Calendar.getInstance().getTime());
        System.out.println("- [LOG] " + time + " : " + log);
    }
}
