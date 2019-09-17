package my.util.authentication.database;

import my.util.authentication.ServiceAccount;
import my.util.authentication.ServiceAccountFinder;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

/**
 * An implement to get service account by sp usp_get_service_account_by_name
 * Created by eric on 5/5/19.
 */
public class ServiceAccountFinderImpl extends JdbcDaoSupport implements ServiceAccountFinder  {
    private static final String SPToGetServiceAccountByName = "{call usp_get_service_account_by_name(?, ?, ?)}";

    public ServiceAccount find(String envName, String serviceName, String userName){
        //ServiceAccount account;
        ServiceAccount serviceAccount = this.getJdbcTemplate().execute((Connection conn) -> {
            CallableStatement callable = conn.prepareCall(SPToGetServiceAccountByName);
            callable.setString(1, serviceName);
            callable.setString(2, envName);
            callable.setString(3, userName);
            return callable;
        }, (CallableStatement cs) -> {
            cs.execute();
            ResultSet rs = cs.getResultSet();
            ServiceAccount account = null;
            if (rs.next()){
                account = new ServiceAccount();
                account.setServiceName(serviceName);
                account.setEnvName(envName);
                account.setName(rs.getString("name"));
                account.setPasswd(rs.getString("passwd"));
                account.setToken(rs.getString("token"));
                rs.close();
            }
            return account;
        });
        return serviceAccount;
    }

}
