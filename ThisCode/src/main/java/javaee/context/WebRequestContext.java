package javaee.context;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import framework.context.RequestContext;

public class WebRequestContext implements RequestContext {
	String yellow = "\u001b[00;33m";
	String end    = "\u001b[00m";
	
	private Map<String, String[]> parameters;
	private HttpServletRequest request;
	private HttpSession session;
	
	public WebRequestContext() {}
	
	@Override
	public String getCommandPath() {		
		String path = request.getRequestURI();
		String target= path.replace("fn", "").replace("ThisCord", "").replace("/","");
		return target;
	}

	@Override
	public String[] getParameter(String key) {
		
		return parameters.get(key);
	}
	

	@Override
	public Object getRequest() {
		return request;
	}

	@Override
	public void setRequest(Object request) {
		this.request = (HttpServletRequest) request;
		this.parameters = this.request.getParameterMap();
		this.session = this.request.getSession(true);
		System.out.println(yellow+"WebRequestContext.java "+end+":\t\tセッションID="+ this.session.getId());
	}
	
	@Override
	public void setAttribute(String param, Object obj) {
		this.request.setAttribute(param, obj);
	}
	
	
	@Override
	public void setSession(Object session) {
		this.session = (HttpSession) session;
	}
	
	@Override
	public void invalidateSession(){
		session.invalidate();
	}
	
	@Override
	public void setAttributeInSession(String key, Object obj) {
		session.setAttribute(key, obj);
	}
	
	@Override
	public Object getAttributeInSession(String key) {
		return session.getAttribute(key);
	}
}
