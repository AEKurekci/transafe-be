package net.corda.transafe.service;

import net.corda.core.identity.CordaX500Name;
import net.corda.transafe.request.DocumentTransferRequest;
import net.corda.transafe.request.ReceiveFileRequest;
import net.corda.transafe.response.DocumentTransferResponse;
import net.corda.transafe.response.ReceiveFileResponse;

import java.util.concurrent.ExecutionException;

public interface IDocumentTransferService {

    DocumentTransferResponse sendFile(DocumentTransferRequest request, CordaX500Name fromWho) throws ExecutionException, InterruptedException;

    ReceiveFileResponse receiveFile(ReceiveFileRequest request) throws ExecutionException, InterruptedException;
}
