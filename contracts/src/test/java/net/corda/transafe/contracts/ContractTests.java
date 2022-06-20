package net.corda.transafe.contracts;

import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.identity.AnonymousParty;
import net.corda.core.identity.CordaX500Name;
import net.corda.testing.core.TestIdentity;
import net.corda.testing.node.MockServices;
import net.corda.transafe.states.TransferState;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import static net.corda.testing.node.NodeTestUtils.ledger;

public class ContractTests {
    private final MockServices ledgerServices = new MockServices();
    TestIdentity Operator1 = new TestIdentity(new CordaX500Name("IsBankasi",  "Istanbul",  "TR"));
    TestIdentity Operator2 = new TestIdentity(new CordaX500Name("TurkTelekom",  "Ankara",  "TR"));

    @Test
    public void TwoContractCannotHaveSameEmail() {
        UniqueIdentifier Identity1 = new UniqueIdentifier();
        UniqueIdentifier Identity2 = new UniqueIdentifier();
        TransferState transferState = new TransferState("DOSYA","title" ,"senderHost",new AnonymousParty(Operator1.getPublicKey()), Identity1.toString(),
                new AnonymousParty(Operator2.getPublicKey()),Identity2.toString(),LocalDateTime.now(),LocalDateTime.now().plusDays(2),new UniqueIdentifier(),true);

        ledger(ledgerServices, l -> {
            l.transaction(tx -> {
                tx.input(TransferContract.ID, transferState);
                tx.output(TransferContract.ID, transferState);
                tx.command(Operator1.getPublicKey(), new TransferContract.Commands.SendFile());
                return tx.fails(); //fails because of having inputs
            });
            l.transaction(tx -> {
                tx.output(TransferContract.ID, transferState);
                tx.command(Operator1.getPublicKey(), new TransferContract.Commands.SendFile());
                return tx.verifies();
            });
            return null;
        });

    }
}
