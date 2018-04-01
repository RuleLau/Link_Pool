package org.servlet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@WebServlet("/books2")
public class BookDataSource extends HttpServlet{
    private Connection connection = null;
    private Statement statement = null;
    private ResultSet resultSet = null;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter printWriter = resp.getWriter();
        try {
            Context context = new InitialContext();
            DataSource ds = (DataSource) context.lookup("jdbc/sampleDS");
            connection = ds.getConnection();
            statement = connection.createStatement();
            String sql = "select * from Books";
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                String result = String.format("id: %s--name: %s",id,name);
                printWriter.println("<div>" + result + "</div>");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Context context = new InitialContext();
            DataSource ds = (DataSource) context.lookup("jdbc/sampleDS");
            connection = ds.getConnection();
            statement = connection.createStatement();
            int id = Integer.parseInt(req.getParameter("id"));
            String sql = "update Books set name = 'hello' where id = " + id;
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
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
        Context context = null;
        try {
            context = new InitialContext();
            DataSource ds = (DataSource) context.lookup("jdbc/sampleDS");
            connection = ds.getConnection();
            statement = connection.createStatement();
            int id = Integer.parseInt(req.getParameter("id")) ;
            String sql = "delete from Books where id = " + id;
            statement.executeUpdate(sql);
        } catch (NamingException e) {
            e.printStackTrace();
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
        try {
            Context context = new InitialContext();
            DataSource ds = (DataSource) context.lookup("jdbc/sampleDS");
            connection = ds.getConnection();
            statement = connection.createStatement();
            String name = req.getParameter("name");
            String sql = "insert into Books (name) values (" + name + ")";
            statement.executeUpdate(sql);
        } catch (NamingException e) {
            e.printStackTrace();
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

