package classroom.filmfestival_a;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.List;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import database.MySessionFactory;
import database.festivaldatabase.DirectorName;
import database.festivaldatabase.FilmTitle;

public class Movies08 {

    public static final String RESULT = "results/classroom/filmfestival/movies08.pdf";

    public static final Logger LOGGER = Logger.getLogger(Movies08.class.getName());

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        Document document = new Document();
        try {
            OutputStream os = new FileOutputStream(RESULT);
            PdfWriter writer = PdfWriter.getInstance(document, os);
            writer.setStrictImageSequence(true);
            document.open();
            Session session = (Session) MySessionFactory.currentSession();
            Query q = session.createQuery("from FilmTitle order by title");
            java.util.List<FilmTitle> results = q.list();
            File f;
            Image img;
            Paragraph p;
            Chunk c;
            Font bold = new Font(Font.HELVETICA, 12, Font.BOLD);
            Font italic = new Font(Font.HELVETICA, 12, Font.ITALIC);
            for (FilmTitle movie : results) {
                f = new File("resources/classroom/filmposters/" + movie.getFilmId() + ".jpg");
                if (f.exists()) {
                    img = Image.getInstance(f.getPath());
                    img.scaleToFit(72, 144);
                    document.add(img);
                }
                p = new Paragraph(20);
                c = new Chunk(movie.getTitle(), bold);
                c.setAnchor("http://cinema.lowagie.com/titel.php?id=" + movie.getFilmId());
                p.add(c);
                c = new Chunk(" (" + movie.getYear() + ") ", italic);
                p.add(c);
                c = new Chunk("IMDB");
                c.setAnchor("http://www.imdb.com/title/tt" + movie.getImdb());
                p.add(c);
                document.add(p);
                Set<DirectorName> directors = movie.getDirectorNames();
                List list = new List();
                for (DirectorName director : directors) {
                    list.add(director.getName());
                }
                document.add(list);
            }
            document.close();
        } catch (IOException e) {
            LOGGER.error("IOException: ", e);
        } catch (DocumentException e) {
            LOGGER.error("DocumentException: ", e);
        }
    }
}
