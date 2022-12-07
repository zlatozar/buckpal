package io.reflectoring.buckpal;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import org.junit.jupiter.api.Test;

import com.tngtech.archunit.core.importer.ClassFileImporter;

import io.reflectoring.buckpal.archunit.HexagonalArchitecture;

/**
 * Architecture structure test based on our custom ArchUnit rules.
 */
class DependencyRuleTests {

    @Test
    void validateRegistrationContextArchitecture() {
        HexagonalArchitecture.boundedContext("io.reflectoring.buckpal.account")

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
                .importPackages("io.reflectoring.buckpal.."));
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
                .importPackages("io.reflectoring.buckpal.."));
    }

}
