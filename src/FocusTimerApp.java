public class FocusTimerApp {
    public static void main(String[] args) {
        // 設置簡潔的 UI 風格
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
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
