package org.example.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.example.data.repository.ItemRepository;
import org.example.data.repository.UserRepository;
import org.example.service.SessionStore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class IntegrationTestBase {

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            File file = new File("./test_mydatabase.db");
            file.deleteOnExit();
        }));
    }


    @Value("${server.host}")
    private String serverHost;
    @Value("${server.port}")
    private int serverPort;

    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected ItemRepository itemRepository;
    @Autowired
    protected SessionStore sessionStore;

    protected SignUpRequest signUpRequestSeller;
    protected SignUpRequest signUpRequestBuyer;

    protected String sellerLogin;
    protected String buyerLogin;
    protected String password;
    protected String buyerSessionId;
    protected String sellerSessionId;

    protected Item.Builder newItem1;
    protected Item.Builder newItem2;

    protected SignUpServiceGrpc.SignUpServiceBlockingStub signUpServiceStub;
    protected LoginServiceGrpc.LoginServiceBlockingStub loginServiceStub;
    protected SellerServiceGrpc.SellerServiceBlockingStub sellerServiceBlockingStub;
    protected ManagedChannel channel;

    @BeforeEach
    void setUp() {
        sellerLogin = "Lisa";
        buyerLogin = "Masha";
        password = "lisa";
        signUpRequestSeller = SignUpRequest.newBuilder().setRole(Role.SELLER).setLogin(sellerLogin).setPassword(password).build();
        signUpRequestBuyer = SignUpRequest.newBuilder().setRole(Role.BUYER).setLogin(buyerLogin).setPassword(password).build();
        channel = ManagedChannelBuilder.forAddress(serverHost, serverPort)
                .usePlaintext()
                .build();
        signUpServiceStub = SignUpServiceGrpc.newBlockingStub(channel);
        loginServiceStub = LoginServiceGrpc.newBlockingStub(channel);
        sellerServiceBlockingStub = SellerServiceGrpc.newBlockingStub(channel);

        newItem1 = Item.newBuilder().setInvoicePriceCents(10).setQuantity(10).setSellPriceCents(20).setDeleted(false).setName("tomato");
        newItem2 = Item.newBuilder().setInvoicePriceCents(5).setQuantity(5).setSellPriceCents(10).setDeleted(false).setName("potato");
    }

    protected void signUpSeller() {
        if(sellerSessionId == null) {
            signUpServiceStub.signUp(signUpRequestSeller);
        }
    }

    protected void signUpBuyer() {
        if(buyerSessionId == null) {
            signUpServiceStub.signUp(signUpRequestBuyer);
        }
    }

    protected LoginResponse buyerLogin(){
        var pair = login(buyerLogin);
        buyerSessionId = pair.getFirst();
        return pair.getSecond();
    }

    protected LoginResponse sellerLogin() {
        var pair = login(sellerLogin);
        sellerSessionId = pair.getFirst();
        return pair.getSecond();
    }

    private Pair<String, LoginResponse> login(String login) {
        var loginRequest = LoginRequest.newBuilder().setLogin(login).setPassword(password).build();
        var response = loginServiceStub.login(loginRequest);
        var sessionId = response.getSessionId();
        return Pair.of(sessionId, response);
    }

    protected List<Item> addItemsToDB() {
        signUpSeller();
        sellerLogin();
        var updateItemRequest = UpdateItemListRequest.newBuilder().addItem(newItem1).addItem(newItem2).setSessionId(sellerSessionId).build();
        sellerServiceBlockingStub.updateItemList(updateItemRequest);
        return sellerServiceBlockingStub.getItemList(GetItemListRequest.newBuilder().setSessionId(sellerSessionId).build()).getItemList();
    }

    @AfterEach
    public void cleanUp() throws InterruptedException {
        userRepository.deleteAll();
    }

}
