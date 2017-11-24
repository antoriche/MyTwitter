package dal.dao;

import java.util.List;

import business.user.TweetDto;

public interface TweetDao {

  public int insertTweet(TweetDto tweet);

  public TweetDto findTweetById(int id);

  public int findLastTweetId();

  public List<TweetDto> findTweets(int last, int num, Integer respondTo);

}
