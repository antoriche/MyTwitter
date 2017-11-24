package business.user;

public interface User extends UserDto {

  /**
   * Verifie le mot de passe en parametre avec celui du user.
   * 
   * @param password mot de passe a comparer
   * @return true if password entered as parameter corresponds to the user's password, false
   *         otherwise
   */
  public boolean checkPassword(String password);

  public boolean checkValidite();

}
