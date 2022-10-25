package pro.patrykkrawczyk.fxpricehandler.service.listener;

import java.io.IOException;

public interface MessageListener {

    void onMessage(String message) throws IOException;
}
