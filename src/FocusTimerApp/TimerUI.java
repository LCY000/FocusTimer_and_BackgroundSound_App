package FocusTimerApp;
import javax.swing.*;
import javafx.embed.swing.JFXPanel;
import com.formdev.flatlaf.FlatLightLaf;
import java.awt.*;

public class TimerUI {
    private JFrame frame;
    private JPanel mainPanel, topPanel, bottomPanel;
    private JPanel contentPanel, timePanel;
    private CardLayout cardLayout; // 用於切換專注時間/正計時界面
    private JLabel phaseDisplayLabel;
    private JLabel pomodoroTimeDisplayLabel; // 顯示時間的Label
    private JLabel stopwatchTimeDisplayLabel; 

    private JSeparator separator;
    
    private PomodoroTimer pomodoroTimer; // 專注計時器邏輯
    private StopwatchLogic stopwatchLogic;  // 正計時器邏輯

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
        timePanel = new JPanel(new GridLayout(2,1));
        phaseDisplayLabel = new JLabel(" ", SwingConstants.CENTER);
        phaseDisplayLabel.setFont(new Font("微軟正黑體", Font.BOLD, 16));
        pomodoroTimeDisplayLabel = new JLabel(" ", SwingConstants.CENTER);
        pomodoroTimeDisplayLabel.setFont(new Font("SansSerif", Font.BOLD, 30));
        timePanel.add(phaseDisplayLabel, BorderLayout.CENTER);
        timePanel.add(pomodoroTimeDisplayLabel, BorderLayout.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridheight = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(20, 0, 10, 0);
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
        gbc.insets = new Insets(40, 10, 0, 10);
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

        // **按鈕邏輯**
        pauseBtn.setEnabled(false); //預設不能按
        stopBtn.setEnabled(false);

        startBtn.addActionListener(e -> {
            int focusTime = Integer.parseInt(focusTimeField.getText());
            int breakTime = Integer.parseInt(breakTimeField.getText());
            
            pomodoroTimer = new PomodoroTimer(
                focusTime,
                breakTime,
                () -> SwingUtilities.invokeLater(() -> pomodoroTimeDisplayLabel.setText(pomodoroTimer.formatTime())),
                () -> SwingUtilities.invokeLater(() -> {
                    String phase = pomodoroTimer.isFocusPhase() ? "專注階段" : "休息階段";
                    phaseDisplayLabel.setText(phase);
                    JOptionPane.showMessageDialog(frame, phase + "開始！");
                })
            );
            pomodoroTimer.start();
            phaseDisplayLabel.setText("專注階段");  //預設顯示
    
            startBtn.setEnabled(false);
            pauseBtn.setEnabled(true);
            stopBtn.setEnabled(true);
        });
    
        pauseBtn.addActionListener(e -> {
            if (pomodoroTimer != null) {
                if (pauseBtn.getText().equals("暫停")) {
                    pomodoroTimer.pause();
                    pauseBtn.setText("繼續");
                } else {
                    pomodoroTimer.resume();
                    pauseBtn.setText("暫停");
                }
            }
        });
    
        stopBtn.addActionListener(e -> {
            if (pomodoroTimer != null) {
                pomodoroTimer.stop();
            }
            phaseDisplayLabel.setText(" ");
            pomodoroTimeDisplayLabel.setText(" ");
            startBtn.setEnabled(true);
            pauseBtn.setEnabled(false);
            stopBtn.setEnabled(false);
            pauseBtn.setText("暫停"); // 恢復按鈕名稱
        });

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

        // 顯示計時時間的標籤
        stopwatchTimeDisplayLabel = new JLabel("00:00", SwingConstants.CENTER);
        stopwatchTimeDisplayLabel.setFont(new Font("SansSerif", Font.BOLD, 36));

        JLabel textLabel = new JLabel("計時:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.fill = GridBagConstraints.VERTICAL;
        panel.add(textLabel, gbc);

        // 按鈕區域
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

        JButton resetBtn = new JButton("重置");
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.insets = new Insets(0, 10, 0, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(resetBtn, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridheight = 2;
        gbc.gridwidth = 4;
        gbc.insets = new Insets(8, 0, 0, 0);
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(stopwatchTimeDisplayLabel, gbc);

        borderPanel.add(panel, BorderLayout.NORTH);

        // 初始按鈕狀態
        pauseBtn.setEnabled(false);
        resetBtn.setEnabled(false);

        // **計時邏輯**
        stopwatchLogic = new StopwatchLogic(() -> {
            // 每秒更新時間顯示
            SwingUtilities.invokeLater(() -> stopwatchTimeDisplayLabel.setText(stopwatchLogic.formatTime()));
        });

        // **按鈕邏輯**
        startBtn.addActionListener(e -> {
            stopwatchLogic.start();
            startBtn.setEnabled(false);
            pauseBtn.setEnabled(true);
            resetBtn.setEnabled(true);
        });

        pauseBtn.addActionListener(e -> {
            stopwatchLogic.pause();
            startBtn.setEnabled(true);
            pauseBtn.setEnabled(false);
        });

        resetBtn.addActionListener(e -> {
            stopwatchLogic.reset();
            startBtn.setEnabled(true);
            pauseBtn.setEnabled(false);
            resetBtn.setEnabled(false);
        });

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
        // 初始化 JavaFX 環境
        new JFXPanel();

        JPanel panel = new JPanel(new GridLayout(2, 1));
        // 音樂播放器變數
        final MP3Player[] mp3Player = {null};

        // 音樂選擇與控制
        JPanel musicControl = new JPanel(new FlowLayout());
        musicControl.add(new JLabel("音樂:"));
        JComboBox<String> musicSelector = new JComboBox<>(new String[] { "海浪", "下雨", "夜晚", "Minecraft" });
        musicControl.add(musicSelector);

        JButton playBtn = new JButton("播放");
        JButton pauseBtn = new JButton("暫停");
        JButton forwardBtn = new JButton("快進30s");
        JButton stopBtn = new JButton("結束");

        // 初始狀態：只有播放按鈕可用
        pauseBtn.setEnabled(false);
        forwardBtn.setEnabled(false);
        stopBtn.setEnabled(false);

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

        // **按鈕動作邏輯**
        playBtn.addActionListener(e -> {
            String selectedMusic = (String) musicSelector.getSelectedItem();
            if (mp3Player[0] != null) {
                new Thread(() -> mp3Player[0].stop()).start(); // 停止上一首音樂
            }
            mp3Player[0] = new MP3Player(selectedMusic);
            new Thread(() -> {
                // 等待 MP3Player 初始化
                while (!mp3Player[0].isInitialized()) {
                    try {
                        Thread.sleep(100); // 等待初始化完成
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                }
                SwingUtilities.invokeLater(() -> {
                    if (mp3Player[0].isInitialized()) {
                        new Thread(() -> mp3Player[0].play()).start();
                        playBtn.setEnabled(false);
                        pauseBtn.setEnabled(true);
                        forwardBtn.setEnabled(true);
                        stopBtn.setEnabled(true);
                    } else {
                        playBtn.setEnabled(true); // 初始化失敗，允許重新選擇
                    }
                });
            }).start();
        });
        
        
        stopBtn.addActionListener(e -> {
            if (mp3Player[0] != null) {
                new Thread(() -> mp3Player[0].stop()).start();
                playBtn.setEnabled(true); // 停止後允許重新播放
                pauseBtn.setEnabled(false);
                forwardBtn.setEnabled(false);
                stopBtn.setEnabled(false);
                pauseBtn.setText("暫停"); // 恢復按鈕名稱
            }
        });
        
        
        pauseBtn.addActionListener(e -> {
            if (mp3Player[0] != null && mp3Player[0].isInitialized()) {
                if (pauseBtn.getText().equals("暫停")) {
                    new Thread(() -> mp3Player[0].pause()).start();
                    pauseBtn.setText("繼續");
                } else {
                    new Thread(() -> mp3Player[0].resume()).start();
                    pauseBtn.setText("暫停");
                }
            }
        });

        forwardBtn.addActionListener(e -> {
            if (mp3Player[0] != null && mp3Player[0].isInitialized()) {
                System.out.println("快進按鈕被觸發");
                new Thread(() -> mp3Player[0].fastForward(30)).start(); // 快進 30 秒
            } else {
                System.out.println("快進無效：播放器未初始化");
            }
        });

        // **音量滑塊動作邏輯**
        volumeSlider.addChangeListener(e -> {
            if (mp3Player[0] != null && mp3Player[0].isInitialized()) {
                double volume = volumeSlider.getValue() / 100.0; // 音量範圍 0.0 ~ 1.0
                new Thread(() -> mp3Player[0].setVolume(volume)).start();
            } else {
                System.out.println("音樂播放器未初始化，無法調整音量！");
            }
        });
        

        return panel;
    }
}
