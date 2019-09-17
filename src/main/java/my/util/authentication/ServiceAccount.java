package my.util.authentication;

/**
 * ServiceAccount with service name, env name, account name, password and token
 * The password was made of password and token then encrypted by md5
 * Created by eric on 5/5/19.
 */
public class ServiceAccount {
    private String serviceName;
    private String envName;
    private String name;
    private String passwd;
    private String token;

    public String getEnvName() {
        return envName;
    }

    public void setEnvName(String envName) {
        this.envName = envName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
