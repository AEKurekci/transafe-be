package net.corda.transafe.states;

import lombok.Getter;
import net.corda.core.contracts.BelongsToContract;
import net.corda.core.contracts.LinearState;
import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.AnonymousParty;
import net.corda.transafe.contracts.TransferContract;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Getter
@BelongsToContract(TransferContract.class)
public class TransferState implements LinearState {
    private final String file;
    private final String title;
    private final String senderHost;
    private final AnonymousParty sender;
    private final String senderAccount;
    private final AnonymousParty receiver;
    private final String receiverAccount;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final UniqueIdentifier linearId;
    private final boolean isReceived;

    public TransferState(String file, String title, String senderHost, AnonymousParty sender, String senderAccount,
                         AnonymousParty receiver, String receiverAccount, LocalDateTime startDate, LocalDateTime endDate,
                         UniqueIdentifier linearId, boolean isReceived) {
        this.file = file;
        this.title = title;
        this.senderHost = senderHost;
        this.sender = sender;
        this.senderAccount = senderAccount;
        this.receiver = receiver;
        this.receiverAccount = receiverAccount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.linearId = linearId;
        this.isReceived = isReceived;
    }

    @Override public UniqueIdentifier getLinearId() { return linearId; }
    @Override public List<AbstractParty> getParticipants() {
        return Arrays.asList(sender, receiver);
    }

    public TransferState receiveTransfer(AnonymousParty sender, AnonymousParty receiver){
        return new TransferState(this.file, this.title, senderHost, sender, this.senderAccount, receiver, this.receiverAccount, this.startDate, this.endDate, this.linearId, true);
    }

    @Override
    public String toString() {
        return "TransferState{" +
                "file='" + file + '\'' +
                ", sender=" + sender +
                ", senderAccount='" + senderAccount + '\'' +
                ", receiver=" + receiver +
                ", receiverAccount='" + receiverAccount + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", linearId=" + linearId +
                ", isReceived=" + isReceived +
                '}';
    }
}
