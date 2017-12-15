package user;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

public class FollowingUser {

    private String username;
    private LocalDate followingTime ;
    private LocalDateTime unfollowingTime ;

    public FollowingUser(String username){
        this.username = username;
        followingTime = LocalDate.now();
        int unFollowingDay = ThreadLocalRandom.current().nextInt(0, 7);
        int unFollowingHour = ThreadLocalRandom.current().nextInt(0, 24);;
        unfollowingTime = LocalDateTime.now().plusDays(unFollowingDay).plusHours(unFollowingHour);
    }

    public String getUsername() {
        return username;
    }
}
