package business.exception;

public class FatalException extends RuntimeException {
  public FatalException() {

  }

  public FatalException(String msg) {
    super(msg);
  }
}
