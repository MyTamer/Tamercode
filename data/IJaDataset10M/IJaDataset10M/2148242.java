package webcamstudio;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import webcamstudio.ffmpeg.ProcessRenderer;
import webcamstudio.streams.SourceDesktop;
import webcamstudio.streams.SourceMovie;
import webcamstudio.streams.SourceWebcam;

/**
 *
 * @author patrick
 */
public class TESTTCPIP {

    public static void main(String[] args) {
        File file = new File("/dev/video0");
        SourceDesktop movie = new SourceDesktop();
        movie.setRate(30);
        movie.read();
        while (movie.isPlaying()) {
            movie.getFrame();
            try {
                Thread.sleep(1000 / 1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(TESTTCPIP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
