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

export default {
  name: 'ChartCard',
  components: {
    ECharts
  },
  data() {
    const $q = useQuasar()

    return {
      $q,
      model: false,
      options: {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'line'
          },
          formatter: function (params) {
            return `${params[0].axisValue}</br>${params[0].data}`
          }
        },
        xAxis: {
          type: 'category',
          data: ['1:00', '2:00', '3:00', '4:00', '5:00', '6:00', '7:00', '8:00', '9:00', '10:00', '11:00', '12:00'],
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
            data: [
              40, 250, 100, 300, 280, 600, 300, 250, 180, 500, 400, 260,
            ],
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
<!--<template>-->
<!--  <q-card>-->
<!--    <div ref="container" style="width: 100%;height: 100%" class="flex flex-center"></div>-->
<!--  </q-card>-->
<!--</template>-->

<!--<script>-->
<!--import * as echarts from 'echarts';-->

<!--export default {-->
<!--  setup() {-->
<!--    return {-->
<!--    }-->
<!--  },-->
<!--  mounted() {-->
<!--    this.setupChart();-->
<!--  },-->
<!--  methods: {-->
<!--    setupChart() {-->
<!--      let chart = echarts.init(this.$refs.container, null, {renderer: 'svg'})-->
<!--      chart.setOption(-->
<!--          {-->
<!--            tooltip: {-->
<!--              trigger: 'axis',-->
<!--              axisPointer: {-->
<!--                type: 'shadow'-->
<!--              },-->
<!--              formatter: function (params) {-->
<!--                return `${params[0].axisValue}</br>${params[0].data}`-->
<!--              }-->
<!--            },-->
<!--            xAxis: {-->
<!--              type: 'category',-->
<!--              data: ['1:00', '2:00', '3:00', '4:00', '5:00', '6:00', '7:00', '8:00', '9:00', '10:00', '11:00', '12:00'],-->
<!--              boundaryGap: false,-->
<!--              axisTick: false,-->
<!--              axisLine: false,-->
<!--              axisLabel: false-->
<!--            },-->
<!--            yAxis: {-->
<!--              type: 'value',-->
<!--              splitLine: false,-->
<!--              axisLine: false,-->
<!--              axisTick: false,-->
<!--              axisLabel: false,-->
<!--            },-->
<!--            lineStyle: {-->
<!--              color: '#0E5FFF'-->
<!--            },-->
<!--            series: [-->
<!--              {-->
<!--                symbol: 'none',-->
<!--                data: [-->
<!--                  40, 250, 100, 300, 280, 600, 300, 250, 180, 500, 400, 260,-->
<!--                ],-->
<!--                type: 'line',-->
<!--                smooth: true,-->
<!--              },-->
<!--            ],-->
<!--          },-->
<!--          true-->
<!--      )-->

<!--    },-->
<!--  }-->
<!--}-->
<!--</script>-->

<!--<style>-->
<!--</style>-->
