package fit.fm.server.utils;

import javax.websocket.EncodeException;
import javax.websocket.EndpointConfig;

public class Encoder implements javax.websocket.Encoder.Text<String> {

	@Override
	public String encode(String message) throws EncodeException {
		return message;
	}

	@Override
	public void init(EndpointConfig endpointConfig) {
	}

	@Override
	public void destroy() {
		// Close resources (if any used)
	}

}