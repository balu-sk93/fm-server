package fit.fm.server.core;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import fit.fm.server.dto.GameSession;
import fit.fm.server.utils.CacheMap;

@Component
public class SessionService {
	
    public static long expiryInMillis;

	private static Map<String, GameSession> gameSessions = new CacheMap<String, GameSession>();

	public static void addSession(String sessionId, GameSession gameSession) {
		gameSessions.put(sessionId, gameSession);
	}

	public static GameSession getSession(String sessionId) {
		return gameSessions.get(sessionId);
	}

	public static void removeSession(String sessionId) {
		gameSessions.remove(sessionId);
	}

	public long getExpiryInMillis() {
		return expiryInMillis;
	}

	@Value("${timeout}")
	public void setExpiryInMillis(long expiry) {
		expiryInMillis = expiry;
	}
	
}
