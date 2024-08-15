package de.k0ju.noteworthy.note;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface NoteRepository extends PagingAndSortingRepository<Note, Long>, CrudRepository<Note, Long> {
	@Transactional(readOnly = true)
	Iterable<Note> findByCategoryId(Long categoryId, Sort sort);
}