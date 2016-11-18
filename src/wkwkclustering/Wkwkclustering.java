/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wkwkclustering;

import java.util.Scanner;

/**
 *
 * @author adarwawan
 */
public class Wkwkclustering {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        Scanner reader = new Scanner(System.in);  // Reading from System.in
        System.out.println("Enter filename: ");
        String s = reader.next();
        System.out.println(s);
        AccessClustering accessClustering = new AccessClustering(s, 1);
        // 1. kMeans 2. HierarCLus 3. myKMeans 4. MyAgnes
    }
    
}
