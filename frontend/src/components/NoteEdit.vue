<template>
  <v-dialog :model-value="props.modelValue" max-width="700" @after-leave="close">
    <v-card title="Notiz bearbeiten" color="primary">
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
              <v-text-field v-model="noteTitle" label="Titel" variant="solo" bg-color="primary-darken-1" clearable hide-details />
            </v-toolbar>
          </v-card-item>

          <v-textarea v-model="noteText" label="Text" variant="solo" bg-color="info" rows="8" no-resize hide-details autofocus />
        </v-card>
      </v-card-text>

      <v-card-actions class="px-6" color="primary">
        <v-btn variant="plain" @click.stop="close">Abbrechen</v-btn>
        <v-btn variant="tonal" @click.stop="editNotes(props.noteProp.id)">Speichern</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>

  <Notification :userMsg="userMsg" />
</template>

<script setup>
import { ref, watch, inject } from 'vue'
import { fetchCategory, editNote } from '@/services/noteService'

// Variables
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
  modelValue: Boolean,
  noteProp: Object
})
const emit = defineEmits([
  'noteEdited',
  'update:modelValue'
])
const refreshNav = inject('refreshNav')

// Data Processing
const fetchSection = async (selectedSection) => {
  const { responseData, error } = await fetchCategory()
  if (responseData.value != null) {
    sections.value = responseData.value.data
    const i = sections.value.findIndex(item => item.id === selectedSection)
    selectedCategory.value = sections.value[i]
  } else if (error.value != null) {
    userMsg.value = {
      message: 'Kategorien konnten nicht geladen werden. Bitte Datenbankverbindung prüfen.',
      visible: true,
      color: 'error'
    }
  }
}

const editNotes = async (id) => {
  const note = {
    category: selectedCategory.value,
    noteTitle: noteTitle.value,
    noteText: noteText.value
  }
  const { error } = await editNote(id, note)
  if (error.value != null) {
    userMsg.value = {
      message: 'Fehler beim Speichern der Notiz.',
      visible: true,
      color: 'error'
    }
  } else {    
    if (props.noteProp.category.id !== selectedCategory.value.id) {
      const refreshSelect = selectedCategory.value.id
      refreshNav({refreshSelect})
    } else {
      emit('noteEdited', selectedCategory.value.id)
    }
    emit('update:modelValue', false)
    userMsg.value = {
      message: 'Abgespeichert',
      visible: true,
      color: 'success'
    }
  }
}

// Methodes
const close = () => {
  emit('update:modelValue', false)
}

watch(() => props.noteProp, (newValue) => {
  if (newValue) {
    fetchSection(props.noteProp.category.id)
    noteTitle.value = props.noteProp.noteTitle
    noteText.value = props.noteProp.noteText
  }
})
</script>