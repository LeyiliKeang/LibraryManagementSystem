package groupwork.ui.dialog;

import com.sun.awt.AWTUtilities;
import groupwork.my.MyMouseAdapter;
import groupwork.my.components.MyButton;
import groupwork.my.components.MyPanel;
import groupwork.my.model.MyTableModel;
import groupwork.sql.dao.BookBorrowedDao;
import groupwork.sql.model.Book;
import groupwork.sql.model.Borrowed;
import groupwork.sql.model.Reader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

public class OneBookBorrowedInfoDialog extends JDialog {
    private JPanel contentPane;
    private MyPanel myPanel1;
    private MyPanel myPanel2;
    private MyButton buttonOK;
    private MyButton buttonCancel;
    private MyPanel myPanel3;
    private JScrollPane oneBookBorrowedInfoTableScrollPane;
    private JTable oneBookBorrowedInfoTable;
    private JLabel oneBookBorrowedInfoTableCountLabel;

    private static Point location = new Point();

    private MyTableModel OneBookBorrowedInfoDefaultTableModel = new MyTableModel();

    private int oneBookBorrowedInfoTableRow;

    public OneBookBorrowedInfoDialog() {
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
                AWTUtilities.setWindowOpacity(OneBookBorrowedInfoDialog.this, 0f);
                ActionListener listener = new ActionListener() {
                    float alpha = 0;

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (alpha < 0.8) {
                            AWTUtilities.setWindowOpacity(OneBookBorrowedInfoDialog.this, alpha += 0.2);
                        } else {
                            AWTUtilities.setWindowOpacity(OneBookBorrowedInfoDialog.this, 1);
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
//        OneBookBorrowedInfoDialog dialog = new OneBookBorrowedInfoDialog();
//        dialog.pack();
//        dialog.setVisible(true);
//        System.exit(0);
//    }

    private void initComponents() {
        OneBookBorrowedInfoDefaultTableModel.setColumnIdentifiers(new Object[]{"学号", "姓名", "借书日期", "应还书日期", "实还书日期"});
        oneBookBorrowedInfoTable.setModel(OneBookBorrowedInfoDefaultTableModel);
        oneBookBorrowedInfoTable.getTableHeader().setReorderingAllowed(false);
        oneBookBorrowedInfoTable.getTableHeader().setFont(new Font("微软雅黑", 0, 16));
        oneBookBorrowedInfoTableScrollPane.setViewportView(oneBookBorrowedInfoTable);
        oneBookBorrowedInfoTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        oneBookBorrowedInfoTable.addMouseListener(new MyMouseAdapter() {
            @Override
            public void mouseDoubleClicked(MouseEvent e) {
                oneBookBorrowedInfoTableRow = oneBookBorrowedInfoTable.rowAtPoint(e.getPoint());
                if (oneBookBorrowedInfoTableRow > -1) {
                    Reader.setSelectID(oneBookBorrowedInfoTable.getValueAt(oneBookBorrowedInfoTable.getSelectedRow(), 0).toString());
                    ReaderInfoDialog readerInfoDialog = new ReaderInfoDialog();
                    readerInfoDialog.pack();
                    readerInfoDialog.setLocationRelativeTo(getContentPane());
                    readerInfoDialog.setVisible(true);
                }
            }
        });

        oneBookBorrowedInfoLoad();
    }

    private void oneBookBorrowedInfoLoad() {
        OneBookBorrowedInfoDefaultTableModel.setRowCount(0);
        BookBorrowedDao bookBorrowedDao = new BookBorrowedDao();
        java.util.List<Borrowed> list = null;
        try {
            list = bookBorrowedDao.getOneBookAllBorrowedInfo(Book.getSelectID());
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        for (Borrowed b : list) {
            OneBookBorrowedInfoDefaultTableModel.addRow(new Object[]{b.getReaderID(), b.getReaderName(), b.getBorrowDate(), b.getShouldReturnDate(), b.getReturnDate()});
        }
        oneBookBorrowedInfoTableCountLabel.setText("一共有" + OneBookBorrowedInfoDefaultTableModel.getRowCount() + "条记录");
    }
}
