package business;

import business.user.TweetDto;
import business.user.UserDto;

public interface Factory {


  UserDto getUserDto();

  TweetDto getTweetDto();

}
