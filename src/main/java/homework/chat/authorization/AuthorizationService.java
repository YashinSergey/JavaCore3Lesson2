package homework.chat.authorization;


import java.util.HashMap;
import java.util.Map;

public class AuthorizationService implements Authorization {

    public Map<String, String> users = new HashMap<>();

    public AuthorizationService() {
        users.put("john", "123");
        users.put("valery", "345");
        users.put("bob", "567");
        users.put("jenifer", "789");
    }

    @Override
    public boolean authUser(String userName, String password) {
        String psw = users.get(userName);
        return psw != null && psw.equals(password);
    }
}
