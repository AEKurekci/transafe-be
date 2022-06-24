package net.corda.transafe.utilities;

import co.paralleluniverse.fibers.Suspendable;
import com.r3.corda.lib.accounts.contracts.states.AccountInfo;
import com.r3.corda.lib.accounts.workflows.services.AccountService;
import com.r3.corda.lib.accounts.workflows.services.KeyManagementBackedAccountService;
import net.corda.core.contracts.StateAndRef;
import net.corda.core.flows.FlowException;
import net.corda.core.flows.FlowLogic;
import net.corda.core.flows.StartableByRPC;
import net.corda.core.flows.StartableByService;
import net.corda.core.node.services.vault.QueryCriteria;
import net.corda.transafe.states.TransferState;

import java.util.Collections;
import java.util.List;

@StartableByRPC
@StartableByService
public class MyTransfer extends FlowLogic<List<StateAndRef<TransferState>>>{

    private final String whoAmI;
    public MyTransfer(String whoAmI) {
        this.whoAmI = whoAmI;
    }

    @Override
    @Suspendable
    public List<StateAndRef<TransferState>> call() throws FlowException {
        AccountService accountService = getServiceHub().cordaService(KeyManagementBackedAccountService.class);
        if(accountService.accountInfo(whoAmI).isEmpty()){
            throw new FlowException("Girilen isme ait hesap bulunamamistir. Uygulama yoneticisi ile iletisime geciniz.");
        }
        AccountInfo myAccount = accountService.accountInfo(whoAmI).get(0).getState().getData();
        QueryCriteria.VaultQueryCriteria criteria = new QueryCriteria.VaultQueryCriteria().withExternalIds(Collections.singletonList(myAccount.getIdentifier().getId()));
        return getServiceHub().getVaultService().queryBy(TransferState.class, criteria).getStates();
    }
}