import javax.swing.tree.DefaultMutableTreeNode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class PlayList extends Library{
    private String name;
    public PlayList() throws SQLException {

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

    public void deletePlaylist(DefaultMutableTreeNode name) throws SQLException {
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

