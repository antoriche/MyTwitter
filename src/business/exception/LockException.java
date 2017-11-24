package business.exception;

public class LockException extends RuntimeException {

  public LockException() {

  }

  public LockException(String msg) {
    super(msg);
  }

}
