package dal.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import business.Factory;
import business.exception.FatalException;
import business.user.UserDto;
import dal.DalBackendServices;
import dal.DalServices;

public class UserDaoImpl implements UserDao {
  private DalBackendServices dal;
  private Factory factory;

  /**
   * Constructeur de UserDaoImpl.
   * 
   * @param dal acces DB
   * @param factory instanciation dtos
   */
  public UserDaoImpl(DalServices dal, Factory factory) {
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
  private UserDto fillUser(ResultSet res) throws SQLException {
    UserDto user = null;

    if (res.next()) {
      user = this.factory.getUserDto();
      user.setUtilisateurId(res.getInt(1));
      user.setEmail(res.getString(2));
      user.setPseudo(res.getString(3));
      user.setMotDePasse(res.getString(4));
      user.setDateInscription(res.getTimestamp(5).toLocalDateTime().toLocalDate());
      user.setNom(res.getString(6));
      user.setPrenom(res.getString(7));
      user.setVersion(res.getInt(8));
    }
    return user;


  }


  /**
   * Cherche le user ayant login comme pseudo.
   * 
   * @param login login a rechercher
   * 
   * @return UserDto having login as pseudo, null if not found
   */
  public UserDto findUserByLogin(String login) {
    String sql =
        "SELECT utilisateur_id, email, pseudo, mot_de_passe, date_inscription, nom, prenom, version_utilisateur FROM jsb.utilisateurs WHERE pseudo = ?";

    try {
      PreparedStatement procedure = dal.prepare(sql);
      procedure.setString(1, login);
      ResultSet res = procedure.executeQuery();
      return fillUser(res);

    } catch (SQLException exc) {
      throw new FatalException("Erreur \"findUserByLogin\"");
    }

  }


  @Override
  public boolean createUser(UserDto user) {
    String sql = "INSERT INTO jsb.utilisateurs VALUES(DEFAULT,?,?,?,?,?,?,?)";
    try {
      PreparedStatement procedure = dal.prepare(sql);
      procedure.setString(1, user.getEmail());
      procedure.setString(2, user.getPseudo());
      procedure.setString(3, user.getMotDePasse());
      procedure.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
      procedure.setString(5, user.getPrenom());
      procedure.setString(6, user.getNom());
      procedure.setInt(7, 0);
      int resultat = procedure.executeUpdate();
      if (resultat != 0) {
        return true;
      }
    } catch (SQLException exc) {
      throw new FatalException("erreur \"createUser\"");
    }
    return false;
  }


  @Override
  public UserDto findUserByEmail(String email) {
    String sql =
        "SELECT utilisateur_id, email, pseudo, mot_de_passe, date_inscription, nom, prenom, version_utilisateur FROM jsb.utilisateurs WHERE email = ?";

    try {
      PreparedStatement procedure = dal.prepare(sql);
      procedure.setString(1, email);
      ResultSet res = procedure.executeQuery();
      return fillUser(res);
    } catch (SQLException exc) {
      throw new FatalException("Erreur \"findUserByEmail\"");
    }

  }


  @Override
  public UserDto findUserById(int id) {
    String sql =
        "SELECT utilisateur_id, email, pseudo, mot_de_passe, date_inscription, nom, prenom, version_utilisateur FROM jsb.utilisateurs WHERE utilisateur_id = ?";

    try {
      PreparedStatement procedure = dal.prepare(sql);
      procedure.setInt(1, id);
      ResultSet res = procedure.executeQuery();
      return fillUser(res);
    } catch (SQLException exc) {
      throw new FatalException("Erreur \"findUserById\"");
    }
  }


}
