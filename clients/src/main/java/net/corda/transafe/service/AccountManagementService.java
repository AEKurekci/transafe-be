package net.corda.transafe.service;

import com.r3.corda.lib.accounts.contracts.states.AccountInfo;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.corda.core.contracts.StateAndRef;
import net.corda.core.identity.Party;
import net.corda.core.messaging.CordaRPCOps;
import net.corda.transafe.utilities.CreateNewAccount;
import net.corda.transafe.utilities.ShareAccountTo;
import net.corda.transafe.flows.GetAllAccountsFlow;
import net.corda.transafe.request.HandShakeRequest;
import net.corda.transafe.response.HandShakeResponse;
import net.corda.transafe.response.ReceiveAllAccountsResponse;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@Service
@NoArgsConstructor
@Setter
public class AccountManagementService implements IAccountManagementService{

    private CordaRPCOps proxy;

    @Override
    public HandShakeResponse handShake(HandShakeRequest request) throws ExecutionException, InterruptedException {
        HandShakeResponse response = new HandShakeResponse();
        Set<Party> matchingPasties = proxy.partiesFromName(request.getTargetNodeName(),false);
        Iterator<Party> iter = matchingPasties.iterator();
        String result = proxy.startTrackedFlowDynamic(ShareAccountTo.class,request.getFromPerson(),iter.next()).getReturnValue().get();
        response.setMessage(result);
        return response;
    }

    @Override
    public ReceiveAllAccountsResponse receiveAllAccounts() throws ExecutionException, InterruptedException {
        ReceiveAllAccountsResponse response = new ReceiveAllAccountsResponse();
        List<StateAndRef<AccountInfo>> result = proxy.startTrackedFlowDynamic(GetAllAccountsFlow.Initiator.class).getReturnValue().get();
        response.setAccounts(result);
        response.setSuccess(true);
        return response;
    }

    @Override
    public String createAccount(String accountName) throws ExecutionException, InterruptedException {
        return proxy.startTrackedFlowDynamic(CreateNewAccount.class, accountName)
                .getReturnValue()
                .get();
    }

}
