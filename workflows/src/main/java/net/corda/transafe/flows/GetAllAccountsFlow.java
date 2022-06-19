package net.corda.transafe.flows;

import co.paralleluniverse.fibers.Suspendable;
import com.r3.corda.lib.accounts.contracts.states.AccountInfo;
import com.r3.corda.lib.accounts.workflows.services.AccountService;
import com.r3.corda.lib.accounts.workflows.services.KeyManagementBackedAccountService;
import net.corda.core.contracts.StateAndRef;
import net.corda.core.flows.FlowException;
import net.corda.core.flows.FlowLogic;
import net.corda.core.flows.InitiatingFlow;
import net.corda.core.flows.StartableByRPC;

import java.util.List;

public class GetAllAccountsFlow {
    @InitiatingFlow
    @StartableByRPC
    public static class Initiator extends FlowLogic<List<StateAndRef<AccountInfo>>> {

        @Suspendable
        @Override
        public List<StateAndRef<AccountInfo>> call() throws FlowException {

            AccountService accountService = getServiceHub().cordaService(KeyManagementBackedAccountService.class);

            return accountService.allAccounts();
        }
    }

}
