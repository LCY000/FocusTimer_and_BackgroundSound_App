import javax.swing.*;

import com.formdev.flatlaf.FlatLightLaf;
import java.awt.*;

public class TimerUI {
    private JFrame frame;
    private JPanel mainPanel, topPanel, bottomPanel;
    private JPanel contentPanel, timePanel;
    private CardLayout cardLayout; // 用於切換專注時間/正計時界面
    private JLabel timeDisplayLabel; // 新增的顯示時間的Label
    private JSeparator separator;

    public void createAndShowGUI() {
        FlatLightLaf.setup(); // 設定 FlatLaf 主題

        // 初始化主框架
        frame = new JFrame("Focus Timer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(440, 600);
        frame.setLayout(new BorderLayout());
        mainPanel = new JPanel(new GridBagLayout());
        frame.add(mainPanel, BorderLayout.NORTH);
        GridBagConstraints gbc = new GridBagConstraints();

        // **第1區域: 頂部選擇模式區**
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.insets = new Insets(20, 0, 20, 0);
        gbc.anchor = GridBagConstraints.NORTH; // 置頂
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(modePanel(), gbc);

        // **添加水平分界線** //不知道為甚麼顯示不出來
        separator = new JSeparator(SwingConstants.HORIZONTAL);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 10, 0);
        mainPanel.add(separator, gbc); // 添加分界線

        // **第2區域: 計時設定區**
        // 計時內容區域
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        contentPanel.add(createFocusPanel(), "FocusMode"); // 專注時間界面
        contentPanel.add(createTimerPanel(), "TimerMode"); // 正計時界面

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 0, 5, 0);
        mainPanel.add(contentPanel, gbc);

        // **第3區域: 時間顯示區**
        timePanel = new JPanel();
        timeDisplayLabel = new JLabel("00:00", SwingConstants.CENTER);
        timeDisplayLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        timePanel.add(timeDisplayLabel, BorderLayout.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridheight = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 0, 10, 0);
        mainPanel.add(timePanel, gbc);

        // **第3區域: 背景音樂控制區**
        bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.setBorder(BorderFactory.createTitledBorder("背景音樂"));
        JPanel backgroundMusicPanel = createBackgroundMusicPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 20)); // 設定組件間距：水平間距10px，垂直間距20px
        bottomPanel.add(backgroundMusicPanel);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridheight = 3;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.insets = new Insets(50, 10, 0, 10);
        mainPanel.add(bottomPanel, gbc);

        // **第4區域: 自訂提示音控制區**
        JPanel customSoundPanel = createCustomSoundPanel();

        // 顯示/隱藏按鈕
        JToggleButton toggleSoundPanelBtn = new JToggleButton("計時提示音效設定");
        toggleSoundPanelBtn.addActionListener(e -> {
            boolean isVisible = customSoundPanel.isVisible();
            customSoundPanel.setVisible(!isVisible);
            frame.pack(); // 調整視窗大小
        });

        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridheight = 1;
        gbc.insets = new Insets(20, 0, 20, 0);
        mainPanel.add(toggleSoundPanelBtn, gbc);

        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridheight = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 10, 10, 10);
        mainPanel.add(customSoundPanel, gbc);

        frame.setVisible(true);
        frame.pack(); // 強制更新大小(原本分界線顯示不出來)
    }

    private JPanel modePanel() {
        topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JToggleButton focusModeBtn = new JToggleButton("專注時間");
        JToggleButton timerModeBtn = new JToggleButton("正計時");
        ButtonGroup modeGroup = new ButtonGroup();
        modeGroup.add(focusModeBtn);
        modeGroup.add(timerModeBtn);
        focusModeBtn.setSelected(true); // 預設專注時間
        topPanel.add(focusModeBtn);
        topPanel.add(timerModeBtn);

        // 按鈕事件
        focusModeBtn.addActionListener(e -> cardLayout.show(contentPanel, "FocusMode"));
        timerModeBtn.addActionListener(e -> cardLayout.show(contentPanel, "TimerMode"));

        return topPanel;
    }

    // 專注時間界面
    private JPanel createFocusPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // **panel上方區域**

        // 專注時間設定
        JPanel focusSettings = new JPanel(new FlowLayout());
        focusSettings.add(new JLabel("專注時間 (分鐘):"));
        JTextField focusTimeField = new JTextField("25", 5);
        focusSettings.add(focusTimeField);

        focusSettings.add(new JLabel("  休息 (分鐘):"));
        JTextField breakTimeField = new JTextField("5", 5);
        focusSettings.add(breakTimeField);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(focusSettings, gbc);

        // **panel下方區域**
        // **控制按鈕**
        JPanel controlButtons = new JPanel(new FlowLayout());
        JButton startBtn = new JButton("開始");
        JButton pauseBtn = new JButton("暫停");
        JButton stopBtn = new JButton("結束");
        controlButtons.add(startBtn);
        controlButtons.add(pauseBtn);
        controlButtons.add(stopBtn);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 0);
        panel.add(controlButtons, gbc);

        return panel;
    }

    // 測試音效播放
    private void playTestSound(String soundName) {
        // 在此實現音效播放的邏輯，例如：
        System.out.println("播放提示音效: " + soundName);
        // 可用 JavaFX MediaPlayer 或其他音效播放類
    }

    // 調整音量
    private void adjustVolume(int volume) {
        System.out.println("提示音量調整為: " + volume + "%");
        // 可實現與音量調整相關的邏輯
    }

    // 正計時界面，碼表計時
    private JPanel createTimerPanel() {
        JPanel borderPanel = new JPanel(new BorderLayout());
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel textLabel = new JLabel("計時:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.fill = GridBagConstraints.VERTICAL;
        panel.add(textLabel, gbc);

        JButton startBtn = new JButton("開始");
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.insets = new Insets(0, 10, 0, 0);
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(startBtn, gbc);

        JButton pauseBtn = new JButton("暫停");
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.insets = new Insets(0, 10, 0, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(pauseBtn, gbc);

        JButton stopBtn = new JButton("結束");
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.insets = new Insets(0, 10, 0, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(stopBtn, gbc);

        borderPanel.add(panel, BorderLayout.NORTH);

        return borderPanel;
    }

    // 背景音樂控制區
    private JPanel createCustomSoundPanel() {
        JPanel customSoundPanel = new JPanel(new GridLayout(3, 1));
        customSoundPanel.setBorder(BorderFactory.createTitledBorder("提示音效"));

        // 1. 提示音效選擇
        JPanel soundSelectorPanel = new JPanel(new FlowLayout());
        soundSelectorPanel.add(new JLabel("提示音效:"));
        JComboBox<String> soundSelector = new JComboBox<>(new String[] { "鈴聲", "鳥鳴聲", "鐘聲" });
        soundSelectorPanel.add(soundSelector);
        customSoundPanel.add(soundSelectorPanel);

        // 2. 測試音效按鈕
        JPanel testSoundPanel = new JPanel(new FlowLayout());
        JButton testSoundButton = new JButton("測試音效");
        testSoundPanel.add(testSoundButton);
        customSoundPanel.add(testSoundPanel);

        // 測試音效按鈕動作邏輯
        testSoundButton.addActionListener(e -> {
            String selectedSound = (String) soundSelector.getSelectedItem();
            playTestSound(selectedSound); // 測試音效播放
        });

        // 3. 音量滑桿
        JPanel volumeControlPanel = new JPanel(new FlowLayout());
        volumeControlPanel.add(new JLabel("音量:"));
        JSlider volumeSlider = new JSlider(0, 100, 50);
        volumeControlPanel.add(volumeSlider);
        customSoundPanel.add(volumeControlPanel);

        // 音量滑桿邏輯
        volumeSlider.addChangeListener(e -> {
            int volume = volumeSlider.getValue();
            adjustVolume(volume); // 調整音量
        });

        customSoundPanel.setVisible(false); // 預設隱藏

        return customSoundPanel;
    }

    // 背景音樂控制區
    private JPanel createBackgroundMusicPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1));

        // 音樂選擇與控制
        JPanel musicControl = new JPanel(new FlowLayout());
        musicControl.add(new JLabel("背景音樂:"));
        JComboBox<String> musicSelector = new JComboBox<>(new String[] { "海浪聲", "下雨聲", "夜晚聲音", "Minecraft" });
        musicControl.add(musicSelector);

        JButton playBtn = new JButton("播放");
        JButton pauseBtn = new JButton("暫停");
        JButton forwardBtn = new JButton("快進");
        JButton stopBtn = new JButton("結束");
        musicControl.add(playBtn);
        musicControl.add(pauseBtn);
        musicControl.add(forwardBtn);
        musicControl.add(stopBtn);

        panel.add(musicControl);

        // 音量控制
        JPanel volumeControl = new JPanel(new FlowLayout());
        volumeControl.add(new JLabel("音量:"));
        JSlider volumeSlider = new JSlider(0, 100, 50);
        volumeControl.add(volumeSlider);

        panel.add(volumeControl);

        return panel;
    }
}
