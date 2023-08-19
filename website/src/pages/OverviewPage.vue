<template>
    <q-page class="items-center justify-evenly full-width q-pa-lg">
      <div class="row">
        <q-card :class="$q.dark.isActive?'bg-dark':''" style="background-color: #FAFAFA">
          <q-card-section class="text-h6">
            Line Chart
          </q-card-section>
        </q-card>
      </div>
      <div class="row">
        <div class="col-12 col-md q-mt-lg">
          <chart-card/>
        </div>
        <div class="card-space"></div>
        <div class="col-12 col-md q-mt-lg">
          <chart-card/>
        </div>
      </div>
    </q-page>

</template>

<script>
import {defineComponent} from 'vue';
import ChartCard from 'components/ChartCard.vue';

export default defineComponent({
  name: 'OverviewPage',
  components: {ChartCard},
  setup() {
    return {};
  }
});
</script>
<style lang="sass">
.chart-container
  display: flex
  flex: 1
  padding: 20px

.card-space
  width: 24px
  height: 0
  display: block

@media (max-width: 767px)
  .chart-page
    flex-direction: column

@media (max-width: $breakpoint-md-min)
  .card-space
    display: none

</style>
