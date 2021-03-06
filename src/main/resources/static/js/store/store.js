import Vue from 'vue'
import Vuex from 'vuex'
import messageApi from 'api/messages'
import commentApi from 'api/comment'

Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        messages,
        profile: frontendData.profile,
    },
    getters: {
        sortedMessages: state => (state.messages || {}).sort((a, b) => -(a.id - b.id))
    },
    mutations: {
        addMessageMutation(state, message) {
            state.messages = [
                ...state.messages,
                message
            ]
        },
        updateMessageMutation(state, message) {
            const updateIndex = state.messages.findIndex(item => item.id === message.id)

            state.messages = [
                ...state.messages.slice(0, updateIndex),
                message,
                ...state.messages.slice(updateIndex + 1)
            ]
        },
        removeMessageMutation(state, message) {
            const deleteIndex = state.messages.findIndex(item => item.id === message.id)

            if (deleteIndex > -1) {
                state.messages = [
                    ...state.messages.slice(0, deleteIndex),
                    ...state.messages.slice(deleteIndex + 1)
                ]
            }
        },
        addCommentMutation(state, comment) {
            const updateIndex = state.messages.findIndex(item => item.id === comment.message.id)
            const message = state.messages[updateIndex]

            state.messages = [
                ...state.messages.slice(0, updateIndex),
                {
                    ...message,
                    comments: [
                        ...message.comments,
                        comment
                    ]
                },
                ...state.messages.slice(updateIndex + 1)
            ]
        }
    },
    actions: {
        async addMessageAction({commit, state}, message) {
            const result = await messageApi.add(message)
            const data = await result.json()
            const index = state.messages.findIndex(item => item.id === data.id)

            if (index > -1) {
                commit('updateMessageMutation', data)
            } else {
                commit('addMessageMutation', data)
            }
        },
        async updateMessageAction({commit}, message) {
            const result = await messageApi.update(message)
            const data = await result.json()
            commit('updateMessageMutation', data)
        },
        async removeMessageAction({commit}, message) {
            const result = await messageApi.remove(message.id)
            if (result.ok) {
                commit('removeMessageMutation', message)
            }
        },
        async addCommentAction({commit, state}, comment) {
            const response = await commentApi.add(comment)
            const data = await response.json()
            commit('addCommentMutation', comment)
        }
    }
})
