<template>
  <v-app>
    <Navigation :title="title" :refreshSelection="refreshSelection" @toggleSelection="handleSelection" @refresh="refresh" />
    <v-main>
      <NoteView :filterCategory="filterCategory" :refreshView="refreshView" @refreshDone="refresh" />
    </v-main>
  </v-app>
</template>

<script setup>
import { ref, provide } from 'vue'

// Variables
const title = "Noteworthy"
const filterCategory = ref()
const refreshView = ref()
const refreshSelection = ref()

// Imports/Exports
const refreshNav = (data) => {
  refreshSelection.value = data.refreshSelect
  filterCategory.value = data.refreshSelect
}
provide('refreshNav', refreshNav)

// Methods
const handleSelection = (selectedCategory) => {  
  filterCategory.value = selectedCategory
}

const refresh = () => {
  refreshView.value = !refreshView.value
}
</script>