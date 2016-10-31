package ca.imdc.newp;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by imdc on 18/08/2016.
 */

public class cryptoHash  {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES";
    public  void encrypt(String key, File inputFile, File outputFile)
            throws CryptoException {
        doCrypto(Cipher.ENCRYPT_MODE, key, inputFile, outputFile);
    }
    public  void decrypt(String key, File inputFile, File ouptutFile)
            throws CryptoException {
        doCrypto(Cipher.DECRYPT_MODE, key, inputFile, ouptutFile);
    }
    private  void doCrypto(int cipherMode, String key, File inputFile,
                           File outputFile) throws CryptoException {
        //android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        try {
            final int BUFFER_SIZE = 1024 * 4;
            byte[] buf = new byte[BUFFER_SIZE];
            Key secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(cipherMode, secretKey);
            FileInputStream inputStream = new FileInputStream(inputFile);
            FileOutputStream outputStream = new FileOutputStream(outputFile);
            CipherOutputStream out = new CipherOutputStream(outputStream, cipher);
            int nRead = 0;
            while((nRead = inputStream.read(buf)) >= 0)
            {
                out.write(buf,0,nRead);
            }
            out.close();
            inputStream.close();
            outputStream.close();
        } catch (NoSuchPaddingException | NoSuchAlgorithmException
                | InvalidKeyException | IOException ex){
            System.out.println(ex.getMessage());
        }
    }
}