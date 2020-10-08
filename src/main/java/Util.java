import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

abstract class Util {
    static final String menu = "\n===================="
            + "\n        MENU        \n"
            + "1 to insert data\n"
            + "2 to view   data\n"
            + "3 to delete data\n"
            + "4 to edit   data\n"
            + "5 to show report\n"
            + "0 to exit\n"
            + "====================\n";

    static final String insert = "\n===================="
            + "\n        INSERT DATA        \n"
            + "1 to insert    user\n"
            + "2 to insert   issue\n"
            + "3 to insert project\n"
            + "0 to return\n"
            + "====================\n";

    static final String view = "\n===================="
            + "\n        VIEW DATA        \n"
            + "1 to view   users\n"
            + "2 to view   issue\n"
            + "3 to view project\n"
            + "0 to return\n"
            + "====================\n";
    static final String delete = "\n===================="
            + "\n        DELETE DATA        \n"
            + "1 to delete   users\n"
            + "2 to delete   issue\n"
            + "3 to delete project\n"
            + "0 to return\n"
            + "====================\n";

    static final String edit = "\n===================="
            + "\n        EDIT DATA        \n"
            + "1 to edit    users\n"
            + "2 to edit   issues\n"
            + "3 to edit projects\n"
            + "0 to return\n"
            + "====================\n";

    static final String show = "\n===================="
            + "\n        GENERATE REPORT        \n"
            + "1 to view users\n"
            + "0 to return\n"
            + "====================\n";

    static final String issueSeverity = "What type of severity is our problem?\n"
            + "(Type one of the next digit)\n"
            + "1:Critical\n"
            + "2:Major\n"
            + "3:Minor\n"
            + "4:Low";

    static final String issuePriority = "What type of priority is our problem?\n"
            + "(Type one of the next digit)\n"
            + "1:Highest\n"
            + "2:High\n"
            + "3:Low\n"
            + "4:Lowest";

    static final String issueUpdate = "Choose what we need to update\n"
            + "1: Name of issue\n"
            + "2: Owner of project\n"
            + "3: Description of problem\n"
            + "4: Severity level\n"
            + "5: Priority level";


    static final String noUserCreated = "There is no one user created,(return)";
    static final String noProjectCreated = "There is no one project created,(return)";
    static final String noIssueCreated = "There is no one issue created,(return)";

    public static int isDigit() {
        Integer i = null;
        Scanner scanner = new Scanner(System.in);
        try {
            i = scanner.nextInt();
        } catch (NumberFormatException | InputMismatchException e) {
            e.printStackTrace();
            System.out.println("Please type integer value!");
        }
        if (i != null) {
            return i;
        }
        return isDigit();

    }

    public static boolean checkUser(Connection ctn, int i) throws SQLException {
        try (PreparedStatement ps = ctn.prepareStatement("SELECT * from users where id = ?")) {
            ps.setInt(1, i);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return true;
            }
        }
        return false;
    }

    public static String getIssueSeverity() {
        String sevLevel = null;
        switch (App.switcher(3)) {
            case 1:
                sevLevel = "Critical";
                break;
            case 2:
                sevLevel = "Major";
                break;
            case 3:
                sevLevel = "Minor";
                break;
            case 4:
                sevLevel = "Low";
                break;
        }
        return sevLevel;
    }

    public static String getIssuePriority() {
        String priLevel = null;
        switch (App.switcher(3)) {
            case 1:
                priLevel = "Highest";
                break;
            case 2:
                priLevel = "High";
                break;
            case 3:
                priLevel = "Low";
                break;
            case 4:
                priLevel = "Lowest";
                break;
        }
        return priLevel;
    }

    public static String viewAllTable(Connection connect, ResultSet set) throws SQLException {
        // implement cascade delete!!
        //or truncate
        return String.format("|%-5.4s%-6s %-28.28s%-14.14s%-14.9s%4.12s%4s%n     ",//recent corrections
                set.getInt("id"),
                set.getString("name"),
                set.getString("descriptions"),
                set.getString("severity"),
                set.getString("priority"),
                Project.getName(connect, set.getInt("ownerID")), "   |")
                + "\n^------------------------------------------------------------------" +
                "-----------------------------------------------------------------------^"; //DROP CONCATENATION
    }
}
