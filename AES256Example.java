import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.Base64;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class AES256Example {

    // Tạo key co dinh theo chuan AES-256 (bit) ------------------ Tạo mã hoá
    private static SecretKey generateKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);  
        return keyGen.generateKey();
    }

    // Ma hoa chuoi bang AES, tra ve chuoi ma hoa bieu dien duoi dinh dang Base64 (De doc)
    // Truyen vao chuoi + key
    public static String encrypt(String plainText, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes); 
    }

    // Giai ma de Test thu xem ma hoa dung khong\
    // Truyen vao chuoi da ma hoa (Base64, tra ve chuoi ban dau)
    public static String decrypt(String encryptedText, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
        return new String(decryptedBytes);
    }

    // Lưu File thường (.txt) 
    public static void saveNormalFile ( String content , String fileName) {

        try {
            File file = new File( fileName + ".txt" );
            FileWriter writer = new FileWriter(file);

            writer.write(content);
            writer.close();

            System.out.println("Normal file saved : " + file.getAbsolutePath()); 
            // in ra path đang ở đâu 

        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    // Lưu file bảo mật .zip với nội dung mã hoá
    public static void saveConfidentialFile ( String content , String fileName,  SecretKey key ) {

        try {
            String encryptedContent = encrypt(content, key);
            File zipFile = new File(fileName + ".zip");
            FileOutputStream fos = new FileOutputStream(zipFile);
            ZipOutputStream zos = new ZipOutputStream(fos);
            ZipEntry entry = new ZipEntry(fileName + ".txt");
            zos.putNextEntry(entry);
            zos.write(encryptedContent.getBytes());
            zos.closeEntry();
            
            zos.close();
            System.out.println("File bao mat saved : " + zipFile.getAbsolutePath());
        } catch ( Exception e ) {
            e.printStackTrace();
        
        }
    }



    public static void main(String[] args) {
        try {
            // Tao Khoa bi mat
            SecretKey key = generateKey();

            // Dua vao mot chuoi ban dau
            String message = "A bottle of water";

            // Ma hoa va giai ma
            String encryptedMessage = encrypt(message, key);
            System.out.println("Chuoi sau khi ma hoa: " + encryptedMessage);

            // luu file thuong
            saveNormalFile(message, "NormalFile");

            // file bao mat ( ma hoa )
            saveConfidentialFile(message, "SecureFile", key);

            // Giai ma de test thu ma hoa ban dau co dung khong (Base64)
            String decryptedMessage = decrypt(encryptedMessage, key);
            System.out.println("Chuoi goc: " + decryptedMessage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}