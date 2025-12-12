/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package educover.backend;

import java.util.Random;

/**
 *
 * @author aizik
 */
public class GenerateOTP {
    public static String generateOTPNumber() {
        Random rand = new Random();
        int otp = rand.nextInt(900000)+ 100000;
        return String.valueOf(otp);  
    }
}
