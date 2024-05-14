package audioLibrary.sql;

import audioLibrary.exceptions.InvalidArgumentsException;
import audioLibrary.user.AuthenticatedUser;

import java.sql.*;
import java.util.List;

public class SqlManager {
    private final String url;
    private final String username;
    private final String password;
    private Connection connection;

    public SqlManager(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
        try {
            this.connection = DriverManager.getConnection(this.url, this.username, this.password);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void createAccountTable() {
        String sql = "CREATE TABLE Account (" +
                "id INT PRIMARY KEY," +
                "username VARCHAR(50) NOT NULL UNIQUE," +
                "password VARCHAR(50) NOT NULL);";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
            System.out.println("Account table created successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteTable(String tableName) {
        String sql = "DROP TABLE " + tableName;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
            System.out.println("Table '" + tableName + "' deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertInfoAccount(Integer id, String usernamee, String passwordd) {
        String sql = "INSERT INTO Account (id, username, password) VALUES (?, ?, ?);";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.setString(2, usernamee);
            statement.setString(3, passwordd);
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("A new row was inserted into the Users table.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Integer numberOfRows(String tableName) {
        String sql = "SELECT COUNT(*) FROM " + tableName;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Integer existsInTable(String[] columns, String[] param, String tableName){
        String sql = "SELECT ";
        String where = " WHERE ";
        for (String column : columns) {
            sql = sql + column + ", ";
            where = where + column + " = ? and ";
        }
        sql = sql.substring(0, sql.length() - 2);
        sql = sql + " FROM " + tableName ;
        sql = sql + where;
        sql = sql.substring(0, sql.length() - 5);
        sql = sql + ";";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 1; i <= param.length; i++) {
                statement.setString(i, param[i-1]);
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                if(resultSet.next()) {
                    return 1;
                }
                else return 0;
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }
}
