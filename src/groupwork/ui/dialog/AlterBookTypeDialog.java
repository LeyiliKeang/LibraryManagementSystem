package groupwork.ui.dialog;

import com.sun.awt.AWTUtilities;
import groupwork.my.components.MyButton;
import groupwork.my.components.MyPanel;
import groupwork.sql.dao.BookTypeDao;
import groupwork.sql.model.BookType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

public class AlterBookTypeDialog extends JDialog {
    private JPanel contentPane;
    private MyPanel myPanel1;
    private MyButton buttonOK;
    private MyButton buttonCancel;
    private JTextField bookTypeIDTextField;
    private JTextField bookTypeNameTextField;

    private static Point location = new Point();

    public AlterBookTypeDialog() {
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
                AWTUtilities.setWindowOpacity(AlterBookTypeDialog.this, 0f);
                ActionListener listener = new ActionListener() {
                    float alpha = 0;

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (alpha < 0.8) {
                            AWTUtilities.setWindowOpacity(AlterBookTypeDialog.this, alpha += 0.2);
                        } else {
                            AWTUtilities.setWindowOpacity(AlterBookTypeDialog.this, 1);
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
        if (!bookTypeIDTextField.getText().trim().equals("") && !bookTypeNameTextField.getText().trim().equals("")) {
            BookTypeDao bookTypeDao = new BookTypeDao();
            BookType bookType = new BookType();
            bookType.setBookTypeID(bookTypeIDTextField.getText().trim());
            bookType.setBookTypeName(bookTypeNameTextField.getText().trim());
            try {
                bookTypeDao.updateBookType(bookType, BookType.getSelectID());
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
            JOptionPane.showMessageDialog(this, "请填写", "提示", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

//    public static void main(String[] args) {
//        AlterBookTypeDialog dialog = new AlterBookTypeDialog();
//        dialog.pack();
//        dialog.setVisible(true);
//        System.exit(0);
//    }

    private void initComponents() {
        BookTypeDao bookTypeDao = new BookTypeDao();
        try {
            BookType bookType = bookTypeDao.getOne(BookType.getSelectID());
            bookTypeIDTextField.setText(bookType.getBookTypeID());
            bookTypeNameTextField.setText(bookType.getBookTypeName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
