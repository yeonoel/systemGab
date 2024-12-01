package kernel.tech.systemgab.business;

import kernel.tech.systemgab.dao.entity.Client;
import kernel.tech.systemgab.dao.entity.Compte;
import kernel.tech.systemgab.dao.entity.Transaction;
import kernel.tech.systemgab.dao.repository.ClientRepository;
import kernel.tech.systemgab.dao.repository.CompteRepository;
import kernel.tech.systemgab.dao.repository.TransactionRepository;
import kernel.tech.systemgab.utils.contract.Response;
import kernel.tech.systemgab.utils.dto.CompteDto;
import kernel.tech.systemgab.utils.dto.TransactionDto;
import kernel.tech.systemgab.utils.enums.StatutTransaction;
import kernel.tech.systemgab.utils.enums.TypeOperation;
import kernel.tech.systemgab.utils.transformer.TransactionTransformer;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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


    public Response<CompteDto> retrait(TransactionDto transactionDto) {
        Response<CompteDto> response = new Response<>();

        if(transactionDto.getCompteId() == null) {
            response.setStatus("Error");
            response.setMessage("L'identifiant du Compte est vide" + transactionDto.getCompteId());
            return response;
        }
        if(transactionDto.getClientId() == null) {
            response.setStatus("Error");
            response.setMessage("L'identifiant du client est obligatoire" + transactionDto.getClientId());
            return response;
        }

        Optional<Client> client = clientRepository.findById(Long.valueOf(transactionDto.getClientId()));
        if (!client.isPresent()) {
            response.setStatus("Error");
            response.setMessage("Client  introuvable");
            return response;

        }

        // Rechercher le compte
        Optional<Compte> compte = compteRepository.findByCompteIdAndClient(transactionDto.getCompteId(), client.get());
        if (!compte.isPresent()) {
            response.setStatus("Error");
            response.setMessage("Compte introuvable");
            return response;

        }
        Compte comptefound = compte.get();

        // On verie le solde suffisant
        if (comptefound.getSolde().compareTo(transactionDto.getMontant()) < 0) {
            response.setStatus("error");
            response.setMessage("Solde insuffisant");

            Transaction transaction = new Transaction();
            transaction.setCompte(comptefound);
            transaction.setTypeOperation(TypeOperation.RETRAIT);
            transaction.setMontant(transactionDto.getMontant());
            transaction.setDateTransaction(LocalDateTime.now());
            transaction.setStatut(StatutTransaction.ECHEC);
            transaction.setMessageErreur("Solde insuffisant");

            transactionRepository.save(transaction);
            return response;
        }

        // Déduire le montant du solde
        comptefound.setSolde(comptefound.getSolde().subtract(transactionDto.getMontant()));
        Compte compteSave =  compteRepository.save(comptefound);

        // ON enregistre  la transaction
        Transaction transaction = new Transaction();
        transaction.setCompte(comptefound);
        transaction.setTypeOperation(TypeOperation.RETRAIT);
        transaction.setMontant(transactionDto.getMontant());
        transaction.setDateTransaction(LocalDateTime.now());
        transaction.setStatut(StatutTransaction.SUCCES);
        transactionRepository.save(transaction);

        CompteDto compteDto = new CompteDto();
        compteDto.setNouveauSolde(compteSave.getSolde());

        response.setStatus("success");
        response.setMessage("Retrait effectué avec succès");
        response.setData(compteDto);
        return response;
    }

    public Response<CompteDto> depot(TransactionDto transactionDto) {
        Response<CompteDto> response = new Response<>();

        Optional<Compte> compte = compteRepository.findById(transactionDto.getCompteId());
        if (!compte.isPresent()) {
            response.setStatus("Error");
            response.setMessage("Compte introuvable");
            return response;

        }
        Compte comptefound = compte.get();

        // On ajoute le montant au solde
        comptefound.setSolde(comptefound.getSolde().add(transactionDto.getMontant()));
        Compte compteSave =  compteRepository.save(comptefound);

        // Enregistrement de la transaction la transaction
        Transaction transaction = new Transaction();
        transaction.setCompte(comptefound);
        transaction.setTypeOperation(TypeOperation.DEPOT);
        transaction.setMontant(transactionDto.getMontant());
        transaction.setDateTransaction(LocalDateTime.now());
        transaction.setStatut(StatutTransaction.SUCCES);
        transactionRepository.save(transaction);

        CompteDto compteDto = new CompteDto();
        compteDto.setNouveauSolde(compteSave.getSolde());

        response.setStatus("success");
        response.setMessage("Dépôt effectué avec succès");
        response.setData(compteDto);
        return response;
    }

    public Response<TransactionDto> historique(TransactionDto transactionDto) {
        Response<TransactionDto> response = new Response<>();

        if (transactionDto.getCompteId() == null) {
            response.setStatus("Error");
            response.setMessage("L'identifiant du Compte est obligatoire.");
            return response;
        }
        if (transactionDto.getClientId() == null) {
            response.setStatus("Error");
            response.setMessage("L'identifiant du Client est obligatoire.");
            return response;
        }

        // ON verifie que existe client
        Optional<Client> client = clientRepository.findById(Long.valueOf(transactionDto.getClientId()));
        if (client.isEmpty()) {
            response.setStatus("Error");
            response.setMessage("Client introuvable avec l'ID " + transactionDto.getClientId());
            return response;
        }

        // On verifie si le compte existe si oui on le recupere sinon on retourne l'erreur
        Optional<Compte> compte = compteRepository.findByCompteIdAndClient(transactionDto.getCompteId(), client.get());
        if (compte.isEmpty()) {
            response.setStatus("Error");
            response.setMessage("Compte introuvable pour le Client.");
            return response;
        }

        // On recuperer  les transactions
        List<Transaction> transactions = transactionRepository.findAllByCompte(compte.get());
        if (transactions.isEmpty()) {
            response.setStatus("Error");
            response.setMessage("Aucune transaction trouvée pour ce compte.");
            return response;
        }

        // Transformation en DTO
        List<TransactionDto> transactionDtos = transactionTransformer.toDtoList(transactions);

        // Construction de la réponse
        response.setStatus("Success");
        response.setMessage("Transactions récupérées avec succès.");
        response.setDatas(transactionDtos);

        return response;

    }
}



