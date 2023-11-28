package javaee.context;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import framework.context.RequestContext;

public class WebRequestContext implements RequestContext {
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
		parameters = this.request.getParameterMap();
		session = this.request.getSession();
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
