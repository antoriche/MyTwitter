package business.user;

import java.time.LocalDateTime;

public interface TweetDto {

  public int getTweetId();

  public LocalDateTime getDate();

  public String getText();

  public int getProprietaire();

  public Integer getResponseTo();

  public int getVersion();


  public void setVersion(int version);

  public void setTweetId(int id);

  public void setDate(LocalDateTime date);

  public void setText(String text);

  public void setProprietaire(int user);

  public void setResponseTo(Integer responseTo);


}
