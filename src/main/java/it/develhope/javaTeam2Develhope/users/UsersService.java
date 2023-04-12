package it.develhope.javaTeam2Develhope.users;

import java.io.*;
import java.sql.*;
import java.util.List;
import java.util.Scanner;

public class UsersService {

    public static void authorizeSub(){};
    public static void authorizePay(){};
    public static void subscribe() throws SQLException {
        Connection conn = null;
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/newdb", "viewer", "**********");
        Statement statement = conn.createStatement();
        Scanner scanner = new Scanner(System.in);
        String query = scanner.nextLine();
        ResultSet rs = statement.executeQuery(query);
        System.out.println("Printing your query...");
        while (rs.next()){
        rs = statement.getResultSet();//controllare se getResultSet e rs.GetString non facciano la stessa cosa
        String queryPrint = rs.getString(query);//vedi sopra
        System.out.println("Congrats! You're subscribed");}
    };
    public static void pay(){};
    public static void eraseUser(){};
    public static void addUSer(){};
    public static void eraseSub(){};
    public static void finaliseOrder(){};
    public static void addBook(){};
    public static void eraseBook(){};

    public static void createFile(String fileName, List<String[]> book) {
        File file = new File(fileName + ".txt");
        try (FileWriter writer = new FileWriter(file, true)) {
            for (String[] row : book) {
                String rowData = String.join("\t                   ", row);
                writer.write(rowData + System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*public static void add() throws IOException {

        //TODO rename or modify or delete variables and objects below:

        List<List<String[]>> data = new ArrayList<>();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Inserisci il titolo del libro: ");
        title = reader.readLine();

        System.out.print("Inserisci l'autore del libro: ");
        author = reader.readLine();

        System.out.print("Inserisci l'argomento del libro: ");
        topic = reader.readLine();

        System.out.print("Inserisci l'editore del libro: ");
        publisher = reader.readLine();

        System.out.print("Inserisci anno: ");
        year = Integer.parseInt(reader.readLine());

        System.out.print("Inserisci ISBN: ");
        ISBN = reader.readLine();


        List<String[]> info = Collections.singletonList(new String[]{title, author, topic, publisher, String.valueOf(year), ISBN});
        data.add(info);
        DatabaseFileCreator fileCreator = new DatabaseFileCreator();
        fileCreator.createFile("book_database", info);

    }*/

    public static void display() {
        try(BufferedReader reader = new BufferedReader(new FileReader("book_database.txt"));){
            String bookDB = reader.readLine();
            while (bookDB != null) { //in questo modo legge direttamente il file e non l'array
                System.out.println(bookDB);
                bookDB = reader.readLine();}
        } catch (IOException e) {
            throw new RuntimeException(e);}}


    public static void erase() throws FileNotFoundException {
        PrintWriter eraser = new PrintWriter("book_database.txt");
        eraser.close();
    }

}

