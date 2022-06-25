package net.corda.transafe;


import net.corda.core.identity.CordaX500Name;
import net.corda.transafe.request.DocumentTransferRequest;
import net.corda.transafe.request.ReceiveFileRequest;
import net.corda.transafe.response.DocumentTransferResponse;
import net.corda.transafe.response.ReceiveFileResponse;
import net.corda.transafe.service.DocumentTransferService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DocumentTransferServiceTest {

    @Mock
    private DocumentTransferService documentTransferService;

    @Test
    public void sendFile() throws ExecutionException, InterruptedException {
        //Given
        DocumentTransferRequest documentTransferRequest = new DocumentTransferRequest();
        documentTransferRequest.setFile("exampleFile");
        documentTransferRequest.setTitle("title");
        documentTransferRequest.setReceiver("exampleReceiver");
        documentTransferRequest.setSender("exampleSender");
        CordaX500Name cordaX500Name = new CordaX500Name("example example", "123", "ZZ");
        DocumentTransferResponse documentTransferResponse = new DocumentTransferResponse();
        documentTransferResponse.setTransactionId("11111");
        documentTransferResponse.setSuccess(true);

        when(documentTransferService.sendFile(documentTransferRequest, cordaX500Name)).thenReturn(documentTransferResponse);

        //Then
        DocumentTransferResponse result = documentTransferService.sendFile(documentTransferRequest, cordaX500Name);
        assertNotNull(result.getTransactionId());

    }


    public void receiveFile() throws ExecutionException, InterruptedException {
        //Given
        ReceiveFileRequest request = new ReceiveFileRequest();
        request.setReceiver("exampleReceiver");
        request.setSender("exampleSender");
        request.setLinearId("11111");
        request.setSenderHost("exampleHost");
        ReceiveFileResponse response = new ReceiveFileResponse();
        response.setFile("exampleFile");
        response.setTitle("exampleTitle");
        response.setTransactionId("111111");

        when(documentTransferService.receiveFile(request)).thenReturn(response);
        //Then
        ReceiveFileResponse result = documentTransferService.receiveFile(request);
        assertNotNull(result);

    }

}
