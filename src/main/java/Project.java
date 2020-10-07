import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

//COMMENT ALL CLASS!!!
public class Project {
    private static final Logger LOGGER = Logger.getLogger(Project.class.getName());

    public static void insert(Connection connect) throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Project name: ");
        String pjName = sc.nextLine();
        if (Users.viewCount(connect) == 0) {
            System.out.println("There is no one user created, (return).");
            return;
        }
        System.out.println("Total users count: " + Users.viewCount(connect));
        Users.viewAllUsers(connect);
        System.out.println("Type user id: ");

        int id = Util.isDigit();

        while (true) {
            if (Util.checkUser(connect, id)) {
                String query = "INSERT INTO projects(id,name,ownerId) VALUES (?,?,?)";

                try (PreparedStatement prepStatement = connect.prepareStatement(query)) {
                    prepStatement.setInt(1, id);
                    prepStatement.setString(2, pjName);
                    prepStatement.setInt(3, id);
                    prepStatement.executeUpdate();
                    break;
                } catch (SQLException e) {
                    LOGGER.error("exception : {}", e.getCause());
                    e.printStackTrace();
                }
            } else {
                System.out.println("This ID is not assign to any user.. ");
                break;
            }
        }
    }


    public static void update(Connection connect, int projectID) throws SQLException {
        Scanner sc = new Scanner(System.in);
        boolean isFinished = true;// maybe without loop somehow?
        do {
            System.out.print("Select to change:\n1: project name\n2: user owner\n 0: return\n");
            switch (App.switcher(2)) {
                case 1:
                    System.out.print("Rename project: ");
                    String projectName = sc.nextLine();
                    String queryProject = "UPDATE projects SET name = ? WHERE id = ?";
                    try (PreparedStatement prepStatement = connect.prepareStatement(queryProject)) {
                        prepStatement.setString(1, projectName);
                        prepStatement.setInt(2, projectID);
                        prepStatement.executeUpdate();
                        break;
                    } catch (SQLException e) {
                        LOGGER.error("exception : {}", e.getCause());
                        e.printStackTrace();
                    }
                case 2:
                    if (Users.viewCount(connect) == 0) {
                        System.out.println("There is no one user created, (return).");
                        return;
                    }
                    while (true) {//may be debug point
                        Users.viewAllUsers(connect);
                        System.out.println("Users count: " + Users.viewCount(connect));
                        System.out.println("Now choose user id to assign to list above: " + "\n");

                        int id = Util.isDigit();

                        if (Util.checkUser(connect, id)) {
                            String queryUser = "update  projects set ownerID =? WHERE id = ?";
                            try (PreparedStatement prepStatement = connect.prepareStatement(queryUser)) {
                                prepStatement.setInt(1, id);
                                prepStatement.setInt(2, projectID);
                                prepStatement.executeUpdate();
                                break;
                            } catch (SQLException e) {
                                LOGGER.error("exception : {}", e.getCause());
                                e.printStackTrace();
                            }
                        } else {
                            System.out.println("This ID is not assign to any user.. ");
                        }
                    }
                    break;
                case 0:
                    return;
            }
            System.out.print("Keep updating?\n1: Yes\n0: No\n");
            switch (App.switcher(1)) {
                case 1:
                    isFinished = true;
                    break;
                case 0:
                    isFinished = false;
                    break;
            }
        } while (isFinished);
    }

    public static void delete(Connection connect, int projectID) {
        String query = "DELETE FROM projects WHERE ownerId = ?"; //edited recently!
        try (PreparedStatement prepStatement = connect.prepareStatement(query)) {
            prepStatement.setInt(1, projectID);
            prepStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("exception : {}", e.getCause());
            e.printStackTrace();
        }
    }

    public static void viewAllProjects(Connection connect) {
        String query = "SELECT * FROM projects";
        try (PreparedStatement prepStatement = connect.prepareStatement(query)) {
            ResultSet resultSet = prepStatement.executeQuery();
            System.out.println("+====================+");
            System.out.print("|");
            System.out.printf("%-4s%-9s%-5s%1s%n", " ID", " NAME", " USER", "  |");
            System.out.println("+====================+");

            while (resultSet.next()) {
                System.out.print("| ");
                System.out.printf("%-4.4s%-11.9s%-3.4s%s%n",
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("ownerID"), " |");
                System.out.println("^--------------------^");
            }
        } catch (SQLException e) {
            LOGGER.error("exception : {}", e.getCause());
            e.printStackTrace();
        }
    }

    public static int viewCount(Connection connect) {
        String query = "SELECT COUNT(*) FROM projects";
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

    public static void viewIssues(Connection connect, int uid) {
        String query = "SELECT * FROM issues WHERE ownerID = ?";
        try (PreparedStatement prepStatement = connect.prepareStatement(query)) {
            prepStatement.setInt(1, uid);
            ResultSet resultSet = prepStatement.executeQuery();
            System.out.println("+================================================================================================================================+");
            System.out.print("|");
            System.out.printf("%-4s%-11s%-14s%-14s%-61s%-5s%2s%n",
                    "ID", "NAME", "DESCRIPTION", "SEVERITY", "PRIORITY", "PROJECT", "|");
            System.out.println("+================================================================================================================================+");

            while (resultSet.next()) {
                System.out.println(Util.viewAllTable(connect, resultSet));
            }
        } catch (SQLException e) {
            LOGGER.error("exception : {}", e.getCause());
            e.printStackTrace();
        }
    }

    public static int viewIssuesCount(Connection connection, int uid) {
        String query = "SELECT count(*) FROM issues WHERE ownerID = ?";
        try (PreparedStatement prepStatement = connection.prepareStatement(query)) {
            prepStatement.setInt(1, uid);
            ResultSet resultSet = prepStatement.executeQuery();
            return resultSet.getInt(1);
        } catch (SQLException e) {
            LOGGER.error("exception : {}", e.getCause());
            e.printStackTrace();
        }
        return 0;
    }

    public static String getName(Connection connect, int id) {
        String query = "SELECT * FROM projects WHERE id = ?";
        try (PreparedStatement prepStatement = connect.prepareStatement(query)) {
            prepStatement.setInt(1, id);
            ResultSet resultSet = prepStatement.executeQuery();
            return resultSet.getString("name");
        } catch (SQLException e) {
            LOGGER.error("exception : {}", e.getCause());
            e.printStackTrace();
        }
        return "no name ";
    }
}
