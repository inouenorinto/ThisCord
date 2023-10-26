package context;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import beans.DeviceBean;
import beans.UserBean;

public class WebRequestContext implements RequestContext {
	private Map<String, String[]> parameters;
	private HttpServletRequest request;
	private HttpSession session = null;
	
	public WebRequestContext() {}
	
	@Override
	public String getCommandPath() {
		String path = request.getRequestURI();
		
		/*String[] commandPath = path.split("/");
		System.out.println(commandPath[3]);*/
		
		String target= path.replace("sensi", "").replace("SensiRecoder", "").replace("/","");
		return target;
	}

	@Override
	public String[] getParameter(String key) {
		
		return parameters.get(key);
	}
	
	@Override
	public void setParameterMap(Map<String, String[]> map) {
		parameters = map;
	}

	@Override
	public Object getRequest() {
		return request;
	}

	@Override
	public void setRequest(Object request) {
		this.request = (HttpServletRequest) request;
	}
	
	public void InvalidateSession() {
		session = request.getSession();
		session.invalidate();
	}
	
	public void setUserBeanInSession(UserBean bean) {
		session = request.getSession();
		session.setAttribute("bean", bean);
	}
	
	public UserBean getUserBeanInSession() {
		session = request.getSession();
		return (UserBean)session.getAttribute("bean");
	}
	
	public void setDeviceBeanInSession(DeviceBean bean) {
		session = request.getSession();
		session.setAttribute("bean", bean);
	}
	
	public DeviceBean getDeviceBeanInSession() {
		session = request.getSession();
		return (DeviceBean)session.getAttribute("bean");
	}
}
