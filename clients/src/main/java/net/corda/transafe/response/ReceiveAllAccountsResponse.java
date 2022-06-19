package net.corda.transafe.response;

import com.r3.corda.lib.accounts.contracts.states.AccountInfo;
import lombok.Getter;
import lombok.Setter;
import net.corda.core.contracts.StateAndRef;

import java.util.List;

@Getter
@Setter
public class ReceiveAllAccountsResponse {
    private String error;
    private boolean isSuccess;
    private List<StateAndRef<AccountInfo>> accounts;
}
