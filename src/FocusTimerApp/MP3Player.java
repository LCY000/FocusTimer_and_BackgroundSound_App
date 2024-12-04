package FocusTimerApp;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class MP3Player {
    private MediaPlayer mediaPlayer;
    private Media media;
    private boolean isInitialized = false; // 表示播放器是否已初始化

    public MP3Player(String fileName) {
        new Thread(() -> {
            try {
                String filePath = "resources/background_sounds/" + fileName + ".mp3";
                media = new Media(new File(filePath).toURI().toString());
                mediaPlayer = new MediaPlayer(media);
                isInitialized = true;

                // 自動重播邏輯
                mediaPlayer.setOnEndOfMedia(() -> {
                    if (isInitialized) {
                        mediaPlayer.seek(javafx.util.Duration.ZERO); // 回到開頭
                        mediaPlayer.play(); // 自動重播
                    }
                });
            } catch (Exception e) {
                isInitialized = false; // 標記初始化失敗
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(null, "無法加載音樂文件：" + fileName, "錯誤", JOptionPane.ERROR_MESSAGE);
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

    // public void fastForward(double seconds) {
    //     if (mediaPlayer != null) {
    //         javafx.application.Platform.runLater(() -> {
                
    //             //快轉前時間
    //             System.out.println("當前播放時間: " + mediaPlayer.getCurrentTime().toSeconds() + " 秒");
    //             double currentTime = mediaPlayer.getCurrentTime().toSeconds();
    //             double totalDuration = mediaPlayer.getTotalDuration().toSeconds();
    //             double newTime = Math.min(currentTime + seconds, totalDuration); // 確保不超過總時長
    //             mediaPlayer.pause();
    //             mediaPlayer.seek(javafx.util.Duration.seconds(newTime));
    //             mediaPlayer.play();
    //             System.out.println("快進到: " + newTime + " 秒");
    //             updateTimeDisplay();
                
    //         });
    //     }
    // }
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

    // //後臺測試時間
    // private void updateTimeDisplay() {
    //     javafx.application.Platform.runLater(() -> {
    //         if (mediaPlayer != null) {
    //             System.out.println("**快轉後執行**");
    //             System.out.println("播放器狀態: " + mediaPlayer.getStatus());
    //             System.out.println("總時長: " + mediaPlayer.getTotalDuration());
    //             double currentTime = mediaPlayer.getCurrentTime().toSeconds();
    //             System.out.println("當前播放時間: " + currentTime + " 秒");
    //         }
    //     });
    // }
}
