package business.ucc;

import business.Factory;
import business.exception.BizException;
import business.exception.FatalException;
import business.user.User;
import business.user.UserDto;
import dal.DalServices;
import dal.dao.UserDao;

public class UserUccImpl implements UserUcc {
  private UserDao userDao;
  private DalServices dal;
  Factory factory;

  /**
   * Constructeur de UserUccImpl.
   * 
   * @param dao dao a utiliser
   * @param dal dal a utiliser
   */
  public UserUccImpl(UserDao userDao, DalServices dal, Factory factory) {
    super();
    this.userDao = userDao;
    this.dal = dal;
    this.factory = factory;
  }

  @Override
  public UserDto signup(UserDto user) {

    dal.startTransaction();
    try {
      if (!((User) user).checkValidite()) {
        throw new BizException("utilisateur invalide");
      }
      if (this.userDao.findUserByLogin(user.getPseudo()) == null) {
        if (this.userDao.findUserByEmail(user.getEmail()) == null) {
          if (this.userDao.createUser(user)) {
            return this.userDao.findUserByLogin(user.getPseudo());

          }
          throw new FatalException("Impossible de créer un nouveau utilisateur");
        }
        throw new BizException("Erreur : Cet email est déja utilisé");
      }
      throw new BizException("Erreur : Ce pseudo est déja utilisé");
    } catch (Exception exc) {
      dal.rollback();
      throw exc;
    } finally {
      dal.commit();
    }



  }


  @Override
  public UserDto connect(UserDto user) {
    dal.startTransaction();
    try {
      User use = (User) this.userDao.findUserByLogin(user.getPseudo());
      if (use != null && use.checkPassword(user.getMotDePasse())) {
        return (UserDto) use;
      }
      return null;
    } catch (Exception exc) {
      dal.rollback();
      throw exc;
    } finally {
      dal.commit();
    }



  }


  @Override
  public UserDto getUserFromId(int id) {
    dal.startTransaction();
    try {
      UserDto user;
      user = userDao.findUserById(id);
      return user;
    } catch (Exception exc) {
      dal.rollback();
      throw exc;
    } finally {
      dal.commit();
    }
  }


}
