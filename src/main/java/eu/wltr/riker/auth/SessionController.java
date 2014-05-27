package eu.wltr.riker.auth;


import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("session/")
public class SessionController {

	@RequestMapping
	public Map<String, Object> getSession(AuthContext ctx) {
		Map<String, Object> map = new HashMap<>();

		map.put("sessionToken", ctx.getSession().getToken());
		map.put("userToken", ctx.getUser().getToken());
		map.put("userName", ctx.getUser().getName());

		return map;

	}

}
