package entagged.audioformats.mp3.util;

import entagged.audioformats.exceptions.*;
import entagged.audioformats.mp3.*;
import java.io.*;

public class Id3v1TagReader {

    public Id3v1Tag read(RandomAccessFile raf) throws CannotReadException, IOException {
        Id3v1Tag tag = new Id3v1Tag();
        raf.seek(raf.length() - 128);
        byte[] b = new byte[3];
        raf.read(b);
        raf.seek(0);
        String tagS = new String(b);
        if (!tagS.equals("TAG")) {
            throw new CannotReadException("There is no Id3v1 Tag in this file");
        }
        raf.seek(raf.length() - 128 + 3);
        String songName = read(raf, 30);
        String artist = read(raf, 30);
        String album = read(raf, 30);
        String year = read(raf, 4);
        String comment = read(raf, 30);
        String trackNumber = "";
        raf.seek(raf.getFilePointer() - 2);
        b = new byte[2];
        raf.read(b);
        if (b[0] == 0) {
            Integer track = new Integer(b[1]);
            trackNumber = track.toString();
        }
        byte genreByte = raf.readByte();
        raf.seek(0);
        tag.setTitle(songName);
        tag.setArtist(artist);
        tag.setAlbum(album);
        tag.setYear(year);
        tag.setComment(comment);
        tag.setTrack(trackNumber);
        tag.setGenre(tag.translateGenre(genreByte));
        return tag;
    }

    private String read(RandomAccessFile raf, int length) throws IOException {
        byte[] b = new byte[length];
        raf.read(b);
        String ret = new String(b).trim();
        int i = ret.indexOf(" ");
        if (i != -1) return ret.substring(0, i + 1);
        return ret;
    }
}
