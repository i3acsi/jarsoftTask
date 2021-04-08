package ru.gasevsky.jarsoft.repo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.gasevsky.jarsoft.model.Banner;
import ru.gasevsky.jarsoft.model.User;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;

@Component
@EnableScheduling
@Slf4j
public class SessionMap {
    private final ConcurrentHashMap<User, ConcurrentLinkedDeque<Pair<Date, Integer>>> sessionMap = new ConcurrentHashMap<>();


    public List<Integer> getWatchedBanners(String ip, String userAgent) {
        User user = new User(ip, userAgent);
        cutList(user);
        ConcurrentLinkedDeque<Pair<Date, Integer>> dq = sessionMap.get(user);
        if (dq == null || dq.isEmpty())
            return new ArrayList<Integer>();
        else
            return dq.stream().map(Pair::getSecond).collect(Collectors.toList());
    }

    public void acceptBanner(String ip, String userAgent, Banner banner) {
        User user = new User(ip, userAgent);
        Pair<Date, Integer> entry = Pair.of(new Date(System.currentTimeMillis()), banner.getId());
        sessionMap.compute(user, (u, q) -> {
            if (q == null)
                q = new ConcurrentLinkedDeque<>();
            q.push(entry);
            return q;
        });
    }

    private void cutList(User user) {
        final Date yesterday = Date.from(LocalDate.now().minusDays(1L).atStartOfDay(ZoneId.systemDefault()).toInstant());
        ConcurrentLinkedDeque<Pair<Date, Integer>> deque = sessionMap.get(user);
        if (deque != null)
            do {
                Pair<Date, Integer> pair = deque.pollLast();
                if (pair == null)
                    break;
                if (pair.getFirst().after(yesterday)) {
                    deque.addLast(pair);
                    break;
                }
            } while (!deque.isEmpty());
    }

    @Scheduled(fixedDelay = 86400000)
    public void cleanMap() {
        log.info("CLEANING MAP STARTED: " + LocalDate.now());
        final Date yesterday = Date.from(LocalDate.now().minusDays(1L).atStartOfDay(ZoneId.systemDefault()).toInstant());
        sessionMap.forEach((key, value) -> {
            if (value.isEmpty() || value.getFirst().getFirst().before(yesterday))
                sessionMap.remove(key);
        });
        log.info("CLEANING MAP FINISHED: " + LocalDate.now());
    }
}
