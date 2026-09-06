import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  selector: 'ui-pill-nav',
  standalone: true,
  imports: [RouterLink, RouterLinkActive],
  template: `
    <nav class="mx-auto flex w-max items-center gap-1 rounded-full border border-white/10 bg-black/50 p-1.5 backdrop-blur-xl">
      @for (item of items; track item.path) {
        <a
          [routerLink]="item.path"
          routerLinkActive="!bg-ember !text-white shadow-[0_0_24px_rgba(255,69,0,0.45)]"
          [routerLinkActiveOptions]="{ exact: item.path === '/' }"
          class="rounded-full px-4 py-2 text-sm text-mute transition duration-300 hover:text-cream">
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
