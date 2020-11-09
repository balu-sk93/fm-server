package fit.fm.server.utils;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;

import fit.fm.server.dto.GameSession;
import fit.fm.server.service.GameService;

public class CacheMap <K, V> extends ConcurrentHashMap<K, V> {

    private static final long serialVersionUID = 1L;

    private Map<K, Long> timeMap;
    
    @Value("${timeout}")
    private long expiryInMillis;
    private GameService gameService;

    public CacheMap() {
    	
        initialize();
    }

    public CacheMap(long expiryInMillis) {
        this.expiryInMillis = expiryInMillis;
        initialize();
    }
    
    public CacheMap(long expiryInMillis, int size) {
        this.expiryInMillis = expiryInMillis;
        initialize();
    }

    void initialize() {
    	gameService = new GameService();
    	timeMap = new ConcurrentHashMap<K, Long>();
        new CleanerThread().start();
    }

    @Override
    public V put(K key, V value) {
        Date date = new Date();
        timeMap.put(key, date.getTime());
        V returnVal = super.put(key, value);
        return returnVal;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (K key : m.keySet()) {
            put(key, m.get(key));
        }
    }

    @Override
    public V putIfAbsent(K key, V value) {
        if (!containsKey(key))
            return put(key, value);
        else
            return get(key);
    }

    class CleanerThread extends Thread {
        @Override
        public void run() {
            while (true) {
                retry();
                try {
                    Thread.sleep(expiryInMillis / 2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void retry() {
            long currentTime = new Date().getTime();
            if (timeMap!=null)
            for (K key : timeMap.keySet()) {
                if (currentTime > (timeMap.get(key) + expiryInMillis)) {
                	GameSession session = (GameSession) get(key);
                	if (session.getRetry() >= 2) {
                		gameService.closeSession(session.getSession());
                		remove(key);
                		timeMap.remove(key);
                	} else {
                		gameService.retry(session);
                	}
                }
            }
        }
    }
}