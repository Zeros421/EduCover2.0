/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package educover.backend;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Fia
 */
public class RecoveryPlanTable extends AbstractTableModel{
        private final String[] columns = {
            "Course Code",
            "Failed Component",
            "Week Range",
            "Task",
            "Deadline",
            "Status"
    };

    private final Class<?>[] columnTypes = {
            String.class, String.class, String.class,
            String.class, String.class, String.class
    };

    private RecoveryPlan plan;

    public RecoveryPlanTable() {
        this.plan = new RecoveryPlan("UNKNOWN");
    }

    public void setPlan(RecoveryPlan plan) {
        this.plan = plan != null ? plan : new RecoveryPlan("UNKNOWN");
        fireTableDataChanged();
    }

    public RecoveryPlan getPlan() {
        return plan;
    }

    @Override
    public int getRowCount() {
        return plan.getTasks().size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnTypes[columnIndex];
    }

private boolean editMode = false;
private int editableRow = -1;

public void startEditRow(int row) {
    this.editMode = true;
    this.editableRow = row;
    fireTableRowsUpdated(row, row);
}

public void stopEdit() {
    int r = this.editableRow;
    this.editMode = false;
    this.editableRow = -1;
    if (r >= 0) fireTableRowsUpdated(r, r);
}

public void onEditRow(int row) {
    editMode = true;
    editableRow = row;
    fireTableRowsUpdated(row, row);
}

public void onSavePlan() {
    int old = editableRow;
    editMode = false;
    editableRow = -1;
    if (old >= 0) fireTableRowsUpdated(old, old);
}

@Override
public boolean isCellEditable(int rowIndex, int columnIndex) {
    boolean editableColumn = (columnIndex >= 2);
    return editMode && rowIndex == editableRow && editableColumn;
}

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        AcademicTask task = plan.getTasks().get(rowIndex);
        switch (columnIndex) {
            case 0: return task.getCourseCode();
            case 1: return task.getComponentName();
            case 2: return task.getWeekRange();
            case 3: return task.getDescription();
            case 4: return task.getDeadline();
            case 5: return task.getStatus().name();
            default: return null;
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        AcademicTask task = plan.getTasks().get(rowIndex);
        String value = (aValue == null) ? "" : aValue.toString().trim();

        switch (columnIndex) {
            case 2:
                task.setWeekRange(value);
                break;
            case 3:
                task.setDescription(value);
                break;
            case 4:
                task.setDeadline(value);
                break;
            case 5:
                try {
                    TaskStatus status = TaskStatus.valueOf(value.toUpperCase());
                    task.setStatus(status);
                } catch (IllegalArgumentException ex) {

                }
                break;
        }

        fireTableCellUpdated(rowIndex, columnIndex);
    }


    public void addTask(AcademicTask task) {
        List<AcademicTask> internal = new ArrayList<>(plan.getTasks());
        internal.add(task);

        RecoveryPlan newPlan = new RecoveryPlan(plan.getStudentId());
        for (AcademicTask t : internal) {
            newPlan.addTask(t);
        }
        setPlan(newPlan);
    }

    public void removeRow(int rowIndex) {
        plan.removeTask(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }
}
