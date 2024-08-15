<template>
    <v-dialog activator="parent" max-width="340">
        <template v-slot:default="{ isActive }">
            <v-card title="Kategorie bearbeiten" color="primary">
                <v-divider thickness="2" />
                <v-card-text class="pb-0">
                    <v-row dense>
                        <v-col cols="auto">
                            <v-select v-model="select" :items="icons" label="Icon" item-title="name" variant="outlined" width="80px">
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
                            <v-text-field v-model="categoryName" label="Name*" variant="outlined" clearable :rules="[required]" autofocus />
                        </v-col>
                    </v-row>
                </v-card-text>

                <v-card-actions class="px-6">
                    <v-btn variant="plain" @click.stop="isActive.value = false">Abbrechen</v-btn>
                    <v-btn variant="tonal" @click.stop="editSection(props.categoryProp.id), isActive.value = false" :disabled="!isValidForm">Speichern</v-btn>
                </v-card-actions>
            </v-card>
        </template>
    </v-dialog>

    <Notification :userMsg="userMsg" />
</template>

<script setup>
import { ref, onMounted, computed, onUpdated } from 'vue'
import { fetchIcons, editCategory } from '@/services/noteService'

// Variables
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
    categoryProp: Object
})
const emit = defineEmits([
    'categoryEdited'
])

// Computed
const isValidForm = computed(() => !!categoryName.value)

// Data Processing
const fetchIcon = async () => {
    const { responseData, error } = await fetchIcons()
    if (responseData.value != null) {
        icons.value = responseData.value.data
        const i = icons.value.findIndex(icon => icon.name === props.categoryProp.prependIcon)
        select.value = icons.value[i].name
    } else if (error.value != null) {
        userMsg.value = {
            message: 'Fehler beim Laden der Icons.',
            visible: true,
            color: 'error'
        }
    }
}

const editSection = async (id) => {
    const category = {
        name: categoryName.value,
        prependIcon: select.value
    }
    const { error } = await editCategory(id, category)
    if (error.value != null) {
        userMsg.value = {
            message: 'Fehler beim Speichern der Kategorie.',
            visible: true,
            color: 'error'
        }
    } else {
        emit('categoryEdited')
        userMsg.value = {
            message: 'Abgespeichert',
            visible: true,
            color: 'success'
        }
    }
}

// Methods
onMounted(fetchIcon)

onUpdated(() => {
    categoryName.value = props.categoryProp.name
})

const required = value => !!value || 'Bitte einen Namen eingeben.'
</script>