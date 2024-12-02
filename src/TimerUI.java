import javax.swing.*;
import java.awt.*;

public class TimerUI {
    private JFrame frame;
    private JPanel mainPanel, topPanel, bottomPanel;
    private JPanel contentPanel, timePanel;
    private CardLayout cardLayout; // 用於切換專注時間/正計時界面
    private JLabel timeDisplayLabel; // 新增的顯示時間的Label

    

    public void createAndShowGUI() {
        // 初始化主框架
        frame = new JFrame("Focus Timer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 600);
        frame.setLayout(new BorderLayout());
        mainPanel = new JPanel(new GridBagLayout());
        frame.add(mainPanel,BorderLayout.NORTH);
        GridBagConstraints gbc = new GridBagConstraints();

        // **頂部選擇區**
        topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JToggleButton focusModeBtn = new JToggleButton("專注時間");
        JToggleButton timerModeBtn = new JToggleButton("正計時");
        ButtonGroup modeGroup = new ButtonGroup();
        modeGroup.add(focusModeBtn);
        modeGroup.add(timerModeBtn);
        focusModeBtn.setSelected(true); // 預設專注時間
        topPanel.add(focusModeBtn);
        topPanel.add(timerModeBtn);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.insets = new Insets(20,0,20,0);
        gbc.anchor = GridBagConstraints.NORTH; // 置頂
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(topPanel, gbc);

        // **添加水平分界線**
        JSeparator separator1 = new JSeparator(SwingConstants.HORIZONTAL);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridheight = 1; 
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0,0,10,0);
        mainPanel.add(separator1, gbc); // 添加分界線


        // **中間區域**
        // 計時內容區域
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        // 專注時間界面
        contentPanel.add(createFocusPanel(), "FocusMode");

        // 正計時界面
        contentPanel.add(createTimerPanel(), "TimerMode");

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridheight = 4;  
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.insets = new Insets(5,0,5,0);
        mainPanel.add(contentPanel, gbc);

        // **時間顯示區域 (顯示計時器時間)**
        timePanel = new JPanel();
        timePanel.setLayout(new BorderLayout());
        timeDisplayLabel = new JLabel("00:00", SwingConstants.CENTER);  // 初始時間
        timePanel.add(timeDisplayLabel, BorderLayout.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridheight = 2;  
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.insets = new Insets(10,0,10,0);
        mainPanel.add(timePanel, gbc);

        // **添加水平分界線**
        JSeparator separator2 = new JSeparator(SwingConstants.HORIZONTAL);
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridheight = 1;  
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10,0,10,0);
        mainPanel.add(separator2, gbc); // 添加分界線

        // **下方背景音樂控制區**
        bottomPanel = createBackgroundMusicPanel();
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridheight = 3;  
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.insets = new Insets(10,0,0,0);
        mainPanel.add(bottomPanel, gbc);


        // 按鈕事件
        focusModeBtn.addActionListener(e -> cardLayout.show(contentPanel, "FocusMode"));
        timerModeBtn.addActionListener(e -> cardLayout.show(contentPanel, "TimerMode"));

        frame.setVisible(true);
    }

    // 創建專注時間界面
    private JPanel createFocusPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // **panel上方區域**

        // 專注時間設定
        JPanel focusSettings = new JPanel(new FlowLayout());
        focusSettings.add(new JLabel("專注 (分鐘):"));
        JTextField focusTimeField = new JTextField("25", 5);
        focusSettings.add(focusTimeField);

        focusSettings.add(new JLabel("休息 (分鐘):"));
        JTextField breakTimeField = new JTextField("5", 5);
        focusSettings.add(breakTimeField);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(focusSettings, gbc);

        // **panel中間區域**

        // **提示音效區域**
        JPanel soundSettings = new JPanel(new GridLayout(3, 1));
        soundSettings.setBorder(BorderFactory.createTitledBorder("提示音效"));

        // 1. 提示音效選擇
        JPanel soundSelectorPanel = new JPanel(new FlowLayout());
        soundSelectorPanel.add(new JLabel("提示音效:"));
        JComboBox<String> soundSelector = new JComboBox<>(new String[]{"鈴聲", "鳥鳴聲", "鐘聲"});
        soundSelectorPanel.add(soundSelector);
        soundSettings.add(soundSelectorPanel);

        // 2. 測試音效按鈕
        JPanel testSoundPanel = new JPanel(new FlowLayout());
        JButton testSoundButton = new JButton("測試音效");
        testSoundPanel.add(testSoundButton);
        soundSettings.add(testSoundPanel);

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
        soundSettings.add(volumeControlPanel);

        // 音量滑桿邏輯
        volumeSlider.addChangeListener(e -> {
            int volume = volumeSlider.getValue();
            adjustVolume(volume); // 調整音量
        });

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridheight = 4;  
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10,0,10,0);
        panel.add(soundSettings, gbc);

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
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0,0,0,0);
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

    // 創建正計時界面
    private JPanel createTimerPanel() {
        JPanel panel = new JPanel(new FlowLayout());

        panel.add(new JLabel("計時 (秒):"));
        JTextField timerField = new JTextField("60", 5);
        panel.add(timerField);

        JButton startBtn = new JButton("開始");
        JButton pauseBtn = new JButton("暫停");
        JButton stopBtn = new JButton("結束");
        panel.add(startBtn);
        panel.add(pauseBtn);
        panel.add(stopBtn);

        return panel;
    }

    // 創建背景音樂控制區
    private JPanel createBackgroundMusicPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1));

        // 音樂選擇與控制
        JPanel musicControl = new JPanel(new FlowLayout());
        musicControl.add(new JLabel("背景音樂:"));
        JComboBox<String> musicSelector = new JComboBox<>(new String[]{"海浪聲", "下雨聲", "夜晚聲音"});
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
