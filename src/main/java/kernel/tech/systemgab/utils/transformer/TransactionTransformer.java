package kernel.tech.systemgab.utils.transformer;


import kernel.tech.systemgab.dao.entity.Client;
import kernel.tech.systemgab.dao.entity.Transaction;
import kernel.tech.systemgab.utils.dto.ClientDto;
import kernel.tech.systemgab.utils.dto.TransactionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionTransformer {

    TransactionDto toDto(Transaction transaction);

    List<TransactionDto> toDtoList(List<Transaction> transactionList);
}
