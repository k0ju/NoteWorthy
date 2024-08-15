<template>
  <v-btn v-if="$vuetify.display.mobile == false" prepend-icon="mdi-note-plus" @click.stop="dialogActive = true">Notiz hinzufügen</v-btn>
  <v-btn v-else icon="mdi-note-plus" @click.stop="dialogActive = true" aria-label="Notiz hinzfügen" />

  <v-dialog v-model="dialogActive" max-width="700">
    <v-card title="Notiz hinzufügen" color="primary">
      <v-divider thickness="2" />
      <v-card-text>
        <v-select v-model="selectedCategory" :items="sections" label="Kategorie" variant="outlined">
          <template v-slot:item="{ props, item }">
            <v-list-item v-bind="props" title="" class="text-primary" density="compact">
              <template v-slot:prepend>
                <v-avatar>
                  <v-icon color="primary">{{ item.raw.prependIcon }}</v-icon>
                </v-avatar>
              </template>
              <v-list-item-title>{{ item.raw.name }}</v-list-item-title>
            </v-list-item>
          </template>
          <template v-slot:selection="{ item }">
            <v-list-item title="" :prepend-icon="item.raw.prependIcon">
              <v-list-item-title>{{ item.raw.name }}</v-list-item-title>
            </v-list-item>
          </template>
        </v-select>

        <v-card color="info" class="rounded-0">
          <v-card-item class="pa-0">
            <v-toolbar density="compact" class="rounded-0">
              <v-text-field v-model="noteTitle" label="Titel" variant="solo" bg-color="primary-darken-1" clearable hide-details autofocus />
            </v-toolbar>
          </v-card-item>

          <v-textarea v-model="noteText" label="Text" variant="solo" bg-color="info" rows="8" no-resize hide-details />
        </v-card>
      </v-card-text>

      <v-card-actions class="px-6" color="primary">
        <v-btn variant="plain" @click.stop="dialogActive = false">Abbrechen</v-btn>
        <v-btn variant="tonal" @click.stop="saveNotes">Speichern</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>

  <Notification :userMsg="userMsg" />
</template>

<script setup>
import { ref, watch } from 'vue'
import { fetchCategory, saveNote } from '@/services/noteService'

// Variables
const dialogActive = ref(false)
const selectedCategory = ref()
const noteTitle = ref()
const noteText = ref()
const sections = ref([])
const userMsg = ref({
  message: '',
  visible: false,
  color: ''
})

// Imports/Exports
const props = defineProps({
  selectedSection: Number
})
const emit = defineEmits([
  'noteSaved',
  'selected'
])

// Data Processing
const fetchSection = async (selectedSection) => {
  const { responseData, error } = await fetchCategory()
  if (responseData.value != null) {
    sections.value = responseData.value.data
    const i = sections.value.findIndex(item => item.id === selectedSection)
    selectedCategory.value = i > 0 ? sections.value[i] : sections.value[0]
  } else if (error.value != null) {
    if (error.value.message === 'Request failed with status code 404') {
      userMsg.value = {
        message: 'Bitte lege zunächst mindestens eine Kategorie an.',
        visible: true,
        color: 'error'
      }
      dialogActive.value = false
    } else {
      userMsg.value = {
        message: 'Kategorien konnten nicht geladen werden. Bitte Datenbankverbindung prüfen.',
        visible: true,
        color: 'error'
      }
      dialogActive.value = false
    }
  }
}

const saveNotes = async () => {
  const note = {
    category: selectedCategory.value,
    noteTitle: noteTitle.value,
    noteText: noteText.value
  }
  const { error } = await saveNote(note)
  if (error.value != null) {
    userMsg.value = {
      message: 'Fehler beim Speichern der Notiz.',
      visible: true,
      color: 'error'
    }
  } else {
    emit('noteSaved', selectedCategory.value.id)
    dialogActive.value = false
    noteTitle.value = '' //TODO: clean reset with form
    noteText.value = '' //TODO: clean reset with form
    selectedCategory.value = sections.value[0] //TODO: clean reset with form
    userMsg.value = {
      message: 'Abgespeichert',
      visible: true,
      color: 'success'
    }
  }
}

// Methodes
watch(() => dialogActive.value, (newValue) => {
  if (newValue) {
    fetchSection(props.selectedSection)
  }
})
</script>