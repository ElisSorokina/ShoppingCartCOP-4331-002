package org.example.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import jakarta.transaction.Transactional;
import org.example.data.repository.CartRepository;
import org.example.data.repository.UserRepository;
import org.example.service.SessionStore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class LoginServiceGrpcImplIntegrationTest {

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
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private SessionStore sessionStore;


    private SignUpServiceGrpc.SignUpServiceBlockingStub signUpServiceStub;
    private LoginServiceGrpc.LoginServiceBlockingStub loginServiceStub;
    private ManagedChannel channel;
    private String login;
    private String password;
    private SignUpRequest signUpRequestSeller;
    private SignUpRequest signUpRequestBuyer;

    @BeforeEach
    public void setup() throws InterruptedException {
        channel = ManagedChannelBuilder.forAddress(serverHost, serverPort)
                .usePlaintext()
                .build();
        signUpServiceStub = SignUpServiceGrpc.newBlockingStub(channel);
        loginServiceStub = LoginServiceGrpc.newBlockingStub(channel);
        login = "Lisa";
        password = "lisa";
        signUpRequestSeller = SignUpRequest.newBuilder().setRole(Role.SELLER).setLogin(login).setPassword(password).build();
        signUpRequestBuyer = SignUpRequest.newBuilder().setRole(Role.BUYER).setLogin(login).setPassword(password).build();
    }

    @Test
    public void testSuccessfulSignUp_seller() {
        signUpServiceStub.signUp(signUpRequestSeller);
        var user = userRepository.findByLogin(login).get();
        assertEquals(login, user.getLogin());
        assertEquals(Role.SELLER, user.getRole());
        //password is encrypted
        assertNotEquals(password, user.getPassword());
    }

    @Test
    public void testDuplicateLogin() {
        testSuccessfulSignUp_seller();
        assertThrows(StatusRuntimeException.class, () -> signUpServiceStub.signUp(signUpRequestSeller));
    }

    @Test
    @Transactional
    public void testSuccessfulSignUp_buyer() {
        signUpServiceStub.signUp(signUpRequestBuyer);

        var user = userRepository.findByLogin(login).get();
        var cart = cartRepository.findByBuyer(user);
        assertEquals(login, user.getLogin());
        assertEquals(Role.BUYER, user.getRole());
        assertEquals(user, cart.getBuyer());
        assertNotEquals(password, user.getPassword());
        assertTrue(cart.getCartEntries().isEmpty());
    }

    @Test
    public void testBuyerLogin() {
        signUpServiceStub.signUp(signUpRequestBuyer);

        var loginRequest = LoginRequest.newBuilder().setLogin(login).setPassword(password).build();
        var response = loginServiceStub.login(loginRequest);
        assertNotNull(response.getSessionId());
        var userFromDB = userRepository.findByLogin(loginRequest.getLogin()).get();
        var userFromSessionStore = sessionStore.getUser(UUID.fromString(response.getSessionId()));
        assertEquals(userFromDB, userFromSessionStore);
    }

    @Test
    public void testSellerLoginIncorrectPassword() {
        userRepository.deleteAll();
        signUpServiceStub.signUp(signUpRequestSeller);
        var loginRequest = LoginRequest.newBuilder().setLogin(login).setPassword("incorrect").build();
        StatusRuntimeException statusRuntimeException = assertThrows(StatusRuntimeException.class, () -> loginServiceStub.login(loginRequest));
        assertEquals(Status.UNAUTHENTICATED.getCode(), statusRuntimeException.getStatus().getCode());
        assertEquals("Invalid credentials", statusRuntimeException.getStatus().getDescription());
    }

    @AfterEach
    public void cleanUp() throws InterruptedException {
        userRepository.deleteAll();
    }
}
