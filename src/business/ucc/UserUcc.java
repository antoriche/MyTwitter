package business.ucc;

import business.user.UserDto;

public interface UserUcc {

  /**
   * Permet de verifier la validite de connexion.
   * 
   * @param user utilisateur a connecter
   * @return true if connection request is successful, false otherwise
   */
  public UserDto connect(UserDto user);

  public UserDto signup(UserDto user);

  public UserDto getUserFromId(int id);

}
