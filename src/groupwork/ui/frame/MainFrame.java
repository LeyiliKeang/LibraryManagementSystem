package groupwork.ui.frame;

import com.sun.awt.AWTUtilities;
import groupwork.my.MyMouseAdapter;
import groupwork.my.components.ClockLabel;
import groupwork.my.model.MyTableModel;
import groupwork.my.components.MyButton;
import groupwork.my.components.MyPanel;
import groupwork.sql.dao.*;
import groupwork.sql.model.Book;
import groupwork.sql.model.Borrowed;
import groupwork.sql.model.Manager;
import groupwork.sql.model.Reader;
import groupwork.ui.dialog.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainFrame extends JFrame {
    private JPanel contentPane;
    private MyPanel maxPane;
    private MyPanel topPane;
    private MyPanel tabPane;
    private MyPanel bookPane;
    private MyPanel readerPane;
    private MyPanel borrowedPane;
    private MyPanel managerPane;
    private MyPanel myPanel1;
    private MyPanel myPanel2;
    private JTable bookTable;
    private MyPanel myPanel3;
    private MyButton bookSearchButton;
    private JTextField bookSearchTextField;
    private JComboBox bookSearchComboBox;
    private JScrollPane bookTableScrollPane;
    private MyButton bookTypeButton;
    private MyButton deleteBookButton;
    private MyButton alterBookButton;
    private MyButton addBookButton;
    private MyPanel myPanel4;
    private MyPanel myPanel5;
    private JTable readerTable;
    private MyButton addReaderButton;
    private MyButton deleteReaderButton;
    private MyButton alterReaderButton;
    private MyPanel myPanel6;
    private MyPanel myPanel7;
    private JTable borrowedTable;
    private MyButton borrowBookButton;
    private MyButton returnBookButton;
    private MyPanel myPanel8;
    private MyPanel myPanel9;
    private JTable managerTable;
    private MyButton addManagerButton;
    private MyButton deleteManagerButton;
    private MyButton alterManagerButton;
    private MyButton readerSearchButton;
    private MyButton borrowedSearchButton;
    private MyButton managerSearchButton;
    private JScrollPane readerTableScrollPane;
    private JScrollPane borrowedTableScrollPane;
    private JScrollPane managerTableScrollPane;
    private JTextField readerSearchTextField;
    private JComboBox readerSearchComboBox;
    private JComboBox borrowedSearchComboBox;
    private JTextField borrowedSearchTextField;
    private JComboBox managerSearchComboBox;
    private JTextField managerSearchTextField;
    private JTabbedPane tabbedPane;
    private MyPanel rankPane;
    private MyPanel myPanel11;
    private MyPanel myPanel12;
    private MyPanel myPanel13;
    private JTable readerBorrowedInfoTable;
    private JTable bookBorrowedInfoTable;
    private MyPanel myPanel10;
    private JTextField toDateTextField;
    private JTextField fromDateTextField;
    private MyButton rankButton;
    private JScrollPane readerBorrowedInfoTableScrollPane;
    private JScrollPane bookBorrowedInfoTableScrollPane;
    private JLabel bookTableCountLabel;
    private JLabel readerTableCountLabel;
    private JLabel borrowedTableCountLabel;
    private JLabel readerBorrowedInfoTableCountLabel;
    private JLabel bookBorrowedInfoTableCountLabel;
    private MyPanel myPanel14;
    private MyPanel clockPane;
    private ClockLabel clockLabel;
    private JLabel managerTableCountLabel;
    private MyButton renewBookButton;
    private JToolBar menuToolBar;
    private MyButton startButton;
    private MyButton helpButton;

    private static Point location = new Point();
    private int tabSelect = 0;//用于JTabbedPane选项卡的点击事件

    private MyTableModel bookDefaultTableModel = new MyTableModel();//自定义TableModel不允许编辑
    private MyTableModel readerDefaultTableModel = new MyTableModel();
    private MyTableModel borrowedDefaultTableModel = new MyTableModel();
    private MyTableModel managerDefaultTableModel = new MyTableModel();
    private MyTableModel readerBorrowedInfoTableModel = new MyTableModel();
    private MyTableModel bookBorrowedInfoTableModel = new MyTableModel();

    private BookDao bookDao = new BookDao();
    private ReaderDao readerDao = new ReaderDao();
    private BorrowedDao borrowedDao = new BorrowedDao();
    private ReaderBorrowedDao readerBorrowedDao = new ReaderBorrowedDao();
    private BookBorrowedDao bookBorrowedDao = new BookBorrowedDao();
    private ManagerDao managerDao = new ManagerDao();

    private int bookTableRow;
    private int readerTableRow;
    private int readerBorrowedInfoTableRow;
    private int bookBorrowedInfoTableRow;
    private int borrowedTableRow;

    public MainFrame() {
        initComponents();//初始化组件
        initBookPaneComponents();//初始化图书信息页面组件
        initReaderPaneComponents();//初始化读者信息页面组件
        initBorrowedPaneComponents();//初始化借阅信息页面组件
        initRankPaneComponents();
        initManagerPaneComponents();//初始化管理员信息页面组件

        setIconImage(Toolkit.getDefaultToolkit().getImage("imgs/Books.png"));
        setUndecorated(true);
        setContentPane(contentPane);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);

        //添加窗口打开时透明度变化的效果
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                AWTUtilities.setWindowOpacity(MainFrame.this, 0f);
                ActionListener listener = new ActionListener() {
                    float alpha = 0;

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (alpha < 0.9) {
                            AWTUtilities.setWindowOpacity(MainFrame.this, alpha += 0.1);
                        } else {
                            AWTUtilities.setWindowOpacity(MainFrame.this, 1);
                            Timer source = (Timer) e.getSource();
                            source.stop();
                        }
                    }
                };
                new Timer(50, listener).start();
            }
        });
    }

//    public static void main(String[] args) {
//        //获取与系统匹配的主题
//        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        new MainFrame().setVisible(true);
//    }

    //初始化图书信息面板
    private void initBookPaneComponents() {
//        bookSearchButton.setIcon(new ImageIcon("imgs/search.png"));
//        bookSearchButton.setBorder(null);

        //初始化图书信息表
        bookDefaultTableModel.setColumnIdentifiers(new Object[]{"编号", "书名", "作者", "出版社", "类型", "现存", "入库时间"});
        bookTable.setModel(bookDefaultTableModel);
        bookTable.getTableHeader().setReorderingAllowed(false);
        bookTable.getTableHeader().setFont(new Font("微软雅黑", 0, 16));
        bookTableScrollPane.setViewportView(bookTable);
        bookTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);//将表设置为只能单选

        //为图书信息表添加双击事件，以显示图书的所有信息
        bookTable.addMouseListener(new MyMouseAdapter() {
            @Override
            public void mouseDoubleClicked(MouseEvent e) {
                bookTableRow = bookTable.rowAtPoint(e.getPoint());
                if (bookTableRow > -1) {
                    Book.setSelectID(bookTable.getValueAt(bookTable.getSelectedRow(), 0).toString());
                    BookInfoDialog bookInfoDialog = new BookInfoDialog();
                    bookInfoDialog.pack();
                    bookInfoDialog.setLocationRelativeTo(MainFrame.this);
                    bookInfoDialog.setVisible(true);
                }
            }
        });

        //从数据库读取图书信息并加载到图书表上
        bookInfoLoad();

        //添加图书类型按钮事件
        bookTypeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BookTypeFrame bookTypeFrame = new BookTypeFrame();
                bookTypeFrame.setLocationRelativeTo(MainFrame.this);
                bookTypeFrame.setVisible(true);
            }
        });

        //添加图书按钮事件
        addBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddBookDialog addBookDialog = new AddBookDialog();
                addBookDialog.pack();
                addBookDialog.setLocationRelativeTo(MainFrame.this);
                addBookDialog.setVisible(true);
                bookInfoLoad();
            }
        });

        //修改图书按钮事件
        alterBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = bookTable.getSelectedRow();
                if (row == -1) {
                    JOptionPane.showMessageDialog(getContentPane(), "请选择", "提示", JOptionPane.WARNING_MESSAGE);
                } else {
                    Book.setSelectID(bookTable.getValueAt(bookTable.getSelectedRow(), 0).toString());
                    AlterBookDialog alterBookDialog = new AlterBookDialog();
                    alterBookDialog.pack();
                    alterBookDialog.setLocationRelativeTo(MainFrame.this);
                    alterBookDialog.setVisible(true);
                    bookInfoLoad();
                }
            }
        });

        //删除图书按钮事件
        deleteBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = bookTable.getSelectedRow();
                if (row == -1) {
                    JOptionPane.showMessageDialog(getContentPane(), "请选择", "提示", JOptionPane.WARNING_MESSAGE);
                } else {
                    Book.setSelectID(bookTable.getValueAt(bookTable.getSelectedRow(), 0).toString());
                    int i = JOptionPane.showConfirmDialog(MainFrame.this, "确认删除吗？", "提示", JOptionPane.YES_NO_OPTION);
                    if (i == JOptionPane.YES_OPTION) {
                        try {
                            bookDao.deleteBook(Book.getSelectID());
                            JOptionPane.showMessageDialog(MainFrame.this, "删除成功", "提示", JOptionPane.WARNING_MESSAGE);
                            bookInfoLoad();
                        } catch (ClassNotFoundException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(MainFrame.this, "删除失败", "提示", JOptionPane.WARNING_MESSAGE);
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(MainFrame.this, "删除失败", "提示", JOptionPane.WARNING_MESSAGE);
                        }
                    }
                }
            }
        });

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

    //初始化读者信息面板
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
                    readerInfoDialog.setLocationRelativeTo(MainFrame.this);
                    readerInfoDialog.setVisible(true);
                }
            }
        });

        readerInfoLoad();

        addReaderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddReaderDialog addReaderDialog = new AddReaderDialog();
                addReaderDialog.pack();
                addReaderDialog.setLocationRelativeTo(MainFrame.this);
                addReaderDialog.setVisible(true);
                readerInfoLoad();
            }
        });

        alterReaderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = readerTable.getSelectedRow();
                if (row == -1) {
                    JOptionPane.showMessageDialog(getContentPane(), "请选择", "提示", JOptionPane.WARNING_MESSAGE);
                } else {
                    Reader.setSelectID(readerTable.getValueAt(readerTable.getSelectedRow(), 0).toString());
                    AlterReaderDialog alterReaderDialog = new AlterReaderDialog();
                    alterReaderDialog.pack();
                    alterReaderDialog.setLocationRelativeTo(MainFrame.this);
                    alterReaderDialog.setVisible(true);
                    readerInfoLoad();
                }
            }
        });

        deleteReaderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = readerTable.getSelectedRow();
                if (row == -1) {
                    JOptionPane.showMessageDialog(getContentPane(), "请选择", "提示", JOptionPane.WARNING_MESSAGE);
                } else {
                    Reader.setSelectID(readerTable.getValueAt(readerTable.getSelectedRow(), 0).toString());
                    int i = JOptionPane.showConfirmDialog(MainFrame.this, "确认删除吗？", "提示", JOptionPane.YES_NO_OPTION);
                    if (i == JOptionPane.YES_OPTION) {
                        try {
                            readerDao.deleteReader(Reader.getSelectID());
                            JOptionPane.showMessageDialog(MainFrame.this, "删除成功", "提示", JOptionPane.WARNING_MESSAGE);
                            readerInfoLoad();
                        } catch (ClassNotFoundException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(MainFrame.this, "删除失败", "提示", JOptionPane.WARNING_MESSAGE);
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(MainFrame.this, "删除失败", "提示", JOptionPane.WARNING_MESSAGE);
                        }
                    }
                }
            }
        });

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

    private void initBorrowedPaneComponents() {
        borrowedSearchButton.setIcon(new ImageIcon("imgs/search.png"));
        borrowedSearchButton.setBorder(null);

        //初始化借阅信息表
        borrowedDefaultTableModel.setColumnIdentifiers(new Object[]{"学号", "姓名", "图书编号", "图书名称", "借书日期", "应还书日期", "实还书日期"});
        borrowedTable.getTableHeader().setReorderingAllowed(false);
        borrowedTable.setModel(borrowedDefaultTableModel);
        borrowedTable.getTableHeader().setFont(new Font("微软雅黑", 0, 16));
        borrowedTableScrollPane.setViewportView(borrowedTable);
        borrowedTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        borrowedTable.addMouseListener(new MyMouseAdapter() {
            @Override
            public void mouseDoubleClicked(MouseEvent e) {
                borrowedTableRow = borrowedTable.rowAtPoint(e.getPoint());
                if (borrowedTableRow > -1) {
                    Reader.setSelectID(borrowedTable.getValueAt(borrowedTable.getSelectedRow(), 0).toString());
                    OneReaderBorrowedInfoDialog oneReaderBorrowedInfoDialog = new OneReaderBorrowedInfoDialog();
                    oneReaderBorrowedInfoDialog.pack();
                    oneReaderBorrowedInfoDialog.setLocationRelativeTo(MainFrame.this);
                    oneReaderBorrowedInfoDialog.setVisible(true);
                }
            }
        });

        borrowedInfoLoad();

        borrowBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BorrowedDialog borrowedDialog = new BorrowedDialog();
                borrowedDialog.pack();
                borrowedDialog.setLocationRelativeTo(MainFrame.this);
                borrowedDialog.setVisible(true);
                borrowedInfoLoad();
            }
        });

        renewBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = borrowedTable.getSelectedRow();
                if (row == -1) {
                    JOptionPane.showMessageDialog(getContentPane(), "请选择", "提示", JOptionPane.WARNING_MESSAGE);
                } else {
                    if (borrowedTable.getValueAt(borrowedTable.getSelectedRow(), 6) == null) {
                        Reader.setSelectID(borrowedTable.getValueAt(borrowedTable.getSelectedRow(), 0).toString());
                        Book.setSelectID(borrowedTable.getValueAt(borrowedTable.getSelectedRow(), 2).toString());
                        Borrowed.setSelectID(borrowedTable.getValueAt(borrowedTable.getSelectedRow(), 4).toString());
                        int i = JOptionPane.showConfirmDialog(getContentPane(), "确定续借吗？", "提示", JOptionPane.YES_NO_OPTION);
                        if (i == JOptionPane.YES_OPTION) {
                            String borrowDateString = Borrowed.getSelectID();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            try {
                                java.util.Date borrowDate = sdf.parse(borrowDateString);
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(borrowDate);
                                calendar.add(Calendar.MONTH, 3);
                                long shouldReturn = calendar.getTimeInMillis();
                                long returnDate = System.currentTimeMillis();
                                long betweenDays = (returnDate - shouldReturn) / (1000 * 60 * 60 * 24);
                                if (betweenDays > 0) {
                                    JOptionPane.showMessageDialog(getContentPane(), "请缴费" + betweenDays * 0.2f + "元", "提示", JOptionPane.WARNING_MESSAGE);
                                    try {
                                        borrowedDao.returnBook(Reader.getSelectID(), Book.getSelectID(), Borrowed.getSelectID(), true);

                                        java.util.Date newBorrowDate = new java.util.Date();
                                        Calendar newCalendar = Calendar.getInstance();
                                        newCalendar.setTime(newBorrowDate);
                                        newCalendar.add(Calendar.MONTH, 3);
                                        java.util.Date newShouldReturnDate = newCalendar.getTime();

                                        Borrowed borrowed = new Borrowed();
                                        borrowed.setReaderID(Reader.getSelectID());
                                        borrowed.setBookID(Book.getSelectID());
                                        borrowed.setBorrowDate(sdf.format(newBorrowDate));
                                        borrowed.setShouldReturnDate(sdf.format(newShouldReturnDate));
                                        borrowedDao.addBorrowed(borrowed);
                                        JOptionPane.showMessageDialog(getContentPane(), "续借成功", "提示", JOptionPane.WARNING_MESSAGE);
                                    } catch (ClassNotFoundException ex) {
                                        ex.printStackTrace();
                                        JOptionPane.showMessageDialog(getContentPane(), "续借失败", "提示", JOptionPane.WARNING_MESSAGE);
                                    } catch (SQLException ex) {
                                        ex.printStackTrace();
                                        JOptionPane.showMessageDialog(getContentPane(), "续借失败", "提示", JOptionPane.WARNING_MESSAGE);
                                    }
                                } else {
                                    try {
                                        borrowedDao.returnBook(Reader.getSelectID(), Book.getSelectID(), Borrowed.getSelectID(), true);

                                        java.util.Date newBorrowDate = new java.util.Date();
                                        Calendar newCalendar = Calendar.getInstance();
                                        newCalendar.setTime(newBorrowDate);
                                        newCalendar.add(Calendar.MONTH, 3);
                                        java.util.Date newShouldReturnDate = newCalendar.getTime();

                                        Borrowed borrowed = new Borrowed();
                                        borrowed.setReaderID(Reader.getSelectID());
                                        borrowed.setBookID(Book.getSelectID());
                                        borrowed.setBorrowDate(sdf.format(newBorrowDate));
                                        borrowed.setShouldReturnDate(sdf.format(newShouldReturnDate));
                                        borrowedDao.addBorrowed(borrowed);
                                        JOptionPane.showMessageDialog(getContentPane(), "续借成功", "提示", JOptionPane.WARNING_MESSAGE);
                                    } catch (ClassNotFoundException ex) {
                                        ex.printStackTrace();
                                        JOptionPane.showMessageDialog(getContentPane(), "续借失败", "提示", JOptionPane.WARNING_MESSAGE);
                                    } catch (SQLException ex) {
                                        ex.printStackTrace();
                                        JOptionPane.showMessageDialog(getContentPane(), "续借失败", "提示", JOptionPane.WARNING_MESSAGE);
                                    }
                                }
                            } catch (ParseException ex) {
                                ex.printStackTrace();
                            }
                        }
                        borrowedInfoLoad();
                    } else {
                        JOptionPane.showMessageDialog(getContentPane(), "该书已经还啦", "提示", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        });

        returnBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = borrowedTable.getSelectedRow();
                if (row == -1) {
                    JOptionPane.showMessageDialog(getContentPane(), "请选择", "提示", JOptionPane.WARNING_MESSAGE);
                } else {
                    if (borrowedTable.getValueAt(borrowedTable.getSelectedRow(), 6) == null) {
                        Reader.setSelectID(borrowedTable.getValueAt(borrowedTable.getSelectedRow(), 0).toString());
                        Book.setSelectID(borrowedTable.getValueAt(borrowedTable.getSelectedRow(), 2).toString());
                        Borrowed.setSelectID(borrowedTable.getValueAt(borrowedTable.getSelectedRow(), 4).toString());
                        ReturnBookDialog returnBookDialog = new ReturnBookDialog();
                        returnBookDialog.pack();
                        returnBookDialog.setLocationRelativeTo(MainFrame.this);
                        returnBookDialog.setVisible(true);
                        borrowedInfoLoad();
                    } else {
                        JOptionPane.showMessageDialog(getContentPane(), "该书已经还啦", "提示", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        });

        borrowedSearchComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    borrowedInfoLoad();
                }
            }
        });

        Document document = borrowedSearchTextField.getDocument();
        document.addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                borrowedInfoLoad();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                borrowedInfoLoad();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
    }

    private void initRankPaneComponents() {
        readerBorrowedInfoTableModel.setColumnIdentifiers(new Object[]{"排名", "学号", "姓名", "性别", "班级", "借阅次数"});
        readerBorrowedInfoTable.getTableHeader().setReorderingAllowed(false);
        readerBorrowedInfoTable.setModel(readerBorrowedInfoTableModel);
        readerBorrowedInfoTable.getTableHeader().setFont(new Font("微软雅黑", 0, 16));
        readerBorrowedInfoTableScrollPane.setViewportView(readerBorrowedInfoTable);
        readerBorrowedInfoTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        readerBorrowedInfoTable.addMouseListener(new MyMouseAdapter() {
            @Override
            public void mouseDoubleClicked(MouseEvent e) {
                readerBorrowedInfoTableRow = readerBorrowedInfoTable.rowAtPoint(e.getPoint());
                if (readerBorrowedInfoTableRow > -1) {
                    Reader.setSelectID(readerBorrowedInfoTable.getValueAt(readerBorrowedInfoTable.getSelectedRow(), 1).toString());
                    OneReaderBorrowedInfoDialog oneReaderBorrowedInfoDialog = new OneReaderBorrowedInfoDialog();
                    oneReaderBorrowedInfoDialog.pack();
                    oneReaderBorrowedInfoDialog.setLocationRelativeTo(MainFrame.this);
                    oneReaderBorrowedInfoDialog.setVisible(true);
                }
            }
        });

        readerBorrowedInfoLoad();

        bookBorrowedInfoTableModel.setColumnIdentifiers(new Object[]{"排名", "编号", "书名", "作者", "出版社", "类型", "借阅次数"});
        bookBorrowedInfoTable.getTableHeader().setReorderingAllowed(false);
        bookBorrowedInfoTable.setModel(bookBorrowedInfoTableModel);
        bookBorrowedInfoTable.getTableHeader().setFont(new Font("微软雅黑", 0, 16));
        bookBorrowedInfoTableScrollPane.setViewportView(bookBorrowedInfoTable);
        bookBorrowedInfoTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookBorrowedInfoTable.addMouseListener(new MyMouseAdapter() {
            @Override
            public void mouseDoubleClicked(MouseEvent e) {
                bookBorrowedInfoTableRow = bookBorrowedInfoTable.rowAtPoint(e.getPoint());
                if (bookBorrowedInfoTableRow > -1) {
                    Book.setSelectID(bookBorrowedInfoTable.getValueAt(bookBorrowedInfoTable.getSelectedRow(), 1).toString());
                    OneBookBorrowedInfoDialog oneBookBorrowedInfoDialog = new OneBookBorrowedInfoDialog();
                    oneBookBorrowedInfoDialog.pack();
                    oneBookBorrowedInfoDialog.setLocationRelativeTo(MainFrame.this);
                    oneBookBorrowedInfoDialog.setVisible(true);
                }
            }
        });

        bookBorrowedInfoLoad();

        rankButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                readerBorrowedInfoLoad();
                bookBorrowedInfoLoad();
            }
        });
    }

    private void initManagerPaneComponents() {
//        Manager.setManagerPower("0");
        if (Manager.getManagerPower().equals("0")) {
            addManagerButton.setVisible(false);
            deleteManagerButton.setVisible(false);
            managerSearchComboBox.setEnabled(false);
            managerSearchTextField.setEnabled(false);
        }
        managerSearchButton.setIcon(new ImageIcon("imgs/search.png"));
        managerSearchButton.setBorder(null);

        clockPane.setIcon(new ImageIcon("imgs/clockbg1.png"));
        new Thread(clockLabel).start();

        //初始化管理员信息表
        managerDefaultTableModel.setColumnIdentifiers(new Object[]{"身份证号", "姓名", "电话"});
        managerTable.getTableHeader().setReorderingAllowed(false);
        managerTable.setModel(managerDefaultTableModel);
        managerTable.getTableHeader().setFont(new Font("微软雅黑", 0, 16));
        managerTableScrollPane.setViewportView(managerTable);
        managerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        managerInfoLoad();

        addManagerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddManagerDialog addManagerDialog = new AddManagerDialog();
                addManagerDialog.pack();
                addManagerDialog.setLocationRelativeTo(MainFrame.this);
                addManagerDialog.setVisible(true);
                managerInfoLoad();
            }
        });

        alterManagerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = managerTable.getSelectedRow();
                if (row == -1) {
                    JOptionPane.showMessageDialog(getContentPane(), "请选择", "提示", JOptionPane.WARNING_MESSAGE);
                } else {
                    Manager.setSelectID(managerTable.getValueAt(managerTable.getSelectedRow(), 0).toString());
                    AlterManagerDialog alterManagerDialog = new AlterManagerDialog();
                    alterManagerDialog.pack();
                    alterManagerDialog.setLocationRelativeTo(MainFrame.this);
                    alterManagerDialog.setVisible(true);
                    managerInfoLoad();
                }
            }
        });

        deleteManagerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = managerTable.getSelectedRow();
                if (row == -1) {
                    JOptionPane.showMessageDialog(getContentPane(), "请选择", "提示", JOptionPane.WARNING_MESSAGE);
                } else {
                    int n = JOptionPane.showConfirmDialog(getContentPane(), "确认删除吗？", "删除对话框", JOptionPane.YES_NO_OPTION);
                    if (n == JOptionPane.YES_OPTION) { // 如果用户确认信息
                        try {
                            managerDao.deleteManager(managerTable.getValueAt(row, 0).toString());
                            JOptionPane.showMessageDialog(MainFrame.this, "删除成功", "提示", JOptionPane.WARNING_MESSAGE);
                            managerInfoLoad();
                        } catch (ClassNotFoundException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(MainFrame.this, "删除失败", "提示", JOptionPane.WARNING_MESSAGE);
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(MainFrame.this, "删除失败", "提示", JOptionPane.WARNING_MESSAGE);
                        }
                    }
                }
            }
        });

        managerSearchComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    managerInfoLoad();
                }
            }
        });

        Document document = managerSearchTextField.getDocument();
        document.addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                managerInfoLoad();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                managerInfoLoad();
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
            bookDefaultTableModel.addRow(new Object[]{b.getBookID(), b.getBookName(), b.getAuthor(), b.getPress(), b.getBookType(), b.getExisting(), b.getStorageDate()});
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

    private void borrowedInfoLoad() {
        borrowedDefaultTableModel.setRowCount(0);
        java.util.List<Borrowed> list = null;
        try {
            list = borrowedDao.fuzzyQuery(borrowedSearchComboBox.getSelectedItem().toString(), borrowedSearchTextField.getText().trim());
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        for (Borrowed b : list) {
            borrowedDefaultTableModel.addRow(new Object[]{b.getReaderID(), b.getReaderName(), b.getBookID(), b.getBookName(), b.getBorrowDate(), b.getShouldReturnDate(), b.getReturnDate()});
        }
        borrowedTableCountLabel.setText("一共有" + borrowedDefaultTableModel.getRowCount() + "条记录");
    }

    private void managerInfoLoad() {
        managerDefaultTableModel.setRowCount(0);
        if (Manager.getManagerPower().equals("0")) {
            try {
                Manager manager = managerDao.getOne(Manager.getIdNumber());
                managerDefaultTableModel.addRow(new Object[]{manager.getManagerIDNumber(), manager.getManagerName(), manager.getManagerPhone()});
                managerTableCountLabel.setText("一共有" + managerDefaultTableModel.getRowCount() + "条记录");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            java.util.List<Manager> list = null;
            try {
                list = managerDao.fuzzyQuery(managerSearchComboBox.getSelectedItem().toString(), managerSearchTextField.getText().trim());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            for (Manager manager : list) {
                managerDefaultTableModel.addRow(new Object[]{manager.getManagerIDNumber(), manager.getManagerName(), manager.getManagerPhone()});
            }
            managerTableCountLabel.setText("一共有" + managerDefaultTableModel.getRowCount() + "条记录");
        }
    }

    private void readerBorrowedInfoLoad() {
        readerBorrowedInfoTableModel.setRowCount(0);
        java.util.List<Reader> list = null;
        if (fromDateTextField.getText().trim().equals("") && toDateTextField.getText().trim().equals("")) {
            try {
                list = readerBorrowedDao.getDateReaderBorrowedInfo(null, null);
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(getContentPane(), "请输入格式正确的日期", "提示", JOptionPane.WARNING_MESSAGE);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(getContentPane(), "请输入格式正确的日期", "提示", JOptionPane.WARNING_MESSAGE);
            }
        }
        if (!fromDateTextField.getText().trim().equals("") && toDateTextField.getText().trim().equals("")) {
            try {
                list = readerBorrowedDao.getDateReaderBorrowedInfo(fromDateTextField.getText().trim(), null);
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(getContentPane(), "请输入格式正确的日期", "提示", JOptionPane.WARNING_MESSAGE);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(getContentPane(), "请输入格式正确的日期", "提示", JOptionPane.WARNING_MESSAGE);
            }
        }
        if (fromDateTextField.getText().trim().equals("") && !toDateTextField.getText().trim().equals("")) {
            try {
                list = readerBorrowedDao.getDateReaderBorrowedInfo(null, toDateTextField.getText().trim());
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(getContentPane(), "请输入格式正确的日期", "提示", JOptionPane.WARNING_MESSAGE);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(getContentPane(), "请输入格式正确的日期", "提示", JOptionPane.WARNING_MESSAGE);
            }
        }
        if (!fromDateTextField.getText().trim().equals("") && !toDateTextField.getText().trim().equals("")) {
            try {
                list = readerBorrowedDao.getDateReaderBorrowedInfo(fromDateTextField.getText().trim(), toDateTextField.getText().trim());
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(getContentPane(), "请输入格式正确的日期", "提示", JOptionPane.WARNING_MESSAGE);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(getContentPane(), "请输入格式正确的日期", "提示", JOptionPane.WARNING_MESSAGE);
            }
        }
        int rank = 0;
        int lastReaderCount = 0;
        int readerCount = 0;
        for (Reader r : list) {
            readerCount = r.getCount();
            if (readerCount == lastReaderCount) {
                readerBorrowedInfoTableModel.addRow(new Object[]{rank, r.getReaderID(), r.getReaderName(), r.getReaderSex(), r.getReaderClass(), r.getCount()});
            } else {
                readerBorrowedInfoTableModel.addRow(new Object[]{++rank, r.getReaderID(), r.getReaderName(), r.getReaderSex(), r.getReaderClass(), r.getCount()});
            }
            lastReaderCount = readerCount;
        }
        readerBorrowedInfoTableCountLabel.setText("一共有" + readerBorrowedInfoTableModel.getRowCount() + "条记录");
    }

    private void bookBorrowedInfoLoad() {
        bookBorrowedInfoTableModel.setRowCount(0);
        java.util.List<Book> list = null;
        if (fromDateTextField.getText().trim().equals("") && toDateTextField.getText().trim().equals("")) {
            try {
                list = bookBorrowedDao.getDateBookBorrowedInfo(null, null);
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(getContentPane(), "请输入格式正确的日期", "提示", JOptionPane.WARNING_MESSAGE);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(getContentPane(), "请输入格式正确的日期", "提示", JOptionPane.WARNING_MESSAGE);
            }
        }
        if (!fromDateTextField.getText().trim().equals("") && toDateTextField.getText().trim().equals("")) {
            try {
                list = bookBorrowedDao.getDateBookBorrowedInfo(fromDateTextField.getText().trim(), null);
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(getContentPane(), "请输入格式正确的日期", "提示", JOptionPane.WARNING_MESSAGE);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(getContentPane(), "请输入格式正确的日期", "提示", JOptionPane.WARNING_MESSAGE);
            }
        }
        if (fromDateTextField.getText().trim().equals("") && !toDateTextField.getText().trim().equals("")) {
            try {
                list = bookBorrowedDao.getDateBookBorrowedInfo(null, toDateTextField.getText().trim());
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(getContentPane(), "请输入格式正确的日期", "提示", JOptionPane.WARNING_MESSAGE);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(getContentPane(), "请输入格式正确的日期", "提示", JOptionPane.WARNING_MESSAGE);
            }
        }
        if (!fromDateTextField.getText().trim().equals("") && !toDateTextField.getText().trim().equals("")) {
            try {
                list = bookBorrowedDao.getDateBookBorrowedInfo(fromDateTextField.getText().trim(), toDateTextField.getText().trim());
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(getContentPane(), "请输入格式正确的日期", "提示", JOptionPane.WARNING_MESSAGE);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(getContentPane(), "请输入格式正确的日期", "提示", JOptionPane.WARNING_MESSAGE);
            }
        }
        int rank = 0;
        int lastReaderCount = 0;
        int readerCount = 0;
        for (Book b : list) {
            readerCount = b.getCount();
            if (readerCount == lastReaderCount) {
                bookBorrowedInfoTableModel.addRow(new Object[]{rank, b.getBookID(), b.getBookName(), b.getAuthor(), b.getPress(), b.getBookType(), b.getCount()});
            } else {
                bookBorrowedInfoTableModel.addRow(new Object[]{++rank, b.getBookID(), b.getBookName(), b.getAuthor(), b.getPress(), b.getBookType(), b.getCount()});
            }
            lastReaderCount = readerCount;
        }
        bookBorrowedInfoTableCountLabel.setText("一共有" + bookBorrowedInfoTableModel.getRowCount() + "条记录");
    }

    private void initComponents() {
        topPane.setIcon(new ImageIcon("imgs/top3.png"));
//        managerPane.setIcon(new ImageIcon("imgs/managerpanebg2.png"));
//        bookPane.setIcon(new ImageIcon("imgs/managerpanebg2.png"));
//        readerPane.setIcon(new ImageIcon("imgs/managerpanebg2.png"));
//        borrowedPane.setIcon(new ImageIcon("imgs/managerpanebg2.png"));
//        rankPane.setIcon(new ImageIcon("imgs/managerpanebg2.png"));
        tabPane.setIcon(new ImageIcon("imgs/panebg.png"));


        //将表格设置为透明
//        myPanel1.setIcon(new ImageIcon("imgs/table1.png"));
        bookTableScrollPane.getViewport().setOpaque(false);
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setOpaque(false);
        bookTable.setSelectionForeground(Color.RED);
        bookTable.setDefaultRenderer(Object.class, renderer);

//        myPanel4.setIcon(new ImageIcon("imgs/table1.png"));
        readerTableScrollPane.getViewport().setOpaque(false);
        readerTable.setSelectionForeground(Color.RED);
        readerTable.setDefaultRenderer(Object.class, renderer);

//        myPanel6.setIcon(new ImageIcon("imgs/table1.png"));
        borrowedTableScrollPane.getViewport().setOpaque(false);
        borrowedTable.setSelectionForeground(Color.RED);
        borrowedTable.setDefaultRenderer(Object.class, renderer);

//        myPanel12.setIcon(new ImageIcon("imgs/table1.png"));
        readerBorrowedInfoTableScrollPane.getViewport().setOpaque(false);
        readerBorrowedInfoTable.setSelectionForeground(Color.RED);
        readerBorrowedInfoTable.setDefaultRenderer(Object.class, renderer);

//        myPanel13.setIcon(new ImageIcon("imgs/table1.png"));
        bookBorrowedInfoTableScrollPane.getViewport().setOpaque(false);
        bookBorrowedInfoTable.setSelectionForeground(Color.RED);
        bookBorrowedInfoTable.setDefaultRenderer(Object.class, renderer);

        managerTableScrollPane.getViewport().setOpaque(false);
        managerTable.setSelectionForeground(Color.RED);
        managerTable.setDefaultRenderer(Object.class, renderer);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final JPopupMenu popupMenu = new JPopupMenu();

                JMenuItem switchManager = new JMenuItem("切换管理员");
                switchManager.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        super.mousePressed(e);
                        popupMenu.setVisible(false);
                        int i = JOptionPane.showConfirmDialog(getContentPane(), "确定却换吗？", "提示", JOptionPane.YES_NO_OPTION);
                        if (i == JOptionPane.YES_OPTION) {
                            dispose();
                            EventQueue.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    LoginFrame loginFrame = new LoginFrame();
                                    loginFrame.setVisible(true);
                                }
                            });
                        }
                    }
                });
                popupMenu.add(switchManager);

                JMenuItem closeMenuItem = new JMenuItem("退出");
                closeMenuItem.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        super.mousePressed(e);
                        popupMenu.setVisible(false);
                        int i = JOptionPane.showConfirmDialog(getContentPane(), "确定退出吗？", "提示", JOptionPane.YES_NO_OPTION);
                        if (i == JOptionPane.YES_OPTION) {
                            System.exit(0);
                        }
                    }
                });
                popupMenu.add(closeMenuItem);
                popupMenu.show(startButton, 0, startButton.getHeight());
            }
        });

//        helpButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                final JPopupMenu popupMenu = new JPopupMenu();
//                JMenuItem updateMenuItem = new JMenuItem("更新");
//                updateMenuItem.addMouseListener(new MouseAdapter() {
//                    @Override
//                    public void mousePressed(MouseEvent e) {
//                        super.mousePressed(e);
//                        popupMenu.setVisible(false);
//                        UpdateDialog updateDialog = new UpdateDialog();
//                        new Thread(updateDialog).start();
//                        updateDialog.pack();
//                        updateDialog.setLocationRelativeTo(MainFrame.this);
//                        updateDialog.setVisible(true);
//                    }
//                });
//                popupMenu.add(updateMenuItem);
//
//                JMenuItem aboutMenuItem = new JMenuItem("关于");
//                aboutMenuItem.addMouseListener(new MouseAdapter() {
//                    @Override
//                    public void mousePressed(MouseEvent e) {
//                        super.mousePressed(e);
//                        popupMenu.setVisible(false);
//                        AboutDialog aboutDialog = new AboutDialog();
//                        aboutDialog.pack();
//                        aboutDialog.setLocationRelativeTo(MainFrame.this);
//                        aboutDialog.setVisible(true);
//                    }
//                });
//                popupMenu.add(aboutMenuItem);
//
//                popupMenu.show(helpButton, 0, helpButton.getHeight());
//            }
//        });


        topPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                location.x = e.getX();
                location.y = e.getY();
            }
        });

        topPane.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Point p = getLocation();
                setLocation(p.x + e.getX() - location.x, p.y + e.getY() - location.y);
            }
        });

        tabbedPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Rectangle rectangle = tabbedPane.getBoundsAt(tabSelect);
                if (rectangle.contains(e.getX(), e.getY())) {
                    //单击刷新列表
                    switch (tabSelect) {
                        case 0:
                            bookSearchTextField.setText("");
                            bookSearchComboBox.setSelectedIndex(0);
                            bookInfoLoad();
                            break;
                        case 1:
                            readerSearchTextField.setText("");
                            readerSearchComboBox.setSelectedIndex(0);
                            readerInfoLoad();
                            break;
                        case 2:
                            borrowedSearchTextField.setText("");
                            borrowedSearchComboBox.setSelectedIndex(0);
                            borrowedInfoLoad();
                            break;
                        case 3:
                            fromDateTextField.setText("");
                            toDateTextField.setText("");
                            readerBorrowedInfoLoad();
                            bookBorrowedInfoLoad();
                            break;
                        case 4:
                            managerSearchTextField.setText("");
                            managerSearchComboBox.setSelectedIndex(0);
                            managerInfoLoad();
                            break;
                        default:
                            break;
                    }
                } else {
                    tabSelect = tabbedPane.getSelectedIndex();
                }
            }
        });
    }
}
