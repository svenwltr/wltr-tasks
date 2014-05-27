package eu.wltr.riker.auth;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import eu.wltr.riker.auth.bo.AuthBo;
import eu.wltr.riker.auth.pojo.Session;
import eu.wltr.riker.auth.pojo.User;


@Service
public class AuthInterceptor extends HandlerInterceptorAdapter {

	private static final String NAME_SID = "session_id";
	private static final String NAME_SECRET = "session_secret";

	@Autowired
	private AuthBo authBo;

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

		String uri = request.getRequestURI();

		if (!uri.startsWith("/api/") || uri.startsWith("/api/login/"))
			return true;

		String sid = null;
		String secret = null;

		for (Cookie c : request.getCookies()) {
			if (NAME_SID.equals(c.getName()))
				sid = c.getValue();

			if (NAME_SECRET.equals(c.getName()))
				secret = c.getValue();

		}

		User user = authBo.getUserBySession(sid);
		Session session = authBo.getSession(user, sid);

		if (!authBo.verifySession(session, secret)) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return false;

		} else {
			AuthContext ctx = new AuthContext(user, session);
			request.setAttribute(AuthContext.class.getCanonicalName(), ctx);

			return true;

		}

	}

}