<template>
  <div class="radar-chart" ref="chartContainer"></div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import * as d3 from 'd3'

interface AnalysisScores {
  pores: number
  blackheads: number
  pigmentation: number
  wrinkles: number
  porphyrin: number
  sensitivity: number
  darkCircles: number
}

interface Props {
  scores?: Partial<AnalysisScores>
  width?: number
  height?: number
  animationDuration?: number
}

const props = withDefaults(defineProps<Props>(), {
  scores: () => ({
    pores: 70,
    blackheads: 60,
    pigmentation: 50,
    wrinkles: 65,
    porphyrin: 55,
    sensitivity: 75,
    darkCircles: 45,
  }),
  width: 106,
  height: 103,
  animationDuration: 1000,
})

const chartContainer = ref<HTMLElement | null>(null)
let svg: d3.Selection<SVGSVGElement, unknown, null, undefined> | null = null
let pathElement: d3.Selection<SVGPathElement, unknown, null, undefined> | null = null
let circleElements: d3.Selection<SVGCircleElement, unknown, null, undefined>[] = []

const drawChart = (animate = true) => {
  if (!chartContainer.value) return

  const width = props.width
  const height = props.height
  const centerX = width / 2
  const centerY = height / 2
  const radius = Math.min(width, height) / 2 - 5
  const maxValue = 100
  const numCircles = 5

  if (!svg) {
    d3.select(chartContainer.value).selectAll('*').remove()

    svg = d3
      .select(chartContainer.value)
      .append('svg')
      .attr('width', width)
      .attr('height', height)
      .attr('viewBox', `0 0 ${width} ${height}`)
      .attr('fill', 'none')

    for (let i = 1; i <= numCircles; i++) {
      const circleRadius = (radius * i) / numCircles
      svg
        .append('circle')
        .attr('cx', centerX)
        .attr('cy', centerY)
        .attr('r', circleRadius)
        .attr('stroke', '#D1D5DB')
        .attr('stroke-width', 0.5125)
        .attr('fill', 'none')
    }

    const numAxes = 6
    const axes = [
      { name: 'pores', angle: -Math.PI / 2 },
      { name: 'redness', angle: -Math.PI / 2 + (Math.PI * 2) / numAxes },
      { name: 'blackheads', angle: -Math.PI / 2 + (Math.PI * 2 * 2) / numAxes },
      { name: 'keratin', angle: -Math.PI / 2 + (Math.PI * 2 * 3) / numAxes },
      { name: 'darkCircles', angle: -Math.PI / 2 + (Math.PI * 2 * 4) / numAxes },
      { name: 'porphyrin', angle: -Math.PI / 2 + (Math.PI * 2 * 5) / numAxes },
    ]

    axes.forEach((axis) => {
      const x = centerX + radius * Math.cos(axis.angle)
      const y = centerY + radius * Math.sin(axis.angle)

      svg!
        .append('line')
        .attr('x1', centerX)
        .attr('y1', centerY)
        .attr('x2', x)
        .attr('y2', y)
        .attr('stroke', '#D1D5DB')
        .attr('stroke-width', 0.5125)
    })

    pathElement = svg.append('path').attr('fill', '#D4B896').attr('fill-opacity', 0.3).attr('stroke', '#B59A79').attr('stroke-width', 1.025)

    circleElements = axes.map(() => {
      return svg!.append('circle').attr('r', 1.5).attr('fill', '#B59A79')
    })
  }

  const numAxes = 6
  const axes = [
    { name: 'pores', angle: -Math.PI / 2 },
    { name: 'redness', angle: -Math.PI / 2 + (Math.PI * 2) / numAxes },
    { name: 'blackheads', angle: -Math.PI / 2 + (Math.PI * 2 * 2) / numAxes },
    { name: 'keratin', angle: -Math.PI / 2 + (Math.PI * 2 * 3) / numAxes },
    { name: 'darkCircles', angle: -Math.PI / 2 + (Math.PI * 2 * 4) / numAxes },
    { name: 'porphyrin', angle: -Math.PI / 2 + (Math.PI * 2 * 5) / numAxes },
  ]

  const finalDataPoints = axes.map((axis) => {
    const value = props.scores?.[axis.name as keyof AnalysisScores] || 0
    const normalizedValue = value / maxValue
    return {
      x: centerX + radius * normalizedValue * Math.cos(axis.angle),
      y: centerY + radius * normalizedValue * Math.sin(axis.angle),
      value,
      angle: axis.angle,
    }
  })

  const initialDataPoints = axes.map((axis) => ({
    x: centerX,
    y: centerY,
    value: 0,
    angle: axis.angle,
  }))

  const line = d3
    .line<{ x: number; y: number }>()
    .x((d) => d.x)
    .y((d) => d.y)
    .curve(d3.curveLinearClosed)

  if (animate) {
    const initialPathData = [...initialDataPoints, initialDataPoints[0]]
    const finalPathData = [...finalDataPoints, finalDataPoints[0]]

    pathElement!.datum(initialPathData).attr('d', line)

    pathElement!
      .transition()
      .duration(props.animationDuration)
      .ease(d3.easeCubicOut)
      .attrTween('d', function () {
        const interpolate = d3.interpolateArray(initialPathData, finalPathData)
        return (t: number) => {
          const currentData = interpolate(t)
          return line(currentData) || ''
        }
      })

    circleElements.forEach((circle, index) => {
      const initialPoint = initialDataPoints[index]
      const finalPoint = finalDataPoints[index]

      circle.attr('cx', initialPoint.x).attr('cy', initialPoint.y)

      circle
        .transition()
        .duration(props.animationDuration)
        .ease(d3.easeCubicOut)
        .attr('cx', finalPoint.x)
        .attr('cy', finalPoint.y)
    })
  } else {
    const finalPathData = [...finalDataPoints, finalDataPoints[0]]
    pathElement!.datum(finalPathData).attr('d', line)

    circleElements.forEach((circle, index) => {
      const point = finalDataPoints[index]
      circle.attr('cx', point.x).attr('cy', point.y)
    })
  }
}

onMounted(() => {
  setTimeout(() => {
    drawChart(true)
  }, 100)
})

watch(
  () => props.scores,
  () => {
    drawChart(true)
  },
  { deep: true }
)
</script>

<style scoped>
.radar-chart {
  overflow: hidden;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
}
</style>
