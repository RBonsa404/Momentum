import { AfterViewInit, Component, ElementRef, OnDestroy, ViewChild, signal } from '@angular/core';
import ApexCharts from 'apexcharts';
import { ApiService, DashboardDto } from '../../core/api';
import { GlassCardComponent } from '../../shared/ui/glass-card';
import { FloatingBadgeComponent } from '../../shared/ui/floating-badge';
import { cascadeIn } from '../../shared/animations/gsap';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [GlassCardComponent, FloatingBadgeComponent],
  template: `
    <div class="relative py-8 text-center">
      <div class="mb-6 flex flex-wrap justify-center gap-3">
        <ui-floating-badge label="Focus" delay="0s" />
        <ui-floating-badge label="Deep work" delay="0.4s" />
        <ui-floating-badge label="Récap" delay="0.8s" />
      </div>
      <h1 class="hero-title">Ta trajectoire.</h1>
      <p class="mt-3 text-mute">Score, régularité et procrastination — filtrés dans le temps.</p>
      <div class="mt-6 inline-flex rounded-full border border-white/10 bg-black/40 p-1">
        @for (p of periods; track p) {
          <button
            class="rounded-full px-4 py-1.5 text-sm"
            [class.accent-grad]="period() === p"
            [class.text-white]="period() === p"
            (click)="load(p)">
            {{ p }}
          </button>
        }
      </div>
    </div>

    <div class="grid gap-5 md:grid-cols-4">
      <ui-glass-card title="Planifié">{{ data()?.planned ?? 0 }}</ui-glass-card>
      <ui-glass-card title="Fait">{{ data()?.done ?? 0 }}</ui-glass-card>
      <ui-glass-card title="Non fait">{{ data()?.unfinished ?? 0 }}</ui-glass-card>
      <ui-glass-card title="Procrastination">
        <span class="text-3xl font-bold text-ember">{{ data()?.procrastinationScore ?? 0 }}</span>
      </ui-glass-card>
    </div>

    <div class="mt-6 grid gap-5 md:grid-cols-2">
      <ui-glass-card title="Complétion">
        <div #chart class="h-64"></div>
      </ui-glass-card>
      <ui-glass-card title="Heatmap de régularité">
        <div class="grid grid-cols-7 gap-1.5">
          @for (cell of data()?.heatmap ?? []; track cell.dayDate) {
            <div
              class="aspect-square rounded-md"
              [style.background]="heat(cell.done, cell.planned)"
              [title]="cell.dayDate + ' · ' + cell.done + '/' + cell.planned"></div>
          }
        </div>
      </ui-glass-card>
    </div>
  `
})
export class DashboardPage implements AfterViewInit, OnDestroy {
  @ViewChild('chart') chartEl?: ElementRef<HTMLDivElement>;
  readonly periods = ['day', 'week', 'month'];
  readonly period = signal('week');
  readonly data = signal<DashboardDto | null>(null);
  private chart?: ApexCharts;

  constructor(private readonly api: ApiService) {}

  ngAfterViewInit(): void {
    this.load('week');
    cascadeIn('ui-glass-card');
  }

  ngOnDestroy(): void {
    this.chart?.destroy();
  }

  load(period: string): void {
    this.period.set(period);
    this.api.dashboard(period).subscribe({
      next: (dto) => {
        this.data.set(dto);
        this.renderChart(dto);
      },
      error: () => this.data.set(this.fallback())
    });
  }

  heat(done: number, planned: number): string {
    const ratio = planned === 0 ? 0 : done / planned;
    const alpha = 0.12 + ratio * 0.7;
    return `rgba(255,69,0,${alpha})`;
  }

  private renderChart(dto: DashboardDto): void {
    if (!this.chartEl) {
      return;
    }
    const series = (dto.heatmap ?? []).map((h) => (h.planned === 0 ? 0 : Math.round((100 * h.done) / h.planned)));
    const cats = (dto.heatmap ?? []).map((h) => h.dayDate);
    const options = {
      chart: {
        type: 'area',
        height: 240,
        toolbar: { show: false },
        animations: { enabled: true, speed: 1400 }
      },
      stroke: { curve: 'smooth', width: 3, colors: ['#FF4500'] },
      fill: {
        type: 'gradient',
        gradient: { shadeIntensity: 1, opacityFrom: 0.45, opacityTo: 0.05, colorStops: [
          { offset: 0, color: '#FF4500', opacity: 0.5 },
          { offset: 100, color: '#FF6B35', opacity: 0.02 }
        ] }
      },
      dataLabels: { enabled: false },
      grid: { borderColor: 'rgba(255,255,255,0.06)' },
      xaxis: { categories: cats.length ? cats : ['—'], labels: { style: { colors: '#9CA3AF' } } },
      yaxis: { max: 100, labels: { style: { colors: '#9CA3AF' } } },
      series: [{ name: 'Complétion %', data: series.length ? series : [0] }],
      tooltip: { theme: 'dark' }
    };
    this.chart?.destroy();
    this.chart = new ApexCharts(this.chartEl.nativeElement, options);
    void this.chart.render();
  }

  private fallback(): DashboardDto {
    return {
      from: '',
      to: '',
      planned: 0,
      done: 0,
      unfinished: 0,
      postponed: 0,
      missedJournals: 0,
      procrastinationScore: 0,
      completionRate: 0,
      heatmap: []
    };
  }
}
