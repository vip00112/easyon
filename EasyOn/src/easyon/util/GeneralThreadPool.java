package easyon.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/** ThreadPool 실행/파기 **/
public class GeneralThreadPool {
    private static final GeneralThreadPool instance = new GeneralThreadPool();

    public static GeneralThreadPool getInstance() {
        return instance;
    }

    private ExecutorService pool; // ThreadPool

    private GeneralThreadPool() {
        // 유동적인 리소스 관리를 위해 CachedThreadPool을 사용
        pool = Executors.newCachedThreadPool(new CachedThreadFactory("CachedThreadPool"));
    }

    /** pool에 등록(실행)
     *  @param r Runnable 구현 클래스 **/
    public void execute(Runnable r) {
        pool.execute(r);
    }

    /** pool에 등록된 모든 쓰레드 종료 및 pool 파기**/
    public void destroy() {
        pool.shutdownNow();
        pool = null;
    }

    // pool에 등록될 Thread의 group과 name을 정의할 Factory
    private class CachedThreadFactory implements ThreadFactory {
        private final ThreadGroup group;
        private int currentNum;

        public CachedThreadFactory(String name) {
            group = new ThreadGroup(name);
        }

        private int nextNum() {
            currentNum++;
            if (currentNum >= Integer.MAX_VALUE) {
                currentNum = 0;
            }
            return currentNum;
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread th = new Thread(group, r);
            th.setName(group.getName() + "-" + nextNum());
            return th;
        }
    }

}
