package context;

import javax.servlet.http.HttpServletResponse;

public class WebResponseContext implements ResponseContext {
	private Object result;
	private String target;
	private HttpServletResponse response;
	
	public WebResponseContext() {}
	@Override
	public Object getResult() {
		return result;
	}

	@Override
	public String getTarget() {
		return target;
	}

	@Override
	public void setResult(Object bean) {
		result = bean;

	}

	@Override
	public void setTarget(String transferInfo) {
		target=transferInfo;
	}

	@Override
	public void setResponse(Object obj) {
		response = (HttpServletResponse)obj;

	}

	@Override
	public Object getResponse() {
		return response;
	}

}
