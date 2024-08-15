package de.k0ju.noteworthy.icon;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IconRepository extends CrudRepository<Icon, Long> {

}