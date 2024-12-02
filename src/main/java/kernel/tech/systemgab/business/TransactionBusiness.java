package kernel.tech.systemgab.business;

import kernel.tech.systemgab.dao.entity.Client;
import kernel.tech.systemgab.dao.entity.Compte;
import kernel.tech.systemgab.dao.entity.Transaction;
import kernel.tech.systemgab.dao.repository.ClientRepository;
import kernel.tech.systemgab.dao.repository.CompteRepository;
import kernel.tech.systemgab.dao.repository.TransactionRepository;
import kernel.tech.systemgab.utils.contract.Response;
import kernel.tech.systemgab.utils.dto.CompteDto;
import kernel.tech.systemgab.utils.dto.ResponseTransactionDto;
import kernel.tech.systemgab.utils.dto.TransactionDto;
import kernel.tech.systemgab.utils.enums.StatutTransaction;
import kernel.tech.systemgab.utils.enums.TypeOperation;
import kernel.tech.systemgab.utils.transformer.TransactionTransformer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


/**
 BUSINESS for All transaction
 *
 * @author yeonoel
 *
 */
@Slf4j
@Service
public class TransactionBusiness {

    private final TransactionRepository transactionRepository;
    private final CompteRepository compteRepository;
    private final ClientRepository clientRepository;
    private final TransactionTransformer transactionTransformer;


    public TransactionBusiness(TransactionRepository transactionRepository, CompteRepository compteRepository, ClientRepository clientRepository, TransactionTransformer transactionTransformer) {
        this.transactionRepository = transactionRepository;
        this.compteRepository = compteRepository;
        this.clientRepository = clientRepository;
        this.transactionTransformer = transactionTransformer;
    }


    /**
     * executeTransaction by using TransactionDto as object.
     *
     * @param transactionDto
     * @return CompteDto
     *
     */
    public Response<CompteDto> executeTransaction(TransactionDto transactionDto) {
        log.info("----begin "+ transactionDto.getTypeOperation() +" Transaction -----");
        Response<CompteDto> response = new Response<>();

        // checks if field is filled CompteId
        if(transactionDto.getCompteId() == null || transactionDto.getCompteId() == 0) {
            response.setStatus("Error");
            response.setMessage("le champ  CompteId  doit être correctement rempli" + transactionDto.getCompteId());
            return response;
        }
        // checks if field is filled ClientId
        if(transactionDto.getClientId() == null || transactionDto.getClientId() == 0) {
            response.setStatus("Error");
            response.setMessage("le champ  ClientId  doit être correctement rempli" + transactionDto.getClientId());
            return response;
        }

        //check if customer exits
        Optional<Client> client = clientRepository.findById(Long.valueOf(transactionDto.getClientId()));
        if (!client.isPresent()) {
            response.setStatus("Error");
            response.setMessage("Client  introuvable");
            return response;

        }

        // Check if account exist
        Optional<Compte> compte = compteRepository.findByCompteIdAndClient(transactionDto.getCompteId(), client.get());
        if (!compte.isPresent()) {
            response.setStatus("Error");
            response.setMessage("Compte introuvable");
            return response;

        }
        Compte compteFound = compte.get();



        //Check  type operation
        Compte compteSave = null;
        if(transactionDto.getTypeOperation() == TypeOperation.RETRAIT ) {
            // I check if the balance is sufficient
            if (compteFound.getSolde().compareTo(transactionDto.getMontant()) < 0) {
                response.setStatus("Error");
                response.setMessage("Solde insuffisant");
                //Create new transaction  for ECHEC withdrawal
                newTransaction(compteFound, transactionDto, TypeOperation.RETRAIT ,StatutTransaction.ECHEC, "Solde insuffisant");

                return response;
            }
            // Subtract balance amount
            compteFound.setSolde(compteFound.getSolde().subtract(transactionDto.getMontant()));
            compteSave =  compteRepository.save(compteFound);
            //Create new transaction  for SUCCESS withdrawal
            newTransaction(compteFound, transactionDto, TypeOperation.RETRAIT ,StatutTransaction.SUCCES, "Operation effectuée avec succes");
            response.setMessage("Retrait effectué avec succes");

        } else if(transactionDto.getTypeOperation() == TypeOperation.DEPOT) {
             //add the amount to the  balance
            compteFound.setSolde(compteFound.getSolde().add(transactionDto.getMontant()));
            compteSave =  compteRepository.save(compteFound);
            //Create new transaction  for SUCCESS withdrawal
            newTransaction(compteFound, transactionDto, TypeOperation.DEPOT ,StatutTransaction.SUCCES, "");
            response.setMessage("Depot effectué avec succes");

        }



        CompteDto compteDto = new CompteDto();
        compteDto.setNouveauSolde(compteSave.getSolde());

        response.setStatus("Success");
        response.setData(compteDto);

        log.info("----end "+ transactionDto.getTypeOperation() +" Transaction -----");
        return response;
    }



    /**
     * historique by using TransactionDto as object.
     *
     * @param transactionDto
     * @return ResponseTransactionDto
     *
     */
    public Response<ResponseTransactionDto> historique(TransactionDto transactionDto) {
        log.info("----begin historique Transaction -----");
        Response<ResponseTransactionDto> response = new Response<>();

        // checks if field is filled CompteId
        if(transactionDto.getCompteId() == null || transactionDto.getCompteId() == 0) {
            response.setStatus("Error");
            response.setMessage("le champ  CompteId  doit être correctement rempli" + transactionDto.getCompteId());
            return response;
        }
        // checks if field is filled ClientId
        if(transactionDto.getClientId() == null || transactionDto.getClientId() == 0) {
            response.setStatus("Error");
            response.setMessage("le champ  ClientId  doit être correctement rempli" + transactionDto.getClientId());
            return response;
        }

        //check if customer exits
        Optional<Client> client = clientRepository.findById(Long.valueOf(transactionDto.getClientId()));
        if (!client.isPresent()) {
            response.setStatus("Error");
            response.setMessage("Client  introuvable");
            return response;

        }

        // Check if account exist
        Optional<Compte> compte = compteRepository.findByCompteIdAndClient(transactionDto.getCompteId(), client.get());
        if (!compte.isPresent()) {
            response.setStatus("Error");
            response.setMessage("Compte introuvable");
            return response;

        }
        Compte compteFound = compte.get();

        // On recuperer  les transactions
        List<Transaction> transactions = transactionRepository.findAllByCompte(compte.get());
        if (transactions.isEmpty()) {
            response.setStatus("Error");
            response.setMessage("Aucune transaction trouvée pour ce compte.");
            return response;
        }

        //Transformation en DTO
        List<ResponseTransactionDto> transactionDtos = transactionTransformer.toDtoList(transactions);

        //Construction de la réponse
        response.setStatus("Success");
        response.setMessage("HIstorique récupéreés avec succès.");
        response.setDatas(transactionDtos);

        log.info("----end historique Transaction -----");
        return response;

    }

    public void newTransaction(Compte comptefound, TransactionDto transactionDto, TypeOperation type,  StatutTransaction status, String message) {
        Transaction transaction = new Transaction();
        transaction.setCompte(comptefound);
        transaction.setTypeOperation(type);
        transaction.setMontant(transactionDto.getMontant());
        transaction.setDateTransaction(LocalDateTime.now());
        transaction.setStatut(status);
        transaction.setMessage(message);
        transactionRepository.save(transaction);

    }
}



