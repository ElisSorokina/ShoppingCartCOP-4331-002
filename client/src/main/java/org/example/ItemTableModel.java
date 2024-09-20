package org.example;

import org.example.grpc.Item;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class ItemTableModel extends AbstractTableModel {

    public static final int ID_COL = 0;
    public static final int NAME_COL = 1;
    public static final int QUANTITY_COL = 2;
    public static final int INVOICE_PRICE_COL = 3;
    public static final int SELL_PRICE_COL = 4;
    public static final int DELETED_COL = 5;

    private final String[] columnNames = {"ID", "Name", "Quantity", "Invoice Price", "Sell Price", "Deleted"};
    private final Class<?>[] columnClasses = {String.class, String.class, Integer.class, Double.class, Double.class, Boolean.class};

    private List<Item> itemList;

    public ItemTableModel(List<Item> itemList) {
        this.itemList = new ArrayList<>(itemList);
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
            case QUANTITY_COL:
                return item.getQuantity();
            case INVOICE_PRICE_COL:
                return item.getInvoicePriceCents() / 100.0; // Convert cents to dollars
            case SELL_PRICE_COL:
                return item.getSellPriceCents() / 100.0; // Convert cents to dollars
            case DELETED_COL:
                return item.getDeleted();
            default:
                throw new IndexOutOfBoundsException("Column index out of bounds");
        }
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        Item item = itemList.get(rowIndex);

        switch (columnIndex) {
            case NAME_COL:
                item = item.toBuilder().setName((String) value).build();
                break;
            case QUANTITY_COL:
                item = item.toBuilder().setQuantity((Integer) value).build();
                break;
            case INVOICE_PRICE_COL:
                item = item.toBuilder().setInvoicePriceCents((int) ((Double) value * 100)).build();
                break;
            case SELL_PRICE_COL:
                item = item.toBuilder().setSellPriceCents((int) ((Double) value * 100)).build();
                break;
            case DELETED_COL:
                item = item.toBuilder().setDeleted((Boolean) value).build();
                break;
        }

        itemList.set(rowIndex, item);
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    public List<Item> getItemList() {
        return itemList;
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