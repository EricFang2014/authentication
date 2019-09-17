package my.util.authentication;

/**
 * An interface to get ServiceAccount by service name, env
 * Created by eric on 5/5/19.
 */
public interface ServiceAccountFinder {
    ServiceAccount find(String envName, String serviceName, String userName);
}
