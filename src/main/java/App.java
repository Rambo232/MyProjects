import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class App {
    private static final Logger LOGGER = Logger.getLogger(App.class.getName());

    public static void main(String[] args) {
        PropertyConfigurator.configure("C:\\Users\\Kirill\\BugTracker\\log4j.properties");
        LOGGER.info("Starting App...");

        try {
            Class.forName("org.sqlite.JDBC");

        } catch (ClassNotFoundException e) {
            LOGGER.error("exception : {}", e.getCause());
            e.printStackTrace();
        }
        try (Connection connect = DriverManager.getConnection("jdbc:sqlite:bug_Tracker.db")) {
            start(connect);
        } catch (SQLException e) {
            LOGGER.error("exception : {}", e.getCause());
            e.printStackTrace();
        }
    }

    public static int switcher(int limit) {
        try {
            Scanner sc = new Scanner(System.in);
            int number = sc.nextInt();
            if (number >= 0 && number <= limit) {
                return number;
            } else {
                System.out.println("Incorrect number, type correct one in limit ");
                switcher(limit);
            }
        } catch (InputMismatchException e) {
            LOGGER.error("exception : {}", e.getCause());
            System.out.println("Type a number...");
        }
        return 0;
    }

    private static void start(Connection connect) throws SQLException {
        while (true) {
            System.out.print(Util.menu);

            switch (switcher(5)) {
                case 1:
                    LOGGER.info("Invoke insertInfo method");
                    insertInfo(connect);
                    break;
                case 2:
                    LOGGER.info("Invoke viewInfo method");

                    viewInfo(connect);
                    break;
                case 3:
                    LOGGER.info("Invoke deleteInfo method");

                    deleteInfo(connect);
                    break;
                case 4:
                    LOGGER.info("Invoke editInfo method");

                    editInfo(connect);
                    break;
                case 5:
                    LOGGER.info("Invoke showReport method");
                    showReport(connect);
                    break;
                case 0:
                    return;
            }
        }
    }

    private static void insertInfo(Connection connect) throws SQLException {
        boolean exit = false;
        while (!exit) {
            System.out.print(Util.insert);
            switch (switcher(3)) {
                case 1:
                    Users.insert(connect);
                    break;
                case 2:
                    Issues.insert(connect);
                    break;
                case 3:
                    Project.insert(connect);
                    break;
                case 0:
                    exit = true;
                    break;
            }
        }
    }

    private static void viewInfo(Connection connect) {
        boolean exit = false;
        while (!exit) {
            System.out.println(Util.view);
            switch (switcher(3)) {
                case 1:
                    if (Users.viewCount(connect) == 0) {
                        System.out.println(Util.noUserCreated);
                        return;
                    }
                    System.out.println("Total users: " + Users.viewCount(connect));
                    Users.viewAllUsers(connect);
                    break;
                case 2:
                    if (Issues.viewIssuesCount(connect) == 0) {// recent corrections
                        System.out.println(Util.noIssueCreated);
                        return;
                    }
                    Issues.viewAllIssues(connect);
                    System.out.println("Issues count: " + Issues.viewIssuesCount(connect));
                    break;
                case 3:
                    if (Project.viewCount(connect) == 0) {
                        System.out.println(Util.noProjectCreated);
                        return;
                    }
                    Project.viewAllProjects(connect);
                    System.out.println("Project count: " + Project.viewCount(connect));
                    break;
                case 0:
                    exit = true;
                    break;
            }
        }
    }

    private static void deleteInfo(Connection connect) throws SQLException {
        System.out.println(Util.delete);
        switch (switcher(3)) {
            case 1:
                if (Users.viewCount(connect) == 0) {
                    System.out.println(Util.noUserCreated);
                    return;
                }
                while (true) {
                    Users.viewAllUsers(connect);
                    System.out.println("User id to delete, see table above \n  ^");
                    int id = Util.isDigit();
                    if (Util.checkUser(connect, id)) {
                        Users.delete(connect, id);
                        break;
                    } else {
                        System.out.println("This ID is not assign to any user..");
                    }
                }
                break;
            case 2:
                if (Issues.viewIssuesCount(connect) == 0) {
                    System.out.println(Util.noIssueCreated);
                    return;
                }
                while (true) {
                    Issues.viewAllIssues(connect);
                    System.out.println("Issue id to delete, see table above    ^");
                    int id = Util.isDigit();
                    if (Util.checkUser(connect, id)) {
                        Issues.delete(connect, id);
                        break;
                    } else {
                        System.out.println("This ID is not assign to any issue..");
                    }
                }
                break;
            case 3:
                if (Project.viewCount(connect) == 0) {
                    System.out.println(Util.noProjectCreated);
                    return;
                }
                Project.viewAllProjects(connect);
                System.out.println("Project id to delete(same as user id for now),\n see table above ^");
                int id = Util.isDigit();
                /*This method don't delete project if user isn't exist
                 * */
                if (Util.checkUser(connect, id)) {
                    Project.delete(connect, id);
                    break;
                } else {
                    System.out.println("This ID is not assign to any project..");
                    return;
                }
            case 0:
                break;
        }
    }

    private static void editInfo(Connection connect) throws SQLException {
        boolean exit = false;
        while (!exit) {
            System.out.println(Util.edit);
            switch (switcher(3)) {
                case 1:
                    if (Users.viewCount(connect) == 0) {
                        System.out.println(Util.noUserCreated);
                        return;
                    }
                    while (true) {
                        Users.viewAllUsers(connect);
                        System.out.println("Users count : " + Users.viewCount(connect));
                        System.out.println("User ID to edit, see table above");
                        int id = Util.isDigit();
                        if (Util.checkUser(connect, id)) {
                            Users.update(connect, id);
                            break;
                        } else {
                            System.out.println("This ID is not assign to any user..");
                        }
                    }
                    break;
                case 2:
                    if (Issues.viewIssuesCount(connect) == 0) {
                        System.out.println(Util.noIssueCreated);
                        return;
                    }
                    while (true) {
                        Issues.viewAllIssues(connect);
                        System.out.println("Issues count : " + Issues.viewIssuesCount(connect));
                        System.out.println("Issue ID to edit, see table above ^");
                        int id = Util.isDigit();
                        if (Util.checkUser(connect, id)) {
                            Issues.update(connect, id);
                            break;
                        } else {
                            System.out.println("This ID is not assign to any issue..");
                        }
                    }
                    break;
                case 3:
                    if (Project.viewCount(connect) == 0 || Users.viewCount(connect) == 0) {//fix it
                        System.out.println(Util.noProjectCreated);// need to fix logic below.
                        return;
                    }
                    while (true) {
                        Project.viewAllProjects(connect);
                        System.out.println("Projects count : " + Project.viewCount(connect));
                        System.out.println("Project ID to edit, see table above ^");
                        int id = Util.isDigit();
                        if (Util.checkUser(connect, id)) {
                            Project.update(connect, id);
                            break;
                        } else {
                            System.out.println("This ID is not assign to any project..");
                        }
                    }
                    break;
                case 0:
                    exit = true;
                    break;
            }
        }
    }

    private static void showReport(Connection connect) throws SQLException {
        boolean exit = false;
        while (!exit) {
            System.out.println(Util.show);
            switch (switcher(1)) {
                case 1:
                    Users.viewAllUsers(connect);
                    break;
                case 0:
                    exit = true;
                    break;
            }
            if (Users.viewCount(connect) == 0) {
                System.out.println("There is no one user created,(return)");
                return;
            }
            Users.viewAllUsers(connect);
            System.out.println(Users.viewCount(connect));
            System.out.println("Type user id");
            int id = Util.isDigit();
            if (Util.checkUser(connect, id)) {
                if (Users.viewProjectsCount(connect, id) == 0) {
                    System.out.println("There is no project assign to this user..");
                    return;
                }
                Users.viewProjects(connect, id);
                System.out.println("Projects count to this user : " + Users.viewProjectsCount(connect, id));

            } else {
                System.out.println("There is no user with such id,(return)");
                break;
            }
            boolean exit3 = false;
            while (!exit3) {
                System.out.println("Type project id: ");
                int id2 = Util.isDigit();
                if (Util.checkUser(connect, id2)) {
                    if (Project.viewIssuesCount(connect, id2) == 0) {
                        System.out.println("There is no one issues on this project..");
                        return;
                    }
                    Project.viewIssues(connect, id2);
                    System.out.println("Issues count : " + Project.viewIssuesCount(connect, id2));
                    exit3 = true;
                } else {
                    System.out.println("There is no project with this id");
                    break;
                }
            }
        }
    }
}

