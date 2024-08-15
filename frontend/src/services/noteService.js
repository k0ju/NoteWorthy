import { ref } from 'vue'
import axios from 'axios'

const api = axios.create({
  baseURL: `http://localhost:8080`
})

async function request(method, url, data = null) {
  const responseData = ref(null)
  const error = ref(null)

  try {
    const response = await api({ method, url, data })
    responseData.value = response
  } catch (err) {
    error.value = err
  }

  return { responseData, error }
}

export async function fetchIcons() {
  return request('get', `/icon`)
}

export async function fetchCategory() {
  return request('get', `/category`)
}

export async function editCategory(id, category) {
  return request('put', `/category/edit/${id}`, category)
}

export async function saveCategory(category) {
  return request('post', `/category/save`, category)
}

export async function deleteCategory(categoryId) {
  return request('delete', `/category/${categoryId}`)
}

export async function fetchNotes(categoryId) {
  return request('get', `/note/get/${categoryId}`)
}

export async function editNote(id, note) {
  return request('put', `/note/edit/${id}`, note)
}

export async function saveNote(note) {
  return request('post', `/note/save`, note)
}

export async function deleteNote(noteId) {
  return request('delete', `/note/${noteId}`)
}