// MP3Player

package FocusTimerApp;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class MP3Player {
    public MediaPlayer mediaPlayer;
    private Media media;
    private boolean isInitialized = false; // 表示播放器是否已初始化

    public MP3Player(String folderPath, String fileName) {
        new Thread(() -> {
            try {
                String filePath = folderPath + "/" + fileName + ".mp3";
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

                mediaPlayer.setOnReady(() -> {
                    System.out.println("音檔: "+ fileName + ".mp3  " + "長度: " + mediaPlayer.getMedia().getDuration().toSeconds() + " 秒");
                });
                mediaPlayer.setOnError(() -> {
                    System.out.println("MP3撥放發生錯誤: " + mediaPlayer.getError().getMessage());
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
