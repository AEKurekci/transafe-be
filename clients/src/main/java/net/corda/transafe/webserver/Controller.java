package net.corda.transafe.webserver;


import com.fasterxml.jackson.databind.ObjectMapper;
import net.corda.client.jackson.JacksonSupport;
import net.corda.core.contracts.StateAndRef;
import net.corda.core.identity.CordaX500Name;
import net.corda.core.messaging.CordaRPCOps;
import net.corda.transafe.utilities.MyTransfer;
import net.corda.transafe.flows.GetAllTransactionsFlow;
import net.corda.transafe.request.*;
import net.corda.transafe.response.*;
import net.corda.transafe.service.AccountManagementService;
import net.corda.transafe.service.DocumentTransferService;
import net.corda.transafe.service.IAccountManagementService;
import net.corda.transafe.service.IDocumentTransferService;
import net.corda.transafe.states.TransferState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Define your API endpoints here.
 */
@RestController
@RequestMapping("/") // The paths for HTTP requests are relative to this base path.
public class Controller {
    private static final Logger logger = LoggerFactory.getLogger(Controller.class);
    private final CordaRPCOps proxy;
    private final CordaX500Name me;
    private final IDocumentTransferService documentTransferService;
    private final IAccountManagementService accountManagementService;

    public Controller(NodeRPCConnection rpc, DocumentTransferService documentTransferService, AccountManagementService accountManagementService) {
        this.proxy = rpc.proxy;
        this.me = proxy.nodeInfo().getLegalIdentities().get(0).getName();
        documentTransferService.setProxy(this.proxy);
        this.documentTransferService = documentTransferService;
        accountManagementService.setProxy(this.proxy);
        this.accountManagementService = accountManagementService;
    }

    @Configuration
    class Plugin {
        @Bean
        public ObjectMapper registerModule() {
            return JacksonSupport.createNonRpcMapper();
        }
    }

    @CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
    @GetMapping(value = "/me",produces = APPLICATION_JSON_VALUE)
    public Map<String, String> whoAmI(){
        HashMap<String, String> myMap = new HashMap<>();
        myMap.put("me", me.toString());
        return myMap;
    }

    @CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
    @PostMapping(value =  "createAccount/{acctName}")
    public ResponseEntity<String> createAccount(@PathVariable String acctName){
        try{
            logger.info("Calling createAccount operation");
            String response = accountManagementService.createAccount(acctName);
            logger.info("Called createAccount operation");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        }catch (Exception e) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(e.getMessage());
        }
    }

    @CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
    @PostMapping(value = "handShake", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<HandShakeResponse> handShake(@RequestBody HandShakeRequest request){
        HandShakeResponse result = null;
        try{
            logger.info("Calling handShake operation");
            result = accountManagementService.handShake(request);
            logger.info("Called handShake operation");
            return ResponseEntity.status(HttpStatus.OK).body(result);

        }catch (Exception e) {
            if(result == null){
                result = new HandShakeResponse();
            }
            result.setMessage(e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(result);
        }
    }

    @CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
    @PostMapping(value = "sendFile", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<DocumentTransferResponse> sendFile(@RequestBody DocumentTransferRequest request){
        try{
            logger.info("Calling sendFile operation");
            DocumentTransferResponse responseBody = documentTransferService.sendFile(request, me);
            logger.info("Called sendFile operation");
            return ResponseEntity.ok(responseBody);
        }catch (Exception e){
            DocumentTransferResponse response = new DocumentTransferResponse();
            response.setSuccess(false);
            response.setError(e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }
    }

    @CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
    @GetMapping(value = "getTransfers",produces = APPLICATION_JSON_VALUE)
    public List<StateAndRef<TransferState>> getIOUs() {
        // Filter by state type: IOU.
        return proxy.vaultQuery(TransferState.class).getStates();
    }

    @CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
    @PostMapping(value = "getHistoricDataByLinearId",produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<GetAllTransfersResponse> getHistoricDataByLinearId(@RequestBody GetAllTransfersRequest request) {
        GetAllTransfersResponse response = new GetAllTransfersResponse();
        try{
            logger.info("Calling GetAllTransactionsFlow flow");
            List<StateAndRef<TransferState>> auditTrail = proxy.startFlowDynamic(GetAllTransactionsFlow.Initiator.class,
                    request.getLinearId()).getReturnValue().get();
            response.setTransferHistory(auditTrail);
            response.setSuccess(true);
            logger.info("Called GetAllTransactionsFlow flow");
            return ResponseEntity.ok(response);
        }catch (Exception e){
            response.setSuccess(false);
            response.setError(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
    @PostMapping(value = "getMyTransfers", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<GetMyTransfersResponse> getMyTransfer(@RequestBody GetMyTransfersRequest request){
        GetMyTransfersResponse response = new GetMyTransfersResponse();

        try{
            logger.info("Calling MyTransfer flow");
            List<StateAndRef<TransferState>> myTransferStates = proxy.startTrackedFlowDynamic(MyTransfer.class, request.getAccountName()).getReturnValue().get();
            response.setMyTransfers(myTransferStates);
            logger.info("Called MyTransfer flow");
            return ResponseEntity.ok(response);
        }catch (Exception e){
            response.setError(e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }
    }

    @CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
    @PostMapping(value = "receiveFile", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ReceiveFileResponse> receiveFile(@RequestBody ReceiveFileRequest request){
        try{
            logger.info("Calling receiveFile operation");
            ReceiveFileResponse response = documentTransferService.receiveFile(request);
            logger.info("Called receiveFile operation");
            return ResponseEntity.ok(response);
        }catch (Exception e){
            ReceiveFileResponse response = new ReceiveFileResponse();
            response.setError(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
    @GetMapping(value = "receiveAllAccounts")
    public ResponseEntity<ReceiveAllAccountsResponse> receiveAllAccounts(){
        try{
            logger.info("Calling receiveAllAccounts operation");
            return ResponseEntity.ok(accountManagementService.receiveAllAccounts());
        } catch (Exception e) {
            ReceiveAllAccountsResponse response = new ReceiveAllAccountsResponse();
            response.setError(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}