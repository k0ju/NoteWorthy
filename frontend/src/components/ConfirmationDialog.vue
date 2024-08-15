<template>
  <v-dialog :model-value="props.modelValue" max-width="610" @after-leave="close">
    <v-card color="primary">
      <v-card-title class="px-6 py-4">{{ title }}</v-card-title>
      <v-divider thickness="2" />
      <v-card-text class="px-6 pb-2">
        <p>{{ text }}</p>
        <br>
        <p class="custom-text-color">Diese Aktion kann nicht rückgängig gemacht werden!</p>
      </v-card-text>

      <v-card-actions class="px-6">
        <v-btn variant="plain" @click.stop="close">Abbrechen</v-btn>
        <v-btn variant="tonal" @click.stop="confirmDelete">Löschen</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
// Imports/Exports
const props = defineProps({
  modelValue: Boolean,
  title: String,
  text: String
})
const emit = defineEmits([
  'confirmDelete',
  'update:modelValue'
])

// Methods
const confirmDelete = () => {
  emit('confirmDelete')
  emit('update:modelValue', false)
}

const close = () => {
  emit('update:modelValue', false)
}
</script>

<style scoped>
.custom-text-color {
  color: rgb(var(--v-theme-error))
}
</style>