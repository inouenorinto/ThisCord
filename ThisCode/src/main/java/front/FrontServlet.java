package front;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import context.RequestContext;
import context.ResponseContext;
import controller.ApplicationController;
import controller.WebApplicationController;


public class FrontServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req,res);
	}
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		ApplicationController app = new WebApplicationController();
		System.out.println("dopost");
		RequestContext reqc = app.getRequest(req);
		ResponseContext resc = app.handleRequest(reqc);

		resc.setResponse(res);
		app.handleResponse(reqc, resc);
	}
}
