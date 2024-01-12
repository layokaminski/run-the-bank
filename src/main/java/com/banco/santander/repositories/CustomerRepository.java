package com.banco.santander.repositories;

import com.banco.santander.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID>, JpaSpecificationExecutor<Customer> {

    @Query(value = """
    SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END
    FROM customer
    WHERE document = :value
    """, nativeQuery = true)
    Boolean existsByFieldAndValue(final String value);

    @Query(value = """
    SELECT *
    FROM customer
    WHERE document = :document
    """, nativeQuery = true)
    Optional<Customer> findByDocument(String document);
}
