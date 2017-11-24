package dal.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import business.Factory;
import business.exception.FatalException;
import business.user.TweetDto;
import dal.DalBackendServices;
import dal.DalServices;

public class TweetDaoImpl implements TweetDao {
  private DalBackendServices dal;
  private Factory factory;

  /**
   * Constructeur de UserDaoImpl.
   * 
   * @param dal acces DB
   * @param factory instanciation dtos
   */
  public TweetDaoImpl(DalServices dal, Factory factory) {
    this.dal = (DalBackendServices) dal;

    this.factory = factory;
  }

  /**
   * Remplit le dto avec les donnees de la requete.
   * 
   * @param statement requete a executer
   * @return le Dto correspondant
   * @throws SQLException catch ailleurs
   */
  private TweetDto fillTweet(ResultSet res) throws SQLException {
    TweetDto tweet = null;

    if (res.next()) {
      tweet = this.factory.getTweetDto();
      tweet.setTweetId(res.getInt(1));
      tweet.setDate(res.getTimestamp(2).toLocalDateTime());
      tweet.setText(res.getString(3));
      tweet.setProprietaire(res.getInt(4));
      tweet.setResponseTo(res.getInt(5));
      tweet.setVersion(res.getInt(6));
    }
    return tweet;


  }



  @Override
  public int insertTweet(TweetDto tweet) {
    String sql = "INSERT INTO jsb.tweets VALUES(DEFAULT,?,?,?,?,0) RETURNING tweet_id";
    try {
      PreparedStatement procedure = dal.prepare(sql);
      procedure.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
      procedure.setString(2, tweet.getText());
      procedure.setInt(3, tweet.getProprietaire());

      if (tweet.getResponseTo() == null) {
        procedure.setNull(4, java.sql.Types.INTEGER);
      } else {
        procedure.setInt(4, tweet.getResponseTo());
      }


      ResultSet rs = procedure.executeQuery();
      rs.next();


      int resultat = rs.getInt(1); // procedure.executeUpdate();
      if (resultat > 0) {
        return resultat;
      }
    } catch (SQLException exc) {
      exc.printStackTrace();
      throw new FatalException("erreur \"insertTweet\"");
    }
    return -1;
  }

  @Override
  public TweetDto findTweetById(int id) {
    String sql =
        "SELECT tweet_id, date_tweet, text_tweet, proprietaire, responseTo, version_tweet FROM jsb.tweets WHERE tweet_id = ?";

    try {
      PreparedStatement procedure = dal.prepare(sql);
      procedure.setInt(1, id);
      ResultSet res = procedure.executeQuery();
      return fillTweet(res);
    } catch (SQLException exc) {
      exc.printStackTrace();
      throw new FatalException("Erreur \"findTweetById\"");
    }
  }

  @Override
  public int findLastTweetId() {
    String sql =
        "SELECT tweet_id, date_tweet, text_tweet, proprietaire, responseTo, version_tweet FROM jsb.tweets AND tweet_id = (SELECT max(tweet_id) FROM jsb.tweets WHERE responseTo = NULL)";

    try {
      PreparedStatement procedure = dal.prepare(sql);
      ResultSet res = procedure.executeQuery();
      return fillTweet(res).getTweetId();
    } catch (SQLException exc) {
      exc.printStackTrace();
      throw new FatalException("Erreur \"findLastTweetId\"");
    }
  }

  @Override
  public List<TweetDto> findTweets(int last, int num, Integer respondTo) {
    ArrayList<TweetDto> ret = new ArrayList();
    String sql =
        "SELECT tweet_id, date_tweet, text_tweet, proprietaire, responseTo, version_tweet FROM jsb.tweets WHERE tweet_id <= ? AND respondTo = ? ORDER BY tweet_id LIMITS ?";

    try {
      PreparedStatement procedure = dal.prepare(sql);
      procedure.setInt(1, last);
      procedure.setInt(3, num);
      if (respondTo == null) {
        procedure.setNull(2, java.sql.Types.INTEGER);
      } else {
        procedure.setInt(2, respondTo);
      }
      ResultSet res = procedure.executeQuery();
      TweetDto dto;
      while ((dto = fillTweet(res)) != null) {
        ret.add(dto);
      }
      return ret;
    } catch (SQLException exc) {
      exc.printStackTrace();
      throw new FatalException("Erreur \"findLastTweetId\"");
    }
  }


}
