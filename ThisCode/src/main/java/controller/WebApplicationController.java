package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import commands.AbstractCommand;
import commands.CommandFactory;
import context.RequestContext;
import context.ResponseContext;
import context.WebRequestContext;
import context.WebResponseContext;

public class WebApplicationController implements ApplicationController {
	public RequestContext getRequest(Object request) {
		RequestContext reqc = new WebRequestContext();
		System.out.println("kokomade getRequest");
		reqc.setRequest(request);
		return reqc;
	}
	
	public ResponseContext handleRequest(RequestContext req) {
		System.out.println("kokomade handleRequest1");
		AbstractCommand command = CommandFactory.getCommand(req);
		command.init(req);
		System.out.println("kokomade handleRequest2");
		ResponseContext resc = command.execute(new WebResponseContext());
		return resc;
	}
	
	public void handleResponse(RequestContext reqc, ResponseContext resc) {
		HttpServletRequest req = (HttpServletRequest)reqc.getRequest();
		HttpServletResponse res = (HttpServletResponse)resc.getResponse();
		System.out.println("kokomade handleResponse");
		req.setAttribute("result", resc.getResult());
		System.out.println("target"+resc.getTarget());
		RequestDispatcher rd = req.getRequestDispatcher(resc.getTarget());
		
		try {
			rd.forward(req, res);
		} catch(ServletException e) {
			
		} catch(IOException e) {
			
		}
	}
}
