import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/*

Nhan Vo

CECS 343-project

*/
class StreamPlayerGUI extends JFrame {

    private ArrayList<String[]>SongURl=new ArrayList<>();

    private boolean isPause;

    private boolean isPlaying;

    private BasicPlayer player;

    private JPanel main;

<<<<<<< HEAD
<<<<<<< HEAD
    private final PauseListener pl=new PauseListener();
=======
    private JButton Play,Pause,Skipf, Skipb, Delete,Add,justPlay;
>>>>>>> parent of ee638a2... enter correct button to just play song
=======
    private JButton Play,Pause,Skipf, Skipb, Delete,Add;
>>>>>>> parent of 63c94ba... update

    private ButtonListener bl= new ButtonListener();

    private PauseListener pl=new PauseListener();

    private SkipFoward sf=new SkipFoward();

    private SkipBackward sb=new SkipBackward();

    private JTable table;

    private JPopupMenu popmenu=new JPopupMenu();

<<<<<<< HEAD
<<<<<<< HEAD
        TreeList listTree=new TreeList();

<<<<<<< HEAD
=======
>>>>>>> parent of 8d16ae2... update change
        JPanel sidePanel=new JPanel();

        PlayList playlists=new PlayList();
=======
    private JScrollPane scrollPane;
>>>>>>> parent of ee638a2... enter correct button to just play song

        sidePanel.setLayout(new FlowLayout());

        main.setLayout(new FlowLayout());

<<<<<<< HEAD
        JMenuBar topMenu1=new JMenuBar();

        JMenu menu1=new JMenu("File");
=======
        //main.add(sidePanel);

        JMenuBar topMenu=new JMenuBar();
>>>>>>> parent of 8d16ae2... update change

        JMenu menu2=new JMenu("Controls");

        JMenuItem mitem1=new JMenuItem("Play");

        menu2.add(mitem1);

        JMenuItem mitem2=new JMenuItem("Add File to Library");

        menu1.add(mitem2);
=======
    private JTextField playSong;

    private JLabel notInData;

    private JScrollPane scrollPane;

    private int CurrentSelectedRow;

    private String[] Cname={"ID","Tilte","Genre","Artist","Released Year","Comment"};
>>>>>>> parent of 63c94ba... update

    private DefaultTableModel newTable = new DefaultTableModel(Cname, 0);

<<<<<<< HEAD
        menu2.add(mitem3);

        JMenuItem mitem4=new JMenuItem("Add New Playlist");

<<<<<<< HEAD
        menu1.add(mitem4);

        topMenu1.add(menu1);

        topMenu1.add(menu2);

        //topMenu.add(Box.createHorizontalStrut(800));

        this.add(topMenu1, BorderLayout.NORTH);
=======
        topMenu.add(menu);

        topMenu.add(Box.createHorizontalStrut(800));
>>>>>>> parent of 8d16ae2... update change
=======
    private String user="root";
    private String password="";
    private String url="jdbc:mysql://localhost:3306/mp3player";
    private String columns;

    private Connection connection = DriverManager.getConnection(url, user, password);

    public StreamPlayerGUI() throws SQLException {
>>>>>>> parent of 63c94ba... update

        main = new JPanel();

        main.setLayout(new FlowLayout());

        try {
            System.out.println("Connected to SQL data base");
            Statement statement=connection.createStatement();
            columns="SELECT * FROM songs";
            ResultSet resultSet=statement.executeQuery(columns);
            System.out.println("Retrieveing information from SQL data base...");
            while(resultSet.next()){
                System.out.println("...");
                String iD=resultSet.getString("SongID");
                String title= resultSet.getString("Title");
                String genre=resultSet.getString("Genre");
                String artist=resultSet.getString("Artist");
                String year=resultSet.getString("Year");
                String comment=resultSet.getString("Comment");
                String URl=resultSet.getString("URL");
                newTable.addRow(new Object[]{iD,title,genre,artist,year,comment});
                SongURl.add(new String[]{iD, URl});
            }
            System.out.println("Done");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

<<<<<<< HEAD
=======
        table=new JTable(newTable);

        popmenu.setPopupSize(50,100);

>>>>>>> parent of 63c94ba... update
        MouseListener mouseListener = new MouseAdapter() {
            //this will print the selected row index when a user clicks the table
            public void mousePressed(MouseEvent e) {
                CurrentSelectedRow = table.getSelectedRow();
            }
        };

        table.addMouseListener(mouseListener);

        TableColumn column = table.getColumnModel().getColumn(0);
        column.setPreferredWidth(10);

        column = table.getColumnModel().getColumn(1); //Description is Column 1
        column.setPreferredWidth(150);

        column=table.getColumnModel().getColumn(4);
        column.setPreferredWidth(25);

        table.setVisible(true);

        Skipf=new JButton(">>");

        Play=new JButton(">");

        Pause=new JButton("| |");

        Skipb=new JButton("<<");

<<<<<<< HEAD
<<<<<<< HEAD
        pause.addActionListener(pl);
=======
        justPlay = new JButton("Play without adding");
>>>>>>> parent of ee638a2... enter correct button to just play song
=======
        Delete=new JButton("Delete");
>>>>>>> parent of 63c94ba... update

        Add =new JButton("Add");

<<<<<<< HEAD
<<<<<<< HEAD
        skipf.addActionListener(sf);
=======
        playSong = new JTextField("url to play song",15);
>>>>>>> parent of ee638a2... enter correct button to just play song
=======
        notInData= new JLabel("enter the url to just play song");
>>>>>>> parent of 63c94ba... update


        playSong = new JTextField("",15);

        Play.addActionListener(bl);

        Pause.addActionListener(pl);

        Skipf.addActionListener(sf);

        Skipb.addActionListener(sb);

        Delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    deleteSong();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        Add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               String url =JOptionPane.showInputDialog(this,"Enter the URL of the song");
                try {
                    addSong(url);
                } catch (UnsupportedTagException | SQLException | InvalidDataException | IOException unsupportedTagException) {
                    unsupportedTagException.printStackTrace();
                }
            }
        });

<<<<<<< HEAD
<<<<<<< HEAD
        mitem4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String listName=JOptionPane.showInputDialog(null,"Please enter the playlist name");
                listTree.addPlaylist(listName);
                playlists.setName(listName);
=======
        JScrollPane scrollPane = new JScrollPane(table);
=======
        scrollPane = new JScrollPane(table);
>>>>>>> parent of 63c94ba... update

        scrollPane.setPreferredSize(new Dimension(1450,250+10*SongURl.size()));

        JMenuItem add=new JMenuItem("Add");

        JMenuItem delete=new JMenuItem("Delete");

        JMenuItem play=new JMenuItem("Play");

        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
<<<<<<< HEAD

                String n=FileOpener();

>>>>>>> parent of 8d16ae2... update change
                try {
                    playlists.createPlaylist();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
=======
                String url =JOptionPane.showInputDialog(this,"Enter the URL of the song");
                try {
                    addSong(url);
                } catch (InvalidDataException | IOException | UnsupportedTagException | SQLException invalidDataException) {
                    invalidDataException.printStackTrace();
>>>>>>> parent of 63c94ba... update
                }
            }
        });

<<<<<<< HEAD
<<<<<<< HEAD
        JScrollPane scrollPane = new JScrollPane(table);
=======
        justPlay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
            });
>>>>>>> parent of ee638a2... enter correct button to just play song

        scrollPane.setPreferredSize(new Dimension(750,300));

        table.setComponentPopupMenu(new PopupMenu());
=======
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



        play.addActionListener(bl);

        popmenu.add(add);

        popmenu.add(delete);

        popmenu.add(play);

        table.setComponentPopupMenu(popmenu);
>>>>>>> parent of 63c94ba... update

        main.add(scrollPane);

        main.add(Skipb);

        main.add(Play);

        main.add(Pause);

        main.add(Skipf);

        main.add(Delete);

        main.add(Add);

        main.add(playSong);

<<<<<<< HEAD
<<<<<<< HEAD
=======
        main.add(justPlay);

>>>>>>> parent of ee638a2... enter correct button to just play song
=======


>>>>>>> parent of 63c94ba... update
        main.setDropTarget(new MyDropTarget(this));

        player = new BasicPlayer();

        this.setTitle("StreamPlayer by Nhan Vo");//change the name to yours

        this.setSize(1500, 350+30*SongURl.size());

        this.add(main);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public String getURL(){
        String n= SongURl.get(CurrentSelectedRow)[1];
        return n;
    }


    public void DisplayError(String n){
        JPanel error=new JPanel();
        JOptionPane.showMessageDialog(error,n,"ERROR",JOptionPane.ERROR_MESSAGE);
    }

    public void addSong(String n) throws InvalidDataException, IOException, UnsupportedTagException, SQLException {
        Mp3Song song=new Mp3Song(n);
        if(SongExist(song)){
            DisplayError(song.getTitle()+" already Exists ");
        }
        else{
            String insert="INSERT INTO `songs`(Title, Genre, Artist, Year, Comment, URL) VALUES (?,?,?,?,?,?)";
            PreparedStatement preparedStatement=connection.prepareStatement(insert);
            preparedStatement.setString(1,song.getTitle());
            preparedStatement.setString(2,song.getGenres());
            preparedStatement.setString(3,song.getArtist());
            preparedStatement.setInt(4,song.getReleasedYear());
            preparedStatement.setString(5,song.getComment());
            preparedStatement.setString(6,song.getURL());
            preparedStatement.executeUpdate();
            SongURl.add(new String[]{getLastestSongId(),song.getURL()});
            newTable.addRow(new Object[]{SongURl.get(SongURl.size()-1)[0],song.getTitle(),song.getGenres(),song.getArtist(),song.getReleasedYear()});
        }
    }

    public String getLastestSongId(){
        int max;
        if(isEmpty()){
            max=0;
        }
        else{
            max= Integer.parseInt(SongURl.get(0)[0]);
            for(int i=0;i<SongURl.size();i++){
                int n=Integer.parseInt(SongURl.get(i)[0]);
                if(n>max){
                    max=n;
                }
            }
        }
        return Integer.toString(max+1);
    }

    public Boolean SongExist(Mp3Song song){
        for(int i=0;i< SongURl.size();i++){
            if(song.getURL().equals(SongURl.get(i)[1])){
                return true;
            }
        }
        return false;
    }

    public Boolean isEmpty(){

        return (SongURl.size()==0) ? true:false;

    }

    public Boolean isPlaying(){
        if(bl.getPlayingRow()==CurrentSelectedRow&& isPlaying==true){
            return true;
        }
        return false;
    }

    public void deleteSong() throws SQLException {
        String delete="DELETE FROM songs WHERE SongID=" +SongURl.get(CurrentSelectedRow)[0];
        if(isPlaying){
            DisplayError("   Song id: "+SongURl.get(CurrentSelectedRow)[0]+" is playing \nCannot delete at this time");
        }
        else{
            PreparedStatement preparedStatement=connection.prepareStatement(delete);
            preparedStatement.executeUpdate();
            newTable.removeRow(CurrentSelectedRow);
            SongURl.remove(CurrentSelectedRow);
            for(int i=0;i<SongURl.size();i++){
                System.out.println(SongURl.get(i)[0]);
            }
            CurrentSelectedRow--;
        }
    }



    class ButtonListener implements ActionListener {

        int n;
        @Override

        public void actionPerformed(ActionEvent e) {

            String url=getURL();
            //create if, output and url assignment statements for the other two channels

            try {

                if(isPause==true&& pl.getPauseRow()==CurrentSelectedRow){

                    player.resume();

                    isPause=false;

                    isPlaying=true;

                    n=CurrentSelectedRow;

                }
<<<<<<< HEAD
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
<<<<<<< HEAD

=======

>>>>>>> parent of ee638a2... enter correct button to just play song
=======
>>>>>>> parent of 63c94ba... update
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

<<<<<<< HEAD
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

=======
        public int getPlayingRow(){
            return n;
>>>>>>> parent of 63c94ba... update
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
            if(CurrentSelectedRow<SongURl.size()-1){
                CurrentSelectedRow+=1;
            }
            String url=getURL();
            try {
                player.open(new File(url));
                player.play();
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
            String url=getURL();
            try {
                player.open(new File(url));
                player.play();
            } catch (BasicPlayerException basicPlayerException) {
                basicPlayerException.printStackTrace();
            }

        }
    }

}
