package it.develhope.users;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql .*;
import java.time.LocalDate;
//METODI NON AGGIORNATI SECONDO I VALORI ATTUALI DI USER
    public class UsersRepo {
        private Connection conn;
        private BufferedReader reader;

        /**
         * Initialize connection through constructor
         */
        public UsersRepo(){
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
        }


        /**
         * This method allows to create a new user table record
         *
         * @param users An object of type User that contains the new record
         */
        public void insertUser(Users users) {
            try{
                System.out.println("--------------------INSERISCI UTENTE-----------------------");
                System.out.println("-----------------------------------------------------------");
                System.out.println("Type (1=DEVELOPER, 2=WRITER, 3=READER):");
                users.setType(UsersTypeEnum.values()[Integer.parseInt(reader.readLine()) - 1]);
                System.out.println("Nome:");
                users.setName(reader.readLine());
                System.out.println("Cognome:");
                users.setSurname(reader.readLine());
                System.out.println("Data di nascita (YYYY-MM-DD):");
                users.setBirthday(LocalDate.parse(reader.readLine()));
                System.out.println("Indirizzo:");
                users.setAddress(reader.readLine());
                System.out.println("Data di iscrizione (YYYY-MM-DD):");
                users.setDateOfSubscription(LocalDate.parse(reader.readLine()));
                PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO `user` (type, name, surname, birthday, address, dateOfSubscription) VALUES (?, ?, ?, ?, ?, ?)");
                stmt.setString(1, users.getType().toString());
                stmt.setString(2, users.getName());
                stmt.setString(3, users.getSurname());
                stmt.setDate(4, Date.valueOf(users.getBirthday()));
                stmt.setString(5, users.getAddress());
                stmt.setDate(6, Date.valueOf(users.getDateOfSubscription()));

                stmt.executeUpdate();
                System.out.println("Utente creato");
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
         * This method allows to update a user table record
         *
         * @param users An object of type User that contains the updated record
         */
        public void updateUser(Users users) {
            try {
                System.out.println("--------------------AGGIORNA UTENTE-----------------------");
                System.out.println("-----------------------------------------------------------");
                System.out.println("ID dell'utente da aggiornare:");
                int userId = Integer.parseInt(reader.readLine());
                PreparedStatement stmt = conn.prepareStatement(
                        "SELECT * FROM `user` WHERE id = ?");
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    System.out.println("Type (1=DEVELOPER, 2=WRITER, 3=READER):");
                    users.setType(UsersTypeEnum.values()[Integer.parseInt(reader.readLine()) - 1]);
                    System.out.println("Nome:");
                    users.setName(reader.readLine());
                    System.out.println("Cognome:");
                    users.setSurname(reader.readLine());
                    System.out.println("Data di nascita (YYYY-MM-DD):");
                    users.setBirthday(LocalDate.parse(reader.readLine()));
                    System.out.println("Indirizzo:");
                    users.setAddress(reader.readLine());
                    System.out.println("Data di iscrizione (YYYY-MM-DD):");
                    users.setDateOfSubscription(LocalDate.parse(reader.readLine()));
                    users.setId(userId);
                    PreparedStatement updateStmt = conn.prepareStatement(
                            "UPDATE `user` SET type=?, name=?, surname=?, birthday=?, address=?, dateOfSubscription=? WHERE id=?");
                    updateStmt.setString(1, users.getType().toString());
                    updateStmt.setString(2, users.getName());
                    updateStmt.setString(3, users.getSurname());
                    updateStmt.setDate(4, Date.valueOf(users.getBirthday()));
                    updateStmt.setString(5, users.getAddress());
                    updateStmt.setDate(6, Date.valueOf(users.getDateOfSubscription()));
                    updateStmt.setInt(7, users.getId());
                    updateStmt.executeUpdate();
                } else {
                    System.out.println("Utente non trovato.");
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
         * This method deletes a user table record
         */
        public void deleteUser() {
            try {
                System.out.println("--------------------ELIMINA UTENTE-----------------------");
                System.out.println("-----------------------------------------------------------");
                System.out.println("ID dell'utente da eliminare:");
                int userId = Integer.parseInt(reader.readLine());
                PreparedStatement stmt = conn.prepareStatement(
                        "SELECT * FROM `user` WHERE id = ?");
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    PreparedStatement deleteStmt = conn.prepareStatement(
                            "DELETE FROM `user` WHERE id = ?");
                    deleteStmt.setInt(1, userId);
                    deleteStmt.executeUpdate();
                } else {
                    System.out.println("Utente non trovato.");
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
         * This method allows to get a user record from the id
         * @param users An object of type User
         * @return the user that corresponds to the user record in the table
         */
        public Users getUserById(Users users) {
            try{
                System.out.println("--------------------TROVA UTENTE-----------------------");
                System.out.println("-----------------------------------------------------------");
                    System.out.println("ID dell'utente:");
                    int userId = Integer.parseInt(reader.readLine());
                PreparedStatement stmt = conn.prepareStatement(
                        "SELECT * FROM `user` WHERE id = ?");
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    users = new Users();
                    users.setId(rs.getInt("id"));
                    users.setType(UsersTypeEnum.valueOf(rs.getString("type")));
                    users.setName(rs.getString("name"));
                    users.setSurname(rs.getString("surname"));
                    users.setBirthday(rs.getDate("birthday").toLocalDate());
                    users.setAddress(rs.getString("address"));
                    users.setDateOfSubscription(rs.getDate("dateOfSubscription").toLocalDate());
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
            return users;
        }
    }
