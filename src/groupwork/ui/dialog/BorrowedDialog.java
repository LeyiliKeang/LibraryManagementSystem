package groupwork.ui.dialog;

import com.sun.awt.AWTUtilities;
import groupwork.my.MyMouseAdapter;
import groupwork.my.components.MyButton;
import groupwork.my.components.MyPanel;
import groupwork.my.model.MyTableModel;
import groupwork.sql.dao.BookDao;
import groupwork.sql.dao.BorrowedDao;
import groupwork.sql.dao.ReaderDao;
import groupwork.sql.model.Book;
import groupwork.sql.model.Borrowed;
import groupwork.sql.model.Reader;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BorrowedDialog extends JDialog {
    private JPanel contentPane;
    private MyPanel myPanel1;
    private MyPanel readerPane;
    private MyPanel myPanel4;
    private JScrollPane readerTableScrollPane;
    private JTable readerTable;
    private MyButton readerSearchButton;
    private JTextField readerSearchTextField;
    private JComboBox readerSearchComboBox;
    private MyPanel bookPane;
    private JScrollPane bookTableScrollPane;
    private JTable bookTable;
    private MyPanel myPanel3;
    private MyButton bookSearchButton;
    private JTextField bookSearchTextField;
    private JComboBox bookSearchComboBox;
    private MyPanel myPanel2;
    private MyButton buttonOK;
    private MyButton buttonCancel;
    private JLabel readerTableCountLabel;
    private JLabel bookTableCountLabel;

    private static Point location = new Point();

    private MyTableModel bookDefaultTableModel = new MyTableModel();
    private MyTableModel readerDefaultTableModel = new MyTableModel();

    private BookDao bookDao = new BookDao();
    private ReaderDao readerDao = new ReaderDao();

    private int bookTableRow;
    private int readerTableRow;

    public BorrowedDialog() {
        setUndecorated(true);
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        initBookPaneComponents();
        initReaderPaneComponents();

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
                AWTUtilities.setWindowOpacity(BorrowedDialog.this, 0f);
                ActionListener listener = new ActionListener() {
                    float alpha = 0;

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (alpha < 0.8) {
                            AWTUtilities.setWindowOpacity(BorrowedDialog.this, alpha += 0.2);
                        } else {
                            AWTUtilities.setWindowOpacity(BorrowedDialog.this, 1);
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
        int readerRow = readerTable.getSelectedRow();
        int bookRow = bookTable.getSelectedRow();
        if (readerRow == -1 || bookRow == -1) {
            JOptionPane.showMessageDialog(getContentPane(), "请选择", "提示", JOptionPane.WARNING_MESSAGE);
        } else {
            Reader.setSelectID(readerTable.getValueAt(readerTable.getSelectedRow(), 0).toString());
            Book.setSelectID(bookTable.getValueAt(bookTable.getSelectedRow(), 0).toString());
            try {
                Book book = bookDao.getOne(Book.getSelectID());
                BorrowedDao borrowedDao = new BorrowedDao();
                int bookNumber = book.getExisting();
                List<Reader> list = readerDao.getReaderBorrowedInfo(Reader.getSelectID());
                int borrowedNumber = list.get(0).getNotReturn();
                if (bookNumber > 0 && borrowedNumber < 5) {
                    Borrowed borrowed = new Borrowed();

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date borrowDate = new Date();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(borrowDate);
                    calendar.add(Calendar.MONTH, 3);
                    Date shouldReturnDate = calendar.getTime();

                    borrowed.setReaderID(Reader.getSelectID());
                    borrowed.setBookID(Book.getSelectID());
                    borrowed.setBorrowDate(sdf.format(borrowDate));
                    borrowed.setShouldReturnDate(sdf.format(shouldReturnDate));
                    borrowedDao.addBorrowed(borrowed);

                    JOptionPane.showMessageDialog(this, "借阅成功", "提示", JOptionPane.WARNING_MESSAGE);
                    dispose();
                } else {
                    if (borrowedNumber < 5) {
                        JOptionPane.showMessageDialog(this, "该书没有现存啦", "提示", JOptionPane.WARNING_MESSAGE);
                    }
                    if (bookNumber > 0) {
                        JOptionPane.showMessageDialog(this, "该同学已经借满五本书啦", "提示", JOptionPane.WARNING_MESSAGE);
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "借阅失败", "提示", JOptionPane.WARNING_MESSAGE);
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "借阅失败", "提示", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

//    public static void main(String[] args) {
//        BorrowedDialog dialog = new BorrowedDialog();
//        dialog.pack();
//        dialog.setVisible(true);
//        System.exit(0);
//    }

    private void initBookPaneComponents() {
        bookSearchButton.setIcon(new ImageIcon("imgs/search.png"));
        bookSearchButton.setBorder(null);

        //初始化图书信息表
        bookDefaultTableModel.setColumnIdentifiers(new Object[]{"编号", "书名", "作者", "出版社", "类型", "现存"});
        bookTable.setModel(bookDefaultTableModel);
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
                    bookInfoDialog.setLocationRelativeTo(BorrowedDialog.this);
                    bookInfoDialog.setVisible(true);
                }
            }
        });

        //从数据库读取图书信息并加载到图书表上
        bookInfoLoad();

        //监听下拉选项框选项的改变并进行数据的模糊查询加载到图书表中
        bookSearchComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    bookInfoLoad();
                }
            }
        });

        //监听搜索输入框文本的变化并进行数据的模糊查询加载到图书表中
        Document document = bookSearchTextField.getDocument();
        document.addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                bookInfoLoad();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                bookInfoLoad();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
    }

    private void initReaderPaneComponents() {
        readerSearchButton.setIcon(new ImageIcon("imgs/search.png"));
        readerSearchButton.setBorder(null);

        //初始化读者信息表
        readerDefaultTableModel.setColumnIdentifiers(new Object[]{"学号", "姓名", "性别", "区队", "电话"});
        readerTable.getTableHeader().setReorderingAllowed(false);
        readerTable.setModel(readerDefaultTableModel);
        readerTable.getTableHeader().setFont(new Font("微软雅黑", 0, 16));
        readerTableScrollPane.setViewportView(readerTable);
        readerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        readerTable.addMouseListener(new MyMouseAdapter() {
            @Override
            public void mouseDoubleClicked(MouseEvent e) {
                readerTableRow = readerTable.rowAtPoint(e.getPoint());
                if (readerTableRow > -1) {
                    Reader.setSelectID(readerTable.getValueAt(readerTable.getSelectedRow(), 0).toString());
                    ReaderInfoDialog readerInfoDialog = new ReaderInfoDialog();
                    readerInfoDialog.pack();
                    readerInfoDialog.setLocationRelativeTo(BorrowedDialog.this);
                    readerInfoDialog.setVisible(true);
                }
            }
        });

        readerInfoLoad();

        readerSearchComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    readerInfoLoad();
                }
            }
        });

        Document document = readerSearchTextField.getDocument();
        document.addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                readerInfoLoad();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                readerInfoLoad();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
    }

    private void bookInfoLoad() {
        bookDefaultTableModel.setRowCount(0);
        java.util.List<Book> list = null;
        try {
            list = bookDao.fuzzyQuery(bookSearchComboBox.getSelectedItem().toString(), bookSearchTextField.getText().trim());
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        for (Book b : list) {
            bookDefaultTableModel.addRow(new Object[]{b.getBookID(), b.getBookName(), b.getAuthor(), b.getPress(), b.getBookType(), b.getExisting()});
        }
        bookTableCountLabel.setText("一共有" + bookDefaultTableModel.getRowCount() + "条记录");
    }

    private void readerInfoLoad() {
        readerDefaultTableModel.setRowCount(0);
        java.util.List<Reader> list = null;
        try {
            list = readerDao.fuzzyQuery(readerSearchComboBox.getSelectedItem().toString(), readerSearchTextField.getText().trim());
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        for (Reader r : list) {
            readerDefaultTableModel.addRow(new Object[]{r.getReaderID(), r.getReaderName(), r.getReaderSex(), r.getReaderClass(), r.getReaderPhone()});
        }
        readerTableCountLabel.setText("一共有" + readerDefaultTableModel.getRowCount() + "条记录");
    }
}
