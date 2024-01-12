package com.banco.santander.mapper;

import com.banco.santander.dtos.client.CustomerDTO;
import com.banco.santander.dtos.client.CustomerPatchDTO;
import com.banco.santander.entities.Customer;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CustomerDTO customerToCustomerDTO(Customer customer);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void customerPatchDTOFromCustomer(CustomerPatchDTO dto, @MappingTarget Customer entity);
}
