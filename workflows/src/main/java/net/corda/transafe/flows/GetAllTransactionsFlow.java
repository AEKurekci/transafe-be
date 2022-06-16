package net.corda.transafe.flows;

import net.corda.core.contracts.StateAndRef;
import net.corda.core.crypto.SecureHash;
import net.corda.core.flows.*;
import net.corda.core.node.StatesToRecord;
import net.corda.core.node.services.Vault;
import net.corda.core.node.services.vault.QueryCriteria;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.utilities.ProgressTracker;
import net.corda.transafe.states.TransferState;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class GetAllTransactionsFlow {

    @InitiatingFlow
    @StartableByRPC
    public static class Initiator extends FlowLogic<List<StateAndRef<TransferState>>>{

        private final String linearId;

        private final ProgressTracker.Step CREATING_CRITERIA = new ProgressTracker.Step("Creating a new criteria.");
        private final ProgressTracker.Step APPLYING_CRITERIA = new ProgressTracker.Step("Applying the created criteria to obtain all transactions.");
        private final ProgressTracker.Step FINALISING_TRANSACTION = new ProgressTracker.Step("Obtaining notary signature and recording transaction.") {
            @Override
            public ProgressTracker childProgressTracker() {
                return FinalityFlow.Companion.tracker();
            }
        };

        // The progress tracker checkpoints each stage of the flow and outputs the specified messages when each
        // checkpoint is reached in the code. See the 'progressTracker.currentStep' expressions within the call()
        // function.
        private final ProgressTracker progressTracker = new ProgressTracker(
                CREATING_CRITERIA,
                APPLYING_CRITERIA,
                FINALISING_TRANSACTION
        );

        public Initiator(String linearId) {
            this.linearId = linearId;
        }

        @Override
        public ProgressTracker getProgressTracker() {
            return progressTracker;
        }

        @Override
        public List<StateAndRef<TransferState>> call() throws FlowException {

            progressTracker.setCurrentStep(CREATING_CRITERIA);
            // Criteria to get a certain LinearState by its linearId.
            UUID id = UUID.fromString(linearId);

            QueryCriteria.LinearStateQueryCriteria linearStateCriteria = new QueryCriteria.LinearStateQueryCriteria()
                    .withUuid(Collections.singletonList(id)).withStatus(Vault.StateStatus.ALL);
            System.out.println(linearStateCriteria);

            progressTracker.setCurrentStep(APPLYING_CRITERIA);
            // By default Corda returns the results in pages of 200 records,
            // you can modify that default size; and you must loop through the pages
            // to get all of the results.
            Vault.Page<TransferState> results = getServiceHub().getVaultService()
                    .queryBy(TransferState.class, linearStateCriteria);

            results.getStates().forEach(state -> System.out.println(state.getState().getData()));
            // This is a simplified version where I get the first record in the result set;
            // again, you must loop through all the pages if you have more than 200 records
            // (it's highly unlikely that your state was updated more than 200 times though).
            SecureHash txHash = results.getStates().get(0).getRef().getTxhash();
            System.out.println("txHash: " + txHash);

            //todo gerekli imzalar toplanmalı
            progressTracker.setCurrentStep(FINALISING_TRANSACTION);
            // Now you have the transaction that created your state
            // (i.e. the transaction that consumed a previous version of your state
            //  and created the new version of your state).
            SignedTransaction tx = getServiceHub().getValidatedTransactions()
                    .getTransaction(txHash);
            subFlow(new FinalityFlow(tx, Collections.emptyList(), StatesToRecord.ALL_VISIBLE));
            return results.getStates();
        }
    }
}
