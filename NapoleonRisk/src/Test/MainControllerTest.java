package Controllers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainControllerTest {

    @Test
    void getPlayerClaim() {
        MainController claim = new MainController();
        MainController claim2 = new MainController();
        claim.setPlayerClaim(false);
        claim2.setPlayerClaim(true);

        assertEquals(false, claim.getPlayerClaim());
        assertEquals(true, claim2.getPlayerClaim());

    }




}