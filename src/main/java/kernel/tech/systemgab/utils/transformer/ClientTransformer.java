package kernel.tech.systemgab.utils.transformer;


import kernel.tech.systemgab.dao.entity.Client;
import kernel.tech.systemgab.utils.dto.ClientDto;
import kernel.tech.systemgab.utils.dto.ClientResponseDto;
import org.mapstruct.Mapper;


/**
 * TRANSFORMER for table "Client"
 *
 * @author yeonoel
 *
 */
@Mapper(componentModel = "spring")
public interface ClientTransformer {
    ClientResponseDto toDto(Client client);
}
