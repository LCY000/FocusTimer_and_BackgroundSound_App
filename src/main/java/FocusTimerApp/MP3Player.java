package FocusTimerApp;

// MP3Player
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class MP3Player {
    public MediaPlayer mediaPlayer;
    private Media media;
    private boolean isInitialized = false; // 表示播放器是否已初始化

    /**
     * @param resourcePath 例如 "/background_sounds/海浪.mp3"
     */
    public MP3Player(String resourcePath) {
        new Thread(() -> {
            try {
                // 1) 從 classpath 取得 URL
                URL url = getClass().getResource(resourcePath);

                if (url == null) {
                    throw new RuntimeException("找不到資源檔: " + resourcePath);
                }

                // 2) 轉成 Media
                media = new Media(url.toExternalForm());
                mediaPlayer = new MediaPlayer(media);
                isInitialized = true;

                // 自動重播邏輯
                mediaPlayer.setOnEndOfMedia(() -> {
                    if (isInitialized) {
                        mediaPlayer.seek(javafx.util.Duration.ZERO); // 回到開頭
                        mediaPlayer.play(); // 自動重播
                    }
                });

                // 當音檔 ready 時印出長度
                mediaPlayer.setOnReady(() -> {
                    double secs = mediaPlayer.getMedia().getDuration().toSeconds();
                    System.out.println("音檔: " + resourcePath + "，長度: " + secs + " 秒");
                });
                mediaPlayer.setOnError(() -> {
                    System.out.println("MP3撥放發生錯誤: " + mediaPlayer.getError().getMessage());
                });

            } catch (Exception e) {
                isInitialized = false; // 標記初始化失敗
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(null, "無法加載音樂文件：" + resourcePath, "錯誤", JOptionPane.ERROR_MESSAGE);
                });
            }
        }).start();
    }


    public boolean isInitialized() {
        return isInitialized;
    }

    public void play() {
        if (mediaPlayer != null) {
            javafx.application.Platform.runLater(() -> mediaPlayer.play());
        }
    }

    public void pause() {
        if (mediaPlayer != null) {
            javafx.application.Platform.runLater(() -> mediaPlayer.pause());
        }
    }

    public void resume() {
        if (mediaPlayer != null) {
            mediaPlayer.play(); // `MediaPlayer` 自帶從暫停點繼續播放的功能
        }
    }

    public void stop() {
        if (mediaPlayer != null) {
            javafx.application.Platform.runLater(() -> mediaPlayer.stop());
        }
    }

    public void fastForward(double seconds) {
        if (mediaPlayer != null) {
            javafx.application.Platform.runLater(() -> {
                if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING || 
                    mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED) {
                    double currentTime = mediaPlayer.getCurrentTime().toSeconds();
                    double totalDuration = mediaPlayer.getTotalDuration().toSeconds();
                    double newTime = Math.min(currentTime + seconds, totalDuration);
                    mediaPlayer.seek(javafx.util.Duration.seconds(newTime));
                    System.out.println("快進到: " + newTime + " 秒");
                } else {
                    System.out.println("無法快進：播放器未在播放或暫停狀態");
                }
            });
        }
    }


    public void setVolume(double volume) {
        if (mediaPlayer != null) {
            javafx.application.Platform.runLater(() -> mediaPlayer.setVolume(volume));
        }
    }

    public void dispose() {
        if (mediaPlayer != null) {
            javafx.application.Platform.runLater(() -> mediaPlayer.dispose());
        }
    }
    

}
