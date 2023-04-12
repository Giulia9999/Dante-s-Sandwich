package it.develhope.javaTeam2Develhope.books;


import java.util.List;

public class BooksRepo {

    private static List<String> bookRow;
    private static final String fileDB = "book_database.txt";

    /*public static void main(String[] args){

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            System.out.println("======================================");
            System.out.println("1. Aggiungi un libro");
            System.out.println("2. Visualizza il database dei libri");
            System.out.println("3. Cerca un libro");
            System.out.println("4. Cancella libri");
            System.out.println("5. Esci");
            System.out.print("Scegli un'opzione: \n");
            System.out.println("======================================");

            int option;
            try {
                option = Integer.parseInt(reader.readLine());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            switch (option) {
                case 1 -> {
                    add(fileDB);
                    System.out.println("Aggiunto con successo!");
                }
                case 2 -> {
                    System.out.println("-----LISTA DEI LIBRI-----");
                    System.out.println("Titolo\t                   Autore\t                  Argomento\t                  Editore\t                   Anno\t                   ISBN");
                    display(fileDB);
                }
                case 3 -> {
                    System.out.println("Cerca libro nel database: ");
                    searching(fileDB, bookRow);
                }
                case 4 -> {
                    erase(fileDB);
                    System.out.println("DB Cancellato!");
                }
                case 5 -> {
                    System.out.println("Arrivederci!");
                    System.exit(0);
                }
                default -> System.out.println("Opzione non valida");
            }
        }
    }


    public static void setBookRow(List<String> bookRow) {
        BookDB.bookRow = bookRow;
    }*/
}




