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


    // la mrthode pour inscrire un client
    @Transactional
    public Response<ClientResponseDto> create(ClientDto clientDto) {
        Response<ClientResponseDto> response = new Response<>();

        if (clientDto.getNom() == null || clientDto.getPrenom() == null) {
            response.setStatus("Error");
            response.setMessage("le champ nom est vide");
            return response;
        }


        if (clientDto.getPassword() == null || clientDto.getPassword().isEmpty()) {
            response.setStatus("Error");
            response.setMessage("le champ password doit etre renseignés");
            return response;
        }

        try {
            // Création du client
            Client client = new Client();
            client.setNom(clientDto.getNom());
            client.setPrenom(clientDto.getPrenom());
            client.setPassword(clientDto.getPassword());
            client.setCreatedAt(LocalDateTime.now());

            // Sauvegarde du client
            client = clientRepository.save(client);

            // Creation du compte
            Compte compte = new Compte();
            if (TypeCompte.isValidLibelle(clientDto.getTypedeCOmpte())) {
                response.setMessage("le champ type de compte n'est pas correct");
                response.setStatus("Error TypeCompte");
                return response;
            }
            // Sauvegarde du compte Bancaire
            compte.setSolde(BigDecimal.ZERO);
            compte.setClient(client);
            compte.setTypeCompte(TypeCompte.valueOf(clientDto.getTypedeCOmpte()));
            compte = compteRepository.save(compte);

            // Creation de la carte
            Carte carte = new Carte();
            carte.setNumeroCarte(generateCardNumber());
            carte.setPassword(passwordEncoder.encode(clientDto.getPassword()));
            carte.setDateExpiration(LocalDate.now().plusYears(3));  // Date d'expiration dans 3 ans
            carte.setEstActive(true);
            carte.setClient(client);

            Carte Newcarte = carteRepository.save(carte);

            ClientResponseDto clientResponseDto = carteTransformer.toDto(Newcarte);
            clientResponseDto.setNumeroCarte(Newcarte.getNumeroCarte());

            response.setStatus("success");
            response.setMessage("votre compte à été avec success");
            response.setData(clientResponseDto);
            return response;


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    // La methode pour generer une Carte
    private String generateCardNumber() {
        return "1234-5678-9876-" + (int)(Math.random() * 10000);
    }


    public Response<ClientDto> login(LoginDto loginDto) {

        Response<ClientDto> response = new Response<>();
        // Je verifie si la carte est existe
        Optional<Carte> optionalCarte = carteRepository.findByNumeroCarte((loginDto.getCardNumber()));
        if (optionalCarte.isEmpty()) {
            response.setStatus("Error");
            response.setMessage("Numéro de carte introuvable !");
            return response;
        }

        Carte carte = optionalCarte.get();

        // Vérifier le code
        if (!passwordEncoder.matches(loginDto.getPassword(), carte.getPassword())) {
            response.setStatus("Error");
            response.setMessage(" Code Pin incorrect !");
            return response;
        }

        // Générer le JWT
        String token = jwtUtil.generateToken(carte.getNumeroCarte());

        response.setStatus("Success");
        response.setToken(token);
        response.setMessage(" Connection reussie !");
        return response;
    }
}



