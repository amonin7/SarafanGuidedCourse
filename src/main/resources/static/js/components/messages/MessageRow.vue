<template>
    <v-card class="my-2">
        <v-card-text primary-title>
            <i>({{ message.id }})</i>
            {{ message.text }}
        </v-card-text>
        <v-card-actions>
            <v-btn value="Edit" @click="edit" small text rounded>Edit</v-btn>
            <v-btn icon @click="del" small>
                <v-icon>{{delButton}}</v-icon>
            </v-btn>
        </v-card-actions>
        <comment-list
                :comments="message.comments"
                :messageId="message.id"
        ></comment-list>
    </v-card>
</template>

<script>
    import {mapActions} from 'vuex'
    import { mdiDelete } from '@mdi/js'
    import CommentList from '../comment/CommentList.vue'

    export default {
        props: ['message', 'editMessage'],
        components: { CommentList },
        data() {
            return {
                delButton: mdiDelete
            }
        },
        methods: {
            ...mapActions(['removeMessageAction']),
            edit() {
                this.editMessage(this.message)
            },
            del() {
                this.removeMessageAction(this.message)
            }
        }
    }
</script>

<style>
    
</style>
