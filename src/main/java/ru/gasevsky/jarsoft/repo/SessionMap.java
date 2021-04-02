package ru.gasevsky.jarsoft.repo;

import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import ru.gasevsky.jarsoft.model.Banner;
import ru.gasevsky.jarsoft.model.User;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;

@Component
public class SessionMap {
    private final ConcurrentHashMap<User, ConcurrentLinkedDeque<Pair<Date, Integer>>> sessionMap = new ConcurrentHashMap<>();


    public List<Integer> getWatchedBanners(String ip, String userAgent) {
        cutList(ip, userAgent);
        return sessionMap
                .getOrDefault(new User(ip, userAgent), new ConcurrentLinkedDeque<>())
                .stream()
                .map(Pair::getSecond)
                .collect(Collectors.toList());
    }

    public void acceptBanner(String ip, String userAgent, Banner banner) {
        sessionMap.compute(new User(ip, userAgent), (u, q) -> {
            if (q == null)
                q = new ConcurrentLinkedDeque<>();
            q.push(Pair.of(new Date(System.currentTimeMillis()), banner.getId()));
            return q;
        });
    }

    private void cutList(String ip, String userAgent) {
        final Date yesterday = Date.from(LocalDate.now().minusDays(1L).atStartOfDay(ZoneId.systemDefault()).toInstant());
        sessionMap.computeIfPresent(new User(ip, userAgent), (u, q) -> {
            while (q.getLast().getFirst().before(yesterday)) {
                q.removeLast();
            }
            return q;
        });
    }
}
