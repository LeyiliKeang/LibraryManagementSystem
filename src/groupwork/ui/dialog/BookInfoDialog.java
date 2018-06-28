package groupwork.ui.dialog;

import com.sun.awt.AWTUtilities;
import groupwork.my.components.MyButton;
import groupwork.my.components.MyPanel;
import groupwork.sql.dao.BookDao;
import groupwork.sql.model.Book;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

public class BookInfoDialog extends JDialog {
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
    private JTextField countTextField;

    private static Point location = new Point();

    public BookInfoDialog() {
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
                AWTUtilities.setWindowOpacity(BookInfoDialog.this, 0f);
                ActionListener listener = new ActionListener() {
                    float alpha = 0;

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (alpha < 0.8) {
                            AWTUtilities.setWindowOpacity(BookInfoDialog.this, alpha += 0.2);
                        } else {
                            AWTUtilities.setWindowOpacity(BookInfoDialog.this, 1);
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
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

//    public static void main(String[] args) {
//        BookInfoDialog dialog = new BookInfoDialog();
//        dialog.pack();
//        dialog.setVisible(true);
//        System.exit(0);
//    }

    private void initComponents() {
//        numberTextField.setDocument(new RegexDocument("\\d{0,10}"));
//        priceTextField.setDocument(new RegexDocument("\\d{0,}"));
//        pageTextField.setDocument(new RegexDocument("\\d{0,}"));
//        existCountTextField.setDocument(new RegexDocument("\\d{0,}"));

        BookDao bookDao = new BookDao();
        try {
            Book book = bookDao.getOne(Book.getSelectID());
            numberTextField.setText(book.getBookID());
            nameTextField.setText(book.getBookName());
            authorTextField.setText(book.getAuthor());
            pressTextField.setText(book.getPress());
            bookTypeComboBox.addItem(book.getBookType());
            pageTextField.setText(Integer.toString(book.getPage()));
            priceTextField.setText(Integer.toString(book.getPrice()));
            existCountTextField.setText(Integer.toString(book.getExisting()));
            countTextField.setText(Integer.toString(book.getInventory()));
            dateTextField.setText(book.getStorageDate());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
