package groupwork.ui.dialog;

import com.sun.awt.AWTUtilities;
import groupwork.my.MyMouseAdapter;
import groupwork.my.components.MyButton;
import groupwork.my.components.MyPanel;
import groupwork.my.model.MyTableModel;
import groupwork.sql.dao.BookDao;
import groupwork.sql.model.Book;
import groupwork.sql.model.BookType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

public class BookTableDialog extends JDialog {
    private JPanel contentPane;
    private MyPanel myPanel1;
    private MyPanel myPanel2;
    private MyButton buttonOK;
    private MyButton buttonCancel;
    private MyPanel myPanel3;
    private JScrollPane bookTableScrollPane;
    private JTable bookTable;
    private JLabel bookTableCountLabel;

    private static Point location = new Point();

    private MyTableModel BookDefaultTableModel = new MyTableModel();

    private int bookTableRow;

    public BookTableDialog() {
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
                AWTUtilities.setWindowOpacity(BookTableDialog.this, 0f);
                ActionListener listener = new ActionListener() {
                    float alpha = 0;

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (alpha < 0.8) {
                            AWTUtilities.setWindowOpacity(BookTableDialog.this, alpha += 0.2);
                        } else {
                            AWTUtilities.setWindowOpacity(BookTableDialog.this, 1);
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
//        BookTableDialog dialog = new BookTableDialog();
//        dialog.pack();
//        dialog.setVisible(true);
//        System.exit(0);
//    }

    private void initComponents() {
        BookDefaultTableModel.setColumnIdentifiers(new Object[]{"编号", "书名", "作者", "出版社"});
        bookTable.setModel(BookDefaultTableModel);
        bookTable.getTableHeader().setReorderingAllowed(false);
        bookTable.getTableHeader().setFont(new Font("微软雅黑", 0, 16));
        bookTableScrollPane.setViewportView(bookTable);
        bookTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookTable.addMouseListener(new MyMouseAdapter() {
            @Override
            public void mouseDoubleClicked(MouseEvent e) {
                bookTableRow = bookTable.rowAtPoint(e.getPoint());
                if (bookTableRow > -1) {
                    Book.setSelectID(bookTable.getValueAt(bookTable.getSelectedRow(), 0).toString());
                    BookInfoDialog bookInfoDialog = new BookInfoDialog();
                    bookInfoDialog.pack();
                    bookInfoDialog.setLocationRelativeTo(getContentPane());
                    bookInfoDialog.setVisible(true);
                }
            }
        });

        BookDao bookDao = new BookDao();
        java.util.List<Book> list = null;
        try {
            list = bookDao.getBookTypeBook(BookType.getSelectID());
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        for (Book b : list) {
            BookDefaultTableModel.addRow(new Object[]{b.getBookID(), b.getBookName(), b.getAuthor(), b.getPress()});
        }
        bookTableCountLabel.setText("一共有" + BookDefaultTableModel.getRowCount() + "条记录");
    }
}
