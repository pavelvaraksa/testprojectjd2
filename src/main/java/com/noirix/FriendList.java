package com.noirix;

import java.util.ArrayList;
import java.util.List;

public class FriendList {

    static List<User> totalFriends = new ArrayList<>();

    public static void main(String[] args) {
        User enzo = new User(1, "Enzo", "Ferrari");
        User carroll = new User(2, "Carroll", "Shelby");
        User henry = new User(3, "Henry", "Ford");
        User bruce = new User(4, "Bruce", "McLaren");
        User ferruccio = new User(5, "Ferruccio", "Lamborghini");

        List<User> enzoFriends = new ArrayList<>();
        enzoFriends.add(carroll);
        enzoFriends.add(ferruccio);
        enzoFriends.add(bruce);
        enzo.setFriends(enzoFriends);

        List<User> carrollFriends = new ArrayList<>();
        carrollFriends.add(henry);
        carrollFriends.add(bruce);
        carroll.setFriends(carrollFriends);

        List<User> henryFriends = new ArrayList<>();
        henryFriends.add(carroll);
        henry.setFriends(henryFriends);

        List<User> bruceFriends = new ArrayList<>();
        bruceFriends.add(enzo);
        bruceFriends.add(carroll);
        bruceFriends.add(ferruccio);
        bruce.setFriends(bruceFriends);

        List<User> ferruccioFriends = new ArrayList<>();
        ferruccioFriends.add(enzo);
        ferruccioFriends.add(bruce);
        ferruccio.setFriends(ferruccioFriends);

        List<User> getResult = getFriendList(enzo, 2);

        for (User userList : getResult) {
            System.out.println(userList);
        }
    }

    public static List<User> getFriendList(User user, int n) {
        List<User> friends = user.getFriends();
        totalFriends.addAll(friends);

        if (n == 1) {
            return totalFriends;
        }

        for (User friend : friends) {
            getFriendList(friend, n - 1);
        }

        return totalFriends;
    }
}


