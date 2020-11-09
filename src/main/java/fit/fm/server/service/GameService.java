package fit.fm.server.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fit.fm.server.core.SessionService;
import fit.fm.server.dto.GameSession;

public class GameService {

	private static final Logger LOG = LoggerFactory.getLogger(GameService.class);
	

	public GameSession createRound(Session session) {
		LOG.info("Creating new instuction");
		GameSession gameSession = SessionService.getSession(session.getId());
		try {
			LOG.info("Reading instruction");
			String instruction = readInput();
			LOG.info("Instruction read {}", instruction);
			if (gameSession == null)
				gameSession = new GameSession(session.getId(), 0, 0, instruction, session);
			else
				gameSession.setKey(instruction);
			SessionService.addSession(session.getId(), gameSession);
			session.getBasicRemote().sendText("Guess the instruction within "+
					TimeUnit.MILLISECONDS.toSeconds(SessionService.expiryInMillis)+" seconds. Score : "+gameSession.getScore());
			LOG.info("Waiting for the client response");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return gameSession;
	}
	
	public GameSession retry(GameSession gameSession) {
		LOG.info("Creating new instuction");
		try {
			String instruction = gameSession.getKey();
			LOG.info("Instruction read {}", instruction);
			gameSession.setRetry(gameSession .getRetry()+1);
			Session session = gameSession.getSession();
			SessionService.addSession(session.getId(), gameSession);
			session.getBasicRemote().sendText("Guess the instruction");
			LOG.info("Waiting for the client response");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return gameSession;
	}

	private boolean checkGameOver(GameSession gameSession) {

		boolean gameOver = false;
		if (gameSession.getScore() < -2) {
			LOG.info("Score less than -2");
			sendMessage(gameSession.getSession(), "Sorry, You lost. Score : "+gameSession.getScore(), true);
			gameOver = true;
		} else if (gameSession.getScore() >= 10) {
			LOG.info("Score equals 10");
			sendMessage(gameSession.getSession(), "Congrats, You have won the game. Score "+gameSession.getScore(), true);
			gameOver = true;
		}
		return gameOver;
	}

	protected void validate(Session session, String message) {
		GameSession gameSession = SessionService.getSession(session.getId());
		int score = gameSession.getScore();
		if (message.equalsIgnoreCase(gameSession.getKey())) {
			score = score + 1;
			LOG.info("Correct Answer ! Score " + score);
			sendMessage(gameSession.getSession(), "Correct Answer ! . Score : "+score, false);
		} else {
			score = score - 1;
			LOG.info("Incorrect Answer ! Score " + score);
			sendMessage(gameSession.getSession(), "Incorrect Answer ! . Score : "+score, false);
		}
		LOG.info("Prev Score {}, Score {}", gameSession.getScore(), score);
		gameSession.setScore(score);
		SessionService.addSession(session.getId(), gameSession);
		if (!checkGameOver(gameSession)) {
			createRound(session);
		}
	}

	private String readInput() {
		System.out.print("Enter the instruction: ");
		String instruction = null;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			instruction = reader.readLine();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return instruction;
	}

	private void sendMessage(Session session, String message, boolean closeSession) {
		try {
			session.getBasicRemote().sendText(message);
			if (closeSession) {
				SessionService.removeSession(session.getId());
				session.close();
			}
		} catch (IOException e) {
			LOG.error("Exception: {}", e.getMessage(), e);
		}
	}

	public void closeSession(Session session) {
		LOG.info("Closing session {}",session.getId());
		SessionService.removeSession(session.getId());
		try {
			session.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
