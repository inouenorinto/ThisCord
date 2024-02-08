package commands;

import framework.command.AbstractCommand;
import framework.context.RequestContext;
import framework.context.ResponseContext;

public class LogoutCommand extends AbstractCommand {

    @Override
    public void execute(RequestContext req, ResponseContext res) {

        String deviceType = req.getDeviceType();
        String target = null;
        if ("Smartphone".equals(deviceType)) {
            target = "spchat.html";
        } else {
            target = "chat.html";
        }

        req.invalidate(); // セッションを無効化

        res.setRedirect("/login.html");
    }
}
