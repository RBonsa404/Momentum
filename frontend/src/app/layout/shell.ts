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
    <header class="sticky top-0 z-20 flex items-center justify-between px-4 sm:px-6 py-4 sm:py-5 backdrop-blur-md bg-black/20">
      <p class="text-xs sm:text-sm font-semibold tracking-[0.3em] text-mute">MOMENTUM</p>
      <div class="hidden md:block">
        <ui-pill-nav />
      </div>
      <button class="text-xs text-mute hover:text-cream px-3 py-1.5 rounded-full border border-white/10 bg-white/5 active:scale-95 transition-all" type="button" (click)="auth.logout()">Quitter</button>
    </header>
    
    <!-- Mobile Bottom Navigation Bar -->
    <div class="md:hidden">
      <ui-pill-nav />
    </div>

    <main class="route-shell mx-auto max-w-6xl px-4 sm:px-6 pt-4 pb-24 md:pb-16">
      <router-outlet />
    </main>
  `
})
export class ShellComponent {
  constructor(readonly auth: AuthStore) {}
}
