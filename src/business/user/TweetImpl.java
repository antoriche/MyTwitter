package business.user;

import java.time.LocalDateTime;

public class TweetImpl implements Tweet {
  private int tweetId;
  private String text;
  private int proprietaire;
  private LocalDateTime date;
  private Integer responseTo;

  private int version;


  public int getVersion() {
    return version;
  }


  public void setVersion(int version) {
    this.version = version;
  }



  public TweetImpl() {
    super();
  }

  @Override
  public String toString() {
    return "TweetImpl [tweetId=" + tweetId + ", text=" + text + ", proprietaire=" + proprietaire
        + ", date=" + date + ", version=" + version + "]";
  }



  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + tweetId;
    return result;
  }


  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    TweetImpl other = (TweetImpl) obj;
    if (tweetId != other.tweetId)
      return false;
    return true;
  }


  @Override
  public boolean checkValidite() {
    if (this.getDate() == null || this.getText() == null || this.getText() == "") {
      return false;
    }

    if (this.getText().length() > 140) {
      return false;
    }


    return true;
  }


  @Override
  public int getTweetId() {
    return tweetId;
  }


  @Override
  public LocalDateTime getDate() {
    return date;
  }


  @Override
  public String getText() {
    return text;
  }


  @Override
  public int getProprietaire() {
    return proprietaire;
  }


  @Override
  public void setTweetId(int id) {
    this.tweetId = id;
  }


  @Override
  public void setDate(LocalDateTime date) {
    this.date = date;
  }


  @Override
  public void setText(String text) {
    this.text = text;
  }


  @Override
  public void setProprietaire(int user) {
    this.proprietaire = user;
  }


  @Override
  public Integer getResponseTo() {
    return responseTo;
  }


  @Override
  public void setResponseTo(Integer responseTo) {
    this.responseTo = responseTo;
    if (responseTo == null || responseTo <= 0)
      this.responseTo = null;
  }



}
