<template>
    <v-container>
        <v-row>
            <v-col cols="12" lg="4" v-for="note in notes" :key="note.id">
                <v-card color="info" class="rounded-0" min-height="150" :height="expandedCards[note.id].cardHeight">
                    <v-card-item class="pa-0">
                        <v-toolbar density="compact" color="primary-darken-1" class="rounded-0">
                            <v-tooltip :text="note.noteTitle" :disabled="note.noteTitle.length < 40" location="bottom">
                                <template v-slot:activator="{ props }">
                                    <v-toolbar-title v-bind="props">{{ note.noteTitle }}</v-toolbar-title>
                                </template>
                            </v-tooltip>
                            <v-hover>
                                <template v-slot:default="{ isHovering, props }">
                                    <v-btn v-bind="props" icon :variant="isHovering ? 'text' : 'plain'" @click.stop="openConfirmDialog(note.id)">
                                        <v-icon icon="mdi-trash-can"></v-icon>
                                    </v-btn>
                                    <v-btn v-bind="props" icon :variant="isHovering ? 'text' : 'plain'" @click.stop="openNoteEdit(note)">
                                        <v-icon icon="mdi-pencil"></v-icon>
                                    </v-btn>
                                    <v-btn :icon="expandedCards[note.id].cardIcon" :variant="isHovering ? 'text' : 'plain'" @click="expandCard(note.id)" />
                                </template>
                            </v-hover>
                        </v-toolbar>
                    </v-card-item>

                    <div v-show="!expandedCards[note.id].expand">
                        <v-card-text class="pt-2 text-pre-wrap custom-text-gradient" @click="expandCard(note.id)">
                            {{ note.noteText.slice(0, expandedCards[note.id].textSlice) }}
                        </v-card-text>
                    </div>

                    <v-expand-transition>
                        <div v-show="expandedCards[note.id].expand">
                            <v-card-text class="pt-2 text-pre-wrap" @click="expandCard(note.id)">
                                {{ note.noteText }}
                            </v-card-text>
                        </div>
                    </v-expand-transition>
                </v-card>
            </v-col>
        </v-row>
    </v-container>

    <NoteEdit v-model="editNote" :noteProp="selectedNote" @noteEdited="fetchNote" />

    <ConfirmationDialog v-model="confirmDialog" :title="varConfirmDelete.title" :text="varConfirmDelete.text" @confirmDelete="deleteNotes(selectedNoteId)" />

    <Notification :userMsg="userMsg" />
</template>

<script setup>
import { ref, watch } from 'vue'
import { fetchNotes, deleteNote } from '@/services/noteService'

// Variables
const confirmDialog = ref(false)
const editNote = ref(false)
const selectedNote = ref(null)
const selectedNoteId = ref(null)
const expandedCards = ref({})
const notes = ref([])
const userMsg = ref({
    message: '',
    visible: false,
    color: ''
})
const varConfirmDelete = ref({
    title: 'Notiz löschen',
    text: 'Willst du die Notiz wirklich löschen?'
})

// Imports/Exports
const props = defineProps({
    filterCategory: Number,
    refreshView: Boolean
})
const emit = defineEmits([
    'refreshDone'
])

// Data Processing
const fetchNote = async (categoryId) => {
    const { responseData, error } = await fetchNotes(categoryId)
    if (responseData.value != null) {
        notes.value = responseData.value.data
        notes.value.forEach(note => {
            if (!note.noteText) {
                note.noteText = ''
            }

            if (!note.noteTitle) {
                note.noteTitle = ''
            }

            if (!expandedCards.value[note.id]) {
                let slicePosition = 180
                let searchSlices = []

                searchSlices.push(note.noteText.indexOf('\n'))
                searchSlices.push(note.noteText.indexOf('\n', searchSlices[0] + 1))
                searchSlices.push(note.noteText.indexOf('\n', searchSlices[1] + 1))

                for (let i = 0; i < searchSlices.length; i++) {
                    if (searchSlices[i] === -1 || searchSlices[i] > 180) break
                    slicePosition = searchSlices[i]
                }

                expandedCards.value[note.id] = {
                    expand: false,
                    cardHeight: 150,
                    cardIcon: 'mdi-chevron-down',
                    textSlice: slicePosition
                }
            }
        })
    } else if (error.value != null) {
        if (error.value.message !== 'Request failed with status code 404') {
            userMsg.value = {
                message: 'Notizen konnten nicht geladen werden.',
                visible: true,
                color: 'error'
            }
        }

        notes.value = []
    }
}

const deleteNotes = async (noteId) => {
    const { error } = await deleteNote(noteId)
    if (error.value != null) {
        userMsg.value = {
            message: 'Notiz konnte nicht gelöscht werden.',
            visible: true,
            color: 'error'
        }
    } else {
        fetchNote(props.filterCategory)
        userMsg.value = {
            message: 'Gelöscht.',
            visible: true,
            color: 'success'
        }
    }
}

// Methodes
const openConfirmDialog = (noteId) => {
    selectedNoteId.value = noteId
    confirmDialog.value = true
}

const openNoteEdit = (note) => {
    selectedNote.value = note
    editNote.value = true
}

const expandCard = (noteId) => {
    expandedCards.value[noteId].expand = !expandedCards.value[noteId].expand
    expandedCards.value[noteId].cardHeight = expandedCards.value[noteId].expand ? undefined : 150
    expandedCards.value[noteId].cardIcon = expandedCards.value[noteId].expand ? 'mdi-chevron-up' : 'mdi-chevron-down'
}

watch(() => props.filterCategory, (newValue) => {
    if (newValue !== undefined) {
        fetchNote(newValue)
    } else {
        notes.value = []
    }
})

watch(() => props.refreshView, (newValue) => {
    if (newValue) {
        fetchNote(props.filterCategory)
        emit('refreshDone', false)
    }
})
</script>

<style scoped>
.custom-text-gradient {
    background-image: linear-gradient(rgb(0, 0, 0), rgb(var(--v-theme-info)));
    color: transparent;
    background-clip: text;
}
</style>