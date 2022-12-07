package com.paysafe.buckpal;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import org.junit.jupiter.api.Test;

import com.paysafe.buckpal.archunit.HexagonalArchitecture;
import com.tngtech.archunit.core.importer.ClassFileImporter;

/**
 * Architecture structure test based on our custom ArchUnit rules.
 */
class HexagonalArchRulesTest {

    @Test
    void validateRegistrationContextArchitecture() {
        HexagonalArchitecture.boundedContext("com.paysafe.buckpal.account")

            .withDomainLayer("domain")

            .withAdaptersLayer("adapter")
            .incoming("in.web")
            .outgoing("out.persistence")
            .and()

            .withApplicationLayer("application")
            .services("service")
            .incomingPorts("port.in")
            .outgoingPorts("port.out")
            .and()

            .withConfiguration("configuration")
            .check(new ClassFileImporter()
                .importPackages("com.paysafe.buckpal.."));
    }

    @Test
    void testPackageDependencies() {
        noClasses()
            .that()
            .resideInAPackage("..domain..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..application..")
            .check(new ClassFileImporter()
                .importPackages("com.paysafe.buckpal.."));
    }

}
