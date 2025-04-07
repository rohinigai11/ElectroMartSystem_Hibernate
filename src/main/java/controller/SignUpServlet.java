package controller;


import java.io.IOException;

import org.hibernate.Session;
import org.hibernate.SharedSessionContract;
import org.hibernate.Transaction;

import util.HibernateUtil;
import util.PasswordUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.User;

@WebServlet("/signup")
public class SignUpServlet  extends HttpServlet{

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
		String name=request.getParameter("name");
		String email=request.getParameter("email");
		String password=request.getParameter("password");
		User user= new User();
		user.setName(name);
		user.setEmail(email);
		user.setPassword(PasswordUtil.hashPassword(password));
			
		try(Session session=HibernateUtil.getSessionFactory().openSession()){
			Transaction tx=session.beginTransaction();
			session.persist(user);
			tx.commit();
			response.sendRedirect("login.html");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}

