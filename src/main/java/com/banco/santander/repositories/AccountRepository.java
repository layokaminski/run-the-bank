package com.banco.santander.repositories;

import com.banco.santander.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID>, JpaSpecificationExecutor<Account> {

    @Query(value = """
    SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END
    FROM account
    WHERE agency = :value
    """, nativeQuery = true)
    Boolean existsByFieldAndValue(final String value);
}
