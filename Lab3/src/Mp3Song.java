import com.mpatric.mp3agic.*;
import java.io.IOException;
import java.util.Calendar;

public class Mp3Song {
    private String URl, Artist, Genres, Title, ReleasedYear;
    public Mp3Song(String url) throws InvalidDataException, IOException, UnsupportedTagException {
        URl=url;
        Mp3File newfile=new Mp3File(URl);
        boolean hasTagv1=newfile.hasId3v1Tag();
        if(hasTagv1==true){
            ID3v1 d3v1Tag= newfile.getId3v1Tag();
            Title=d3v1Tag.getTitle();
            Artist=d3v1Tag.getArtist();
            Genres=d3v1Tag.getGenreDescription();
            ReleasedYear=d3v1Tag.getYear();
        }
        else{
            ID3v2 d3v2Tag= newfile.getId3v2Tag();
            Title=d3v2Tag.getTitle();
            Artist=d3v2Tag.getArtist();
            Genres=d3v2Tag.getGenreDescription();
            ReleasedYear=d3v2Tag.getYear();
        }

    }

    public String getURL(){
        return URl;
    }

    public String getArtist(){
        if(Artist!=null){
            return Artist;
        }
        else{
            return "Unknown";
        }
    }

    public String getGenres(){
        if(Genres!=null){
            return Genres;
        }
        else{
            return "Unknown";
        }

    }

    public String getTitle(){
        if(Title!=null){
            return Title;
        }
        else{
            return "Unknown";
        }
    }

    public int getReleasedYear(){
        if(ReleasedYear!=null){
            return Integer.parseInt(ReleasedYear);
        }
        else{
            return Calendar.getInstance().get(Calendar.YEAR);
        }
    }

}
