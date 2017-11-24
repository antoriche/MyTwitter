package ihm;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Prop {

  // Create and load properties
  private Properties props = new Properties();


  /**
   * Constructeur de la classe Prop.
   * 
   * @param properties path du fichier properties
   */
  public Prop(String properties) {
    FileInputStream input = null;
    try {
      input = new FileInputStream(properties);
      props.load(input);
    } catch (FileNotFoundException exc) {
      exc.printStackTrace();
      System.exit(1);
    } catch (IOException exc) {
      exc.printStackTrace();
      System.exit(2);
    } finally {
      if (input != null) {
        try {
          input.close();
        } catch (IOException ignore) {
          System.out.println("erreur");
        }
      }
    }
    // Print properties
  }

  public String getProperty(String key) {
    return this.props.getProperty(key);
  }



}


