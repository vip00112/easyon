package easyon.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CommonUtil {

    /** cmd 로그 기록
     *  @param log cmd 출력할 로그 **/
    public static void writeCommandLine(String log) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(Calendar.getInstance().getTime());
        System.out.println("- [LOG] " + time + " : " + log);
    }
}
