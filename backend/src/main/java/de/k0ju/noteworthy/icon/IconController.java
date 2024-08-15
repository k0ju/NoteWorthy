package de.k0ju.noteworthy.icon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/icon")
public class IconController {
	@Autowired
	IconRepository iconRepository;

	@GetMapping("")
	public Iterable<Icon> readIcon() {
		Iterable<Icon> icon = iconRepository.findAll();

		if (icon.iterator().hasNext()) {
			return icon;
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No icons found.");
		}
	}
}