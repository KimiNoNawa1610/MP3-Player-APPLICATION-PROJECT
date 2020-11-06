import java.sql.SQLException;
import java.sql.Statement;

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
                "URL VARCHAR(250), "+
                ");";
        statement.executeUpdate(newtable);
        System.out.println("Creating new playlist");
        System.out.println("New playlist created \n");

    }

    public void setName(String name){

        this.name=name;

    }
}
