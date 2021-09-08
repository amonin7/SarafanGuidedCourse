<template>
    <v-app>
        <v-app-bar app>
            <v-toolbar-title>Sarafan</v-toolbar-title>
            <v-btn text
                   v-if="profile"
                   :disabled="$route.path === '/'"
                   @click="showMessages">
                Messages
            </v-btn>
            <v-spacer></v-spacer>
            <v-btn text
                   v-if="profile"
                   :disabled="$route.path === '/profile'"
                   @click="showProfile">
                {{profile.name}}&nbsp;
            </v-btn>
             <v-btn v-if="profile" icon href="/logout">
                <v-icon>{{ logout }}</v-icon>
            </v-btn>

        </v-app-bar>
        <v-main>
            <router-view></router-view>
        </v-main>
    </v-app>

</template>

<script>
    import { mapState, mapMutations } from 'vuex'
    import {addHandler} from "util/ws"
    import { mdiExitToApp } from '@mdi/js'

    export default {
        computed: mapState(['profile', 'messages']),
        methods: {
            ...mapMutations(['addMessageMutation', 'updateMessageMutation', 'removeMessageMutation']),
            showMessages() {
                this.$router.push('/')
            },
            showProfile() {
                this.$router.push('/profile')
            }
        },
        data() {
            return {
                logout: mdiExitToApp,
            }
        },
        created() {
            addHandler(data => {
                if (data.objectType === 'MESSAGE') {
                    switch (data.eventType) {
                        case 'CREATE':
                            this.addMessageMutation(data.body)
                        case 'UPDATE':
                            this.updateMessageMutation(data.body)
                            break
                        case 'REMOVE':
                            this.removeMessageMutation(data.body)
                            break
                        default:
                            console.error('Looks like the event type is unknown "${data.eventType}"')
                    }
                } else {
                    console.error('Looks like the object type is unknown "${data.objectType}"')
                }
            })
        },
        beforeMount() {
            if(!this.profile) {
                this.$router.replace('/auth')
            }
        }

    }
</script>

<style>
    
</style>
