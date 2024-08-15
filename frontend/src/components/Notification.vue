<template>
    <v-snackbar v-model="userMsg.visible" :color="userMsg.color" :timeout="5000" location="top">
        {{ userMsg.message }}

        <template v-slot:actions>
            <v-btn icon="mdi-close" variant="text" @click="userMsg.visible = false" />
        </template>
    </v-snackbar>
</template>

<script setup>
// Imports
const props = defineProps({
    userMsg: Object
})

//TODO: SnackbarQueue (currently in alpha state)
</script>