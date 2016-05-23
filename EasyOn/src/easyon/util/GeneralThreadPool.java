package easyon.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/** ThreadPool ����/�ı� **/
public class GeneralThreadPool {
    private static final GeneralThreadPool instance = new GeneralThreadPool();

    public static GeneralThreadPool getInstance() {
        return instance;
    }

    private ExecutorService pool; // ThreadPool

    private GeneralThreadPool() {
        // �������� ���ҽ� ������ ���� CachedThreadPool�� ���
        pool = Executors.newCachedThreadPool(new CachedThreadFactory("CachedThreadPool"));
    }

    /** pool�� ���(����)
     *  @param r Runnable ���� Ŭ���� **/
    public void execute(Runnable r) {
        pool.execute(r);
    }

    /** pool�� ��ϵ� ��� ������ ���� �� pool �ı�**/
    public void destroy() {
        pool.shutdownNow();
        pool = null;
    }

    // pool�� ��ϵ� Thread�� group�� name�� ������ Factory
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
