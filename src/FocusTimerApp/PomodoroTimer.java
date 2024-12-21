// PomodoroTimer.java

package FocusTimerApp;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PomodoroTimer {
    private ScheduledExecutorService scheduler;
    private int remainingTime; // 當前階段剩餘時間（秒）
    private int focusTime; // 專注時間（秒）
    private int breakTime; // 休息時間（秒）
    private boolean isFocusPhase; // 當前是否是專注階段
    private boolean isPaused; // 是否處於暫停狀態
    private Runnable onTick; // 每秒觸發的動作
    private Runnable onPhaseChange; // 切換階段時觸發的動作

    public PomodoroTimer(int focusMinutes, int breakMinutes, Runnable onTick, Runnable onPhaseChange) {
        this.focusTime = focusMinutes * 60;
        this.breakTime = breakMinutes * 60;
        this.remainingTime = focusTime;
        this.isFocusPhase = true;
        this.isPaused = false;
        this.onTick = onTick;
        this.onPhaseChange = onPhaseChange;
    }

    public void start() {
        if (scheduler == null || scheduler.isShutdown()) {
            scheduler = Executors.newScheduledThreadPool(1);
        }
        scheduler.scheduleAtFixedRate(() -> {
            if (!isPaused) {
                if (remainingTime > 0) {
                    remainingTime--;
                    if (onTick != null) {
                        onTick.run();
                    }
                } else {
                    switchPhase(); // 切換階段
                }
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    public void stop() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }

    public void pause() {
        isPaused = true;
    }

    public void resume() {
        isPaused = false;
    }

    private void switchPhase() {
        isFocusPhase = !isFocusPhase;
        remainingTime = isFocusPhase ? focusTime : breakTime;
        if (onPhaseChange != null) {
            onPhaseChange.run();
        }
    }

    public boolean isFocusPhase() {
        return isFocusPhase;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public String formatTime() {
        int minutes = remainingTime / 60;
        int seconds = remainingTime % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}
