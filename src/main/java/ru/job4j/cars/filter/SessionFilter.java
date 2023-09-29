package ru.job4j.cars.filter;

import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ru.job4j.cars.model.User;

@Component
@Order(2)
public class SessionFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response,
                            FilterChain chain) throws IOException, ServletException {
        HttpSession session = request.getSession();
        addUserToSession(session, request);
        chain.doFilter(request, response);
    }

    private void addUserToSession(HttpSession session, HttpServletRequest request) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setId(-1);
            user.setName("Гость");
        }
        request.setAttribute("user", user);
    }

}