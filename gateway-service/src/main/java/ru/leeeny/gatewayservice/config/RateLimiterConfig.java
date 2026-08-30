package ru.leeeny.gatewayservice.config;

import io.github.bucket4j.distributed.ExpirationAfterWriteStrategy;
import io.github.bucket4j.distributed.proxy.AsyncProxyManager;
import io.github.bucket4j.redis.lettuce.Bucket4jLettuce;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RateLimiterConfig {

	@Value("${spring.data.redis.host}")
	private String redisHost;

	@Value("${spring.data.redis.port}")
	private int redisPort;

	@Bean
	public RedisClient redisClient() {
		return RedisClient.create(
				"redis://" + redisHost + ":" + redisPort
		);
	}

	@Bean
	public AsyncProxyManager<String> rateLimitProxyManager(RedisClient redisClient) {
		StatefulRedisConnection<String, byte[]> connection = redisClient.connect(
				RedisCodec.of(StringCodec.UTF8,
						ByteArrayCodec.INSTANCE)
		);

		return Bucket4jLettuce
				.casBasedBuilder(connection)
				.expirationAfterWrite(
						ExpirationAfterWriteStrategy.basedOnTimeForRefillingBucketUpToMax(Duration.ofSeconds(10))
				).build()
				.asAsync();
	}
}
