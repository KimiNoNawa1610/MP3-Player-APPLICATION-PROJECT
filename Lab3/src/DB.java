import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

abstract class DB {
    protected  String[] Cname={"ID","Tilte","Genre","Artist","Released Year","Comment"};

    protected  String user="root";

    protected  String password="";

    protected  String url="jdbc:mysql://localhost:3306/mp3player";

    protected  Connection connection = DriverManager.getConnection(url, user, password);

    protected  ArrayList<String[]> SongURl=new ArrayList<>();

    protected static int CurrentMax=0;

    protected DB() throws SQLException {}

    public abstract void AddSong(Mp3Song song) throws SQLException;

    public abstract void RemoveSong(int n) throws SQLException;

    public int getSongURLSize(){

        return SongURl.size();

    }

    protected void addsongtoDB(Mp3Song song, String name) throws SQLException {
        String insert="INSERT INTO "+name+" (Title, Genre, Artist, Year, Comment, URL) VALUES (?,?,?,?,?,?)";

        PreparedStatement preparedStatement=MakeSQLStatement(insert);

        preparedStatement.setString(1,song.getTitle());

        preparedStatement.setString(2,song.getGenres());

        preparedStatement.setString(3,song.getArtist());

        preparedStatement.setInt(4,song.getReleasedYear());

        preparedStatement.setString(5,song.getComment());

        preparedStatement.setString(6,song.getURL());

        preparedStatement.executeUpdate();

        if(CurrentMax!=0){
            CurrentMax++;
        }

    }

    protected void removesongfromDB(int n, String name) throws SQLException {
        String delete="DELETE FROM "+name+" WHERE SongID=" +getSongid(n);
        System.out.println(CurrentMax);
        PreparedStatement preparedStatement=MakeSQLStatement(delete);
        preparedStatement.executeUpdate();
    }

    public Boolean isEmpty(){

        return (SongURl.size()==0) ? true:false;

    }

    public String getLastestSongId(){

        if(CurrentMax==0){
            CurrentMax=Integer.parseInt(SongURl.get(SongURl.size()-1)[0]);
        }

        return Integer.toString(CurrentMax);

    }

    public Boolean SongExist(Mp3Song song){

        for(int i=0;i< SongURl.size();i++){

            if(song.getURL().equals(SongURl.get(i)[1])){

                return true;

            }
        }

        return false;
    }

    protected PreparedStatement MakeSQLStatement(String n) throws SQLException {

        PreparedStatement preparedStatement=connection.prepareStatement(n);

        return preparedStatement;
    }

    public String getElement(int n){

        return SongURl.get(n)[1];

    }

    public String getSongid(int n){

        return SongURl.get(n)[0];

    }

}
