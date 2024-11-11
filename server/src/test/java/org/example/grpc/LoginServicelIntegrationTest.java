package org.example.grpc;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import jakarta.transaction.Transactional;
import org.example.data.repository.CartRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


public class LoginServicelIntegrationTest extends IntegrationTestBase {

    @Autowired
    private CartRepository cartRepository;


    @Test
    public void testSuccessfulSignUp_seller() {
        signUpSeller();
        var user = userRepository.findByLogin(sellerLogin).get();
        assertEquals(sellerLogin, user.getLogin());
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
        signUpBuyer();
        var user = userRepository.findByLogin(buyerLogin).get();
        var cart = cartRepository.findByBuyer(user);
        assertEquals(buyerLogin, user.getLogin());
        assertEquals(Role.BUYER, user.getRole());
        assertEquals(user, cart.getBuyer());
        assertNotEquals(password, user.getPassword());
        assertTrue(cart.getCartEntries().isEmpty());
    }


    @Test
    public void testBuyerLogin() {
        signUpBuyer();
        LoginResponse response = buyerLogin();
        assertNotNull(response.getSessionId());
        var userFromDB = userRepository.findByLogin(buyerLogin).get();
        var userFromSessionStore = sessionStore.getUser(UUID.fromString(response.getSessionId()));
        assertEquals(userFromDB, userFromSessionStore);
    }

    @Test
    public void testSellerLoginIncorrectPassword() {
        userRepository.deleteAll();
        signUpSeller();
        var loginRequest = LoginRequest.newBuilder().setLogin(sellerLogin).setPassword("incorrect").build();
        StatusRuntimeException statusRuntimeException = assertThrows(StatusRuntimeException.class, () -> loginServiceStub.login(loginRequest));
        assertEquals(Status.UNAUTHENTICATED.getCode(), statusRuntimeException.getStatus().getCode());
        assertEquals("Invalid credentials", statusRuntimeException.getStatus().getDescription());
    }


}
