import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.util.ArrayList;
import java.util.List;

class MyDropTarget extends DropTarget {

    private JFrame gui;

    public MyDropTarget(JFrame n){
        gui=n;
    }

    public  void drop(DropTargetDropEvent evt) {

        try {

            evt.acceptDrop(DnDConstants.ACTION_COPY);

            List result = new ArrayList();

            result = (List) evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);

            for(Object o : result){

                System.out.println(o.toString());
                if(gui instanceof StreamPlayerGUI) {
                    StreamPlayerGUI gui2 =(StreamPlayerGUI)gui;
                    gui2.addSong(o.toString());
                }
                if(gui instanceof Popup)
                {
                    Popup gui2 =(Popup) gui;
                    gui2.addSong(o.toString());

                }

            }

        }
        catch (Exception ex){

           ex.printStackTrace();

        }

    }

}
