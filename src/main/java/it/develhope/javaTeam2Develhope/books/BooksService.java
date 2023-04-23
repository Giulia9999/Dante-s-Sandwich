package it.develhope.javaTeam2Develhope.books;

import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BooksService {
    /*public static void add(String fileDB) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        try {
            System.out.print("Inserisci il titolo del libro: ");
            book.setTitle(reader.readLine());

            System.out.print("Inserisci l'autore del libro: ");
            book.setAuthor(reader.readLine());

            System.out.print("Inserisci l'argomento del libro: ");
            book.setTopic(reader.readLine());

            System.out.print("Inserisci l'editore del libro: ");
            book.setPublisher(reader.readLine());

            System.out.print("Inserisci anno: ");
            book.setYear(Integer.parseInt(reader.readLine()));

            System.out.print("Inserisci ISBN: ");
            book.setISBN(reader.readLine());
        }catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<String[]> info = Collections.singletonList(new String[]{book.getTitle(), book.getAuthor(), book.getTopic(), book.getPublisher(), String.valueOf(book.getYear()), book.getISBN()});
        createFile(fileDB, info);

    }*/

    private static void createFile(String fileName, List<String[]> book) {
        File file = new File(fileName);
        try (FileWriter writer = new FileWriter(file, true)) {
            for (String[] row : book) {
                String rowData = String.join("\t                   ", row);
                writer.write(rowData + System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void display(String file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file));) {
            String bookDB = reader.readLine();
            while (bookDB != null) { //in questo modo legge direttamente il file e non l'array
                System.out.println(bookDB);
                bookDB = reader.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void erase(String file) {
        PrintWriter eraser;
        try {
            eraser = new PrintWriter(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        eraser.close();
    }

    public static void searching(String fileName, List<String> database) {
        Scanner scanner = new Scanner(System.in);
        String search = scanner.nextLine();
        List<String> dataToRead = new ArrayList<>();
        searchInDatabase(fileName, search, database);
    }

    public static List<String> searchBook(String text, List<String> bookData) {
        List<String> searchWords = Arrays.asList(text.trim().toLowerCase().split("\\s+"));
        return bookData.stream()
                .filter(input -> containsAllWords(input.toLowerCase(), searchWords))
                .collect(Collectors.toList());
    }

    private static boolean containsAllWords(String input, List<String> searchWords) {
        return searchWords.stream().allMatch(input::contains);
    }

    private static void searchInDatabase(String fileName, String search, List<String> database) {
        database = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                database.add(line);
            }
            List<String> searchResults = searchBook(search, database);
            printSearchResults(searchResults);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void printSearchResults(List<String> searchResults) {
        System.out.println("Search results:");
        if (searchResults.isEmpty()) {
            System.out.println("No results found.");
        } else {
            searchResults.forEach(System.out::println);
        }
    }

}