package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.commons.dbcp2.BasicDataSource;

import business.exception.FatalException;

public class DalServicesImpl implements DalServices, DalBackendServices {
  private BasicDataSource ds;
  private ThreadLocal<Connection> threadMap;
  private ThreadLocal<Integer> semaphore;

  /**
   * Constructeur de DalServicesImpl.
   * 
   * @param url url de la base de donnees
   */
  public DalServicesImpl(String url, String user, String password) {
    threadMap = new ThreadLocal<Connection>();
    semaphore = new ThreadLocal<Integer>();
    ds = new BasicDataSource();
    ds.setDriverClassName("org.postgresql.Driver");
    ds.setUsername(user);
    ds.setPassword(password);
    ds.setUrl(url);

  }



  @Override
  public PreparedStatement prepare(String statement) {
    try {
      return threadMap.get().prepareStatement(statement);
    } catch (SQLException exc) {
      throw new FatalException("erreur SQL");
    }
  }



  @Override
  public void startTransaction() {
    openConnexion();
    try {
      threadMap.get().setAutoCommit(false);
    } catch (SQLException exc) {
      throw new FatalException("erreur \"Start transaction\"");
    }
  }



  @Override
  public void commit() {
    try {
      threadMap.get().commit();
      threadMap.get().setAutoCommit(true);
    } catch (SQLException exc) {
      throw new FatalException("erreur \"commit\"");
    } finally {
      closeConnexion();
    }

  }

  @Override
  public void rollback() {
    try {
      threadMap.get().rollback();
    } catch (SQLException exc) {
      throw new FatalException("erreur \"rollback\"");
    }
  }

  @Override
  public void openConnexion() {
    Integer actuelle;
    if (semaphore.get() == null) {
      actuelle = 0;
    } else {
      actuelle = semaphore.get();
    }
    if (actuelle == 0) {
      try {
        threadMap.set(ds.getConnection());
      } catch (SQLException exc) {
        throw new FatalException("Erreur \"openConnexion\"");
      }
    }
    semaphore.set(++actuelle);
  }



  @Override
  public void closeConnexion() {
    Integer actuelle = semaphore.get();
    if (actuelle == 1) {
      Connection connexion = threadMap.get();
      threadMap.remove();
      try {
        connexion.close();
      } catch (SQLException ex) {
        throw new FatalException("Erreur \"closeConnexion\"");
      }
    }
    semaphore.set(--actuelle);
  }
}
