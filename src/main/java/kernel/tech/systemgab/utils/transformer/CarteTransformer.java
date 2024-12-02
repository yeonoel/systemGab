package kernel.tech.systemgab.utils.transformer;


import kernel.tech.systemgab.dao.entity.Carte;
import kernel.tech.systemgab.utils.dto.ClientResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


/**
 * TRANSFORMER for table "Carte"
 *
 * @author yeonoel
 *
 */
@Mapper(componentModel = "spring")
public interface CarteTransformer {

    //@Mapping(source = "numeroCarte",  target = "cardNumero" )
    ClientResponseDto toDto(Carte carte);
}
