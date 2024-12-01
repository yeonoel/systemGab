package kernel.tech.systemgab.rest;


import kernel.tech.systemgab.business.TransactionBusiness;
import kernel.tech.systemgab.utils.contract.Response;
import kernel.tech.systemgab.utils.dto.CompteDto;
import kernel.tech.systemgab.utils.dto.TransactionDto;
import kernel.tech.systemgab.utils.enums.TypeOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
        log.info("effectuer transaction detype  : {}", transactionDto.getTypeOperation());
        Response<CompteDto> response = new Response<>();

        if(transactionDto.getTypeOperation() == TypeOperation.RETRAIT ) {
            response = transactionBusinnes.retrait(transactionDto);
        } else if(transactionDto.getTypeOperation() == TypeOperation.DEPOT) {
            response = transactionBusinnes.depot(transactionDto);
        } else {
            response.setStatus("error transactionType");
            response.setMessage("Type d'operation non supporté");
        }
        return response;
    }

    @RequestMapping(value = "/history", method = RequestMethod.POST , consumes =  {"application/json"}, produces = {"application/json"})
    public Response<TransactionDto> history(@RequestBody TransactionDto transactionDto) {
        log.info("Debut Voir Historique de transaction  : {}");
        Response<TransactionDto> response = new Response<>();
        response = transactionBusinnes.historique(transactionDto);
        return response;
    }




}
