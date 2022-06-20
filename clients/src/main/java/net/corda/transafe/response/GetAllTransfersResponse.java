package net.corda.transafe.response;

import lombok.Getter;
import lombok.Setter;
import net.corda.core.contracts.StateAndRef;
import net.corda.transafe.states.TransferState;

import java.util.List;

@Getter
@Setter
public class GetAllTransfersResponse {
    private List<StateAndRef<TransferState>> transferHistory;
    private boolean isSuccess;
    private String error;
}
