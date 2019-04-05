package hello;

import java.math.BigInteger;
import javax.annotation.PostConstruct;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.FastRawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.response.NoOpProcessor;

@RestController
public class HelloController {

    private Web3j web3j;

    @PostConstruct
    public void init() {
        web3j = Web3j.build(new HttpService());
    }

    @RequestMapping(value = "/getBalance", method = RequestMethod.GET)
    public String getBalance() throws Exception {
        EthGetBalance balance = web3j.ethGetBalance("0xaa9fac0d862c09dddd3ab53001fd72d50c0f7c47",
            DefaultBlockParameterName.LATEST).sendAsync().get();

        return "Balance: " + balance.getBalance();
    }

    @RequestMapping(value = "/getValue", method = RequestMethod.GET)
    public String getValue() throws Exception {
        String privatekey = "0b3750ad283f1c82778b3990fa1358112568855108edd6415f2712de7722ca0f";
        BigInteger privkey = new BigInteger(privatekey, 16);
        ECKeyPair ecKeyPair = ECKeyPair.create(privkey);
        Credentials credentials = Credentials.create(ecKeyPair);

        NoOpProcessor processor = new NoOpProcessor(web3j);

        ContractGasProvider gasProvider = new DefaultGasProvider();

        TransactionManager txManager = new FastRawTransactionManager(web3j, credentials, processor);
        SimpleStorage ss = SimpleStorage.load("0x74e1899807ddF5dc0fbD74d9AF95cAB9939A9769", web3j, txManager, gasProvider);
        return "Value: " + ss.get().sendAsync().get();
    }

    @RequestMapping(value = "/{value}", method = RequestMethod.POST)
    public String setValue(@PathVariable long value) throws Exception {
        String privatekey = "0b3750ad283f1c82778b3990fa1358112568855108edd6415f2712de7722ca0f";
        BigInteger privkey = new BigInteger(privatekey, 16);
        ECKeyPair ecKeyPair = ECKeyPair.create(privkey);
        Credentials credentials = Credentials.create(ecKeyPair);

        NoOpProcessor processor = new NoOpProcessor(web3j);

        ContractGasProvider gasProvider = new DefaultGasProvider();

        TransactionManager txManager = new FastRawTransactionManager(web3j, credentials, processor);
        SimpleStorage ss = SimpleStorage.load("0x74e1899807ddF5dc0fbD74d9AF95cAB9939A9769", web3j, txManager, gasProvider);
        return "Value: " + ss.set(BigInteger.valueOf(value)).sendAsync().get();
    }
}
