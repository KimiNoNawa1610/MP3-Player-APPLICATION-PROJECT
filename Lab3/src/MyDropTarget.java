import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.util.ArrayList;
import java.util.List;

class MyDropTarget extends DropTarget {
    private StreamPlayerGUI gui;

    public MyDropTarget(StreamPlayerGUI n){
        gui=n;
    }

    public  void drop(DropTargetDropEvent evt) {

        try {

            evt.acceptDrop(DnDConstants.ACTION_COPY);

            List result = new ArrayList();

            result = (List) evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);

            for(Object o : result){
                System.out.println(o.toString());
                gui.addSong(o.toString());
            }



        }
        catch (Exception ex){

           ex.printStackTrace();

        }
    }


}
