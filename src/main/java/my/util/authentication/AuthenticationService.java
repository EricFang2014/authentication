package my.util.authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by eric on 4/23/19.
 */
public interface AuthenticationService {
    boolean authorize(HttpServletRequest request, HttpServletResponse response);
}
