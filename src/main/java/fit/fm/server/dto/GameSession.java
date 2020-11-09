package fit.fm.server.dto;

import javax.websocket.Session;

public class GameSession {

	private String sessionId;
	private Integer score;
	private int retry;
	private String key;
	private Session session;
	
	
	public GameSession() {}

	public GameSession(String sessionId, Integer score, int retry, String key, Session session) {
		super();
		this.sessionId = sessionId;
		this.score = score;
		this.retry = retry;
		this.key = key;
		this.session = session;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public int getRetry() {
		return retry;
	}

	public void setRetry(int retry) {
		this.retry = retry;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

}
