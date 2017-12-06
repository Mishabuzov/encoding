package huffman;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;

/**
 * Битовый поток
 */

public class BitOutputStream {

    private OutputStream output; // байтовый поток

    private int currentByte; // текущий байт

    private int numBitsInCurrentByte; // кол-во битов в текущем байте



    public BitOutputStream(OutputStream out) {

        if (out == null){
            throw new NullPointerException("Argument is null");
        }
        output = out;
        currentByte = 0;
        numBitsInCurrentByte = 0;
    }



    public void write(int b) throws IOException {
        if (!(b == 0 || b == 1)){
            throw new IllegalArgumentException("argument must be 0 or 1");
        }
        currentByte = currentByte << 1 | b;
        numBitsInCurrentByte++;

        //Если байт полный, то записываем его
        if (numBitsInCurrentByte == 8) {

            output.write(currentByte);
            numBitsInCurrentByte = 0;
        }
    }

    //дописываем 0 если кол-во битов в байте не равно 8
    public void flush() throws IOException{

        while (numBitsInCurrentByte != 0){
            write(0);
        }
    }

}
