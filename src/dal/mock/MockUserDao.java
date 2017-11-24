package dal.mock;

import java.util.ArrayList;

import business.user.UserDto;
import dal.dao.UserDao;

public class MockUserDao implements UserDao {

  ArrayList<UserDto> list;

  public MockUserDao() {
    list = new ArrayList<UserDto>();
  }

  @Override
  public UserDto findUserByLogin(String login) {
    if (login == null) {
      throw new IllegalArgumentException();
    }
    for (UserDto u : list) {
      if (login.equals(u.getPseudo())) {
        return u;
      }
    }
    return null;
  }

  @Override
  public boolean createUser(UserDto user) {

    return list.add(user);

  }

  @Override
  public UserDto findUserByEmail(String email) {
    if (email == null) {
      throw new IllegalArgumentException();
    }
    for (UserDto user : list) {
      if (user.getEmail().equals(email)) {
        return user;
      }
    }
    return null;
  }

  @Override
  public UserDto findUserById(int id) {

    for (UserDto user : list) {
      if (user.getUtilisateurId() == id) {
        return user;
      }
    }
    return null;
  }

}
