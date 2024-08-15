<template>
    <v-btn v-if="$vuetify.display.mobile == false" class="text-primary" block aria-label="Kategorie hinzfügen" @click.stop="dialog = true">{{ isNavButtonPressed ? '+' : 'Kategorie hinzufügen' }}</v-btn>
    <v-btn v-else class="text-primary" block @click.stop="dialog = true">Kategorie hinzufügen</v-btn>

    <v-dialog max-width="340" v-model="dialog">
        <v-card title="Kategorie hinzufügen" color="primary">
            <v-divider thickness="2" />
            <v-card-text class="pb-0">
                <v-row dense>
                    <v-col cols="auto">
                        <v-select v-model="select" :items="icons" label="Icon" item-title="name" variant="outlined" width="80px" autofocus>
                            <template v-slot:item="{ props, item }">
                                <v-list-item v-bind="props" title="" class="text-primary">
                                    <v-icon>{{ item.raw.name }}</v-icon>
                                </v-list-item>
                            </template>
                            <template v-slot:selection="{ item }">
                                <v-icon>{{ item.raw.name }}</v-icon>
                            </template>
                        </v-select>
                    </v-col>
                    <v-col cols="8,1">
                        <v-text-field v-model="categoryName" label="Name*" variant="outlined" clearable :rules="[required]" />
                    </v-col>
                </v-row>
            </v-card-text>

            <v-card-actions class="px-6">
                <v-btn variant="plain" @click.stop="dialog = false">Abbrechen</v-btn>
                <v-btn variant="tonal" @click.stop="saveSection" :disabled="!isValidForm">Speichern</v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>

    <Notification :userMsg="userMsg" />
</template>

<script setup>
import { ref, onMounted, computed, watch } from 'vue'
import { fetchIcons, saveCategory } from '@/services/noteService'

// Variables
const dialog = ref(false)
const select = ref()
const categoryName = ref()
const icons = ref([])
const userMsg = ref({
    message: '',
    visible: false,
    color: ''
})

// Imports/Exports
const props = defineProps({
    isNavButtonPressed: Boolean
})
const emit = defineEmits([
    'categorySaved'
])

// Computed
const isValidForm = computed(() => !!categoryName.value)

// Data Processing
const fetchIcon = async () => {
    const { responseData, error } = await fetchIcons()
    if (responseData.value != null) {
        icons.value = responseData.value.data
        select.value = icons.value[0].name
    } else if (error.value != null) {
        userMsg.value = {
            message: 'Fehler beim Laden der Icons.',
            visible: true,
            color: 'error'
        }
    }
}

const saveSection = async () => {
    const category = {
        name: categoryName.value,
        prependIcon: select.value,
        appendIcon1: 'mdi-pencil',
        appendIcon2: 'mdi-close'
    }
    const { error } = await saveCategory(category)
    if (error.value != null) {
        userMsg.value = {
            message: 'Fehler beim Speichern der Kategorie.',
            visible: true,
            color: 'error'
        }
    } else {
        emit('categorySaved')
        dialog.value = false
        categoryName.value = '' //TODO: clean reset with form
        select.value = icons.value[0].name //TODO: clean reset with form
        userMsg.value = {
            message: 'Abgespeichert',
            visible: true,
            color: 'success'
        }
    }
}

// Methods
onMounted(fetchIcon)

watch(() => dialog.value, (newValue) => {
    if (newValue && icons.value.length < 1) {
        userMsg.value = {
            message: 'Icons konnten nicht geladen werden. Bitte Datenbankverbindung prüfen.',
            visible: true,
            color: 'error'
        }
        dialog.value = false
    }
})

const required = value => !!value || 'Bitte einen Namen eingeben.'
</script>