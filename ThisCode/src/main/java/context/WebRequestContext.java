package context;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class WebRequestContext implements RequestContext {
	private Map<String, String[]> parameters;
	private HttpServletRequest request;
	private HttpSession session;
	
	public WebRequestContext() {}
	
	@Override
	public String getCommandPath() {		
		String path = request.getRequestURI();
		String target= path.replace("fn", "").replace("ThisCode", "").replace("/","");
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
	public void setAttributeInSession(Object obj) {
		session.setAttribute("object", obj);
	}
	
	@Override
	public Object getAttributeInSession(String key) {
		return session.getAttribute(key);
	}
}
