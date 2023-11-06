package javaee;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import framework.context.RequestContext;
import framework.context.ResponseContext;
import framework.controller.ApplicationController;
import javaee.controller.WebApplicationController;


public class FrontServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req,res);
	}
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		ApplicationController app = new WebApplicationController();
		System.out.println("dopost");
		RequestContext reqc = app.getRequest(req);
		ResponseContext resc = app.getResponse(res);
		app.handleRequest(reqc, resc);

		resc.setResponse(res);
		app.handleResponse(reqc, resc);
	}
}
