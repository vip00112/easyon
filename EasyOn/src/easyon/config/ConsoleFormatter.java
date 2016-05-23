package easyon.config;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class ConsoleFormatter extends Formatter {

    @Override
    public String format(LogRecord record) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuilder sb = new StringBuilder();

        // 1 line : [레벨] 날짜 시간 클래스.메서드(변수)
        sb.append("[" + record.getLevel() + "] ");
        sb.append(sdf.format(record.getMillis()) + " ");
        sb.append(record.getSourceClassName() + ".");
        sb.append(record.getSourceMethodName() + "(");
        if (record.getParameters() != null) {
            sb.append(record.getParameters() != null);
        }
        sb.append(")");
        sb.append("\r\n");

        // 2 line : 사용자 입력 메시지
        if (record.getMessage() != "") {
            sb.append(record.getMessage());
            sb.append("\r\n");
        }

        // 3 line : Exception
        if (record.getThrown() != null) {
            try {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                record.getThrown().printStackTrace(pw);
                pw.close();
                sb.append(sw.toString());
                sb.append("\r\n");
            } catch (Exception e) {
            }
        }
        sb.append("\r\n");
        return sb.toString();
    }

}
