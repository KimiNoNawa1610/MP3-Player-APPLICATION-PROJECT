import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
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
class StreamPlayerGUI extends JFrame {

    private boolean isPause;

    private boolean isPlaying;

    private int isPlayingrow;

    private final TreeList listTree=new TreeList();

    private final BasicPlayer player=new BasicPlayer();

    private final PauseListener pl=new PauseListener();

    private final JTable table;

    private final JTextField playSong;

    private int CurrentSelectedRow;

    private final Library lib=Library.getInstance();

    private final PlayList playList=new PlayList();

    private static StreamPlayerGUI instance=null;

    private static String currentTable="Library";

    private PopupMenu pop=new PopupMenu();

    public static StreamPlayerGUI getInstance() throws SQLException {
        if(instance == null)
        {
            instance =new StreamPlayerGUI();
        }
        return instance;

    }

    private StreamPlayerGUI() throws SQLException {

        JPanel main = new JPanel();

        JPanel controller=new JPanel();

        controller.setSize(new Dimension(400,0));

        JSlider volumeAdjustment = new VolumeControl(player);

        table=lib.getTable();

        table.setRowHeight(20);

        //table.setRowSelectionAllowed(false);

        table.setDragEnabled(true);

        table.setDropMode(DropMode.ON);

        table.getDragEnabled();

        listTree.addPlayListListener(new TreePlaylistSelect());

        JPanel sidePanel=new JPanel();

        sidePanel.setLayout(new FlowLayout());

        main.setLayout(new FlowLayout());

        JMenuBar topMenu1=new JMenuBar();

        JMenu menu1=new JMenu("File");

        JMenu menu2=new JMenu("Controls");

        JMenuItem mitem1=new JMenuItem("Play");

        menu2.add(mitem1);

        JMenuItem mitem2=new JMenuItem("Add File to Library");

        menu1.add(mitem2);

        JMenuItem mitem3=new JMenuItem("Delete");

        menu2.add(mitem3);

        JMenuItem mitem4=new JMenuItem("Add New Playlist");

        menu1.add(mitem4);

        topMenu1.add(menu1);

        topMenu1.add(menu2);

        //topMenu.add(Box.createHorizontalStrut(800));

        this.add(topMenu1, BorderLayout.NORTH);

        this.add(listTree, BorderLayout.WEST);

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

        JLabel notInData = new JLabel("enter the url to play");

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

        mitem4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String listName=JOptionPane.showInputDialog(null,"Please enter the playlist name");
                try {
                    playList.setName(listName);
                    playList.createPlaylist();
                    listTree.addPlaylist(listName);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                JMenuItem n=new JMenuItem(listName);
                n.setName(listName);
                n.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String n=lib.getElement(CurrentSelectedRow);
                        try {
                            Mp3Song s=new Mp3Song(n);
                            playList.AddSong(s);
                            listTree.popRefresh();
                        } catch (InvalidDataException | SQLException | UnsupportedTagException | IOException invalidDataException) {
                            invalidDataException.printStackTrace();
                        }


                    }
                });
                pop.addSubmenuItem(n);
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);

        scrollPane.setPreferredSize(new Dimension(750,300));

        table.setComponentPopupMenu(pop);

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

        this.setTitle("StreamPlayer by Nhan and Brandon");//change the name to yours

        this.setSize(870, 410);

        //this.add(listTree);

        this.add(main, BorderLayout.CENTER);

        this.add(controller, BorderLayout.SOUTH);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public void DisplayError(String n){

        JPanel error=new JPanel();

        JOptionPane.showMessageDialog(error,n,"ERROR",JOptionPane.ERROR_MESSAGE);

    }

    public void addSong(String n) throws InvalidDataException, IOException, UnsupportedTagException, SQLException {

        Mp3Song song=new Mp3Song(n);

        System.out.println(currentTable);

        if(currentTable.equals("Library")){
            if(lib.SongExist(song)&&playList.SongExist(song)){

                DisplayError(song.getTitle()+" already Exists ");

            }
            lib.AddSong(song);
        }
        else{

            playList.AddSong(song);

        }





    }

    public Boolean isPlaying(){

        return isPlayingrow == CurrentSelectedRow && isPlaying;

    }

    public void deleteSong() throws SQLException {



        if(currentTable.equals("Library")){
            if(isPlaying()){

                DisplayError(" Song id: "+lib.getSongid(CurrentSelectedRow)+" is playing \nCannot delete at this time");

            }
            else{
                lib.RemoveSong(CurrentSelectedRow);
            }



        }

        else{
            if(isPlaying()){

                DisplayError(" Song id: "+playList.getSongid(CurrentSelectedRow)+" is playing \nCannot delete at this time");

            }
            else {
                playList.RemoveSong(CurrentSelectedRow);
            }



        }

        CurrentSelectedRow--;

        isPlayingrow--;

    }



    public PopupMenu getPopup(){

        return pop;

    }

    public String FileOpener(){

        JFileChooser choosefile=new JFileChooser();

        choosefile.showOpenDialog(null);

        File file=choosefile.getSelectedFile();

        return file.getAbsolutePath();

    }

    public String getURL(){
        String url="";
        if(currentTable.equals("Library")){
            url=lib.getElement(CurrentSelectedRow);
        }
        else{
            url=playList.getElement(CurrentSelectedRow);
        }
        return url;
    }

    public void stopPlayer() throws BasicPlayerException {
        player.stop();
    }

    class ButtonListener implements ActionListener {

        @Override

        public void actionPerformed(ActionEvent e) {

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

                    player.open(new File(getURL()));

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

        JMenu submenu=new JMenu("Add song to playlist");
        public PopupMenu() throws SQLException {

            setPopupSize(150,100);

            JMenuItem add=new JMenuItem("Add song to library");

            for(int i=0;i<playList.getPlalistname().size();i++){
                String n=playList.getPlalistname().get(i);
                JMenuItem itemN=new JMenuItem(n);
                itemN.setName(n);
                submenu.add(itemN);
                itemN.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        playList.setName(n);
                        String n=lib.getElement(CurrentSelectedRow);
                        try {
                            playList.AddSong(new Mp3Song(n));
                            listTree.popRefresh();
                        } catch (SQLException | InvalidDataException | IOException | UnsupportedTagException throwables) {
                            throwables.printStackTrace();
                        }
                    }
                });
            }

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

            this.add(submenu);

            this.add(delete);

            this.add(play);

        }

        public void addSubmenuItem(JMenuItem item){

            submenu.add(item);

        }

        public void removeSubmenuItem(String n){

            //System.out.println(n);

            for(int i=0;i<submenu.getItemCount();i++){
                //System.out.println(submenu.getItem(i).getName());
                if(submenu.getItem(i).getName().equals(n)){

                    submenu.remove(submenu.getItem(i));

                }
            }

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
    public void returnToLib(){
        currentTable="Library";
        DefaultTableModel model= lib.getNewTable();
        model.setRowCount(0);
        model= (DefaultTableModel) lib.getTable().getModel();
        table.setModel(model);
    }

    public void returnToPlaylist(String n) throws SQLException {
        currentTable=n;
        System.out.println(currentTable);
        DefaultTableModel model= lib.getNewTable();
        model.setRowCount(0);
        model= (DefaultTableModel) playList.getTable().getModel();
        table.setModel(model);
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

            System.out.println(CurrentSelectedRow);

            if(currentTable.equals("Library")) {

                SetInterval(lib.getSongURLSize());
            }

            else{

                SetInterval(playList.getSongURLSize());
            }

            isPlayingrow=CurrentSelectedRow;


        }

    }

    private void SetInterval(int songURLSize) {
        if (CurrentSelectedRow < songURLSize - 1) {

            CurrentSelectedRow += 1;


        } else {

            CurrentSelectedRow = 0;

        }

        isPlayingrow = CurrentSelectedRow;

        try {
            table.setRowSelectionInterval(CurrentSelectedRow, CurrentSelectedRow);
            player.open(new File(getURL()));
            player.play();
        } catch (BasicPlayerException basicPlayerException) {
            basicPlayerException.printStackTrace();
        }
    }

    class SkipBackward implements  ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {

            System.out.println(CurrentSelectedRow);

            if(CurrentSelectedRow>0){

                CurrentSelectedRow-=1;

            }

            else{

                if(currentTable.equals("Library")){

                    CurrentSelectedRow=lib.getSongURLSize()-1;

                }

                else{

                    CurrentSelectedRow=playList.getSongURLSize()-1;

                }

            }

            isPlayingrow=CurrentSelectedRow;

            try {

                player.open(new File(getURL()));

                player.play();

                table.setRowSelectionInterval(CurrentSelectedRow,CurrentSelectedRow);

            } catch (BasicPlayerException basicPlayerException) {

                basicPlayerException.printStackTrace();

            }



        }
    }

    public class TreePlaylistSelect implements TreeSelectionListener {
        DefaultTableModel model= lib.getNewTable();
        public TreePlaylistSelect() throws SQLException {
            super();
        }
        @Override
        public void valueChanged(TreeSelectionEvent e) {
            if(e.getNewLeadSelectionPath()!=null&&!e.getNewLeadSelectionPath().getLastPathComponent().toString().equals("Library")){
                String path= e.getNewLeadSelectionPath().getLastPathComponent().toString();
                model.setRowCount(0);
                playList.setName(path);
                try {
                    model= (DefaultTableModel) playList.getTable().getModel();
                    table.setModel(model);
                    currentTable=path;
                    listTree.setData(path);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

            }
            else if(e.getNewLeadSelectionPath()!=null&&e.getNewLeadSelectionPath().getLastPathComponent().toString().equals("Library")&&!currentTable.equals("Library")){
                model.setRowCount(0);
                model= (DefaultTableModel) lib.getTable().getModel();
                table.setModel(model);
                currentTable="Library";
            }

        }

    }

}