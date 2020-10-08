import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Issues {
    private static final Logger LOGGER = Logger.getLogger(Issues.class.getName());

    public static void insert(Connection connect) throws SQLException {
        LOGGER.info("Hello from issues inserter method!");
        Scanner scanner = new Scanner(System.in);

        System.out.println("Write issue name");
        String issueName = scanner.nextLine();

        Project.viewAllProjects(connect);
        System.out.println("Write user id(owner of the project)\nSee table above  ^ ");
        int id = Util.isDigit();

        System.out.println("Write steps to reproduce(bug)");
        String description = scanner.nextLine();

        String sevLevel;
        System.out.println(Util.issueSeverity);
        sevLevel = Util.getIssueSeverity();

        String priLevel;
        System.out.println(Util.issuePriority);
        priLevel = Util.getIssuePriority();

        while (true) {
            if (Util.checkUser(connect, id)) {
                String query = "INSERT INTO issues(id,name,descriptions,severity,priority,ownerId) VALUES(?,?,?,?,?,?) "; //recent correctness!
                try (PreparedStatement prepStatement = connect.prepareStatement(query)) {
                    prepStatement.setInt(1, id);
                    prepStatement.setString(2, issueName);
                    prepStatement.setString(3, description);
                    prepStatement.setString(4, sevLevel);
                    prepStatement.setString(5, priLevel);
                    prepStatement.setInt(6, id);
                    prepStatement.executeUpdate();
                    break;
                } catch (SQLException e) {
                    LOGGER.error("exception : {}", e.getCause());
                    e.printStackTrace();
                }
            } else {
                System.out.println("This id is not assign to any project\nWanna try again or brake?\n1 : YES\nAny symbol : NO");
                int choice = Util.isDigit();
                if (choice == 1) {
                    return;
                } else {
                    break;
                }
            }
        }
    }

    public static void update(Connection connect, int issueId) throws SQLException {
        System.out.println(Util.issueUpdate);
        switch (App.switcher(5)) {
            case 1:
                System.out.println("Type new name of issue");
                Scanner scanner = new Scanner(System.in);
                String name = scanner.nextLine();

                String query1 = "UPDATE issues SET name = ? WHERE id = ?";

                try (PreparedStatement prepStatement = connect.prepareStatement(query1)) {
                    prepStatement.setString(1, name);
                    prepStatement.setInt(2, issueId);
                    prepStatement.executeUpdate();
                } catch (SQLException e) {
                    LOGGER.error("exception : {}", e.getCause());
                    e.printStackTrace();
                }
                break;
            //RECENTLY FIX!!!
            case 2:
                if (Project.viewCount(connect) == 0) {
                    System.out.println("There is  no one project created,(return)");
                    return;
                }
                Project.viewAllProjects(connect);
                System.out.println("Projects count : " + Project.viewCount(connect));
                System.out.println("Write user id(owner of the project)\nSee table above  ^ ");
                int id = Util.isDigit();

                if (Util.checkUser(connect, id)) {
                    String query2 = "UPDATE  issues SET ownerId = ? WHERE id = ?";
                    try (PreparedStatement prepStatement = connect.prepareStatement(query2)) {
                        prepStatement.setInt(1, id);
                        prepStatement.setInt(2, issueId);
                        prepStatement.executeUpdate();
                        break;
                    } catch (SQLException e) {
                        LOGGER.error("exception : {}", e.getCause());
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("This id is not assign to any project\nWanna try again or brake?\n1 : YES\nAny symbol : NO");
                    int choice = Util.isDigit();
                    if (choice == 1) {
                        return;
                    } else {
                        break;
                    }
                }
            case 3:
                System.out.println("Updating description, write steps to reproducing bug");
                Scanner scan = new Scanner(System.in);
                String steps = scan.nextLine();

                String query = "UPDATE issues SET description = ? WHERE id = ?";

                try (PreparedStatement prepStatement = connect.prepareStatement(query)) {
                    prepStatement.setString(1, steps);
                    prepStatement.setInt(2, issueId);
                    prepStatement.executeUpdate();
                } catch (SQLException e) {
                    LOGGER.error("exception : {}", e.getCause());
                    e.printStackTrace();
                }
                break;
            case 4:
                System.out.println(Util.issueSeverity);
                String sevLevel = Util.getIssueSeverity();
                String query4 = "UPDATE issue SET severity = ? WHERE id = ?";
                try (PreparedStatement prepStatement = connect.prepareStatement(query4)) {
                    prepStatement.setString(1, sevLevel);
                    prepStatement.setInt(2, issueId);
                    prepStatement.executeUpdate();
                } catch (SQLException e) {
                    LOGGER.error("exception : {}", e.getCause());
                    e.printStackTrace();
                }
                break;
            case 5:
                System.out.println(Util.issuePriority);
                String priLevel = Util.getIssuePriority();
                String query5 = "UPDATE issue SET priority = ? WHERE id = ?";
                try (PreparedStatement prepStatement = connect.prepareStatement(query5)) {
                    prepStatement.setString(1, priLevel);
                    prepStatement.setInt(2, issueId);
                    prepStatement.executeUpdate();
                } catch (SQLException e) {
                    LOGGER.error("exception : {}", e.getCause());
                    e.printStackTrace();
                }
                break;
        }

    }

    public static void delete(Connection connect, int issueId) {
        String query = "DELETE FROM issues WHERE id = ?";
        try (PreparedStatement prepStatement = connect.prepareStatement(query)) {
            prepStatement.setInt(1, issueId);
            prepStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("exception : {}", e.getCause());
            e.printStackTrace();
        }
        System.out.println("Issue with id : " + issueId + "was deleted.");
    }

    public static int viewIssuesCount(Connection connect) {
        String query = "SELECT COUNT(*) FROM issues";
        try (PreparedStatement prepStatement = connect.prepareStatement(query)) {
            ResultSet resultSet = prepStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1); // test it!
        } catch (SQLException e) {
            LOGGER.error("exception : {}", e.getCause());
            e.printStackTrace();
        }
        return 0;
    }

    public static void viewAllIssues(Connection connect) {
        String query = "SELECT * FROM issues";
        try (PreparedStatement prepStatement = connect.prepareStatement(query)) {
            ResultSet resultSet = prepStatement.executeQuery();
            System.out.println("+=================================================================================================================================================================+");
            System.out.print("|");
            System.out.printf("%-4s %-6s%-28s%-14s%-14s%-5s%3s%n", "ID", "NAME", "DESCRIPTION", "SEVERITY  ", "PRIORITY", "PROJECT", "|");
            System.out.println("+==============================================================================================================================+");
            while (resultSet.next()) {
                System.out.println(Util.viewAllTable(connect, resultSet));
            }
        } catch (SQLException e) {
            LOGGER.error("exception : {}", e.getCause());
            e.printStackTrace();
        }
    }
}
