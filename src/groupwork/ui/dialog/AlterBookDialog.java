package groupwork.ui.dialog;

import com.sun.awt.AWTUtilities;
import groupwork.my.components.MyButton;
import groupwork.my.components.MyPanel;
import groupwork.my.document.RegexDocument;
import groupwork.sql.dao.BookDao;
import groupwork.sql.dao.BookTypeDao;
import groupwork.sql.model.Book;
import groupwork.sql.model.BookType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;

public class AlterBookDialog extends JDialog {
    private JPanel contentPane;
    private MyPanel myPanel1;
    private MyPanel myPanel2;
    private MyButton buttonOK;
    private MyButton buttonCancel;
    private MyPanel myPanel3;
    private JTextField numberTextField;
    private JTextField authorTextField;
    private JTextField existCountTextField;
    private JTextField pageTextField;
    private JTextField dateTextField;
    private JComboBox bookTypeComboBox;
    private JTextField priceTextField;
    private JTextField nameTextField;
    private JTextField pressTextField;

    private static Point location = new Point();
    private int existing;
    private int inventory;

    public AlterBookDialog() {
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
                AWTUtilities.setWindowOpacity(AlterBookDialog.this, 0f);
                ActionListener listener = new ActionListener() {
                    float alpha = 0;

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (alpha < 0.8) {
                            AWTUtilities.setWindowOpacity(AlterBookDialog.this, alpha += 0.2);
                        } else {
                            AWTUtilities.setWindowOpacity(AlterBookDialog.this, 1);
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
        if (numberTextField.getText().trim().matches("\\d{4}") && !nameTextField.getText().trim().equals("")
                && !authorTextField.getText().trim().equals("") && !pressTextField.getText().trim().equals("")
                && bookTypeComboBox.getSelectedIndex() != -1 && !priceTextField.getText().trim().equals("")
                && !pageTextField.getText().trim().equals("") && dateTextField.getText().trim().matches("\\d{4}-\\d{2}-\\d{2}")
                && !existCountTextField.getText().trim().equals("")) {
            BookTypeDao bookTypeDao = new BookTypeDao();
            BookDao bookDao = new BookDao();
            Book book = new Book();
            book.setBookID(numberTextField.getText().trim());
            book.setBookName(nameTextField.getText().trim());
            book.setAuthor(authorTextField.getText().trim());
            book.setPress(pressTextField.getText().trim());
            book.setPage(Integer.parseInt(pageTextField.getText().trim()));
            book.setPrice(Integer.parseInt(priceTextField.getText().trim()));

            int temp = Integer.parseInt(existCountTextField.getText().trim()) - existing;
            inventory += temp;

            book.setExisting(Integer.parseInt(existCountTextField.getText().trim()));
            book.setInventory(inventory);
            book.setStorageDate(dateTextField.getText().trim());
            try {
                String bookTypeID = bookTypeDao.getBookTypeID(bookTypeComboBox.getSelectedItem().toString());
                book.setBookType(bookTypeID);
                bookDao.updateBook(book, Book.getSelectID());
                JOptionPane.showMessageDialog(this, "修改成功", "提示", JOptionPane.WARNING_MESSAGE);
                dispose();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "修改失败", "提示", JOptionPane.WARNING_MESSAGE);
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "修改失败", "提示", JOptionPane.WARNING_MESSAGE);
            }
        }else {
            JOptionPane.showMessageDialog(this, "请正确填写图书信息", "提示", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

//    public static void main(String[] args) {
//        AlterBookDialog dialog = new AlterBookDialog();
//        dialog.pack();
//        dialog.setVisible(true);
//        System.exit(0);
//    }

    private void initComponents() {
        numberTextField.setDocument(new RegexDocument("\\d{0,4}"));
        priceTextField.setDocument(new RegexDocument("\\d+"));
        pageTextField.setDocument(new RegexDocument("\\d+"));
        existCountTextField.setDocument(new RegexDocument("\\d+"));

        BookDao bookDao = new BookDao();
        BookTypeDao bookTypeDao = new BookTypeDao();
        try {
            List<BookType> list = bookTypeDao.getBookType();
            for (BookType bookType : list) {
                bookTypeComboBox.addItem(bookType.getBookTypeName());
            }

            Book book = bookDao.getOne(Book.getSelectID());
            numberTextField.setText(book.getBookID());
            nameTextField.setText(book.getBookName());
            authorTextField.setText(book.getAuthor());
            pressTextField.setText(book.getPress());
            bookTypeComboBox.setSelectedItem(book.getBookType());
            pageTextField.setText(Integer.toString(book.getPage()));
            priceTextField.setText(Integer.toString(book.getPrice()));
            existing = book.getExisting();
            inventory = book.getInventory();
            existCountTextField.setText(Integer.toString(book.getExisting()));
            dateTextField.setText(book.getStorageDate());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
