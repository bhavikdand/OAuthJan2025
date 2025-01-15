package org.example.oauthjan25.handlers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.oauthjan25.models.User;
import org.example.oauthjan25.services.JwtService;
import org.example.oauthjan25.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2SuccessCallbackHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String photoUrl = oAuth2User.getAttribute("picture");
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setPhoto(photoUrl);

        if(userService.findByEmail(email).isPresent()){
            System.out.println(name + " is trying to log in");
        } else {
            System.out.println(name + " is signing in for the first time");
            user = userService.saveUser(user);
        }


        String token = jwtService.generateToken(user);
        // Similarly generate 1 refresh token
        // jwt_token/access_token -> it will have shorter expiry time as compared to refresh token
        // refresh token will have longer time

        // save the refresh token in db.
        // Also send the refresh token along with another cookie to the browser.

        Cookie cookie = new Cookie("jwt_token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        getRedirectStrategy().sendRedirect(request, response, "/secured");

    }
}
