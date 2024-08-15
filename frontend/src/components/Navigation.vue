<template>
  <v-app-bar color="primary-darken-1">
    <template v-slot:prepend>
      <v-app-bar-nav-icon variant="text" @click.stop="toggleNavButton" />
    </template>
    <v-app-bar-title class="text-h5 font-weight-black">{{ title }}</v-app-bar-title>
    <v-spacer />
    <NoteAdd :selectedSection="selectedSection" @noteSaved="refreshView" />
  </v-app-bar>

  <v-navigation-drawer color="primary" :rail="railToggle" :location="drawerLocation" :permanent="permanentToggle">
    <v-list v-model:selected="selected" density="compact" @click.stop="toggleSelection">
      <v-list-item v-for="section in sections" :key="section.id" :value="section.id" color="secondary">
        <template v-slot:prepend>
          <v-icon>{{ section.prependIcon }}</v-icon>
        </template>
        <v-tooltip :text="section.name" :disabled="section.name.length < 10" :location="tooltipLocation" offset="85">
          <template v-slot:activator="{ props }">
            <v-list-item-title v-bind="props">{{ section.name }}</v-list-item-title>
          </template>
        </v-tooltip>
        <template v-slot:append>
          <v-hover>
            <template v-slot:default="{ isHovering, props }">
              <v-btn v-bind="props" icon size="x-small" :variant="isHovering ? 'text' : 'plain'">
                <v-icon>{{ section.appendIcon1 }}</v-icon>
                <CategoryEdit :categoryProp="section" @categoryEdited="fetchSections" />
              </v-btn>
              <v-btn v-bind="props" icon size="x-small" :variant="isHovering ? 'text' : 'plain'" @click.stop="openConfirmDialog(section.id)">
                <v-icon>{{ section.appendIcon2 }}</v-icon>
              </v-btn>
            </template>
          </v-hover>
        </template>
      </v-list-item>
    </v-list>

    <template v-slot:append>
      <div class="pa-2">
        <CategoryAdd :isNavButtonPressed="isNavButtonPressed" @categorySaved="fetchSections" />
      </div>
    </template>
  </v-navigation-drawer>

  <ConfirmationDialog v-model="confirmDialog" :title="varConfirmDelete.title" :text="varConfirmDelete.text" @confirmDelete="deleteSections(selectedCategoryId)" />

  <Notification :userMsg="userMsg" />
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useDisplay } from 'vuetify'
import { fetchCategory, deleteCategory } from '@/services/noteService'

// Variables
const confirmDialog = ref(false)
const selectedCategoryId = ref(null)
const isNavButtonPressed = ref(false)
const selected = ref([])
const sections = ref([])
const { mobile } = useDisplay()
const userMsg = ref({
  message: '',
  visible: false,
  color: ''
})
const varConfirmDelete = ref({
  title: 'Kategorie löschen',
  text: 'Willst du diese Kategorie und die dazugehörigen Notizen wirklich löschen?'
})

// Imports/Exports
const props = defineProps({
  title: String,
  refreshSelection: Number
})
const emit = defineEmits([
  'toggleSelection',
  'select',
  'refresh'
])

// Computed
const drawerLocation = computed(() => mobile.value ? 'bottom' : 'left') //TODO: On mobile devices, the NavigationDrawer is displayed underneath the AppBar if too many categories are saved.
const railToggle = computed(() => mobile.value ? false : isNavButtonPressed.value)
const permanentToggle = computed(() => mobile.value ? isNavButtonPressed.value : false)
const tooltipLocation = computed(() => mobile.value ? 'top' : 'end')
const selectedSection = computed(() => typeof selected.value[0] === 'undefined' ? 0 : selected.value[0])

// Data Processing
const fetchSections = async () => {
  const { responseData, error } = await fetchCategory()
  if (responseData.value) {
    sections.value = responseData.value.data
  }
  if (error.value != null) {
    if (error.value.message === 'Request failed with status code 404') {
      userMsg.value = {
        message: 'Bitte lege zunächst eine Kategorie an.',
        visible: true,
        color: 'error'
      }
    } else {
      userMsg.value = {
        message: 'Kategorien konnten nicht geladen werden.',
        visible: true,
        color: 'error'
      }
    }
  }
}

const deleteSections = async (categoryId) => {
  const { error } = await deleteCategory(categoryId)
  if (error.value != null) {
    userMsg.value = {
      message: 'Kategorie konnte nicht gelöscht werden.',
      visible: true,
      color: 'error'
    }
  } else {
    fetchSections()
    delete selected.value[0]
    toggleSelection()
    userMsg.value = {
      message: 'Gelöscht.',
      visible: true,
      color: 'success'
    }
  }
}

// Methods
onMounted(fetchSections)

const toggleNavButton = () => {
  isNavButtonPressed.value = !isNavButtonPressed.value
}

const toggleSelection = () => {
  emit('toggleSelection', selected.value[0])
}

const openConfirmDialog = (sectionId) => {
  selectedCategoryId.value = sectionId
  confirmDialog.value = true
}

const refreshView = (selectedCategory) => {
  const i = sections.value.findIndex(item => item.id === selectedCategory)
  if (selected.value[0] === sections.value[i].id) {
    emit('refresh')
  } else {
    selected.value[0] = sections.value[i].id
    toggleSelection()
  }
}

watch(() => props.refreshSelection, (newValue) => {
  if (newValue) {
    selected.value[0] = newValue
  }
})
</script>