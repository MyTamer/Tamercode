package test;

import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.itadaki.bzip2.BZip2DivSufSort;
import org.junit.Test;

/**
 * Tests BZip2DivSufSort
 */
public class TestBZip2DivSufSort {

    /**
	 * Tests a bug whereby trIntroSort() doesn't terminate
	 * @throws IOException
	 */
    @Test(timeout = 2000)
    public void testBug1() throws IOException {
        String expectedData = "dddddddddeeeeeeeeesssssssssyyyyyyyyy,,,,,,,,,eeeeeeeeeaaaaaaaaassssssssseeeeeeeeesss" + "ssssssbbbbbbbbbwwwwwwwww         hhhhhhhhhlllllllllMMMMMMMMM         wwwwwwwwwmmmmmm" + "mmmeeeeeeeeeaaaaaaaaatttttttttlllllllllccccccccceeeeeeeeelllllllll                  " + "wwwwwwwwwhhhhhhhhh         lllllllll         tttttttttfffffffff         aaaaaaaaasss" + "ssssssnnnnnnnnnaaaaaaaaatttttttttaaaaaaaaaaaaaaaaaa         iiiiiiiiitttttttttiiiiii" + "iiiiiiiiiiiiooooooooo                  rrrrrrrrr ";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        for (int i = 0; i < 9; i++) {
            outputStream.write("Mary had a little lamb, its fleece was white as snow".getBytes());
        }
        outputStream.write(0);
        byte[] input = outputStream.toByteArray();
        input[input.length - 1] = input[0];
        int[] output = new int[input.length];
        new BZip2DivSufSort(input, output, input.length - 1).bwt();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < output.length; i++) {
            sb.append((char) output[i]);
        }
        assertEquals(expectedData, sb.toString());
    }
}
