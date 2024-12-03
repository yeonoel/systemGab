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
import kernel.tech.systemgab.utils.exception.*;
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

        //fields  validation
        if(transactionDto.getCompteId() == null || transactionDto.getCompteId() == 0) {
            throw new ValidationException("Le champ CompteId doit être correctement rempli ->" + transactionDto.getCompteId());
        }
        if(transactionDto.getClientId() == null || transactionDto.getClientId() == 0) {
            throw new ValidationException("Le champ ClientId doit être correctement rempli ->" + transactionDto.getClientId());
        }

        //check if customer exits
        Client client = clientRepository.findById(Long.valueOf(transactionDto.getClientId()))
                .orElseThrow(() -> new ClientIntrouvableException("Client Introuvable"));



        // Check if account exist
        Compte compteFound = compteRepository.findByCompteIdAndClient(transactionDto.getCompteId(), client)
                .orElseThrow(() -> new CompteIntrouvableException("Compte Introuvable"));

        //Check  type operation
        Compte compteSave = null;
        if(transactionDto.getTypeOperation() == TypeOperation.RETRAIT ) {
            // I check if the balance is sufficient
            if (compteFound.getSolde().compareTo(transactionDto.getMontant()) < 0) {
                //Create new transaction  for ECHEC withdrawal
                newTransaction(compteFound, transactionDto, TypeOperation.RETRAIT ,StatutTransaction.ECHEC, "Solde insuffisant");
                throw new SoldeInsuffisantException("Solde insuffisant");

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

        // Je contruit la reponse quant tout c bien passé
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

        //fields  validation
        if(transactionDto.getCompteId() == null || transactionDto.getCompteId() == 0) {
            throw new ValidationException("Le champ CompteId doit être correctement rempli ->" + transactionDto.getCompteId());
        }
        if(transactionDto.getClientId() == null || transactionDto.getClientId() == 0) {
            throw new ValidationException("Le champ ClientId doit être correctement rempli ->" + transactionDto.getClientId());
        }

        //check if customer exits
        Client client = clientRepository.findById(Long.valueOf(transactionDto.getClientId()))
                .orElseThrow(() -> new ClientIntrouvableException("Client Introuvable ->" + transactionDto.getClientId()));


        // Check if account exist
        Compte compteFound = compteRepository.findByCompteIdAndClient(transactionDto.getCompteId(), client)
                .orElseThrow(() -> new CompteIntrouvableException("Compte Introuvable"));

        // On recuperer  les transactions
        List<Transaction> transactions = transactionRepository.findAllByCompte(compteFound);
        if (transactions.isEmpty()) {
            throw  new HistoriqueTransactionException("l'histoque de transaction est vide");
        }

        //Transformation en DTO
        List<ResponseTransactionDto> transactionDtos = transactionTransformer.toDtoList(transactions);

        //Je construis la reponse quand tout c'est bien passé
        response.setStatus("Success");
        response.setMessage("HIstorique récupéreés avec succès.");
        response.setDatas(transactionDtos);

        log.info("----end historique Transaction -----");
        return response;

    }

    /**
     * newTransaction by using Compte, TransactionDto, TypeOperation,  StatutTransaction, message
     * to save all transaction in table "Transaction"
     * @param comptefound
     * @param transactionDto
     * @param type
     * @param status
     * @param message
     * @return
     *
     */
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



