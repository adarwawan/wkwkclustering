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
        int choose = 5; 
        String filename = "";
        
        while (choose != 6) {
            if (choose == 5) {
                //input filename
                Scanner reader = new Scanner(System.in);  // Reading from System.in
                System.out.println("Enter filename: ");
                filename = reader.next();
            }
            //Menu
            System.out.println("------- Menu ------");
            System.out.println("1. Weka KMeans Clustering");
            System.out.println("2. Weka Hierarchical Clustering");
            System.out.println("3. myAgnes");
            System.out.println("4. myKMeans");
            System.out.println("5. Change File");
            System.out.println("6. Exit");

            Scanner reader = new Scanner(System.in);
            String s = reader.next();
            choose = Integer.parseInt(s);
//            System.out.println(s);
            if (choose < 5){
                AccessClustering accessClustering = new AccessClustering(filename, choose);
            }
        }
    }
}
