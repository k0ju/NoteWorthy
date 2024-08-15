package de.k0ju.noteworthy.category;

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
@RequestMapping("/category")
public class CategoryController {
	@Autowired
	CategoryRepository categoryRepository;
	
	@GetMapping("")
	public Iterable<Category> readCategory() {
		Iterable<Category> category = categoryRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));

		if (category.iterator().hasNext()) {
			return category;
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No categories found.");
		}
	}

	@PostMapping("/save")
	public void createCategory(@RequestBody Category category) {
		categoryRepository.save(category);
	}
	
	@PutMapping("/edit/{categoryId}")
	public void updateCategory(@PathVariable Long categoryId, @RequestBody Category categoryUpdate) {
		Optional<Category> category = categoryRepository.findById(categoryId);
		if (!category.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category with this id not found.");
		}

		Category categoryInstance = category.get();
		categoryInstance.setName(categoryUpdate.getName());
		categoryInstance.setPrependIcon(categoryUpdate.getPrependIcon());
		categoryRepository.save(categoryInstance);
	}

	@DeleteMapping("/{categoryId}")
	public void deleteCateogry(@PathVariable Long categoryId) {
		Optional<Category> category = categoryRepository.findById(categoryId);
		if (category.isPresent()) {
			categoryRepository.deleteById(categoryId);
			return;
		}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category with this id not found.");
	}
}