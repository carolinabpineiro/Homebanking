package com.Mindhubcohort55.Homebanking.models;
import com.Mindhubcohort55.Homebanking.utils.CardNumberGenerator;
import com.Mindhubcohort55.Homebanking.utils.CvvGenerator;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.*;


@Entity   //es para que Spring cree la tabla en la base de datos
    public class Client {

    @Autowired
    private CvvGenerator cvvGenerator;

    @Autowired
    private CardNumberGenerator cardNumberGenerator;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)   //y aca le pido a la base de datos que genere el id
    private long id;

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private boolean active = true;


    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private Set<Account>accounts = new HashSet<>();

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private List<ClientLoan> clientLoans = new ArrayList<>();

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private Set<Card> clientCards = new HashSet<>();

    public Client() { }

    public Client(String first, String last, String email, String password) {
        this.firstName = first;
        this.lastName = last;
        this.email= email;
        this.password= password;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }


    public Set<Account> getAccounts() { //coleccion de cuentas
        return accounts;
    }

    public void setClientLoans(List<ClientLoan> clientLoans) {
        this.clientLoans = clientLoans;
    }

    public void setClientCards(Set<Card> clientCards) {
        this.clientCards = clientCards;
    }

    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }

    public List<ClientLoan> getClientLoans() {
        return clientLoans;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public Set<Card> getClientCards() {
            return clientCards;
    }

        @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", accounts=" + accounts +
                ", clientLoans=" + clientLoans +
                ", ClientCards=" + clientCards +
                '}';
    }

        public void addAccount(Account account) {
            this.accounts.add(account);
            account.setClient(this);
        }

        public void addClientLoan(ClientLoan clientLoan) {
            this.clientLoans.add(clientLoan);
            clientLoan.setClient(this);
        }

        public void addClientCard(Card card) {
            this.clientCards.add(card);
            card.setNumber(cardNumberGenerator.generateCardNumber());
            card.setClient(this);
            card.setCardHolder(this.firstName + " " + this.lastName);
            card.setCvv(cvvGenerator.generateCvv());
        }



}
