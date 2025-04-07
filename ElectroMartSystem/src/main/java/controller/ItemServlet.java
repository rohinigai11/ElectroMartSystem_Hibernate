package controller;

import dao.ItemDAO;
import model.Item;
import model.User;
import util.HibernateUtil;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = {"/items/*"})
public class ItemServlet extends HttpServlet {
    private ItemDAO itemDAO;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        itemDAO = new ItemDAO();
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect("login.html");
            return;
        }
        User user = (User) session.getAttribute("user");
    	resp.setContentType("application/json");
        String path = req.getPathInfo() == null ? "/" : req.getPathInfo();
        System.out.println("GET request received for path: " + path);

        try {
            if (path.equals("/")) {
                System.out.println("Fetching all items & user");
                HashMap<String,Object> obj=new HashMap<>();
                obj.put("user",user.getName());
                obj.put("items",itemDAO.getAllItems());
                resp.getWriter().write(gson.toJson(obj));
            } else if (path.startsWith("/edit/")) {
                int id = Integer.parseInt(path.substring(6));
                System.out.println("Fetching item with ID: " + id);
                Item item = itemDAO.getItem(id);
                if (item != null) {
                    resp.getWriter().write(gson.toJson(item));
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    resp.getWriter().write("{\"error\":\"Item not found\"}");
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("{\"error\":\"Invalid endpoint\"}");
            }
        } catch (Exception e) {
            System.out.println("Error in doGet: " + e.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            resp.getWriter().write(gson.toJson(error));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String action = req.getParameter("action");
        System.out.println("POST request received with action: " + action);
        Map<String, String> response = new HashMap<>();

        try {
            if ("register".equals(action)) {
                Item it = new Item(
//                    req.getParameter("firstName"),
//                    req.getParameter("lastName"),
//                    req.getParameter("email"),
//                    req.getParameter("mobileNumber"),
//                    Double.parseDouble(req.getParameter("salary")),
//                    req.getParameter("department")
                		
                		req.getParameter("category"),
                		req.getParameter("name"),
                		req.getParameter("brandName"),
                		Double.parseDouble(req.getParameter("price"))
                );
                itemDAO.saveItem(it);
                response.put("message", "Item added successfully");
            } else if ("update".equals(action)) {
                Item it = itemDAO.getItem(Integer.parseInt(req.getParameter("itemId")));
                if (it != null) {
//                    emp.setFirstName(req.getParameter("firstName"));
//                    emp.setLastName(req.getParameter("lastName"));
//                    emp.setEmail(req.getParameter("email"));
//                    emp.setMobileNumber(req.getParameter("mobileNumber"));
//                    emp.setSalary(Double.parseDouble(req.getParameter("salary")));
//                    emp.setDepartment(req.getParameter("department"));
                	
                	it.setCategory(req.getParameter("category"));
                	it.setName(req.getParameter("name"));
                	it.setBrandName(req.getParameter("brandName"));
                	it.setPrice(Double.parseDouble(req.getParameter("price")));
                    itemDAO.updateItem(it);
                    response.put("message", "Item updated successfully");
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.put("error", "Item not found");
                }
            } else if ("delete".equals(action)) {
                int itemId = Integer.parseInt(req.getParameter("itemId"));
                itemDAO.deleteItem(itemId);
                response.put("message", "Item deleted successfully");
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.put("error", "Invalid action");
            }
        } catch (Exception e) {
            System.out.println("Error in doPost: " + e.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.put("error", e.getMessage());
        }
        resp.getWriter().write(gson.toJson(response));
    }

    @Override
    public void destroy() {
        HibernateUtil.shutdown(); // Cleanly close the SessionFactory
        System.out.println("Servlet destroyed, SessionFactory closed");
    }
}