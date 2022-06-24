package net.corda.transafe;

import net.corda.core.identity.CordaX500Name;
import net.corda.core.identity.Party;
import net.corda.core.messaging.CordaRPCOps;
import net.corda.core.node.NodeInfo;
import net.corda.testing.core.TestIdentity;
import net.corda.transafe.service.AccountManagementService;
import net.corda.transafe.service.DocumentTransferService;
import net.corda.transafe.webserver.Controller;
import net.corda.transafe.webserver.NodeRPCConnection;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
public class ControllerTest {
    @Mock
    private MockMvc mockMvc;
    @Mock
    private DocumentTransferService documentTransferService;
    @Mock
    private AccountManagementService accountManagementService;
    @Mock
    private NodeInfo nodeInfo;
    @Mock
    private Party party;
    @Before
    public void setUp(){
        CordaRPCOps proxy = Mockito.mock(CordaRPCOps.class);
        NodeInfo nodeInfo = Mockito.mock(NodeInfo.class);
        CordaX500Name name = new CordaX500Name("TurkTelekom",  "Ankara",  "TR");
        TestIdentity op = new TestIdentity(name);
        //X509Certificate cert = Mockito.mock(X509Certificate.class);
        Party party = op.getParty();
        /*List<NetworkHostAndPort> NHAPL = new ArrayList<NetworkHostAndPort>();
        NHAPL.add(12,new NetworkHostAndPort("8080",12));
        NHAPL.add(12,new NetworkHostAndPort("8080",13));*/
        List<Party> parties = new ArrayList<>();
        parties.add(party);

        //List<PartyAndCertificate> partyList = new ArrayList<PartyAndCertificate>();
        //partyList.add(12,new PartyAndCertificate(certPath()));
        NodeRPCConnection rpc = new NodeRPCConnection();
        rpc.setHost("IsBank");
        rpc.setUsername("Is");
        rpc.setPassword("123");
        rpc.setRpcPort(8080);
        rpc.setProxy(proxy);

        //NodeInfo nodeInfo = new NodeInfo(NHAPL, partyList,12,12);
        when(proxy.nodeInfo()).thenReturn(nodeInfo);
        when(proxy.nodeInfo().getLegalIdentities()).thenReturn(parties);
        when(proxy.nodeInfo().getLegalIdentities().get(0).getName()).thenReturn(name);
        Controller controller = new Controller(rpc, documentTransferService, accountManagementService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void whoami() throws Exception {
        //TestIdentity Operator2 = new TestIdentity(new CordaX500Name("TurkTelekom",  "Ankara",  "TR"));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/me","application/json")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    @Test
    public void createAccount() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/createAccount/IsBank", "2ewq")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

/*
    X509Certificate generateCertificate(String dn, KeyPair pair, int days, String algorithm)
            throws GeneralSecurityException, IOException
    {
        PrivateKey privkey = pair.getPrivate();
        X509CertInfo info = new X509CertInfo();
        Date from = new Date();
        Date to = new Date(from.getTime() + days * 86400000l);
        CertificateValidity interval = new CertificateValidity(from, to);
        BigInteger sn = new BigInteger(64, new SecureRandom());
        X500Name owner = new X500Name(dn);

        info.set(X509CertInfo.VALIDITY, interval);
        info.set(X509CertInfo.SERIAL_NUMBER, new CertificateSerialNumber(sn));
        info.set(X509CertInfo.SUBJECT, new CertificateSubjectName(owner));
        info.set(X509CertInfo.ISSUER, new CertificateIssuerName(owner));
        info.set(X509CertInfo.KEY, new CertificateX509Key(pair.getPublic()));
        info.set(X509CertInfo.VERSION, new CertificateVersion(CertificateVersion.V3));
        AlgorithmId algo = new AlgorithmId(AlgorithmId.md5WithRSAEncryption_oid);
        info.set(X509CertInfo.ALGORITHM_ID, new CertificateAlgorithmId(algo));

        // Sign the cert to identify the algorithm that's used.
        X509CertImpl cert = new X509CertImpl(info);
        cert.sign(privkey, algorithm);

        // Update the algorith, and resign.
        algo = (AlgorithmId)cert.get(X509CertImpl.SIG_ALG);
        info.set(CertificateAlgorithmId.NAME + "." + CertificateAlgorithmId.ALGORITHM, algo);
        cert = new X509CertImpl(info);
        cert.sign(privkey, algorithm);
        return cert;
    }

    private  CertPath certPath() throws CertificateException, FileNotFoundException {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            List mylist = new ArrayList();
            String[] numarray = {"asdgd", "bsgdsds", "csdsdgds"};
            for (int i = 0; i < numarray.length; i++) {
                FileInputStream in = new FileInputStream(numarray[i]);
                Certificate c = cf.generateCertificate(in);
                mylist.add(c);
            }
            CertPath cp = cf.generateCertPath(mylist);
            return cp;
    };*/
}