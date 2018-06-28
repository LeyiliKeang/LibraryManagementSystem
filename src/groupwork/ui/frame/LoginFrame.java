package groupwork.ui.frame;

import com.sun.awt.AWTUtilities;
import groupwork.my.components.MyButton;
import groupwork.my.components.MyPanel;
import groupwork.my.document.RegexDocument;
import groupwork.sql.dao.ManagerDao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

public class LoginFrame extends JFrame {
    private JPanel contentPane;
    private MyPanel maxPane;
    private MyPanel loginPane;
    private MyPanel logoPane;
    private MyPanel textPane;
    private MyPanel myPanel4;
    private JTextField userTextField;
    private MyPanel myPanel1;
    private MyButton resetButton;
    private MyButton loginButton;
    private JPasswordField passwordField;
    private MyButton closeButton;

    private static Point location = new Point();

    public LoginFrame() {
        setIconImage(Toolkit.getDefaultToolkit().getImage("imgs/Books.png"));
        setUndecorated(true);
        getRootPane().setDefaultButton(loginButton);

        maxPane.setIcon(new ImageIcon("imgs/maxbg3.png"));
        closeButton.setIcon(new ImageIcon("imgs/exit.png"));
        logoPane.setIcon(new ImageIcon("imgs/icon.png"));
        textPane.setIcon(new ImageIcon("imgs/text2.png"));

        userTextField.setDocument(new RegexDocument("\\d{0,17}[0-9xX]"));

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(contentPane);
        pack();
        setLocationRelativeTo(null);

        userTextField.requestFocus();

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
//                setExtendedState(JFrame.ICONIFIED);
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                location.x = e.getX();
                location.y = e.getY();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Point p = getLocation();
                setLocation(p.x + e.getX() - location.x, p.y + e.getY() - location.y);
            }
        });

        addWindowListener(new WindowAdapter() {
            //窗口打开时动画
            @Override
            public void windowOpened(WindowEvent e) {
                final int height = getHeight();
                final int width = getWidth();
                final Rectangle rec = getBounds();
                new Thread() {
                    @Override
                    public void run() {
                        for (int i = 0; i <= width; i += 10) {
                            setBounds(rec.x, rec.y, i, height);
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }.start();

                AWTUtilities.setWindowOpacity(LoginFrame.this, 0f);
                ActionListener listener = new ActionListener() {
                    float alpha = 0;

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (alpha < 0.9) {
                            AWTUtilities.setWindowOpacity(LoginFrame.this, alpha += 0.1);
                        } else {
                            AWTUtilities.setWindowOpacity(LoginFrame.this, 1);
                            Timer source = (Timer) e.getSource();
                            source.stop();
                        }
                    }
                };
                new Timer(50, listener).start();
            }
        });
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String managerID = userTextField.getText().trim();
                String password = passwordField.getText().trim();
                ManagerDao managerDao = new ManagerDao();
                try {
                    boolean flag = managerDao.isLogin(managerID, password);
                    if (flag) {
                        EventQueue.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                setVisible(false);
                                new MainFrame().setVisible(true);
                            }
                        });
                    } else {
                        JOptionPane.showMessageDialog(contentPane, "管理员或密码错误", "提示", JOptionPane.WARNING_MESSAGE);
                        userTextField.setText("");
                        passwordField.setText("");
                        userTextField.requestFocus();
                    }
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userTextField.setText("");
                passwordField.setText("");
                userTextField.requestFocus();
            }
        });
    }

    public static void main(String[] args) {
        //获取与系统匹配的主题
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.put("TabbedPane.contentOpaque", false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        new LoginFrame().setVisible(true);
    }
}
