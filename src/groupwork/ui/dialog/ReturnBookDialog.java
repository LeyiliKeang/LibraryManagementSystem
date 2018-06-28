package groupwork.ui.dialog;

import com.sun.awt.AWTUtilities;
import groupwork.my.components.MyButton;
import groupwork.my.components.MyPanel;
import groupwork.sql.dao.BookDao;
import groupwork.sql.dao.BorrowedDao;
import groupwork.sql.model.Book;
import groupwork.sql.model.Borrowed;
import groupwork.sql.model.Reader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ReturnBookDialog extends JDialog {
    private JPanel contentPane;
    private MyPanel myPanel1;
    private MyButton buttonOK;
    private MyButton wrongButton;
    private MyButton buttonCancel;

    private static Point location = new Point();

    public ReturnBookDialog() {
        setUndecorated(true);
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

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
                AWTUtilities.setWindowOpacity(ReturnBookDialog.this, 0f);
                ActionListener listener = new ActionListener() {
                    float alpha = 0;

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (alpha < 0.8) {
                            AWTUtilities.setWindowOpacity(ReturnBookDialog.this, alpha += 0.2);
                        } else {
                            AWTUtilities.setWindowOpacity(ReturnBookDialog.this, 1);
                            Timer source = (Timer) e.getSource();
                            source.stop();
                        }
                    }
                };
                new Timer(50, listener).start();
            }
        });

        wrongButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int i = JOptionPane.showConfirmDialog(ReturnBookDialog.this, "确定损坏吗？", "提示", JOptionPane.YES_NO_OPTION);
                if (i == JOptionPane.YES_OPTION) {
                    BookDao bookDao = new BookDao();
                    try {
                        int price = bookDao.getOne(Book.getSelectID()).getPrice();
                        JOptionPane.showMessageDialog(ReturnBookDialog.this, "请赔款" + price + "元", "提示", JOptionPane.WARNING_MESSAGE);
                        BorrowedDao borrowedDao = new BorrowedDao();
                        borrowedDao.returnBook(Reader.getSelectID(), Book.getSelectID(), Borrowed.getSelectID(), false);

                        dispose();
                    } catch (ClassNotFoundException ex) {
                        ex.printStackTrace();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    private void onOK() {
        // add your code here
        String borrowDateString = Borrowed.getSelectID();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date borrowDate = sdf.parse(borrowDateString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(borrowDate);
            calendar.add(Calendar.MONTH, 3);
            long shouldReturn = calendar.getTimeInMillis();
            long returnDate = System.currentTimeMillis();
            long betweenDays = (returnDate - shouldReturn) / (1000 * 60 * 60 * 24);
            if (betweenDays > 0) {
                JOptionPane.showMessageDialog(this, "请缴费" + betweenDays * 0.2f + "元", "提示", JOptionPane.WARNING_MESSAGE);
                BorrowedDao borrowedDao = new BorrowedDao();
                try {
                    borrowedDao.returnBook(Reader.getSelectID(), Book.getSelectID(), Borrowed.getSelectID(), true);
                    JOptionPane.showMessageDialog(this, "还书成功", "提示", JOptionPane.WARNING_MESSAGE);
                    dispose();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "还书失败", "提示", JOptionPane.WARNING_MESSAGE);
                } catch (SQLException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "还书失败", "提示", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                BorrowedDao borrowedDao = new BorrowedDao();
                try {
                    borrowedDao.returnBook(Reader.getSelectID(), Book.getSelectID(), Borrowed.getSelectID(), true);
                    JOptionPane.showMessageDialog(this, "还书成功", "提示", JOptionPane.WARNING_MESSAGE);
                    dispose();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "还书失败", "提示", JOptionPane.WARNING_MESSAGE);
                } catch (SQLException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "还书失败", "提示", JOptionPane.WARNING_MESSAGE);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

//    public static void main(String[] args) {
//        ReturnBookDialog dialog = new ReturnBookDialog();
//        dialog.pack();
//        dialog.setVisible(true);
//        System.exit(0);
//    }
}
