package kernel.tech.systemgab.utils.transformer;


import kernel.tech.systemgab.dao.entity.Client;
import kernel.tech.systemgab.dao.entity.Transaction;
import kernel.tech.systemgab.utils.dto.ClientDto;
import kernel.tech.systemgab.utils.dto.ResponseTransactionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * TRANSFORMER for table "TRansaction"
 *
 * @author yeonoel
 *
 */
@Mapper(componentModel = "spring")
public interface TransactionTransformer {


    @Mappings({
            @Mapping(source = "dateTransaction",  target = "dateTransaction", qualifiedByName = "formatDate"),
            @Mapping(source = "statut", target = "statut"),
            @Mapping(source = "typeOperation", target = "typeOperation"),
            @Mapping(source = "message", target = "message"),
            @Mapping(source = "montant", target = "montant")})
    ResponseTransactionDto toDto(Transaction transaction);

    List<ResponseTransactionDto> toDtoList(List<Transaction> transactionList);


    // convert date format dd/MM/yyyy HH:mm:ss to dd/MM/yyyy
    @Named("formatDate")
    default String formatDate(LocalDateTime dateTransaction) {
        if(dateTransaction == null) return null;
        return dateTransaction.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}
