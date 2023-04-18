package it.develhope.javaTeam2Develhope.orders;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.time.LocalDate;

@Repository
public class OrdersRepo {
    private Connection conn;
    private BufferedReader reader;

    /**
     * Initialize connection through constructor
     */
   /* public OrdersRepo(){
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/ProjectDatabase?" +
                    "user='root'&password=c620918fazuKl420");
            reader = new BufferedReader(new InputStreamReader(System.in));
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }*/

    /**
     * This method allow to create a new order table record
     * @param orders An object of type Order that contains the new record
     */
    public void insertOrder(Orders orders) {
        try{
            System.out.println("--------------------INSERISCI ORDINE-----------------------");
            System.out.println("-----------------------------------------------------------");
            System.out.println("Peso:");
            orders.setWeight(Double.parseDouble(reader.readLine()));
            System.out.println("Data di ordine (YYYY-MM-DD):");
            orders.setDateOfOrder(LocalDate.parse(reader.readLine()));
            System.out.println("Data di spedizione (YYYY-MM-DD):");
            orders.setDateOfShipping(LocalDate.parse(reader.readLine()));
            System.out.println("Data di arrivo stimata (YYYY-MM-DD):");
            orders.setDateOfArrival(LocalDate.parse(reader.readLine()));
            System.out.println("È un regalo? (1=SI, 0=NO):");
            orders.setIsGift(Short.parseShort(reader.readLine()));
            System.out.println("Dettagli:");
            orders.setDetails(reader.readLine());
            System.out.println("Prezzo totale:");
            orders.setTotalPrice(Float.parseFloat(reader.readLine()));
            System.out.println("Quantità:");
            orders.setQuantity(Integer.parseInt(reader.readLine()));

            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO `order` (weight, dateOfOrder, dateOfShipping, dateOfArrival, isGift, " +
                            "details, totalPrice, quantity) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            stmt.setDouble(1, orders.getWeight());
            stmt.setDate(2, Date.valueOf(orders.getDateOfOrder()));
            stmt.setDate(3, Date.valueOf(orders.getDateOfShipping()));
            stmt.setDate(4, Date.valueOf(orders.getDateOfArrival()));
            stmt.setShort(5, orders.getIsGift());
            stmt.setString(6, orders.getDetails());
            stmt.setFloat(7, orders.getTotalPrice());
            stmt.setInt(8, orders.getQuantity());

            stmt.executeUpdate();
            System.out.println("Ordine creato");
            stmt.close();
        }catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Update an existing order record in the database
     * @param orders An object of type Order that contains the updated record
     */
    public void updateOrder(Orders orders) {
        try {
            System.out.println("--------------------AGGIORNA ORDINE-----------------------");
            System.out.println("-----------------------------------------------------------");
            System.out.println("ID dell'ordine da aggiornare:");
            int orderId = Integer.parseInt(reader.readLine());

            PreparedStatement selectStmt = conn.prepareStatement(
                    "SELECT * FROM `order` WHERE id = ?");
            selectStmt.setInt(1, orderId);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                System.out.println("Peso (kg):");
                orders.setWeight(Double.parseDouble(reader.readLine()));
                System.out.println("Data di ordine (YYYY-MM-DD):");
                orders.setDateOfOrder(LocalDate.parse(reader.readLine()));
                System.out.println("Data di spedizione (YYYY-MM-DD):");
                orders.setDateOfShipping(LocalDate.parse(reader.readLine()));
                System.out.println("Data di arrivo stimata (YYYY-MM-DD):");
                orders.setDateOfArrival(LocalDate.parse(reader.readLine()));
                System.out.println("È un regalo? (1=SI, 0=NO):");
                orders.setIsGift(Short.parseShort(reader.readLine()));
                System.out.println("Dettagli:");
                orders.setDetails(reader.readLine());
                System.out.println("Prezzo totale:");
                orders.setTotalPrice(Integer.parseInt(reader.readLine()));
                System.out.println("Quantità:");
                orders.setQuantity(Integer.parseInt(reader.readLine()));
                orders.setId(orderId);
                PreparedStatement updateStmt = conn.prepareStatement(
                        "UPDATE `order` SET weight = ?, dateOfOrder = ?, dateOfShipping = ?, dateOfArrival = ?, " +
                                "isGift = ?, details = ?, totalPrice = ?, quantity = ? WHERE id = ?");
                updateStmt.setDouble(1, orders.getWeight());
                updateStmt.setDate(2, Date.valueOf(orders.getDateOfOrder()));
                updateStmt.setDate(3, Date.valueOf(orders.getDateOfShipping()));
                updateStmt.setDate(4, Date.valueOf(orders.getDateOfArrival()));
                updateStmt.setShort(5, orders.getIsGift());
                updateStmt.setString(6, orders.getDetails());
                updateStmt.setFloat(7, orders.getTotalPrice());
                updateStmt.setInt(8, orders.getId());
                updateStmt.setInt(9, orders.getQuantity());
                updateStmt.executeUpdate();

                updateStmt.close();
                System.out.println("Ordine aggiornato");
            } else {
                System.out.println("Ordine non trovato");
            }
            selectStmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete an order record from the database
     */
    public void deleteOrder() {
        try {
            System.out.println("--------------------ELIMINA ORDINE-----------------------");
            System.out.println("-----------------------------------------------------------");
            System.out.println("id dell'ordine da eliminare: ");
            int orderId = Integer.parseInt(reader.readLine());
            PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM `order` WHERE id = ?");
            stmt.setInt(1, orderId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("Ordine non trovato");
            } else {
                System.out.println("Ordine eliminato");
            }
            stmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get an order record by its ID from the database
     * @param orders An object of type Order that contains the retrieved record
     * @return it returns the order that corresponds to the order record in the table
     */
    public Orders getOrderById(Orders orders) {
        try {
            System.out.println("--------------------TROVA ORDINE-----------------------");
            System.out.println("-----------------------------------------------------------");
            System.out.println("id del record:");
            int orderId = Integer.parseInt(reader.readLine());
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM `order` WHERE id = ?");

            stmt.setInt(1, orderId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    orders = new Orders();
                    orders.setId(rs.getInt("id"));
                    orders.setWeight(rs.getDouble("weight"));
                    orders.setDateOfOrder(rs.getDate("dateOfOrder").toLocalDate());
                    orders.setDateOfShipping(rs.getDate("dateOfShipping").toLocalDate());
                    orders.setDateOfArrival(rs.getDate("dateOfArrival").toLocalDate());
                    orders.setIsGift(rs.getShort("isGift"));
                    orders.setDetails(rs.getString("details"));
                    orders.setTotalPrice(rs.getFloat("totalPrice"));
                    orders.setQuantity(rs.getInt("quantity"));
                }else {
                    System.out.println("Ordine non trovato");
                }
            }
            stmt.close();
        }catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return orders;
    }
}

