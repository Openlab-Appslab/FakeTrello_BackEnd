package FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.service;

import FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.model.User;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.model.VerificationToken;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.dataAccess.VerificationTokenRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;

@Service
public class VerificationTokenService {
    private VerificationTokenRepository verificationTokenRepository;

    public VerificationToken findByToken(String token){
        return verificationTokenRepository.findByVerificationToken(token);
    }

    public VerificationToken findByUser(User user){
        return verificationTokenRepository.findByUser(user);
    }

    public void save(User user, String token){
        VerificationToken verificationToken = new VerificationToken(token ,user);
        verificationToken.setExpiryDate(calculateExpiryDate(24*60));
    }

    private Timestamp calculateExpiryDate(int expiryTimeInMinutes){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Timestamp(cal.getTime().getTime());
    }

    public void deleteToken(String token){
        verificationTokenRepository.delete(findByToken(token));
    }
}
