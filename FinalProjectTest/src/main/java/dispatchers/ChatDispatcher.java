package dispatchers;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;


@WebServlet("/ChatDispatcher")

public class ChatDispatcher extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String sqlInsert = "INSERT INTO Chat (username, Message, created_at) VALUES (?,?,?)";
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		
    	response.setContentType("text/html");
    	
    	String username = null;
		Cookie cookie = null;
        Cookie[] cookies = null;

         // Get an array of Cookies associated with the this domain
        cookies = request.getCookies();
        if( cookies != null ) {
           for (int i = 0; i < cookies.length; i++) {
              cookie = cookies[i];
              username = URLDecoder.decode(cookie.getValue(), "UTF-8");
              if(cookie.getName().equals("name")){
            	  break;
              }
           }
        }
        boolean loggedIn = false;
		if(username != null){
			if(cookie.getName().equals("name")){
	              loggedIn = true;
   			}
		}
    	
    	String msg = request.getParameter("msg");
    	SimpleDateFormat date = new SimpleDateFormat("dd-MMM-yyy");
    	Date d = new Date();
    	String created = date.format(d);
    	
		try (Connection conn = DriverManager.getConnection(Constant.url,Constant.DBUserName, Constant.DBPassword);
				PreparedStatement ps = conn.prepareStatement(sqlInsert);) {
				ps.setString(1, username);
				ps.setString(2, msg);
				ps.setString(3, created);
				ps.executeUpdate();
				conn.close();
			} catch (SQLException sqle) {
				System.out.println ("SQLException: " + sqle.getMessage());
			}
		response.sendRedirect("chat.jsp");
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		doGet(request,response);
	}
	
}