package mandela.cct.ansteph.kazihealth.data;

import android.os.Looper;
import android.os.Handler;
import androidx.annotation.NonNull;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class AppExecutors {

    private static final Object LOCK = new Object();
    private static AppExecutors instance;
    private final Executor diskIO;
    private final Executor mainThread;
    private final Executor networkIO;
    private final Executor serialExecIO;

    private AppExecutors(Executor diskIO, Executor mainThread, Executor networkIO , Executor serialExecIO) {
        this.diskIO = diskIO;
        this.mainThread = mainThread;
        this.networkIO = networkIO;
        this.serialExecIO = serialExecIO;
    }

    public static AppExecutors getInstance(){
        if(instance == null){
            synchronized (LOCK){
                instance = new AppExecutors(
                        Executors.newSingleThreadExecutor(),
                        Executors.newFixedThreadPool(3),
                        new MainThreadExecutor(),
                        new SerialExecutor(new Executor() {
                            @Override
                            public void execute(Runnable runnable) {

                            }
                        })
                );
            }
        }
        return instance;
    }

    public Executor getDiskIO() {
        return diskIO;
    }

    public Executor getMainThread() {
        return mainThread;
    }

    public Executor getNetworkIO() {
        return networkIO;
    }

    public Executor getSerialExecIO() {
        return serialExecIO;
    }

    private  static class MainThreadExecutor implements  Executor{

        private Handler mainthreadHandler = new Handler(Looper.getMainLooper()) ;

        @Override
        public void execute(@NonNull Runnable runnable) {
            mainthreadHandler.post(runnable);
        }
    }


   private static class SerialExecutor implements Executor {
        final Queue<Runnable> tasks = new ArrayDeque<>();
        final Executor executor;
        Runnable active;

        SerialExecutor(Executor executor) {
            this.executor = executor;
        }

        public synchronized void execute( final Runnable r) {
            tasks.add(new Runnable() {
                public void run() {
                    try {
                        r.run();
                    } finally {
                        scheduleNext();
                    }
                }
            });
            if (active == null) {
                scheduleNext();
            }
        }

        protected synchronized void scheduleNext() {
            if ((active = tasks.poll()) != null) {
                executor.execute(active);
            }
        }
    }





}
