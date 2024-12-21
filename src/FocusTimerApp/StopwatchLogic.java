// StopwatchLogic.java

package FocusTimerApp;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class StopwatchLogic {
    private int elapsedTime; // 經過的時間（以秒為單位）
    private ScheduledExecutorService scheduler;
    private Runnable onTick; // 每秒更新的回呼動作

    public StopwatchLogic(Runnable onTick) {
        this.elapsedTime = 0;
        this.onTick = onTick;
    }

    public void start() {
        if (scheduler == null || scheduler.isShutdown()) {
            scheduler = Executors.newScheduledThreadPool(1);
            scheduler.scheduleAtFixedRate(() -> {
                elapsedTime++;
                if (onTick != null) {
                    onTick.run();
                }
            }, 0, 1, TimeUnit.SECONDS);
        }
    }

    public void pause() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }

    public void reset() {
        pause();
        elapsedTime = 0;
        if (onTick != null) {
            onTick.run(); // 重置時即時更新顯示
        }
    }

    public int getElapsedTime() {
        return elapsedTime;
    }

    public String formatTime() {
        int minutes = elapsedTime / 60;
        int seconds = elapsedTime % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}
