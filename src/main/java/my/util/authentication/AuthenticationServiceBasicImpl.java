package my.util.authentication;

import com.nirvanainfo.common.util.encrypt.Encrypt;
import com.nirvanainfo.common.util.encrypt.EncryptImpl;
import com.nirvanainfo.common.util.web.User;
import com.nirvanainfo.common.util.web.WebUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A basic authentication to check if the request is authorized, it use a ServiceAccountFinder implementation to get service account
 * Created by eric on 4/23/19.
 */
public class AuthenticationServiceBasicImpl implements AuthenticationService {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationServiceBasicImpl.class);
    private static final String MD5 = "MD5";

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    private  String serviceName;


    public String getEnvName() {
        return envName;
    }

    public void setEnvName(String envName) {
        this.envName = envName;
    }

    private String envName;

    public ServiceAccountFinder getServiceAccountFinder() {
        return serviceAccountFinder;
    }

    public void setServiceAccountFinder(ServiceAccountFinder serviceAccountFinder) {
        this.serviceAccountFinder = serviceAccountFinder;
    }

    private ServiceAccountFinder serviceAccountFinder;

    @Override
    public boolean authorize(HttpServletRequest request, HttpServletResponse response) {
        String auth = request.getHeader(WebUtil.Authorization);
        if (StringUtils.isBlank(auth)){
            logger.warn("The {} header is blank, no basic auth is set in header.", WebUtil.Authorization);
            return false;
        }
        User user = WebUtil.getUserFromBase64Auth(auth);
        if (user == null){
            logger.warn("No user is got by base64 auth {}", auth);
            return false;
        }

        if (!doAuthorize(user)){
            logger.warn("The {} header is {}, and not match the expected value", WebUtil.Authorization, auth);
            return false;
        }
        return true;
    }

    protected boolean doAuthorize(User user){
        ServiceAccount account = getServiceAccountFinder().find(getEnvName(), getServiceName(), user.getUser());
        if (account == null){
            logger.warn("No service account is found by envName {}, serviceName {} and user {}"
                    , getEnvName(), getServiceName(), user.getUser());
            return false;
        }

        if (!StringUtils.equals(user.getUser(), account.getName())){
            logger.warn("The request user {} is not equals service account name {}"
                    , user.getUser(), account.getName());
            return false;
        }
        Encrypt encrypt = new EncryptImpl(MD5);
        boolean result = false;
        try {
            String md5 = encrypt.encrypt(user.getPasswd(), account.getToken());
            result = StringUtils.endsWithIgnoreCase(account.getPasswd(), md5);
        }catch (Exception ex){
            logger.error("Failed to get md5 for passwd: {} and token: {}, message: {}",
                    user.getPasswd(), account.getToken(), ex.getMessage());
            result = false;
        }
        return result;



    }

}
