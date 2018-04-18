/*
    Java implementation for RC4 + Vigenere Cipher by:
    Gelio Castro Gracida        150604
    Jose Alonso Zavala Pulido   149974
    Monica Inés Vela Jhanke     150251
 */
import java.util.*;

public class VRC4_Main {

    static int V;
    static String C1;
    static String C2;
    static int[] S = new int [256];
    static byte[] T = new byte [256];
    static byte[] cipher;
    static byte[] cipher2;
    static int keylen;

    public static void initialization(byte[] K) {
        int j = 0;
        keylen = K.length;

        for (int i = 0; i < 256; i++) {
            S[i] = i;
            T[i] = K[i % keylen];
        }

        //INITIAL PERMUTATION
        for (int i = 0; i < 256; i++) {
            j = (j + S[i] + T[i]) % 256;
            int temp = S[i];
            S[i] = S[j];
            S[j] = temp;
        }
    }

    public static byte[] stream_generation(byte[] M)
    {
        int i = 0;
        int j = 0;
        int t;
        int c = 0;
        int temp;
        cipher = new byte[M.length];
        cipher2 = new byte[M.length];

        while(c < M.length)
        {
            i = (i + 1) % 256;
            j = (j + S[i]) % 256;

            temp = S[i];
            S[i] = S[j];
            S[j] = temp;

            t = (S[i] + S[j]) % 256;
            cipher[c] = (byte) S[t];
            c++;
        }

        for(int l = 0; l< M.length; l++)
        {
            cipher2[l] = (byte) (cipher[l] ^ M[l]);
        }

        return cipher2;
    }

    public static byte[] decrypt(byte[] result1)
    {
        byte [] decipher = new byte[result1.length];
        for(int i = 0; i< result1.length; i++)
        {
            decipher[i] = (byte) (cipher[i] ^ cipher2[i]);
        }
        return decipher;
    }

    static String encryptVigenere(String text, final String key) {
        String res = "";
        for (int i = 0, j = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c < 'A' || c > 'Z') continue;
            res += (char)((c + key.charAt(j) - 2 * 'A') % 26 + 'A');
            j = ++j % key.length();
        }
        return res;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        //Leemos del usuario el mensaje que desea encriptar
        System.out.println("Ingrese el mensaje que desea encriptar");
        String message = scanner.nextLine().toUpperCase();
        byte[] M = message.getBytes();

        //Leemos la llave
        System.out.println("Ingrese la llave");
        String keyword = scanner.nextLine().toUpperCase();
        byte[] K = keyword.getBytes();

        initialization(K);
        byte[] result1 = (stream_generation(M));
        String encrypted = new String(result1);
        System.out.println("El cifrado por RC4 es: " + encrypted);

        V = (int) Math.floor(Math.random()*M.length-1);

        String encryptedC1;
        String keywordC1;
        String encryptedC2;
        String keywordC2;
        String keywordC1_;
        String keywordC2_;
        String C;

        encryptedC1 = encrypted.substring(0, V-1);
        keywordC1 = new String(T);
        keywordC1_ = keywordC1.substring(0, V-1);

        encryptedC2 = encrypted.substring(V+1, encrypted.length());
        keywordC2 = new String(T);
        keywordC2_ = keywordC2.substring(V+1, encrypted.length());

        C1 = encryptVigenere(encryptedC1, keywordC1_);
        //System.out.println(C1);
        C2 = encryptVigenere(encryptedC2, keywordC2_);
        //System.out.println(C2);
        C = C1 + C2 + V;
        System.out.println("RC4 + Vigenere: " + C);

        System.out.println("¿Desea desencriptar el mensaje? (s/n)");
        String answer = scanner.nextLine();
        if(answer.equals("s"))
        {
            String result2 = new String(decrypt(result1));
            System.out.println(result2);
        } else if (answer.equals("n"))
        {
            System.out.println("");
        }
    }
}
