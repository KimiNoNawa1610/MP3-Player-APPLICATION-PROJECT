import com.mpatric.mp3agic.*;
import java.io.IOException;
import java.util.Calendar;

public class Mp3Song {
    private String URl, Artist, Genres, Title, Comment,ReleasedYear;
    public Mp3Song(String url) throws InvalidDataException, IOException, UnsupportedTagException {
        URl=url;
        Mp3File newfile=new Mp3File(URl);
        boolean hasTagv1=newfile.hasId3v1Tag();
        if(hasTagv1==true){
            ID3v1 d3v1Tag= newfile.getId3v1Tag();
            Title=d3v1Tag.getTitle();
            Artist=d3v1Tag.getArtist();
            Genres=d3v1Tag.getGenreDescription();
            Comment=d3v1Tag.getComment();
            ReleasedYear=d3v1Tag.getYear();

        }
        else{
            ID3v2 d3v2Tag= newfile.getId3v2Tag();
            Title=d3v2Tag.getTitle();
            Artist=d3v2Tag.getArtist();
            Genres=d3v2Tag.getGenreDescription();
            Comment=d3v2Tag.getComment();
            ReleasedYear=d3v2Tag.getYear();
        }

    }

    public String getURL(){

        return URl;
    }

    public String getArtist(){
        if(Artist!=null){
            if(!Artist.isEmpty()){
                return Artist;
            }
        }

        return "Unknown";
    }

    public String getGenres(){
        if(Genres!=null){
            if(!Genres.isEmpty()){
                return Genres;
            }
        }

        return "Unknown";

    }

    public String getComment(){
        if(Comment!=null){
            if(!Comment.isEmpty()){
                return Comment;
            }
        }
        return "Unknown";
    }

    public String getTitle(){
        if(Title!=null){
            if(!Title.isEmpty()){
                return Title;
            }
        }

        return "Unknown";
    }

    public int getReleasedYear(){
        if(ReleasedYear!=null){
            if(!ReleasedYear.isEmpty()){
                return Integer.parseInt(ReleasedYear);
            }
        }

        return Calendar.getInstance().get(Calendar.YEAR);

    }

}
