package business.ucc;

import java.util.List;

import business.user.TweetDto;

public interface TweetUcc {

  public TweetDto envoyerTweet(TweetDto tweet);

  public List<TweetDto> listTweets(Integer last, int num, Integer respondTo);

}
