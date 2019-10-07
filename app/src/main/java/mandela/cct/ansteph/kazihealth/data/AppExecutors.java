package mandela.cct.ansteph.kazihealth.data;

import android.os.Looper;
import android.os.Handler;
import androidx.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class AppExecutors {

    private static final Object LOCK = new Object();
    private static AppExecutors instance;
    private final Executor diskIO;
    private final Executor mainThread;
    private final Executor networkIO;

    private AppExecutors(Executor diskIO, Executor mainThread, Executor networkIO) {
        this.diskIO = diskIO;
        this.mainThread = mainThread;
        this.networkIO = networkIO;
    }

    public static AppExecutors getInstance(){
        if(instance == null){
            synchronized (LOCK){
                instance = new AppExecutors(
                        Executors.newSingleThreadExecutor(),
                        Executors.newFixedThreadPool(3),
                        new MainThreadExecutor()
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

    private  static class MainThreadExecutor implements  Executor{

        private Handler mainthreadHandler = new Handler(Looper.getMainLooper()) ;

        @Override
        public void execute(@NonNull Runnable runnable) {
            mainthreadHandler.post(runnable);
        }
    }
}
