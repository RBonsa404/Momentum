import { Component, Input } from '@angular/core';

@Component({
  selector: 'ui-floating-badge',
  standalone: true,
  template: `
    <span
      class="float-badge inline-flex items-center gap-2 rounded-full border border-white/10 bg-white/5 px-3 py-1 text-xs text-cream backdrop-blur"
      [style.animationDelay]="delay">
      <span class="h-1.5 w-1.5 rounded-full bg-ember"></span>
      {{ label }}
    </span>
  `
})
export class FloatingBadgeComponent {
  @Input() label = '';
  @Input() delay = '0s';
}
