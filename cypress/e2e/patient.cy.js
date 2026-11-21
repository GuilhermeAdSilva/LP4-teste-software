describe('Functional tests - Create Patient', () => {
    it('should not allow creating a patient without a name', function() {
        cy.visit('http://localhost:8081')
        
        cy.get('#nav button[data-key="pacientes"]').click();
        cy.get('[name="email"]').click();
        cy.get('[name="email"]').type('email@email');
        cy.get('[name="password"]').click();
        cy.get('[name="password"]').type('12345678');
        cy.get('[name="birthDate"]').click();
        cy.get('#form button.primary').click();
        cy.get('#result span.chip').click();
        cy.get('#result span.chip').should('have.text', '400 Bad Request');
        cy.get('#result pre:nth-child(5)').click();
    });

    it('should not allow creating a patient without an email address', function() {
        cy.visit('http://localhost:8081')
        
        cy.get('#nav button[data-key="pacientes"]').click();
        cy.get('[name="name"]').click();
        cy.get('[name="name"]').type('teste');
        cy.get('[name="password"]').click();
        cy.get('[name="password"]').type('12345678');
        cy.get('[name="birthDate"]').click();
        cy.get('#form button.primary').click();
        cy.get('#result span.chip').click();
        cy.get('#result span.chip').should('have.text', '400 Bad Request');
        cy.get('#result pre:nth-child(5)').click();
    });

    it('should not allow creating a patient without a password', function() {
        cy.visit('http://localhost:8081')
        
        cy.get('#nav button[data-key="pacientes"]').click();
        cy.get('[name="name"]').click();
        cy.get('[name="name"]').type('teste');
        cy.get('[name="email"]').click();
        cy.get('[name="email"]').type('teste@teste');
        cy.get('[name="birthDate"]').click();
        cy.get('#form button.primary').click();
        cy.get('#result span.chip').click();
        cy.get('#result span.chip').should('have.text', '400 Bad Request');
        cy.get('#result pre:nth-child(5)').click();
    });

    it('should not allow the creation of a patient without a date of birth', function() {
        cy.visit('http://localhost:8081')
        
        cy.get('#nav button[data-key="pacientes"]').click();
        cy.get('[name="name"]').click();
        cy.get('[name="name"]').type('teste');
        cy.get('[name="email"]').click();
        cy.get('[name="email"]').type('teste@teste');
        cy.get('[name="password"]').click();
        cy.get('[name="password"]').type('12345678');
        cy.get('#form button.primary').click();
        cy.get('#result span.chip').click();
        cy.get('#result span.chip').should('have.text', '400 Bad Request');
    });

    it('must create patient', function() {
        cy.visit('http://localhost:8081')
        
        cy.get('#nav button[data-key="pacientes"]').click();
        cy.get('[name="name"]').click();
        cy.get('[name="name"]').type('paciente cypress');
        cy.get('[name="email"]').click();
        cy.get('[name="email"]').type('email@ok');
        cy.get('[name="password"]').click();
        cy.get('[name="password"]').type('12345678');
        cy.get('[name="birthDate"]').click();
        cy.get('[name="birthDate"]').type('1999-09-07');
        cy.get('#form button.primary').click();
        cy.get('#result span.ok').click();
        cy.get('#result span.ok').should('have.text', '201 Created');
    });
});