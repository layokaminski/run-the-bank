package com.banco.santander.specifications;

import com.banco.santander.entities.Customer;
import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;


public class SpecificationTemplate {

    @And({
            @Spec(path = "name", params = "name", spec = LikeIgnoreCase.class),
    })
    public interface CustomerSpec extends Specification<Customer> {}
}
