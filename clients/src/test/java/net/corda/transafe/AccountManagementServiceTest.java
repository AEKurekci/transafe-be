package net.corda.transafe;

import net.corda.transafe.request.HandShakeRequest;
import net.corda.transafe.response.HandShakeResponse;
import net.corda.transafe.response.ReceiveAllAccountsResponse;
import net.corda.transafe.service.AccountManagementService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.ExecutionException;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccountManagementServiceTest {

    @Mock
    private AccountManagementService accountManagementService;


    @Test
    public void createAccount() throws ExecutionException, InterruptedException {
        //Given
        String accountName = "testUser";

        when(accountManagementService.createAccount(accountName)).thenReturn(accountName);

        //Then
        String result = accountManagementService.createAccount(accountName);
        assertEquals(accountName, result);
    }

    @Test
    public void handShake() throws ExecutionException, InterruptedException {
        //Given
        HandShakeRequest handShakeRequest = new HandShakeRequest();
        handShakeRequest.setFromPerson("testUser");
        handShakeRequest.setTargetNodeName("testNode");

        HandShakeResponse handShakeResponse = new HandShakeResponse();
        handShakeResponse.setMessage("Success");

        when(accountManagementService.handShake(handShakeRequest)).thenReturn(handShakeResponse);
        //Then
        HandShakeResponse result = accountManagementService.handShake(handShakeRequest);
        assertEquals(result.getMessage(),handShakeResponse.getMessage());
    }

    @Test
    public void receiveAllAccounts() throws ExecutionException, InterruptedException {
        //Given
        ReceiveAllAccountsResponse response = new ReceiveAllAccountsResponse();
        response.setSuccess(true);
        response.setError("null");

        when(accountManagementService.receiveAllAccounts()).thenReturn(response);

        //Then
        ReceiveAllAccountsResponse result = accountManagementService.receiveAllAccounts();

        assertEquals(response.getError(),result.getError());

    }
}
