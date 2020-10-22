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

    private JButton Play,Pause,Skipf, Skipb, Delete,Add;

    private ButtonListener bl= new ButtonListener();

    private PauseListener pl=new PauseListener();

    private SkipFoward sf=new SkipFoward();

    private SkipBackward sb=new SkipBackward();

    private JTable table;

    private JPopupMenu popmenu=new JPopupMenu();

    private JTextField playSong;

    private JLabel notInData;

    private JScrollPane scrollPane;

    private int CurrentSelectedRow;

    private String[] Cname={"ID","Tilte","Genre","Artist","Released Year","Comment"};

    private DefaultTableModel newTable = new DefaultTableModel(Cname, 0);

    private String user="root";
    private String password="";
    private String url="jdbc:mysql://localhost:3306/mp3player";
    private String columns;

    private Connection connection = DriverManager.getConnection(url, user, password);

    public StreamPlayerGUI() throws SQLException {

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

        table=new JTable(newTable);

        popmenu.setPopupSize(50,100);

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

        Delete=new JButton("Delete");

        Add =new JButton("Add");

        notInData= new JLabel("enter the url to just play song");


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

        scrollPane = new JScrollPane(table);

        scrollPane.setPreferredSize(new Dimension(1450,250+10*SongURl.size()));

        JMenuItem add=new JMenuItem("Add");

        JMenuItem delete=new JMenuItem("Delete");

        JMenuItem play=new JMenuItem("Play");

        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String url =JOptionPane.showInputDialog(this,"Enter the URL of the song");
                try {
                    addSong(url);
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



        play.addActionListener(bl);

        popmenu.add(add);

        popmenu.add(delete);

        popmenu.add(play);

        table.setComponentPopupMenu(popmenu);

        main.add(scrollPane);

        main.add(Skipb);

        main.add(Play);

        main.add(Pause);

        main.add(Skipf);

        main.add(Delete);

        main.add(Add);

        main.add(notInData);

        main.add(playSong);



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

        public int getPlayingRow(){
            return n;
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
