package vti.accountservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vti.accountservice.model.Account;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

    Optional<Account> findByUsername(String username);

    Boolean existsAccountByEmail(String email);

    Boolean existsAccountByUsername(String username);

    Boolean existsAccountByEmailAndAccountIdNot(String email, int id);

    @Query(value = "SELECT acc FROM Account acc " +
            "WHERE acc.username LIKE %:search% " +
            "OR acc.fullName LIKE %:search% " +
            "OR acc.email LIKE %:search% "
    )
    Page<Account> findAll(Pageable pageable, String search);

    List<Account> findAllByDepartmentId(int id);
}
