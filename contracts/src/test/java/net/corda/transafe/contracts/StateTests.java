package net.corda.transafe.contracts;

import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.identity.AnonymousParty;
import net.corda.core.identity.CordaX500Name;
import net.corda.testing.core.TestIdentity;
import net.corda.testing.node.MockServices;
import net.corda.transafe.states.TransferState;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Date;

public class StateTests {
    private final MockServices ledgerServices = new MockServices();

    @Test
    public void hasFieldOfCorrectType() throws NoSuchFieldException {
        // Does the message field exist?
        TransferState.class.getDeclaredField("file");
        TransferState.class.getDeclaredField("sender");
        TransferState.class.getDeclaredField("senderAccount");
        TransferState.class.getDeclaredField("receiver");
        TransferState.class.getDeclaredField("receiverAccount");
        TransferState.class.getDeclaredField("startDate");
        TransferState.class.getDeclaredField("endDate");
        TransferState.class.getDeclaredField("linearId");
        TransferState.class.getDeclaredField("isReceived");

        // Is the message field of the correct type?
        assert(TransferState.class.getDeclaredField("file").getType().equals(String.class));
        assert(TransferState.class.getDeclaredField("sender").getType().equals(AnonymousParty.class));
        assert(TransferState.class.getDeclaredField("senderAccount").getType().equals(String.class));
        assert(TransferState.class.getDeclaredField("receiver").getType().equals(AnonymousParty.class));
        assert(TransferState.class.getDeclaredField("receiverAccount").getType().equals(String.class));
        assert(TransferState.class.getDeclaredField("startDate").getType().equals(LocalDateTime.class));
        assert(TransferState.class.getDeclaredField("endDate").getType().equals(LocalDateTime.class));
        assert(TransferState.class.getDeclaredField("linearId").getType().equals(UniqueIdentifier.class));
        assert(TransferState.class.getDeclaredField("isReceived").getType().equals(boolean.class));

    }

    @Test
    public void receiveTransferTest(){
        TestIdentity Operator1 = new TestIdentity(new CordaX500Name("IsBankasi",  "Istanbul",  "TR"));
        AnonymousParty sender = new AnonymousParty(Operator1.getPublicKey());

        TestIdentity Operator2 = new TestIdentity(new CordaX500Name("IsBankasi",  "Istanbul",  "TR"));
        AnonymousParty receiver = new AnonymousParty(Operator2.getPublicKey());

        UniqueIdentifier Identity1 = new UniqueIdentifier();
        UniqueIdentifier Identity2 = new UniqueIdentifier();

        Date today = new Date();
        Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));

        TransferState transferState = new TransferState("DOSYA","title" ,"senderHost",new AnonymousParty(Operator1.getPublicKey()), Identity1.toString(),
                new AnonymousParty(Operator2.getPublicKey()),Identity2.toString(), LocalDateTime.now(),LocalDateTime.now(),new UniqueIdentifier(),true);

        /*assert transferState.getParticipants().get(0).getOwningKey()
                !=transferState.getParticipants().get(1).getOwningKey();*/
    }
}

