package com.arimac.backend.pmtool.projectmanagementtool.config;

import com.arimac.backend.pmtool.projectmanagementtool.Response.ResponseController;
import com.arimac.backend.pmtool.projectmanagementtool.repository.UserRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class RequestInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(RequestInterceptor.class);

    private static final String BEARER = "Bearer ";

    private final UserRepository userRepository;

    public RequestInterceptor(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith(BEARER)) {
            token = token.substring(7);
            logger.info("token {}", token);
            try {
                DecodedJWT jwt = JWT.decode(token);
                logger.info("jwt subject {}", jwt.getSubject());

            } catch (Exception e){
                logger.info("JWT Exception", e);
                response.sendError(400);
                return false;
            }
            return true;
        } else {
            response.sendError(400);
            return false;
        }
    }


}
