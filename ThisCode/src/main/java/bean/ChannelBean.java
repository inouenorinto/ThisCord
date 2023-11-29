package bean;

import java.util.ArrayList;
import java.util.List;

public class ChannelBean {
	private List<NoticeSessionBean> members = new ArrayList<>();
	
	public void addMember(NoticeSessionBean bean) {
		members.add(bean);
	}
	
	public void remeveMember(NoticeSessionBean bean) {
		for(NoticeSessionBean tempBean: members) {
			if(tempBean.equals(bean)) {
				members.remove(bean);
			}
		}
	}
}
