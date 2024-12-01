package kernel.tech.systemgab.rest;


import kernel.tech.systemgab.business.ClientBusiness;
import kernel.tech.systemgab.utils.contract.Response;
import kernel.tech.systemgab.utils.dto.ClientDto;
import kernel.tech.systemgab.utils.dto.ClientResponseDto;
import kernel.tech.systemgab.utils.dto.LoginDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/user")
public class ClientController {

    private final ClientBusiness clientBusinnes;

    public ClientController(ClientBusiness clientBusinnes) {
        this.clientBusinnes = clientBusinnes;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST , consumes =  {"application/json"}, produces = {"application/json"})
    public Response<ClientResponseDto> create(@RequestBody ClientDto clientDto) {
        log.info("Client created : {}", clientDto);
        Response<ClientResponseDto> response = new Response<>();
        response = clientBusinnes.create(clientDto);
        return response;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST , consumes =  {"application/json"}, produces = {"application/json"})
    public Response<ClientDto> login(@RequestBody LoginDto loginDto) {
        log.info("Client login : {}", loginDto);
        Response<ClientDto> response = new Response<>();
        response = clientBusinnes.login(loginDto);
        return response;
    }

}
