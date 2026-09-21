import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  selector: 'ui-pill-nav',
  standalone: true,
  imports: [RouterLink, RouterLinkActive],
  template: `
    <nav class="fixed bottom-4 left-1/2 -translate-x-1/2 z-30 flex max-w-[95vw] items-center gap-1 overflow-x-auto rounded-full border border-white/10 bg-black/80 p-1.5 backdrop-blur-2xl shadow-2xl md:relative md:bottom-auto md:left-auto md:translate-x-0 md:w-max">
      @for (item of items; track item.path) {
        <a
          [routerLink]="item.path"
          routerLinkActive="!bg-ember !text-white shadow-[0_0_24px_rgba(255,69,0,0.45)]"
          [routerLinkActiveOptions]="{ exact: item.path === '/' }"
          class="whitespace-nowrap rounded-full px-3 py-1.5 sm:px-4 sm:py-2 text-xs sm:text-sm text-mute transition duration-300 hover:text-cream">
          {{ item.label }}
        </a>
      }
    </nav>
  `
})
export class PillNavComponent {
  readonly items = [
    { path: '/', label: 'Dashboard' },
    { path: '/planning', label: 'Agenda' },
    { path: '/journal', label: 'Récap' },
    { path: '/streak', label: 'Streak' },
    { path: '/goals', label: 'Objectifs' }
  ];
}
