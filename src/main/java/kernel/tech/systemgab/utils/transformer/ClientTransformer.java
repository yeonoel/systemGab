package kernel.tech.systemgab.utils.transformer;


import kernel.tech.systemgab.dao.entity.Client;
import kernel.tech.systemgab.utils.dto.ClientDto;
import kernel.tech.systemgab.utils.dto.ClientResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ClientTransformer {


    ClientResponseDto toDto(Client client);
}
