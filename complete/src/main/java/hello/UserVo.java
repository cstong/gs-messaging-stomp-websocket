package hello;

import java.io.Serializable;
import java.security.Principal;

/**
 * Created by xiaoxiao on 2017/8/14.
 */
public class UserVo implements Principal {

    private String userId;
    private String token;

    public UserVo() {
    }

    public UserVo(String userName) {
        this.userId = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String getName() {
        return this.getUserId();
    }
}
