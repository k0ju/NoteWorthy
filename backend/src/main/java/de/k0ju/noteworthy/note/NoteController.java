package de.k0ju.noteworthy.note;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/note")
public class NoteController {
	@Autowired
	NoteRepository noteRepository;

	@GetMapping("/get/{categoryId}")
	public Iterable<Note> readNote(@PathVariable Long categoryId) {
		Iterable<Note> note = noteRepository.findByCategoryId(categoryId, Sort.by(Sort.Direction.DESC, "id"));
		if (note.iterator().hasNext()) {
			return note;
		} else {
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Note with this category not found.");}
	}
	
	@PostMapping("/save")
	public void createNote(@RequestBody Note note) {
		noteRepository.save(note);
	}

	@PutMapping("/edit/{noteId}")
	public void updateNote(@PathVariable Long noteId, @RequestBody Note noteUpdate) {
		Optional<Note> note = noteRepository.findById(noteId);
		if (!note.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Note with this id not found.");
		}

		Note noteInstance = note.get();
		noteInstance.setCategory(noteUpdate.getCategory());
		noteInstance.setNoteTitle(noteUpdate.getNoteTitle());
		noteInstance.setNoteText(noteUpdate.getNoteText());
		noteRepository.save(noteInstance);
	}

	@DeleteMapping("/{noteId}")
	public void deleteNote(@PathVariable Long noteId) {
		Optional<Note> note = noteRepository.findById(noteId);
		if (note.isPresent()) {
			noteRepository.deleteById(noteId);
			return;
		}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Note with this id not found.");
	}
}