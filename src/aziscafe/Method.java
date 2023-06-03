/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aziscafe;

import java.security.MessageDigest;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 *
 * @author azisr
 */
public class Method {
    public static String id, name, email, password, phone_number, position;
    public static String imagePath = "images/"; 
    public static String savePath = "saves/";
    public static String printPath = "prints/";
    public static int spcKey = 22, spcValue = 18, orderId, formIndex;
    public static LocalDate date;
    public static ArrayList<OrderMenu> orderMenus = new ArrayList();
    public static double cashPay = 0;
    public static Cashier cashier;
    public static OrderHistory orderHistory;

    public static String curr(Double value) {
        return NumberFormat.getCurrencyInstance(new Locale("id", "ID")).format(value);
    }
    
    private static String sha512(final String base) {
        try{
            final MessageDigest digest = MessageDigest.getInstance("SHA-512");
            final byte[] hash = digest.digest(base.getBytes("UTF-8"));
            final StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < hash.length; i++) {
                final String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) 
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }
    
    public static String encrypt(final String base) {
        return sha512(sha512(base));
    }
    
    public static boolean isInteger(String number) {
        try {
            Integer.valueOf(number);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    public static boolean isNumber(String number) {
        try {
            for (char st: number.toCharArray()) {
                Integer.valueOf(String.valueOf(st));
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    public static boolean isDouble(String number) {
        try {
            Double.valueOf(number);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    public static boolean isEmail(String email) {
        String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
        return EMAIL_PATTERN.matcher(email).matches();
    }
}
