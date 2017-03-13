/**
 * $Header:
 * $Id:
 * $Name:
 */
package com.sclerck;

import java.util.Properties;

import org.jasypt.util.text.BasicTextEncryptor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.vertx.core.AbstractVerticle;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.auth.AccessToken;

/**
 * Purpose: Subscribes to live feed from twitter
 * 
 * @author Simon
 * @date 12 Mar 2017
 *
 */
public class TwitterStreamVerticle extends AbstractVerticle {

	@Override
	public void start() throws Exception {

		Gson gson = new GsonBuilder().create();

		Properties properties = new Properties();
		properties.load(TwitterStreamVerticle.class.getClassLoader().getResourceAsStream("twitterreports.properties"));

		StatusListener listener = new StatusListener() {
			public void onStatus(Status status) {
				if (status.getLang().equals("en") && status.getHashtagEntities() != null
						&& status.getHashtagEntities().length > 0)
					vertx.eventBus().send("tweet", gson.toJson(status));
			}

			public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
			}

			public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
			}

			public void onException(Exception ex) {
				ex.printStackTrace();
			}

			public void onScrubGeo(long userId, long upToStatusId) {
			}

			public void onStallWarning(StallWarning warning) {
			}
		};

		TwitterStream twitterStream = new TwitterStreamFactory().getInstance();

		BasicTextEncryptor te = new BasicTextEncryptor();
		te.setPassword(System.getProperty("key"));

		String consumerSecretEnc = properties.getProperty("consumerSecret");

		String consumerSecret = te.decrypt(consumerSecretEnc);

		twitterStream.setOAuthConsumer(properties.getProperty("consumerKey"), consumerSecret);

		String accessTokenSecretEnc = properties.getProperty("accessTokenSecret");

		String accessTokenSecret = te.decrypt(accessTokenSecretEnc);

		AccessToken accessToken = new AccessToken(properties.getProperty("accessToken"), accessTokenSecret);

		twitterStream.setOAuthAccessToken(accessToken);

		twitterStream.addListener(listener); // sample() method internally
												// creates a thread which
												// manipulates TwitterStream and
												// calls these adequate listener
												// methods continuously.
		twitterStream.sample();

	}

}
