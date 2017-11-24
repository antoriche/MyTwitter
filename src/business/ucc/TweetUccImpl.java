package business.ucc;

import java.util.List;

import business.Factory;
import business.exception.BizException;
import business.exception.FatalException;
import business.user.Tweet;
import business.user.TweetDto;
import dal.DalServices;
import dal.dao.TweetDao;

public class TweetUccImpl implements TweetUcc {
  private TweetDao tweetDao;
  private DalServices dal;
  Factory factory;

  /**
   * Constructeur de UserUccImpl.
   * 
   * @param dao dao a utiliser
   * @param dal dal a utiliser
   */
  public TweetUccImpl(TweetDao tweetDao, DalServices dal, Factory factory) {
    super();
    this.tweetDao = tweetDao;
    this.dal = dal;
    this.factory = factory;
  }



  @Override
  public TweetDto envoyerTweet(TweetDto tweet) {
    dal.startTransaction();
    try {
      if (!((Tweet) tweet).checkValidite()) {
        throw new BizException("tweet invalide");
      }
      int id;
      if ((id = this.tweetDao.insertTweet(tweet)) >= 0) {
        return this.tweetDao.findTweetById(id);
      }
      throw new FatalException("erreur lors de l'insertion du tweet");
    } catch (Exception exc) {
      dal.rollback();
      throw exc;
    } finally {
      dal.commit();
    }
  }

  @Override
  public List<TweetDto> listTweets(Integer last, int num, Integer respondTo) {
    dal.startTransaction();
    try {
      if (last == null) {
        last = tweetDao.findLastTweetId();
      }
      return tweetDao.findTweets(last, num, respondTo);
    } catch (Exception exc) {
      dal.rollback();
      throw exc;
    } finally {
      dal.commit();
    }
  }


}
