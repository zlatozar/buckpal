package io.reflectoring.buckpal.account.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

interface AccountRepository extends JpaRepository<AccountJpaEntity, Long> {
}
