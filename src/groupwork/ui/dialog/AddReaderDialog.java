package groupwork.ui.dialog;

import com.sun.awt.AWTUtilities;
import groupwork.my.components.MyButton;
import groupwork.my.components.MyPanel;
import groupwork.my.document.RegexDocument;
import groupwork.sql.dao.ReaderDao;
import groupwork.sql.model.Reader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

public class AddReaderDialog extends JDialog {
    private JPanel contentPane;
    private MyPanel myPanel1;
    private MyPanel myPanel2;
    private MyPanel myPanel3;
    private MyButton buttonOK;
    private MyButton buttonCancel;
    private JTextField numberTextField;
    private JTextField nameTextField;
    private JTextField classTextField;
    private JTextField phoneTextField;
    private JComboBox sexComboBox;

    private static Point location = new Point();

    public AddReaderDialog() {
        setUndecorated(true);
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonCancel);

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
                AWTUtilities.setWindowOpacity(AddReaderDialog.this, 0f);
                ActionListener listener = new ActionListener() {
                    float alpha = 0;

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (alpha < 0.8) {
                            AWTUtilities.setWindowOpacity(AddReaderDialog.this, alpha += 0.2);
                        } else {
                            AWTUtilities.setWindowOpacity(AddReaderDialog.this, 1);
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
        if (numberTextField.getText().trim().matches("\\d{14}") && !nameTextField.getText().trim().equals("")
                && sexComboBox.getSelectedIndex() != -1 && !classTextField.getText().trim().equals("")
                && phoneTextField.getText().trim().matches("\\d{11}")) {
            ReaderDao readerDao = new ReaderDao();
            Reader reader = new Reader();
            reader.setReaderID(numberTextField.getText().trim());
            reader.setReaderName(nameTextField.getText().trim());
            reader.setReaderSex(sexComboBox.getSelectedItem().toString());
            reader.setReaderClass(classTextField.getText().trim());
            reader.setReaderPhone(phoneTextField.getText().trim());
            try {
                readerDao.addReader(reader);
                JOptionPane.showMessageDialog(this, "添加成功", "提示", JOptionPane.WARNING_MESSAGE);
                dispose();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "添加失败", "提示", JOptionPane.WARNING_MESSAGE);
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "添加失败", "提示", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "请正确填写读者信息", "提示", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

//    public static void main(String[] args) {
//        AddReaderDialog dialog = new AddReaderDialog();
//        dialog.pack();
//        dialog.setVisible(true);
//        System.exit(0);
//    }

    private void initComponents() {
        numberTextField.setDocument(new RegexDocument("\\d{0,14}"));
        phoneTextField.setDocument(new RegexDocument("\\d{0,11}"));
    }
}
