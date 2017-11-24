package dal.dao;

import business.user.UserDto;

public interface UserDao {

  /**
   * Cherche le user ayant login comme pseudo.
   * 
   * @param login login a rechercher
   * @return UserDto having login as pseudo, null if not found
   */
  public UserDto findUserByLogin(String login);

  public UserDto findUserByEmail(String email);

  public boolean createUser(UserDto user);

  public UserDto findUserById(int id);
}
