package kernel.tech.systemgab.business;

import jakarta.transaction.Transactional;
import kernel.tech.systemgab.dao.entity.Carte;
import kernel.tech.systemgab.dao.entity.Client;
import kernel.tech.systemgab.dao.entity.Compte;
import kernel.tech.systemgab.dao.repository.CarteRepository;
import kernel.tech.systemgab.dao.repository.ClientRepository;
import kernel.tech.systemgab.dao.repository.CompteRepository;
import kernel.tech.systemgab.springsecurity.JWTUtil;
import kernel.tech.systemgab.utils.contract.Response;
import kernel.tech.systemgab.utils.dto.ClientDto;
import kernel.tech.systemgab.utils.dto.ClientResponseDto;
import kernel.tech.systemgab.utils.dto.LoginDto;
import kernel.tech.systemgab.utils.dto.LoginResponseDto;
import kernel.tech.systemgab.utils.enums.TypeCompte;
import kernel.tech.systemgab.utils.transformer.CarteTransformer;
import kernel.tech.systemgab.utils.transformer.ClientTransformer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;


/**
 BUSINESS for table  "Client"
 *
 * @author yeonoel
 *
 */
@Slf4j
@Service
public class ClientBusiness {

    private final ClientRepository clientRepository;
    private final CompteRepository compteRepository;
    private final CarteRepository carteRepository;
    private final ClientTransformer clientTransformer;
    private final CarteTransformer carteTransformer;
    private PasswordEncoder passwordEncoder;
    private JWTUtil jwtUtil;

    public ClientBusiness(ClientRepository clientRepository,
                          CompteRepository compteRepository,
                          CarteRepository carteRepository,
                          ClientTransformer clientTransformer,
                          CarteTransformer carteTransformer,
                          PasswordEncoder passwordEncoder,
                          JWTUtil jwtUtil
) {
        this.clientRepository = clientRepository;
        this.compteRepository = compteRepository;
        this.carteRepository = carteRepository;
        this.clientTransformer = clientTransformer;
        this.carteTransformer = carteTransformer;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }


    /**
     * create Client by using ClientDto as object.
     *
     * @param clientDto
     * @return ClientResponseDto
     *
     */
    @Transactional
    public Response<ClientResponseDto> create(ClientDto clientDto) {
        log.info("----begin create Client-----");
        Response<ClientResponseDto> response = new Response<>();

        if (clientDto.getNom() == null || clientDto.getNom().isEmpty()) {
            response.setStatus("Error");
            response.setMessage("le champ  Nom  doit être correctement rempli" + clientDto.getNom());
            return response;
        }

        if (clientDto.getPrenom() == null || clientDto.getPrenom().isEmpty()) {
            response.setStatus("Error");
            response.setMessage("le champ  Prenom  doit être correctement rempli" + clientDto.getPrenom());
            return response;
        }


        if (clientDto.getPassword() == null || clientDto.getPassword().isEmpty()) {
            response.setStatus("Error");
            response.setMessage("le champ  Password  doit être correctement rempli");
            return response;
        }

        try {
            // Create client
            Client client = new Client();
            client.setNom(clientDto.getNom());
            client.setPrenom(clientDto.getPrenom());
            client.setPassword(clientDto.getPassword());
            client.setCreatedAt(LocalDateTime.now());

            client = clientRepository.save(client);

            // start Create customer account
            Compte compte = new Compte();
            if (TypeCompte.isValidLibelle(clientDto.getTypedeCOmpte())) {
                response.setMessage("le champ type de compte n'est pas correct" + clientDto.getTypedeCOmpte());
                response.setStatus("Error TypeCompte");
                return response;
            }
            compte.setSolde(BigDecimal.ZERO);
            compte.setClient(client);
            compte.setTypeCompte(TypeCompte.valueOf(clientDto.getTypedeCOmpte()));
            compte = compteRepository.save(compte);
            // end  Create customer account

            // Create customer card
            Carte carte = new Carte();
            carte.setNumeroCarte(generateCardNumber());
            carte.setPassword(passwordEncoder.encode(clientDto.getPassword()));
            // Card Expiry date in 3 years
            carte.setDateExpiration(LocalDate.now().plusYears(3));
            carte.setEstActive(true);
            carte.setClient(client);
            Carte Newcarte = carteRepository.save(carte);
            // end create customer card

            ClientResponseDto clientResponseDto = carteTransformer.toDto(Newcarte);
            clientResponseDto.setNumeroCarte(Newcarte.getNumeroCarte());

            response.setStatus("Success");
            response.setMessage("votre compte à été créé avec success");
            response.setData(clientResponseDto);

            log.info("----end create Client -----");
            return response;


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    /**
     * login Client by using LoginDto as object.
     *
     * @param loginDto
     * @return LoginResponseDto
     *
     */
    public Response<LoginResponseDto> login(LoginDto loginDto) {
        log.info("----begin login  Client -----");

        Response<LoginResponseDto> response = new Response<>();
        // I check if the card exists
        Optional<Carte> optionalCarte = carteRepository.findByNumeroCarte((loginDto.getCardNumber()));
        if (optionalCarte.isEmpty()) {
            response.setStatus("Error");
            response.setMessage("Numéro de carte introuvable !" + loginDto.getCardNumber());
            return response;
        }

        Carte carte = optionalCarte.get();

        // check if password matches
        if (!passwordEncoder.matches(loginDto.getPassword(), carte.getPassword())) {
            response.setStatus("Error");
            response.setMessage("Code Pin incorrect");
            return response;
        }

        // find account by customer
        Compte compte = compteRepository.findByClient(optionalCarte.get().getClient());


        LoginResponseDto loginResponseDto = new LoginResponseDto();
        loginResponseDto.setCardId(optionalCarte.get().getCarteId());
        loginResponseDto.setClientId(optionalCarte.get().getClient().getClientId());
        loginResponseDto.setSolde(compte.getSolde());
        loginResponseDto.setPrenom(compte.getClient().getPrenom());
        loginResponseDto.setNom(compte.getClient().getNom());
        loginResponseDto.setTypeCompte(compte.getTypeCompte());

        //generate JWT
        String token = jwtUtil.generateToken(carte.getNumeroCarte());

        response.setStatus("Success");
        response.setToken(token);
        response.setMessage("Connection reussie");
        response.setData(loginResponseDto);

        log.info("----end login Client -----");
        return response;
    }

    //simulate card number generation
    private String generateCardNumber() {
        return "1234-5678-9876-" + (int)(Math.random() * 10000);
    }

}



