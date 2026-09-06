import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HaloBackgroundComponent } from '../shared/ui/halo-background';
import { PillNavComponent } from '../shared/ui/pill-nav';
import { AuthStore } from '../core/auth.store';

@Component({
  selector: 'app-shell',
  standalone: true,
  imports: [RouterOutlet, HaloBackgroundComponent, PillNavComponent],
  template: `
    <ui-halo-background />
    <header class="sticky top-0 z-20 flex items-center justify-between px-6 py-5">
      <p class="text-sm font-semibold tracking-[0.3em] text-mute">MOMENTUM</p>
      <ui-pill-nav />
      <button class="text-xs text-mute hover:text-cream" type="button" (click)="auth.logout()">Quitter</button>
    </header>
    <main class="route-shell mx-auto max-w-6xl px-6 pb-16">
      <router-outlet />
    </main>
  `
})
export class ShellComponent {
  constructor(readonly auth: AuthStore) {}
}
