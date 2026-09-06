import { Component, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ApiService, StreakDto, StreakRules } from '../../core/api';
import { GlassCardComponent } from '../../shared/ui/glass-card';
import { StreakOrbComponent } from '../../shared/ui/streak-orb';

@Component({
  selector: 'app-streak',
  standalone: true,
  imports: [FormsModule, GlassCardComponent, StreakOrbComponent],
  template: `
    <div class="py-8 text-center">
      <h1 class="hero-title">Streak</h1>
      <p class="mt-2 text-mute">Plus tu tiens, plus l’orbe s’embrase.</p>
    </div>
    <ui-streak-orb [length]="streak()?.currentLength ?? 0" />
    <div class="mt-6 grid gap-5 md:grid-cols-2">
      <ui-glass-card title="État">
        <p class="text-5xl font-extrabold">{{ streak()?.currentLength ?? 0 }}</p>
        <p class="text-sm text-mute">Record {{ streak()?.longestLength ?? 0 }} · {{ streak()?.status ?? '—' }}</p>
      </ui-glass-card>
      <ui-glass-card title="Règles / jokers">
        <form class="space-y-3" (ngSubmit)="saveRules()">
          <input class="w-full rounded-full bg-white/5 px-4 py-2 text-sm" [(ngModel)]="offWeekdays" name="off" placeholder="Jours off (SAT,SUN)" />
          <input class="w-full rounded-full bg-white/5 px-4 py-2 text-sm" type="number" [(ngModel)]="jokers" name="jokers" />
          <button class="accent-grad rounded-full px-5 py-2 text-sm font-semibold" type="submit">Enregistrer</button>
        </form>
      </ui-glass-card>
    </div>
  `
})
export class StreakPage implements OnInit {
  streak = signal<StreakDto | null>(null);
  offWeekdays = 'SAT,SUN';
  jokers = 2;

  constructor(private readonly api: ApiService) {}

  ngOnInit(): void {
    this.api.streak().subscribe({ next: (s) => this.streak.set(s), error: () => this.streak.set({ currentLength: 0, longestLength: 0, status: 'IDLE' }) });
    this.api.streakRules().subscribe({
      next: (r: StreakRules) => {
        this.offWeekdays = r.offWeekdays ?? '';
        this.jokers = r.jokersPerMonth ?? 2;
      },
      error: () => undefined
    });
  }

  saveRules(): void {
    this.api.updateRules(this.offWeekdays, this.jokers).subscribe();
  }
}
