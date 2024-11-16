package org.example;

import org.example.grpc.Cart;
import org.example.grpc.CartEntry;
import org.example.grpc.Item;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class CartTableModel extends AbstractTableModel {

    public static final int ID_COL = 0;
    public static final int NAME_COL = 1;
    public static final int QUANTITY_COL = 2;
    public static final int SELL_PRICE_COL = 3;
    public static final int DELETED_COL = 4;

    private final String[] columnNames = {"ID", "Name", "Quantity",  "Sell Price", "Deleted"};
    private final Class<?>[] columnClasses = {String.class, String.class, Integer.class,  Double.class, Boolean.class};

    private List<CartEntry> cartEntries;
    private boolean[] deleted;
    public CartTableModel(List<CartEntry> cartEntries) {
        this.cartEntries = new ArrayList<>(cartEntries);
        deleted = new boolean[cartEntries.size()];
    }

    @Override
    public int getRowCount() {
        return cartEntries.size();
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
        return columnIndex == QUANTITY_COL || columnIndex == DELETED_COL; // ID is not editable
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        CartEntry cartEntry = cartEntries.get(rowIndex);

        switch (columnIndex) {
            case ID_COL:
                return cartEntry.getItemId();
            case NAME_COL:
                return cartEntry.getItemName();
            case QUANTITY_COL:
                return cartEntry.getItemCount();

            case SELL_PRICE_COL:
                return cartEntry.getSellPrice() / 100.0; // Convert cents to dollars
            case DELETED_COL:
                return deleted[rowIndex];
            default:
                throw new IndexOutOfBoundsException("Column index out of bounds");
        }
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        CartEntry cartEntry = cartEntries.get(rowIndex);

        switch (columnIndex) {

            case QUANTITY_COL:
                cartEntry = cartEntry.toBuilder().setItemCount((Integer) value).build();
                break;

            case DELETED_COL:
                deleted[rowIndex] = ((Boolean) value);
                break;
        }

        cartEntries.set(rowIndex, cartEntry);
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    public List<CartEntry> getCartEntries() {
        return cartEntries;
    }

    public void setCartEntries(List<CartEntry> cartEntries) {
        this.cartEntries = new ArrayList<>(cartEntries);
        fireTableDataChanged(); // Notify JTable that data has changed
    }

    public void addRow(CartEntry cartEntry) {
        cartEntries.add(cartEntry);
        fireTableDataChanged();
    }
    public List<CartEntry> getDeleted(){
        var deletedList = new ArrayList<CartEntry>();
        for (int i = 0; i < cartEntries.size(); i++) {
            if(deleted[i]){
                deletedList.add(cartEntries.get(i));
            }
        }
        return deletedList;
    }
}