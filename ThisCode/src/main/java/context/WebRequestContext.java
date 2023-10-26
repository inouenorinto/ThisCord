package context;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class WebRequestContext implements RequestContext {
	private Map<String, String[]> parameters;
	private HttpServletRequest request;
	
	public WebRequestContext() {}
	
	@Override
	public String getCommandPath() {
		String path = request.getServletPath();

		
		String commandPath= path.substring(1);
		return commandPath;
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
	

}
