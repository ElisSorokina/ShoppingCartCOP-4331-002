package org.example;

import org.example.grpc.Item;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class BuyerItemTableModel extends AbstractTableModel {

    public static final int ID_COL = 0;
    public static final int NAME_COL = 1;
    public static final int SELL_PRICE_COL = 2;
    public static final int SELECT_COL = 3;

    private final String[] columnNames = {"ID", "Name", "Sell Price", "Select"};
    private final Class<?>[] columnClasses = {String.class, String.class, Double.class, Boolean.class};

    private List<Item> itemList;
    private boolean[] selected;

    public BuyerItemTableModel(List<Item> itemList) {
        this.itemList = new ArrayList<>(itemList);
        selected = new boolean[itemList.size()];
    }

    @Override
    public int getRowCount() {
        return itemList.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnClasses[columnIndex];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex != ID_COL; // ID is not editable
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Item item = itemList.get(rowIndex);

        switch (columnIndex) {
            case ID_COL:
                return item.getId();
            case NAME_COL:
                return item.getName();
            case SELL_PRICE_COL:
                return item.getSellPriceCents() / 100.0; // Convert cents to dollars
            case SELECT_COL:
                return selected[rowIndex];
            default:
                throw new IndexOutOfBoundsException("Column index out of bounds");
        }
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        Item item = itemList.get(rowIndex);

        switch (columnIndex) {
            case SELECT_COL:
                selected[rowIndex] = ((Boolean) value);
                break;
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    public List<String> getSelectedIds() {
        var result = new ArrayList<String>();
        for (int i = 0; i < itemList.size(); i++) {
            if (selected[i]) {
                result.add(itemList.get(i).getId());
            }
        }

        return result;
    }


    public void setItemList(List<Item> itemList) {
        this.itemList = new ArrayList<>(itemList);
        fireTableDataChanged(); // Notify JTable that data has changed
    }

    public void addRow(Item item) {
        itemList.add(item);
        fireTableDataChanged();
    }
}