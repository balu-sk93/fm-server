package fit.fm.server.core;

import java.io.IOException;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import fit.fm.server.service.GameService;
import fit.fm.server.utils.Decoder;
import fit.fm.server.utils.Encoder;

@Component
@Controller
@ServerEndpoint(value = "/fm", encoders = Encoder.class, decoders = Decoder.class)
public class ServerImpl extends GameService {

	private static final Logger LOG = LoggerFactory.getLogger(ServerImpl.class);

	Session session;
	
	@OnOpen
	public void onOpen(Session session) throws IOException {
		LOG.info("New session created , Session Id {}", session.getId());
		this.session = session;
		createRound(this.session);
	}

	@OnMessage
	public void onMessage(Session session, String message) throws IOException {
		LOG.info("Session {} , New message received : {}", session.getId(), message);
		validate(session, message);
	}
	
	@OnClose
	public void onClose(Session session) throws IOException {
		LOG.info("Session {} closed", session.getId());
		closeSession(session);
	}
	
}
