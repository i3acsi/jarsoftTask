package ru.gasevsky.jarsoft.repo;

import org.springframework.data.repository.CrudRepository;
import ru.gasevsky.jarsoft.model.Banner;
import ru.gasevsky.jarsoft.model.Request;

import java.util.Date;
import java.util.List;

public interface RequestRepo extends CrudRepository<Request, Integer> {
    List<Request> findByUserAgentAndIpAddressAndCreatedAfter(String userAgent, String ipAddress, Date created);
}
