import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.util.ArrayList;
import java.util.List;

class MyDropTarget extends DropTarget {

    private  JFrame gui=null;
    private PlayList playlist=null;

    public MyDropTarget(JFrame n){
        gui=n;
    }

    public MyDropTarget(PlayList n){

        playlist=n;
    }

    public  void drop(DropTargetDropEvent evt) {
       System.out.println("brandon ormeno");
        try {

            evt.acceptDrop(DnDConstants.ACTION_COPY);

            if(evt.getTransferable().isDataFlavorSupported(DataFlavor.stringFlavor))
           {
               String s = (String) evt.getTransferable().getTransferData(DataFlavor.stringFlavor);
               String[] sepSong=s.split("\n");
               for(int i=0;i<sepSong.length;i++)
               {
                   int index= sepSong[i].indexOf("\t");
                   String id = sepSong[i].substring(0,index);

                   if (gui instanceof Popup) {
                       Popup gui2 = (Popup) gui;
                       playlist = gui2.getPlayList();
                       String url=playlist.geturlbyId(id);
                       gui2.addSong(url);


                   }

               }

            }
            else {

                List result = new ArrayList();

                result = (List) evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);

                for (Object o : result) {

                    System.out.println(o.toString());
                    if (gui instanceof StreamPlayerGUI) {
                        System.out.println();
                        StreamPlayerGUI gui2 = (StreamPlayerGUI) gui;
                        gui2.addSong(o.toString());
                    }
                    if (gui instanceof Popup) {
                        Popup gui2 = (Popup) gui;
                        gui2.addSong(o.toString());

                    }

                }
            }

        }
        catch (Exception ex){

           ex.printStackTrace();

        }

    }

}
