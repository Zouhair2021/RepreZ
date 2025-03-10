package com.zouhair;

import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Demander à l'utilisateur d'entrer un nombre
        System.out.print("Entrez un nombre entre 1 et 9999 : ");
        int nombre = scanner.nextInt();

        // Vérifier si le nombre est dans la plage valide
        if (nombre < 1 || nombre > 9999) {
            System.out.println("Le nombre doit être entre 1 et 9999");
            return;
        }

        // Extraire les différents chiffres
        int milliers = nombre / 1000;
        int centaines = (nombre % 1000) / 100;
        int dizaines = (nombre % 100) / 10;
        int unites = nombre % 10;

        // Afficher les résultats
        System.out.println("\nDécomposition du nombre " + nombre + " :");

        if (milliers > 0) {
            System.out.println("Milliers : " + milliers);
        }
        if (milliers > 0 || centaines > 0) {
            System.out.println("Centaines : " + centaines);
        }
        if (milliers > 0 || centaines > 0 || dizaines > 0) {
            System.out.println("Dizaines : " + dizaines);
        }
        System.out.println("Unités : " + unites);

        scanner.close();
    }
}