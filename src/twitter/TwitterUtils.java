package twitter;

import java.util.ArrayList;
import java.util.List;

import oauth.signpost.OAuth;
import twitter4j.Query;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.http.AccessToken;
import android.content.SharedPreferences;

public class TwitterUtils {

	public static boolean isAuthenticated(SharedPreferences prefs) {

		String token = prefs.getString(OAuth.OAUTH_TOKEN, "");
		String secret = prefs.getString(OAuth.OAUTH_TOKEN_SECRET, "");

		AccessToken a = new AccessToken(token, secret);
		Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(Constants.CONSUMER_KEY,
				Constants.CONSUMER_SECRET);
		twitter.setOAuthAccessToken(a);

		try {
			twitter.getAccountSettings();
			return true;
		} catch (TwitterException e) {
			return false;
		}
	}

	public static void sendTweet(SharedPreferences prefs, String msg)
			throws Exception {
		String token = prefs.getString(OAuth.OAUTH_TOKEN, "");
		String secret = prefs.getString(OAuth.OAUTH_TOKEN_SECRET, "");

		AccessToken a = new AccessToken(token, secret);
		Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(Constants.CONSUMER_KEY,
				Constants.CONSUMER_SECRET);
		twitter.setOAuthAccessToken(a);
		twitter.updateStatus(msg);

	}

	public static ArrayList<String> showTweetsAbout(
			final SharedPreferences prefs, final String queryString) {

		String token = prefs.getString(OAuth.OAUTH_TOKEN, "");
		String secret = prefs.getString(OAuth.OAUTH_TOKEN_SECRET, "");

		AccessToken a = new AccessToken(token, secret);
		Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(Constants.CONSUMER_KEY,
				Constants.CONSUMER_SECRET);
		twitter.setOAuthAccessToken(a);
		List<Tweet> tweets = new ArrayList<Tweet>();
		ArrayList<String> tweetTexts = new ArrayList<String>();

		try {
			tweets = twitter.search(new Query(queryString)).getTweets();

			for (Tweet t : tweets) {
				tweetTexts.add(t.getText() + "\n\n");
			}
		} catch (Exception e) {
			tweetTexts.add("Twitter query failed: " + e.toString());
		}
		return tweetTexts;

	}
}
