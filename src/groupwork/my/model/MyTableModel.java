package groupwork.my.model;

import javax.swing.table.DefaultTableModel;

public class MyTableModel extends DefaultTableModel {
    public MyTableModel() {
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
