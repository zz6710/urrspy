package com.kayak.login.filter;

import java.security.Key;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;

import com.kayak.core.spring.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kayak.config.ConfigUitl;
import com.kayak.core.util.Tools;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class JwtServer {
	private static final Logger log = LoggerFactory.getLogger(JwtServer.class);

	private static Key key;

	public static String makeToken(Date expDate, String subject) {
		checkKey();
		String jws = Jwts.builder().setExpiration(expDate).setSubject(subject).signWith(key).compact();
		return jws;
	}

	public static String parshToken(String compactJws) {
		checkKey();
		return Jwts.parser().setSigningKey(key).parseClaimsJws(compactJws).getBody().getSubject();
	}

	public static Claims parshTokenBody(String compactJws) {
		checkKey();
		return Jwts.parser().setSigningKey(key).parseClaimsJws(compactJws).getBody();
	}

	private static void checkKey() {
		if (key == null) {
			synchronized (JwtServer.class) {
				if (key == null) {
					String secret = null;
					try {
						secret = SpringContextHolder.getApplicationContext().getEnvironment().getProperty("jwt.secret");
					} catch (Exception e) {
					}
					if (Tools.strIsEmpty(secret)) {
						key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
						log.warn("jwt.secret未配置，使用随机秘钥");
					} else {
						key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
					}

				}
			}
		}
	}

}
