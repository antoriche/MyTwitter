package business;

import business.user.TweetDto;
import business.user.TweetImpl;
import business.user.UserDto;
import business.user.UserImpl;

public class FactoryImpl implements Factory {


  public FactoryImpl() {
    super();
    // TODO Auto-generated constructor stub
  }

  @Override
  public UserDto getUserDto() {
    return (UserDto) new UserImpl();
  }

  @Override
  public TweetDto getTweetDto() {
    // TODO Auto-generated method stub
    return (TweetDto) new TweetImpl();
  }

}
