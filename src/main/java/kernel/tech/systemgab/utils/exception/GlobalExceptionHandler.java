package kernel.tech.systemgab.utils.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

/**
 * GlobalExceptionHandler for execption managment
 *
 * @author yeonoel
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Gestion des execption quand un client est introuvale
     */
    @ExceptionHandler(ClientIntrouvableException.class)
    public ResponseEntity<ErrorResponse> handleClientIntrouvable(ClientIntrouvableException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Gestion des execption quand un COpte est introuvale
     */
    @ExceptionHandler(CompteIntrouvableException.class)
    public ResponseEntity<ErrorResponse> handleCompteIntrouvable(CompteIntrouvableException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Gestion des execption quand le numero de carte est introuvale
     */
    @ExceptionHandler(InternalErrorException.class)
    public ResponseEntity<ErrorResponse> handleInternalErrorException(InternalErrorException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Gestion des execption quand le numero de carte est introuvale
     */
    @ExceptionHandler(CartIntrouvableException.class)
    public ResponseEntity<ErrorResponse> handleCartIntrouvableException(CartIntrouvableException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Gestion des execption quand un le solde est insufisant
     */
    @ExceptionHandler(SoldeInsuffisantException.class)
    public ResponseEntity<ErrorResponse> handleSoldeInsuffisant(SoldeInsuffisantException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Gestion des execption pour la validation de champs de requete
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Gestion des execptions quand ya pas d'historique de transaction
     */
    @ExceptionHandler(HistoriqueTransactionException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(HistoriqueTransactionException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Gestion des execption d'erreur interne
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        return buildErrorResponse("Erreur interne du serveur.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * cette methoe est generic elle est utilisé pour contruire le reponse à envoyer au client
     * en cas d'erreur
     */
    private ResponseEntity<ErrorResponse> buildErrorResponse(String message, HttpStatus status) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message
        );
        return new ResponseEntity<>(errorResponse, status);
    }
}
