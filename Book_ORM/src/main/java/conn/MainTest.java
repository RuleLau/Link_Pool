package conn;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.model.Book;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/bookorm")
public class MainTest extends HttpServlet {
    private Session session = null;
    private PrintWriter printWriter ;
    //得到当前连接数据库的session
    public Session getSession(){
        //读取配置文件
        Configuration cfg = new Configuration().configure("hibernate.cfg.xml");
        SessionFactory sessionFactory = cfg.buildSessionFactory();
        Session session = sessionFactory.openSession();
        if (session != null)
            return session;
        return null;
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        printWriter = resp.getWriter();
        session = getSession();
        Transaction ts1 = session.beginTransaction();
        List list =  session.createQuery("from book").list();
        for (int i = 0; i < list.size(); i++) {
            Book book1 = (Book) list.get(i);
            String id = book1.getId().toString();
            String name = book1.getName().toString();
            String result = String.format("id: %s--name: %s",id,name);
            printWriter.println("<div>" + result + "</div>");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        printWriter = resp.getWriter();
        session = getSession();
        Transaction ts2 = session.beginTransaction();
        Book book = new Book();
        book.setId(2);
        session.delete(book);
        ts2.commit();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        printWriter = resp.getWriter();
        session = getSession();
        Transaction ts3 = session.beginTransaction();
        Book book = new Book();
        book.setId(1);
        book.setName("book1 up");
        session.update(book);
        ts3.commit();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        printWriter = resp.getWriter();
        session = getSession();
        Transaction ts4 = session.beginTransaction();
        Book book = new Book();
        book.setName("book2");
        session.save(book);
        ts4.commit();
    }
}
