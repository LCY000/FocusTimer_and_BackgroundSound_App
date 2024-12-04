package FocusTimerApp;
import javax.swing.UIManager;
import com.formdev.flatlaf.FlatLightLaf;

public class App {
    public static void main(String[] args) {
        // 設置簡潔的 UI 風格
    // 設置 FlatLaf 主題
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // 啟動主界面
        javax.swing.SwingUtilities.invokeLater(() -> {
            TimerUI timerUI = new TimerUI();
            timerUI.createAndShowGUI();
        });
    }
}
