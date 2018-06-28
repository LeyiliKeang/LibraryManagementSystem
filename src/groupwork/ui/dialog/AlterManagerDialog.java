package groupwork.ui.dialog;

import com.sun.awt.AWTUtilities;
import groupwork.my.components.MyButton;
import groupwork.my.components.MyPanel;
import groupwork.my.document.RegexDocument;
import groupwork.sql.dao.ManagerDao;
import groupwork.sql.model.Manager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

public class AlterManagerDialog extends JDialog {
    private JPanel contentPane;
    private MyPanel myPanel1;
    private MyPanel myPanel2;
    private JTextField numberTextField;
    private JTextField nameTextField;
    private JTextField passwordTextField;
    private JTextField phoneTextField;
    private MyPanel myPanel3;
    private MyButton buttonOK;
    private MyButton buttonCancel;

    private static Point location = new Point();

    public AlterManagerDialog() {
        setUndecorated(true);
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        initComponents();

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

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
            @Override
            public void windowOpened(WindowEvent e) {
                AWTUtilities.setWindowOpacity(AlterManagerDialog.this, 0f);
                ActionListener listener = new ActionListener() {
                    float alpha = 0;

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (alpha < 0.8) {
                            AWTUtilities.setWindowOpacity(AlterManagerDialog.this, alpha += 0.2);
                        } else {
                            AWTUtilities.setWindowOpacity(AlterManagerDialog.this, 1);
                            Timer source = (Timer) e.getSource();
                            source.stop();
                        }
                    }
                };
                new Timer(50, listener).start();
            }
        });
    }

    private void onOK() {
        // add your code here
        if (numberTextField.getText().trim().matches("\\d{17}[0-9xX]") && !nameTextField.getText().trim().equals("")
                && !passwordTextField.getText().trim().equals("") && phoneTextField.getText().trim().matches("\\d{11}")) {
            ManagerDao managerDao = new ManagerDao();
            Manager m = new Manager();
            m.setManagerIDNumber(numberTextField.getText().trim());
            m.setManagerName(nameTextField.getText().trim());
            m.setManagerPassword(passwordTextField.getText().trim());
            m.setManagerPhone(phoneTextField.getText().trim());
            try {
                managerDao.updateBook(m, Manager.getSelectID());
                JOptionPane.showMessageDialog(this, "修改成功", "提示", JOptionPane.WARNING_MESSAGE);
                dispose();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "修改失败", "提示", JOptionPane.WARNING_MESSAGE);
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "修改失败", "提示", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(getContentPane(), "请正确填写管理员信息", "提示", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

//    public static void main(String[] args) {
//        AlterManagerDialog dialog = new AlterManagerDialog();
//        dialog.pack();
//        dialog.setVisible(true);
//        System.exit(0);
//    }

    private void initComponents() {
        numberTextField.setDocument(new RegexDocument("\\d{0,17}[0-9xX]"));
        phoneTextField.setDocument(new RegexDocument("\\d{0,11}"));

        ManagerDao managerDao = new ManagerDao();
        try {
            Manager manager = managerDao.getOne(Manager.getSelectID());
            numberTextField.setText(manager.getManagerIDNumber());
            nameTextField.setText(manager.getManagerName());
            passwordTextField.setText(manager.getManagerPassword());
            phoneTextField.setText(manager.getManagerPhone());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
