package org.servlet;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;


@WebServlet("/books")
public class Book extends HttpServlet{
    private String url = "jdbc:sqlite:data.sqlite";
    private Connection connection;
    private Statement statement;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter printWriter = resp.getWriter();
        try {
            connection = DriverManager.getConnection(url);
            statement = connection.createStatement();//不能设置占位符，不能执行动态sql语句
            String sql = "SELECT * FROM Books";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                String result = String.format("id: %s\tname: %s", id, name);
                printWriter.println("<div>" + result + "</div>");
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
        printWriter.flush();
        printWriter.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            connection = DriverManager.getConnection(url);
            statement = connection.createStatement();
            String sql = "update Books set name = 'hello' where id = 1";
            statement.executeUpdate(sql);
        }catch (SQLException exception){
            exception.printStackTrace();
        }finally {
            try {
                if (connection != null){
                    connection.close();
                }
            }catch (SQLException exception){
                exception.printStackTrace();
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id")) ;
        try {
            connection = DriverManager.getConnection(url);
            statement = connection.createStatement();
            String sql = "delete from Books where id = " + id;
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        try {
            connection = DriverManager.getConnection(url);
            statement = connection.createStatement();
            String sql = "insert into Books (name) values (" + name + ")";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
