package com.maodou.touchtransferpc.gui;

import com.maodou.touchtransferpc.TouchTransferPcApplication;
import com.maodou.touchtransferpc.component.Config;
import com.maodou.touchtransferpc.constant.PathConstant;
import com.maodou.touchtransferpc.util.ConfigUtil;
import com.maodou.touchtransferpc.util.QRCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;

/**
 * @Description 创建应用窗体
 * @Author Wang Yucui
 * @Date 9/1/2020 10:19 PM
 */
@Slf4j
public class TouchTray {


    private static final long serialVersionUID = 1L;
    private static TrayIcon trayIcon = null;
    static SystemTray tray = SystemTray.getSystemTray();
    private static JDialog iconDialog = new JDialog();
    private static JDialog codeDialog = new JDialog();


    /**
     * @Description 窗口最小化到任务栏托盘
     */
    public static void miniTray() {
        //创建配置文件
        ConfigUtil.createDefaultConfig();
        // 托盘图标
        ImageIcon imageIcon = new ImageIcon(PathConstant.ICONURL);
        //创建图标的dialog
        getJDialog();
        //设置右下角图标
        trayIcon = new TrayIcon(imageIcon.getImage(), "碰碰传", new PopupMenu());
        trayIcon.setImageAutoSize(true);
        trayIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showDialog();
            }
        });
        try {
            tray.add(trayIcon);
        } catch (AWTException e1) {
            e1.printStackTrace();
        }
    }


    public static void showDialog() {
        iconDialog.setVisible(true);
        iconDialog.setAlwaysOnTop(true);
        iconDialog.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                iconDialog.dispose();
            }
        });

    }

    private static JDialog getJDialog() {
        iconDialog.setBounds(Toolkit.getDefaultToolkit().getScreenSize().width - 200,
                Toolkit.getDefaultToolkit().getScreenSize().height - 300, 120, 150);
        JButton codeButton = new JButton("二维码");
        JButton storePathButton = new JButton("文件存储位置");
        JButton exitButton = new JButton("退出");
        iconDialog.setIconImage(Toolkit.getDefaultToolkit().getImage(PathConstant.ICONURL));
        iconDialog.setLayout(new GridLayout(3, 1));
        iconDialog.setTitle("碰碰传");
        iconDialog.add(codeButton);
        iconDialog.add(storePathButton);
        iconDialog.add(exitButton);
        iconDialog.toFront();
        codeButtonListener(codeButton);
        storePathButtonListener(storePathButton);
        exitButtonListener(exitButton);
        return iconDialog;
    }

    private static void storePathButtonListener(JButton storePathButton) {
        storePathButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //创建dialog
                JDialog pathConfigDialog = new JDialog();
                pathConfigDialog.setLayout(new GridLayout());
                pathConfigDialog.setTitle("碰碰传 文件存储位置");
                pathConfigDialog.setIconImage(Toolkit.getDefaultToolkit().getImage(PathConstant.ICONURL));
                int windowWidth = 450;
                int windowHeight = 125;
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                int screenWidth = screenSize.width;
                int screenHeight = screenSize.height;
                pathConfigDialog.setBounds(screenWidth / 2 - windowWidth / 2, screenHeight / 2 - windowHeight / 2, windowWidth, windowHeight);


                //label组件
                JLabel jLabel2 = new JLabel("路径:");
                //文本框组件
                JTextField jTextField = new JTextField(Config.getINSTANCE().getStorePath());
                jTextField.setPreferredSize(new Dimension(365, 31));
                //两个按钮 打开，更改
                JButton openButton = new JButton("打开");
                JButton changeButton = new JButton("更改");
                // panel组件
                JPanel panel1 = new JPanel(new FlowLayout());
                panel1.setBorder(BorderFactory.createLineBorder(Color.darkGray));
                panel1.add(jLabel2, new GridBagConstraints(0, 0, 1, 1, 0.0,
                        0.0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                        new Insets(10, 10, 5, 5), 0, 0));
                panel1.add(jTextField, new GridBagConstraints(1, 0, 9, 1, 0.0,
                        0.0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                        new Insets(10, 10, 5, 5), 0, 0));
                panel1.add(openButton, new GridBagConstraints(0, 1, 1, 1, 0.0,
                        0.0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                        new Insets(5, 5, 5, 5), 0, 0));
                panel1.add(changeButton, new GridBagConstraints(0, 1, 1, 1, 0.0,
                        0.0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                        new Insets(5, 10, 5, 10), 0, 0));
                pathConfigDialog.add(panel1);
                pathConfigDialog.setVisible(true);
                pathConfigDialog.toFront();

                changeButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        String storePath = Config.getINSTANCE().getStorePath();
                        JFileChooser fileChooser = new JFileChooser(storePath);
                        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                        int returnVal = fileChooser.showOpenDialog(fileChooser);
                        if (returnVal == JFileChooser.APPROVE_OPTION) {
                            storePath=fileChooser.getSelectedFile().getPath();
                            jTextField.setText(storePath);
                            Config.getINSTANCE().setStorePath(storePath);
                        }


                    }
                });
                openButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        try {
                            Desktop.getDesktop().open(new File(Config.getINSTANCE().getStorePath()));
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }

                    }
                });

                iconDialog.dispose();
            }
        });
    }

    private static void exitButtonListener(JButton exitButton) {
        exitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                exitApplication(TouchTransferPcApplication.ctx);
            }
        });
    }

    private static void codeButtonListener(JButton codeButton) {
        codeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                getQRCode();
                iconDialog.dispose();
            }
        });
    }

    public static void exitApplication(ConfigurableApplicationContext context) {
        int exitCode = SpringApplication.exit(context, (ExitCodeGenerator) () -> 0);

        System.exit(exitCode);
    }

    private static JDialog getQRCode() {
        JLabel label = new JLabel();
        GetQRCodeImage();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        int codeWith = 155;
        int codeHeight = 150;
        int windowWidth = 310;
        int windowHeight = 350;

        label.setBounds((screenSize.width - codeWith) / 2, (screenSize.height - codeHeight) / 2, codeWith, codeHeight);
        label.setIcon(new ImageIcon(PathConstant.QRCODEURL));

        codeDialog.setBounds(Toolkit.getDefaultToolkit().getScreenSize().width - codeWith,
                Toolkit.getDefaultToolkit().getScreenSize().height - 125, 120, codeHeight);
        codeDialog.setIconImage(Toolkit.getDefaultToolkit().getImage(PathConstant.CODEICONURL));
        codeDialog.setTitle("碰碰传");
        codeDialog.setSize(windowWidth, windowHeight);
        codeDialog.setLocation(screenWidth / 2 - windowWidth / 2, screenHeight / 2 - windowHeight / 2);
        codeDialog.add(label);
        codeDialog.setVisible(true);
        return codeDialog;
    }

    private static void GetQRCodeImage() {
        File file = null;
        PathConstant.QRCODEURL = "QRCode.jpg";
        file = new File(PathConstant.QRCODEURL);
        log.info("iconPath:  " + PathConstant.CODEICONURL);
        //判断二维码是否存在
        if (!file.exists()) {
            String uniqueCode = Config.getINSTANCE().getUniqueCode();
            QRCodeUtil.zxingCodeCreate(uniqueCode, PathConstant.QRCODEURL, 300, PathConstant.CODEICONURL);
        }
    }

    /**
     * 统一设置字体，父界面设置之后，所有由父界面进入的子界面都不需要再次设置字体
     */
    public static void initGlobalFont(Font font) {
        FontUIResource fontRes = new FontUIResource(font);
        for (Enumeration<Object> keys = UIManager.getDefaults().keys(); keys.hasMoreElements(); ) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, fontRes);
            }
        }
    }
}
