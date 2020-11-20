import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Library extends DB {

    private JTable table;

    private DefaultTableModel newTable=new DefaultTableModel(Cname,0);

    private static Library lib=null;

    public static Library getInstance() throws SQLException {
        if(lib==null) {

            lib = new Library();

        }
        return lib;

    }

    private Library() throws SQLException {

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

                getLastestSongId();

            }

            CurrentMax=Integer.parseInt(SongURl.get(SongURl.size()-1)[0]);

            System.out.println("Done");


        } catch (SQLException throwables) {

            throwables.printStackTrace();

        }

        table=new JTable(newTable);
    }

    public JTable getTable(){

        return table;

    }

    public void RemoveSong(int n) throws SQLException {

        removesongfromDB(n,"songs");

        newTable.removeRow(n);

        SongURl.remove(n);

    }

    public void AddSong(Mp3Song song) throws SQLException {

        addsongtoDB(song,"songs");

        SongURl.add(new String[]{getLastestSongId(),song.getURL()});

        newTable.addRow(new Object[]{SongURl.get(SongURl.size()-1)[0],

                song.getTitle(),song.getGenres(),song.getArtist(),song.getReleasedYear(), song.getComment()});

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
