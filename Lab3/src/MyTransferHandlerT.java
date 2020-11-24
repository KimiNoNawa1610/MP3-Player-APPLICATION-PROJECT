import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class MyTransferHandlerT extends TransferHandler {

    private JTable table;
    private DefaultTableModel model;
    private int rowIndex;
    private int collIndex;

    @Override
    public int getSourceActions(JComponent c)
    {
        return COPY_OR_MOVE;


    }

    protected Transferable createTransferable(JComponent source)
    {

        System.out.println("Transferable"+source);
        table=(JTable)source;
        model=(DefaultTableModel) table.getModel();
        rowIndex=table.getSelectedRow();
        collIndex=table.getSelectedColumn();

        model.getValueAt(rowIndex,collIndex);

        String value=(String)model.getValueAt(rowIndex,collIndex);
        Transferable t = new StringSelection(value);
        return t;
    }

    protected void exportDone(JComponent source,Transferable data,int action)
    {
        table=(JTable) source;
        System.out.println("this is what i need " +table);
        model= (DefaultTableModel) table.getModel();
        rowIndex=table.getSelectedRow();
        collIndex=table.getSelectedColumn();

        //model.setValueAt("2",rowIndex,collIndex);
    }
    public boolean canImport(TransferSupport support){
        System.out.println("canImport");
        if(!support.isDataFlavorSupported(DataFlavor.stringFlavor))
        {
            System.out.println("false");
            return false;
        }
        System.out.println("true");
        return true;


    }

    public boolean importData(TransferSupport support)
    {
        System.out.println("importData");
        table=(JTable) support.getComponent();
        Object data= null;
        int row=table.getSelectedRow();
        int col=table.getSelectedColumn();
        try{
            data= (Object) support.getTransferable().getTransferData(DataFlavor.stringFlavor);

        } catch (UnsupportedFlavorException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        model.setValueAt(data,row,col);
        model.fireTableStructureChanged();
        return false;


    }

}
