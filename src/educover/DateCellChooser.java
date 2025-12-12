/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package educover;

/**
 *
 * @author Fia
 */

import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateCellChooser extends AbstractCellEditor implements TableCellEditor{
        private final JDateChooser chooser = new JDateChooser();
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public DateCellChooser() {
        chooser.setDateFormatString("dd/MM/yyyy");
        chooser.getDateEditor().setEnabled(true);
    }

    @Override
    public Object getCellEditorValue() {
        Date date = chooser.getDate();
        return (date == null) ? "" : sdf.format(date);
    }

    @Override
    public java.awt.Component getTableCellEditorComponent(
            JTable table, Object value, boolean isSelected, int row, int column) {

        try {
            if (value != null && !value.toString().isEmpty()) {
                chooser.setDate(sdf.parse(value.toString()));
            } else {
                chooser.setDate(null);
            }
        } catch (Exception ignored) {}

        return chooser;
    }
}
