package context;

public interface RequestContext {
	public String getCommandPath();
	public String[] getParameter(String key);
	public Object getRequest();
	public void setRequest(Object request);
	public void setSession(Object session);
	public void invalidateSession();
}
