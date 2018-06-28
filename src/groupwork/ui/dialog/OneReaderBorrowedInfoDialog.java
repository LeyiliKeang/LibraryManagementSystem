package groupwork.ui.dialog;

import com.sun.awt.AWTUtilities;
import groupwork.my.MyMouseAdapter;
import groupwork.my.components.MyButton;
import groupwork.my.components.MyPanel;
import groupwork.my.model.MyTableModel;
import groupwork.sql.dao.ReaderBorrowedDao;
import groupwork.sql.model.Book;
import groupwork.sql.model.Borrowed;
import groupwork.sql.model.Reader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

public class OneReaderBorrowedInfoDialog extends JDialog {
    private JPanel contentPane;
    private MyPanel myPanel1;
    private MyPanel myPanel2;
    private MyPanel myPanel3;
    private JTable oneReaderBorrowedInfoTable;
    private MyButton buttonOK;
    private MyButton buttonCancel;
    private JScrollPane oneReaderBorrowedInfoTableScrollPane;
    private JLabel oneReaderBorrowedInfoTableCountLabel;

    private static Point location = new Point();

    private MyTableModel OneReaderBorrowedInfoDefaultTableModel = new MyTableModel();

    private int oneReaderBorrowedInfoTableRow;

    public OneReaderBorrowedInfoDialog() {
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
                AWTUtilities.setWindowOpacity(OneReaderBorrowedInfoDialog.this, 0f);
                ActionListener listener = new ActionListener() {
                    float alpha = 0;

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (alpha < 0.8) {
                            AWTUtilities.setWindowOpacity(OneReaderBorrowedInfoDialog.this, alpha += 0.2);
                        } else {
                            AWTUtilities.setWindowOpacity(OneReaderBorrowedInfoDialog.this, 1);
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
//        OneReaderBorrowedInfoDialog dialog = new OneReaderBorrowedInfoDialog();
//        dialog.pack();
//        dialog.setVisible(true);
//        System.exit(0);
//    }

    private void initComponents() {
        OneReaderBorrowedInfoDefaultTableModel.setColumnIdentifiers(new Object[]{"编号", "书名", "借书日期", "应还书日期", "实还书日期"});
        oneReaderBorrowedInfoTable.setModel(OneReaderBorrowedInfoDefaultTableModel);
        oneReaderBorrowedInfoTable.getTableHeader().setReorderingAllowed(false);
        oneReaderBorrowedInfoTable.getTableHeader().setFont(new Font("微软雅黑", 0, 16));
        oneReaderBorrowedInfoTableScrollPane.setViewportView(oneReaderBorrowedInfoTable);
        oneReaderBorrowedInfoTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        oneReaderBorrowedInfoTable.addMouseListener(new MyMouseAdapter() {
            @Override
            public void mouseDoubleClicked(MouseEvent e) {
                oneReaderBorrowedInfoTableRow = oneReaderBorrowedInfoTable.rowAtPoint(e.getPoint());
                if (oneReaderBorrowedInfoTableRow > -1) {
                    Book.setSelectID(oneReaderBorrowedInfoTable.getValueAt(oneReaderBorrowedInfoTable.getSelectedRow(), 0).toString());
                    BookInfoDialog bookInfoDialog = new BookInfoDialog();
                    bookInfoDialog.pack();
                    bookInfoDialog.setLocationRelativeTo(getContentPane());
                    bookInfoDialog.setVisible(true);
                }
            }
        });

        oneReaderBorrowedInfoLoad();
    }

    private void oneReaderBorrowedInfoLoad() {
        OneReaderBorrowedInfoDefaultTableModel.setRowCount(0);
        ReaderBorrowedDao readerBorrowedDao = new ReaderBorrowedDao();
        java.util.List<Borrowed> list = null;
        try {
            list = readerBorrowedDao.getOneReaderAllBorrowedInfo(Reader.getSelectID());
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        for (Borrowed b : list) {
            OneReaderBorrowedInfoDefaultTableModel.addRow(new Object[]{b.getBookID(), b.getBookName(), b.getBorrowDate(), b.getShouldReturnDate(), b.getReturnDate()});
        }
        oneReaderBorrowedInfoTableCountLabel.setText("一共有" + OneReaderBorrowedInfoDefaultTableModel.getRowCount() + "条记录");
    }
}
