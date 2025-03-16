package stock_prediction;

import java.sql.*;
import java.util.Scanner;

public class StockMarketManagement {

    private static final String URL = "jdbc:mysql://localhost:3306/StockMarketDB";
    private static final String USER = "charlie";
    private static final String PASSWORD = "[]";

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n1. Add stock");
            System.out.println("2. Remove stock");
            System.out.println("3. View stocks");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            choice = input.nextInt();
            input.nextLine();

            switch (choice) {
                case 1:
                    addStock(input);
                    break;
                case 2:
                    removeStock(input);
                    break;
                case 3:
                    viewStocks();
                    break;
                case 4:
                    System.out.println("Exiting the program...");
                    break;
                default:
                    System.out.println("Invalid choice, try again.");
            }
        } while (choice != 4);

        input.close();
    }

    private static void addStock(Scanner input) {
        System.out.print("Enter stock name: ");
        String name = input.nextLine();
        System.out.print("Enter stock quantity: ");
        int quantity = input.nextInt();
        System.out.print("Enter stock price: ");
        double price = input.nextDouble();

        String query = "INSERT INTO stocks (name, quantity, price) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setString(1, name);
            pstmt.setInt(2, quantity);
            pstmt.setDouble(3, price);
            pstmt.executeUpdate();
            System.out.println("Stock added successfully!");

        } catch (SQLException e) {
            System.out.println("Failed to add stock.");
            e.printStackTrace();
        }
    }

    private static void removeStock(Scanner input) {
        System.out.print("Enter stock name to remove: ");
        String name = input.nextLine();

        String query = "DELETE FROM stocks WHERE name = ?";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setString(1, name);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Stock removed successfully!");
            } else {
                System.out.println("Stock not found.");
            }

        } catch (SQLException e) {
            System.out.println("Failed to remove stock.");
            e.printStackTrace();
        }
    }

    private static void viewStocks() {
        String query = "SELECT * FROM stocks";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD); Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("\nStock List:");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + ". " + rs.getString("name")
                        + " | Quantity: " + rs.getInt("quantity")
                        + " | Price: $" + rs.getDouble("price"));
            }

        } catch (SQLException e) {
            System.out.println("Failed to retrieve stocks.");
            e.printStackTrace();
        }
    }
}
