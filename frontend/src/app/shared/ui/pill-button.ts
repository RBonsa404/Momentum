import { Component, Input } from '@angular/core';

@Component({
  selector: 'ui-pill-button',
  standalone: true,
  template: `
    <button
      type="button"
      class="rounded-full px-5 py-2.5 text-sm font-semibold transition duration-200 active:scale-95"
      [class.accent-grad]="variant === 'primary'"
      [class.text-white]="variant === 'primary'"
      [class.glow]="variant === 'primary'"
      [class.glass]="variant === 'ghost'"
      [class.text-cream]="variant === 'ghost'">
      <ng-content />
    </button>
  `
})
export class PillButtonComponent {
  @Input() variant: 'primary' | 'ghost' = 'primary';
}
