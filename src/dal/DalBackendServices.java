package dal;

import java.sql.PreparedStatement;


public interface DalBackendServices {

  public PreparedStatement prepare(String statement);


}
