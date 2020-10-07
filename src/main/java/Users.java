import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

class Users {
    private static final Logger LOGGER = Logger.getLogger(Users.class.getName());

    public static void insert(Connection connect) {
        Scanner sc = new Scanner(System.in);
        System.out.println("User name is : ");
        String name = sc.nextLine();

        String insertQuery = "INSERT INTO users(name) VALUES (?)";
        try (PreparedStatement prepStatement = connect.prepareStatement(insertQuery)) {
            prepStatement.setString(1, name);
            prepStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("exception : {}", e.getCause());
            e.printStackTrace();
        }
    }

    public static void update(Connection connect, int id) throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Are yoy really wanna edit name?:  1 - edit name || 0 - return");

        switch (App.switcher(1)) {
            case 1:
                System.out.println("Type new user name...");
                String name = sc.nextLine();

                String query = "UPDATE  users SET  name = ? WHERE id = ?";

                try (PreparedStatement prepStatement = connect.prepareStatement(query)) {
                    prepStatement.setString(1, name);
                    prepStatement.setInt(2, id);
                    prepStatement.executeUpdate();
                } catch (SQLException e) {
                    LOGGER.error("exception : {}", e.getCause());
                    e.printStackTrace();
                }
                break;
            case 0:
                break;
        }
    }

    public static void delete(Connection connect, int projectId) throws SQLException { // think about name
        String query = "DELETE FROM users WHERE  id = ?";
        try (PreparedStatement prepStatement = connect.prepareStatement(query)) {
            prepStatement.setInt(1, projectId);
            prepStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("exception : {}", e.getCause());
            e.printStackTrace();
        }
    }

    public static int viewCount(Connection connect) {
        String query = "SELECT COUNT(*) FROM users";
        try (PreparedStatement prepStatement = connect.prepareStatement(query)) {
            ResultSet resultSet = prepStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1);

        } catch (SQLException e) {
            LOGGER.error("exception : {}", e.getCause());
            e.printStackTrace();
        }
        return 0;
    }

    public static void viewAllUsers(Connection connect) {
        String query = "SELECT * FROM users";
        try (PreparedStatement prepStatement = connect.prepareStatement(query)) {
            ResultSet resultSet = prepStatement.executeQuery();

            System.out.println("+==================+");
            System.out.print("|");
            System.out.printf("%1s%7s%8s%n", "ID", "    NAME ", "|");
            System.out.println("+==================+");

            while (resultSet.next()) {

                System.out.print("| ");
                System.out.printf("%-5s%-11.9s%s%n",
                        resultSet.getInt("id"),
                        resultSet.getString("name"), " |");
                System.out.println("^------------------^");
            }

        } catch (SQLException e) {
            LOGGER.error("exception : {}", e.getCause());
            e.printStackTrace();
        }
    }

    public static void viewProjects(Connection connect, int id) {
        String query = "SELECT * FROM projects WHERE id = ?";
        try (PreparedStatement prepStatement = connect.prepareStatement(query)) {
            prepStatement.setInt(1, id);
            ResultSet resultSet = prepStatement.executeQuery();

            System.out.println("+==================+");
            System.out.print("|");
            System.out.printf("%-4s%-9s%-5s%1s%n", "ID", "NAME", "USER", "|");
            System.out.println("+==================+");

            while (resultSet.next()) {
                System.out.print("|");
                System.out.printf("%-4.4s%-11.9s%-3.4s%s%n", resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("ownerID"), "|");
                System.out.println("+==================+");
            }

        } catch (SQLException e) {
            LOGGER.error("exception : {}", e.getCause());
            e.printStackTrace();
        }
    }

    public static int viewProjectsCount(Connection connect, int id) {
        String query = "SELECT COUNT(*) FROM projects WHERE id = ?";
        try (PreparedStatement prepStatement = connect.prepareStatement(query)) {
            prepStatement.setInt(1, id);
            ResultSet resultSet = prepStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1);

        } catch (SQLException e) {
            LOGGER.error("exception : {}", e.getCause());
            e.printStackTrace();
        }
        return 0;
    }
}
