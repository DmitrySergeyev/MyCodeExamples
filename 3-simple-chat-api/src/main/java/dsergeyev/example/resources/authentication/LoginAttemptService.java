package dsergeyev.example.resources.authentication;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

@Service
public class LoginAttemptService {

	private final int MAX_ATTEMPT = 10;
	public static final int DURATION_HOURS = 1;
	
	private LoadingCache<String, Integer> attemptsCache;

	public LoginAttemptService() {
		super();
		this.attemptsCache = CacheBuilder.newBuilder().expireAfterWrite(DURATION_HOURS, TimeUnit.HOURS)
				.build(new CacheLoader<String, Integer>() {
					public Integer load(String key) {
						return 0;
					}
				});
	}

	public void loginSucceeded(String key) {
		this.attemptsCache.invalidate(key);
	}

	public void loginFailed(String key) {
		int attempts = 0;
		try {
			attempts = this.attemptsCache.get(key);
		} catch (ExecutionException e) {
			attempts = 0;
		}
		attempts++;
		this.attemptsCache.put(key, attempts);
	}

	public boolean isBlocked(String key) {
		try {
			return this.attemptsCache.get(key) >= MAX_ATTEMPT;
		} catch (ExecutionException e) {
			return false;
		}
	}
}
