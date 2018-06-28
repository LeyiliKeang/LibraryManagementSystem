package groupwork.ui.frame;

import com.sun.awt.AWTUtilities;
import groupwork.my.MyMouseAdapter;
import groupwork.my.components.MyButton;
import groupwork.my.components.MyPanel;
import groupwork.my.model.MyTableModel;
import groupwork.sql.dao.BookTypeDao;
import groupwork.sql.model.BookType;
import groupwork.ui.dialog.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

public class BookTypeFrame extends JFrame {
    private JPanel contentPane;
    private MyPanel myPanel4;
    private MyPanel myPanel2;
    private JTextField bookTypeIDTextField;
    private JTextField bookTypeNameTextField;
    private MyButton addButton;
    private MyPanel myPanel1;
    private JScrollPane bookTypeTableScrollPane;
    private JTable bookTypeTable;
    private MyPanel myPanel3;
    private MyButton alterButton;
    private MyButton deleteButton;
    private MyButton cancelButton;

    private static Point location = new Point();

    private MyTableModel bookTypeDefaultTableModel = new MyTableModel();

    private int bookTypeTableRow;

    public BookTypeFrame() {
        setIconImage(Toolkit.getDefaultToolkit().getImage("imgs/Books.png"));
        setUndecorated(true);
        setContentPane(contentPane);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();

        initComponents();

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!bookTypeIDTextField.getText().trim().equals("") && !bookTypeNameTextField.getText().trim().equals("")) {
                    BookTypeDao bookTypeDao = new BookTypeDao();
                    BookType bookType = new BookType();
                    bookType.setBookTypeID(bookTypeIDTextField.getText().trim());
                    bookType.setBookTypeName(bookTypeNameTextField.getText().trim());
                    try {
                        bookTypeDao.addBookType(bookType);
                        JOptionPane.showMessageDialog(BookTypeFrame.this, "添加成功", "提示", JOptionPane.WARNING_MESSAGE);
                        bookTypeIDTextField.setText("");
                        bookTypeNameTextField.setText("");
                        bookTypeInfoLoad();
                    } catch (ClassNotFoundException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(BookTypeFrame.this, "添加失败", "提示", JOptionPane.WARNING_MESSAGE);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(BookTypeFrame.this, "添加失败", "提示", JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(BookTypeFrame.this, "请填写", "提示", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        alterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = bookTypeTable.getSelectedRow();
                if (row == -1) {
                    JOptionPane.showMessageDialog(getContentPane(), "请选择", "提示", JOptionPane.WARNING_MESSAGE);
                } else {
                    BookType.setSelectID(bookTypeTable.getValueAt(bookTypeTable.getSelectedRow(), 0).toString());
                    AlterBookTypeDialog alterBookTypeDialog = new AlterBookTypeDialog();
                    alterBookTypeDialog.pack();
                    alterBookTypeDialog.setLocationRelativeTo(BookTypeFrame.this);
                    alterBookTypeDialog.setVisible(true);
                    bookTypeInfoLoad();
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = bookTypeTable.getSelectedRow();
                if (row == -1) {
                    JOptionPane.showMessageDialog(getContentPane(), "请选择", "提示", JOptionPane.WARNING_MESSAGE);
                } else {
                    BookType.setSelectID(bookTypeTable.getValueAt(bookTypeTable.getSelectedRow(), 0).toString());
                    int i = JOptionPane.showConfirmDialog(BookTypeFrame.this, "确认删除吗？", "提示", JOptionPane.YES_NO_OPTION);
                    if (i == JOptionPane.YES_OPTION) {
                        BookTypeDao bookTypeDao = new BookTypeDao();
                        try {
                            bookTypeDao.deleteBookType(BookType.getSelectID());
                            JOptionPane.showMessageDialog(BookTypeFrame.this, "删除成功", "提示", JOptionPane.WARNING_MESSAGE);
                            bookTypeInfoLoad();
                        } catch (ClassNotFoundException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(BookTypeFrame.this, "该类型下有图书", "提示", JOptionPane.WARNING_MESSAGE);
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(BookTypeFrame.this, "该类型下有图书", "提示", JOptionPane.WARNING_MESSAGE);
                        }
                    }
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
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
            @Override
            public void windowOpened(WindowEvent e) {
                AWTUtilities.setWindowOpacity(BookTypeFrame.this, 0f);
                ActionListener listener = new ActionListener() {
                    float alpha = 0;

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (alpha < 0.8) {
                            AWTUtilities.setWindowOpacity(BookTypeFrame.this, alpha += 0.2);
                        } else {
                            AWTUtilities.setWindowOpacity(BookTypeFrame.this, 1);
                            Timer source = (Timer) e.getSource();
                            source.stop();
                        }
                    }
                };
                new Timer(50, listener).start();
            }
        });
    }

    private void initComponents() {
        bookTypeDefaultTableModel.setColumnIdentifiers(new Object[]{"类型编号", "类型名称"});
        bookTypeTable.setModel(bookTypeDefaultTableModel);
        bookTypeTable.getTableHeader().setReorderingAllowed(false);
        bookTypeTable.getTableHeader().setFont(new Font("微软雅黑", 0, 16));
        bookTypeTableScrollPane.setViewportView(bookTypeTable);
        bookTypeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookTypeTable.addMouseListener(new MyMouseAdapter() {
            @Override
            public void mouseDoubleClicked(MouseEvent e) {
                bookTypeTableRow = bookTypeTable.rowAtPoint(e.getPoint());
                if (bookTypeTableRow > -1) {
                    BookType.setSelectID(bookTypeTable.getValueAt(bookTypeTable.getSelectedRow(), 0).toString());
                    BookTableDialog bookTableDialog = new BookTableDialog();
                    bookTableDialog.pack();
                    bookTableDialog.setLocationRelativeTo(getContentPane());
                    bookTableDialog.setVisible(true);
                }
            }
        });

        bookTypeInfoLoad();
    }

    private void bookTypeInfoLoad() {
        bookTypeDefaultTableModel.setRowCount(0);
        BookTypeDao bookTypeDao = new BookTypeDao();
        java.util.List<BookType> list = null;
        try {
            list = bookTypeDao.getBookType();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        for (BookType bookType : list) {
            bookTypeDefaultTableModel.addRow(new Object[]{bookType.getBookTypeID(), bookType.getBookTypeName()});
        }
    }
}
