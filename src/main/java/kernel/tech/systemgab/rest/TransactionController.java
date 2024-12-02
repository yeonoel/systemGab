package kernel.tech.systemgab.rest;


import kernel.tech.systemgab.business.TransactionBusiness;
import kernel.tech.systemgab.utils.contract.Response;
import kernel.tech.systemgab.utils.dto.CompteDto;
import kernel.tech.systemgab.utils.dto.ResponseTransactionDto;
import kernel.tech.systemgab.utils.dto.TransactionDto;
import kernel.tech.systemgab.utils.enums.TypeOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;



/**
 Controller for all trasactions
 *
 * @author yeonoel
 *
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class TransactionController {

    private final TransactionBusiness transactionBusinnes;

    public TransactionController(TransactionBusiness transactionBusinnes) {
        this.transactionBusinnes = transactionBusinnes;
    }

    @RequestMapping(value = "/transaction", method = RequestMethod.POST , consumes =  {"application/json"}, produces = {"application/json"})
    public Response<CompteDto> effectuerTransaction(@RequestBody TransactionDto transactionDto) {
        log.info("debut tansaction de type  : {}", transactionDto.getTypeOperation());
        Response<CompteDto> response = new Response<>();
        // check type operation and choose the good method
        if(transactionDto.getTypeOperation() == TypeOperation.RETRAIT || transactionDto.getTypeOperation() == TypeOperation.DEPOT ) {
            response = transactionBusinnes.executeTransaction(transactionDto);
        } else {
            response.setStatus("error Transaction Type");
            response.setMessage("Type d'operation non supporté");
        }
        return response;
    }

    @RequestMapping(value = "/history", method = RequestMethod.POST , consumes =  {"application/json"}, produces = {"application/json"})
    public Response<ResponseTransactionDto> history(@RequestBody TransactionDto transactionDto) {
        log.info("Debut  Historique de transaction  : {}");
        Response<ResponseTransactionDto> response = new Response<>();
        response = transactionBusinnes.historique(transactionDto);
        return response;
    }




}
