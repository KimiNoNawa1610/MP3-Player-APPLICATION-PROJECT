import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.ArrayList;

public class Library{

    private final String[] Cname={"ID","Tilte","Genre","Artist","Released Year","Comment"};

    private final DefaultTableModel newTable = new DefaultTableModel(Cname, 0);

    private final String user="root";

    private final String password="";

    private final String url="jdbc:mysql://localhost:3306/mp3player";

    protected final Connection connection = DriverManager.getConnection(url, user, password);

    private static ArrayList<String[]> SongURl=new ArrayList<>();

    protected  JTable table;

    public Library() throws SQLException {

        try {

            System.out.println("Connected to SQL data base");

            Statement statement=connection.createStatement();

            String columns = "SELECT * FROM songs";

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
    }

    public Library(String input) throws SQLException {

        try {

            System.out.println("Connected to SQL data base");

            Statement statement=connection.createStatement();

            String columns = "SELECT * FROM "+input;

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
    }

    public JTable getTable(){

        return table;

    }

    public DefaultTableModel getTableModel(){

        return newTable;

    }

    public void RemoveSong(int n){

        newTable.removeRow(n);

        SongURl.remove(n);

    }

    public Connection getConnection(){
        return connection;
    }

    public void AddSong(Mp3Song song){

        SongURl.add(new String[]{getLastestSongId(),song.getURL()});

        newTable.addRow(new Object[]{SongURl.get(SongURl.size()-1)[0],

                song.getTitle(),song.getGenres(),song.getArtist(),song.getReleasedYear(), song.getComment()});

    }

    public int getSongURLSize(){

        return SongURl.size();

    }

    public String getElement(int n){

        return SongURl.get(n)[1];

    }

    public Boolean isEmpty(){

        return (SongURl.size()==0) ? true:false;

    }

    public String getSongid(int n){

        return SongURl.get(n)[0];

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

    public PreparedStatement MakeSQLStatement(String n) throws SQLException {

        PreparedStatement preparedStatement=connection.prepareStatement(n);

        return preparedStatement;
    }

    public JTable update(String input)
    {
        System.out.println("update");

        try {

            System.out.println("Connected to SQL data base");

            Statement statement=connection.createStatement();

            String columns = "SELECT * FROM "+input;

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
        return table;
    }





}
