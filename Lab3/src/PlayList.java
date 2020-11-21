import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class PlayList extends DB {
    private String name;
    private DefaultTableModel newTable=new DefaultTableModel(Cname,0);
    private JTable table;
    public PlayList() throws SQLException {

    }

    @Override
    public void AddSong(Mp3Song song) throws SQLException {

        addsongtoDB(song,name);

        SongURl.add(new String[]{getLastestSongId(),song.getURL()});

        newTable.addRow(new Object[]{SongURl.get(SongURl.size()-1)[0],

                song.getTitle(),song.getGenres(),song.getArtist(),song.getReleasedYear(), song.getComment()});
    }

    @Override
    public void RemoveSong(int n) throws SQLException {

        removesongfromDB(n, name);

        newTable.removeRow(n);

        SongURl.remove(n);

    }

    public void createPlaylist() throws SQLException {
        Statement statement=connection.createStatement();
        String newtable="CREATE TABLE "+name+"(" +
                "SongID INTEGER not NULL, "+
                "Title VARCHAR(250), "+
                "Genre VARCHAR(250), "+
                "Artist VARCHAR(250), "+
                "Year INTEGER, "+
                "Comment VARCHAR(350), "+
                "URL VARCHAR(250) "+
                ");";
        statement.executeUpdate(newtable);
        System.out.println("Creating new playlist");
        System.out.println("New playlist created \n");
        statement.close();
    }

    public JTable getTable() throws SQLException {

        try {

            System.out.println("Connected to SQL data base");

            Statement statement=connection.createStatement();

            String columns = "SELECT * FROM "+name;

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
            if(!SongURl.isEmpty()) {
                CurrentMax = Integer.parseInt(SongURl.get(SongURl.size() - 1)[0]);
            }

            System.out.println("Done");

        } catch (SQLException throwables) {

            throwables.printStackTrace();

        }
        table=new JTable(newTable);

        return table;

    }

    public void setTableVisibility(){
        table.setVisible(true);
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

    public ArrayList<String> getPlalistname() throws SQLException {
        ArrayList<String> names= new ArrayList<>();
        Statement statement=connection.createStatement();
        ResultSet resultset= statement.executeQuery("SHOW TABLES");
        while(resultset.next()){
            names.add(resultset.getString(1));
        }
        return names;
    }


}

