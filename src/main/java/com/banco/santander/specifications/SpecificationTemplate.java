package com.banco.santander.specifications;

import com.banco.santander.entities.Account;
import com.banco.santander.entities.Customer;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;
import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;
import java.util.UUID;


public class SpecificationTemplate {

    @And({
            @Spec(path = "name", spec = LikeIgnoreCase.class),
    })
    public interface CustomerSpec extends Specification<Customer> {}

    @And({
            @Spec(path = "status", spec = LikeIgnoreCase.class),
    })
    public interface AccountSpec extends Specification<Account> {}

    public static Specification<Account> filterAccountByCustomerId(UUID customerId) {
        return (root, query, cb) -> {
            query.distinct(true);
            Root<Customer> customer = query.from(Customer.class);
            Expression<Collection<Account>> customersAccount = customer.get("accounts");
            return cb.and(cb.equal(customer.get("id"), customerId), cb.isMember(root, customersAccount));
        };
    }
}
