<template>
  <div class="fullscreen bg-blue text-white text-center q-pa-md flex flex-center">
    <div>
      <div style="font-size: 128px">
        {{ $t('AuthPage.signIn') }}
      </div>
      <q-btn class="q-mt-xl"
             color="white"
             text-color="blue"
             unelevated
             to="/overview"
             no-caps>
        <q-icon left size="3em">
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 108 108" fill="none">
            <path d="M51.63 6H6V51.63H51.63V6Z" fill="#F25022"/>
            <path d="M102 6H56.3699V51.63H102V6Z" fill="#7FBA00"/>
            <path d="M51.63 56.3699H6V102H51.63V56.3699Z" fill="#00A4EF"/>
            <path d="M102 56.3699H56.3699V102H102V56.3699Z" fill="#FFB900"/>
          </svg>
        </q-icon>
        <div class="text-weight-bold">Microsoft</div>
      </q-btn>
    </div>
  </div>
</template>

<script lang="ts">
import {defineComponent} from 'vue';


export default defineComponent({
  name: 'IndexPage',
  setup() {
    return {};
  }
});
</script>
