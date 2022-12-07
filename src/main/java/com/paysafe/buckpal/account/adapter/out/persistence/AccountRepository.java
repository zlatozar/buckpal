package com.paysafe.buckpal.account.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Modify DB interface.
 */
interface AccountRepository extends JpaRepository<AccountJpaEntity, Long> {
}
