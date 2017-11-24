package dal;

public interface DalServices {

  public void startTransaction();

  public void commit();

  public void rollback();

  public void openConnexion();

  public void closeConnexion();

}
