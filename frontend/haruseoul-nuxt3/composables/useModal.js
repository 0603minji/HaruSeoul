// composables/useModal.js
import { reactive } from 'vue';

export function useModal() {
    const modalStates = reactive({});

    const openModal = (id) => {
        modalStates[id] = true;
        // console.log('       컴포저블 useModal, openModal called. modalStates: ', modalStates )
    };

    const closeModal = (id) => {
        modalStates[id] = false;
    };

    const isModalVisible = (id) => modalStates[id] || false;

    return {
        isModalVisible,
        openModal,
        closeModal,
    };
}