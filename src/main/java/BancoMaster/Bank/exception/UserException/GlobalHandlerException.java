package BancoMaster.Bank.exception.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalHandlerException {

    @ExceptionHandler(EmailAlreadyExistException.class)
    public ResponseEntity<RestErrorMessage> email(EmailAlreadyExistException e){
    RestErrorMessage errorMessage = new RestErrorMessage(HttpStatus.ALREADY_REPORTED, e.getMessage());
    return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(errorMessage);
    }

    @ExceptionHandler (CodeExpiredException.class)
    public ResponseEntity<RestErrorMessage>  expired(CodeExpiredException e){
        RestErrorMessage errorMessage = new RestErrorMessage(HttpStatus.BAD_REQUEST, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler (CodenotFoundException.class)
    public ResponseEntity<RestErrorMessage>  codenotfound(CodenotFoundException e){
        RestErrorMessage errorMessage = new RestErrorMessage(HttpStatus.BAD_REQUEST, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler (UsernotfoundException.class)
    public ResponseEntity<RestErrorMessage>  usernotfound(UsernotfoundException e){
        RestErrorMessage errorMessage = new RestErrorMessage(HttpStatus.NOT_FOUND,  e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }

    @ExceptionHandler (InvalidCodeException.class)
    public ResponseEntity<RestErrorMessage>  invalid (InvalidCodeException e){
        RestErrorMessage errorMessage = new RestErrorMessage(HttpStatus.BAD_REQUEST,  e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }





}
