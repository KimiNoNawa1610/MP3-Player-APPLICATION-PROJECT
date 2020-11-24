import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class PlayList extends DB {
    private String name;
    private final DefaultTableModel newTable=new DefaultTableModel(Cname,0);

    public PlayList() throws SQLException {
    //default constructor
    }

    @Override
    public void AddSong(Mp3Song song) throws SQLException {

        Statement statement=connection.createStatement();

        System.out.println(song.getTitle());

        String getid="SELECT SongID FROM `songs` WHERE Title ='"+song.getTitle()+"'";

        ResultSet resultSet=statement.executeQuery(getid);

        String iD="";

        while(resultSet.next()){

            iD=resultSet.getString("SongID");

        }

        if(iD==""){

            Library lib=Library.getInstance();

            lib.AddSong(song);

            resultSet=statement.executeQuery(getid);

            while(resultSet.next()){

                iD=resultSet.getString("SongID");

            }

        }

        String insert="INSERT INTO `"+name+"` (`SongID`) VAlUES ("+iD+");";

        System.out.println(insert);

        statement.executeUpdate(insert);

        SongURl.add(new String[]{iD,song.getURL()});

        newTable.addRow(new Object[]{SongURl.get(SongURl.size()-1)[0],

                song.getTitle(),song.getGenres(),song.getArtist(),song.getReleasedYear(), song.getComment()});


    }

    @Override
    public void RemoveSong(int n) throws SQLException {

        removesongfromDB(n,name);

        newTable.removeRow(n);

        SongURl.remove(n);

    }

    public void createPlaylist() throws SQLException {
        Statement statement=connection.createStatement();
        String newtable="CREATE TABLE "+name+"(" +
                "SongID INTEGER not NULL, "+
                "FOREIGN KEY (SongID) REFERENCES Songs(SongID) ON DELETE CASCADE );";
        statement.executeUpdate(newtable);
        System.out.println("Creating new playlist");
        System.out.println("New playlist created \n");
        statement.close();
    }

    public JTable getTable() throws SQLException {

        try {

            System.out.println("Connected to SQL data base");

            Statement statement=connection.createStatement();

            String columns = "SELECT * FROM Songs INNER JOIN "+name+" ON "+name+".SongID = "+"Songs.SongID";

            ResultSet resultSet=statement.executeQuery(columns);

            System.out.println("Retrieveing information from SQL data base...");

            newTable.setRowCount(0);

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
            if(!SongURl.isEmpty()) {

                CurrentMax = Integer.parseInt(SongURl.get(SongURl.size() - 1)[0]);

            }

            System.out.println("Done");

        } catch (SQLException throwables) {

            throwables.printStackTrace();

        }
        JTable table = new JTable(newTable);

        return table;

    }

    public void deletePlaylist(String name) throws SQLException {
        Statement statement=connection.createStatement();
        String delete="DROP TABLE "+name;
        statement.executeUpdate(delete);
        System.out.println("deleted table: "+name);
    }

    public void setName(String name){

        this.name=name;

    }

    public String getName()
    {
        return this.name;
    }

    public ArrayList<String> getPlalistname() throws SQLException {
        ArrayList<String> names= new ArrayList<>();
        Statement statement=connection.createStatement();
        ResultSet resultset= statement.executeQuery("SHOW TABLES");
        while(resultset.next()){
            if(!resultset.getString(1).equals("songs")) {
                names.add(resultset.getString(1));
            }
        }
        return names;
    }

}

