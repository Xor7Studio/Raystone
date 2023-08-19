<template>
  <div>
    <q-card :class="$q.dark.isActive?'bg-dark':''" style="background-color: #FAFAFA">
      <q-card-section class="text-h6">
        Line Chart
      </q-card-section>
      <q-card-section>
        <ECharts ref="barchart"
                 :option="options"
                 class="q-mt-md"
                 :resizable="true"
                 autoresize style="height: 250px;"
        />
      </q-card-section>
    </q-card>
  </div>
</template>

<script>
import ECharts from 'vue-echarts'
import {useQuasar} from 'quasar';

async function updateData() {

}
export default {
  name: 'ChartCard',
  components: {
    ECharts
  },
  data() {
    const $q = useQuasar()
    const xAxisData = []
    const yAxisData = []

    return {
      $q,
      xAxisData,
      yAxisData,
      model: false,
      options: {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'line'
          },
          formatter: function (params) {
            return `Time: ${params[0].axisValue}</br>Used: ${params[0].data}`
          }
        },
        xAxis: {
          type: 'category',
          data: [],
          boundaryGap: false,
          axisTick: false,
          axisLine: false,
          axisLabel: false
        },
        yAxis: {
          type: 'value',
          splitLine: false,
          axisLine: false,
          axisTick: false,
          axisLabel: false,
        },
        lineStyle: {
          color: '#0E5FFF'
        },
        series: [
          {
            symbol: 'none',
            data: [],
            type: 'line',
            smooth: true,
          },
        ],
      },
      line_chart: null
    }
  }
}
</script>

<style scoped>

</style>
