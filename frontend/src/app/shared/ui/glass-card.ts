import { Component, Input } from '@angular/core';

@Component({
  selector: 'ui-glass-card',
  standalone: true,
  template: `
    <section class="glass p-6 transition duration-300 hover:scale-[1.015] hover:shadow-[0_0_80px_rgba(255,69,0,0.25)]">
      @if (title) {
        <h3 class="mb-3 text-lg font-semibold tracking-tight">{{ title }}</h3>
      }
      <ng-content />
    </section>
  `
})
export class GlassCardComponent {
  @Input() title = '';
}
