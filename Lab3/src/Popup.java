import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*

Nhan Vo

CECS 343-project

*/
class Popup extends JFrame {

    private boolean isPause;

    private boolean isPlaying;

    private int isPlayingrow;

    private  BasicPlayer player=new BasicPlayer();

    private  PauseListener pl=new PauseListener();

    private JTable table;

    private JTextField playSong;

    private int CurrentSelectedRow;

    private  PlayList playList=new PlayList();

    public Popup (String data) throws SQLException {

        JPanel controller=new JPanel();

        JSlider volumeAdjustment = new VolumeControl(player);

        JPanel main = new JPanel();

        playList.setName(data);

        System.out.println(data);

        table=playList.getTable();

        table.setRowHeight(20);

        table.setRowSelectionAllowed(false);

       // table.setFillsViewportHeight(true);
       // table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
       // table.setDragEnabled(true);
        table.getDragEnabled();

        //table.setDropMode(DropMode.ON);

        table.setRowSelectionAllowed(true);

        JPanel sidePanel=new JPanel();

        sidePanel.setLayout(new FlowLayout());

        main.setLayout(new FlowLayout());

        JMenuBar topMenu1=new JMenuBar();

        JMenu menu1=new JMenu("File");

        JMenu menu2=new JMenu("Controls");

        JMenuItem mitem1=new JMenuItem("Play");

        menu2.add(mitem1);

        JMenuItem mitem2=new JMenuItem("Add Song to playlist");

        menu1.add(mitem2);

        JMenuItem mitem3=new JMenuItem("Delete");

        menu2.add(mitem3);

        JMenuItem mitem4=new JMenuItem("Add New Playlist");

        menu1.add(mitem4);

        topMenu1.add(menu1);

        topMenu1.add(menu2);

        //topMenu.add(Box.createHorizontalStrut(800));

        this.add(topMenu1, BorderLayout.NORTH);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                try {
                    player.stop();
                    StreamPlayerGUI.getInstance().returnToPlaylist(data);
                } catch (BasicPlayerException | SQLException basicPlayerException) {
                    basicPlayerException.printStackTrace();
                }
            }
        });

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // this.add(listTree, BorderLayout.WEST);

        TableColumnModel columnModel = table.getColumnModel();

        MouseListener mouseListener = new MouseAdapter() {
            //this will print the selected row index when a user clicks the table
            public void mousePressed(MouseEvent e) {
                CurrentSelectedRow = table.getSelectedRow();
            }
        };

        table.addMouseListener(mouseListener);

        table.setVisible(true);

        JButton skipf = new JButton(">>");

        JButton play1 = new JButton(">");

        JButton pause = new JButton("| |");

        JButton skipb = new JButton("<<");

        JButton stop = new JButton("Stop");

        JLabel notInData = new JLabel("enter the url to just play song");

        playSong = new JTextField("",15);

        ButtonListener bl = new ButtonListener();

        play1.addActionListener(bl);

        pause.addActionListener(pl);

        SkipFoward sf = new SkipFoward();

        skipf.addActionListener(sf);

        SkipBackward sb = new SkipBackward();

        skipb.addActionListener(sb);

        StopListener st=new StopListener();

        stop.addActionListener(st);

        mitem1.addActionListener(bl);

        mitem2.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                String n=FileOpener();

                try {

                    addSong(n);

                } catch (InvalidDataException | IOException | UnsupportedTagException | SQLException invalidDataException) {

                    invalidDataException.printStackTrace();

                }
            }

        });

        mitem3.addActionListener(new ActionListener() {

            @Override

            public void actionPerformed(ActionEvent e) {

                try {

                    deleteSong();

                } catch (SQLException throwables) {

                    throwables.printStackTrace();

                }

            }

        });

        table.setDropTarget( new DropTarget(){

            public synchronized void drop (DropTargetDropEvent dtde){

                Point point=dtde.getLocation();
                //int column = table.ColumnAtPoint(point);
                int row =table.rowAtPoint(point);
                super.drop(dtde);
                 System.out.println("hello");

            }
        });


        //  mitem4.addActionListener(new ActionListener() {
        //     @Override
        //    public void actionPerformed(ActionEvent e) {
        //        String listName=JOptionPane.showInputDialog(null,"Please enter the playlist name");
        //        listTree.addPlaylist(listName);
        //        playlists.setName(listName);
        //        try {
        //            playlists.createPlaylist();
        //        } catch (SQLException throwables) {
        //            throwables.printStackTrace();
        //        }
        //    }
        //});

        JScrollPane scrollPane = new JScrollPane(table);

        scrollPane.setPreferredSize(new Dimension(750,300));

        table.setComponentPopupMenu(new PopupMenu());

        main.add(scrollPane);

        controller.add(skipb);

        controller.add(play1);

        controller.add(pause);

        controller.add(skipf);

        controller.add(stop);

        controller.add(volumeAdjustment);

        controller.add(notInData);

        controller.add(playSong);

        main.setDropTarget(new MyDropTarget(this));

        this.setTitle(data+" Playlist");//change the name to yours

        this.setSize(850, 410);

        //this.add(listTree);

        this.add(main, BorderLayout.CENTER);

        this.add(controller, BorderLayout.SOUTH);

    }

    public void StopPlayer() throws BasicPlayerException {
        player.stop();
    }

    public void refresh() throws SQLException {
        DefaultTableModel model= (DefaultTableModel) playList.getTable().getModel();
        table.setModel(model);
    }




    public void DisplayError(String n){

        JPanel error=new JPanel();

        JOptionPane.showMessageDialog(error,n,"ERROR",JOptionPane.ERROR_MESSAGE);

    }

    public void addSong(String n) throws InvalidDataException, IOException, UnsupportedTagException, SQLException {

        Mp3Song song=new Mp3Song(n);

        if(playList.SongExist(song)){

            DisplayError(song.getTitle()+" already Exists ");

        }

        else{

            playList.AddSong(song);

        }

    }

    public Boolean isPlaying(){

        return isPlayingrow == CurrentSelectedRow && isPlaying;

    }

    public void deleteSong() throws SQLException {

        String delete="DELETE FROM songs WHERE SongID=" +playList.getSongid(CurrentSelectedRow);

        if(isPlaying()){

            DisplayError(" Song id: "+playList.getSongid(CurrentSelectedRow)+" is playing \nCannot delete at this time");

        }
        else{

            playList.RemoveSong(CurrentSelectedRow);

            CurrentSelectedRow--;

            isPlayingrow--;

        }

    }

    public String FileOpener(){

        JFileChooser choosefile=new JFileChooser();

        choosefile.showOpenDialog(null);

        File file=choosefile.getSelectedFile();

        return file.getAbsolutePath();

    }
    public PlayList getPlayList()
    {
        return playList;
    }

    class ButtonListener implements ActionListener {

        @Override

        public void actionPerformed(ActionEvent e) {

            String url=playList.getElement(CurrentSelectedRow);
            //create if, output and url assignment statements for the other two channels

            try {

                if(isPause && pl.getPauseRow()==CurrentSelectedRow){

                    player.resume();

                    isPause=false;

                    isPlaying=true;

                }
                else if(!playSong.getText().equals("")){

                    String song=playSong.getText();

                    try {

                        player.open(new File(song));

                        player.play();

                    } catch (BasicPlayerException basicPlayerException) {

                        basicPlayerException.printStackTrace();

                    }

                    isPause=false;

                    isPlaying=true;

                }

                else{

                    player.open(new File(url));

                    player.play();

                    isPause=false;

                    isPlaying=true;

                }

            } catch (BasicPlayerException ex) {

                System.out.println("BasicPlayer exception");

                Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);

            }

        }

    }

    class PopupMenu extends JPopupMenu{
        public PopupMenu(){
            setPopupSize(150,100);
            JMenuItem add=new JMenuItem("Add song to library");

            JMenuItem delete=new JMenuItem("Delete selected song");

            JMenuItem play=new JMenuItem("Play song");

            add.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {

                    String n=FileOpener();

                    try {

                        addSong(n);

                    } catch (InvalidDataException | IOException | UnsupportedTagException | SQLException invalidDataException) {

                        invalidDataException.printStackTrace();

                    }

                }

            });

            delete.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    try {

                        deleteSong();

                    } catch (SQLException throwables) {

                        throwables.printStackTrace();

                    }

                }

            });

            play.addActionListener(new ButtonListener());

            this.add(add);

            this.add(delete);

            this.add(play);

        }
    }

    class StopListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {

            try {

                isPlaying=false;

                player.stop();

            } catch (BasicPlayerException basicPlayerException) {

                basicPlayerException.printStackTrace();

            }

        }

    }

    class PauseListener implements ActionListener {

        int PauseRow;

        @Override
        public void actionPerformed(ActionEvent e) {

            try{

                PauseRow=CurrentSelectedRow;

                System.out.println(CurrentSelectedRow);

                System.out.println(PauseRow);

                player.pause();

                isPause=true;

                isPlaying=false;

            } catch (BasicPlayerException basicPlayerException) {

                basicPlayerException.printStackTrace();

            }

        }

        public int getPauseRow(){

            return PauseRow;

        }

    }

    class SkipFoward implements  ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {

            if(CurrentSelectedRow<playList.getSongURLSize()-1){

                CurrentSelectedRow+=1;

            }

            else{

                CurrentSelectedRow=0;

            }

            isPlayingrow=CurrentSelectedRow;

            String url=playList.getElement(CurrentSelectedRow);

            try {

                player.open(new File(url));

                player.play();

                table.setRowSelectionInterval(CurrentSelectedRow,CurrentSelectedRow);

            } catch (BasicPlayerException basicPlayerException) {

                basicPlayerException.printStackTrace();

            }

        }

    }

    class SkipBackward implements  ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {

            if(CurrentSelectedRow>0){

                CurrentSelectedRow-=1;

            }

            else{

                CurrentSelectedRow=playList.getSongURLSize()-1;

            }

            isPlayingrow=CurrentSelectedRow;

            String url=playList.getElement(CurrentSelectedRow);

            try {

                player.open(new File(url));

                player.play();

                table.setRowSelectionInterval(CurrentSelectedRow,CurrentSelectedRow);

            } catch (BasicPlayerException basicPlayerException) {

                basicPlayerException.printStackTrace();

            }

        }
    }

}
