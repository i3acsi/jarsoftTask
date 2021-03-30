package ru.gasevsky.jarsoft.repo;

import org.springframework.data.repository.CrudRepository;
import ru.gasevsky.jarsoft.model.Request;

public interface RequestRepo extends CrudRepository<Request, Integer> {
}
