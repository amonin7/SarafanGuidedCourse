<template>
    <v-layout row>
        <v-text-field
                label="New message"
                placeholder="Write some text"
                v-model="text"
                @keyup.enter="save"
        />
        <v-btn @click="save" class="my-3">
            Save
        </v-btn>
    </v-layout>
</template>

<script>
    import {mapActions} from 'vuex'

    export default {
        props: ['messageAttr'],
        data: function () {
            return {
                text: '',
                id: ''
            }
        },
        watch: {
            messageAttr(newVal, oldVal) {
                this.text = newVal.text
                this.id = newVal.id
            }
        },
        methods: {
            ...mapActions(['addMessageAction', 'updateMessageAction']),
            save() {
                const message = {
                    id: this.id,
                    text: this.text
                }
                if (this.id) {
                    this.updateMessageAction(message)
                } else {
                    this.addMessageAction(message)
                }
                this.text = ''
                this.id = ''

            }
        }
    }
</script>

<style>

</style>
