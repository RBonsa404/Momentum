import { Component, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ApiService, JournalEntry } from '../../core/api';
import { GlassCardComponent } from '../../shared/ui/glass-card';
import { Router } from '@angular/router';

@Component({
  selector: 'app-journal',
  standalone: true,
  imports: [FormsModule, GlassCardComponent],
  template: `
    <div class="py-8 text-center">
      <h1 class="hero-title">Récap du soir</h1>
      <p class="mt-2 text-mute"> triple check : victoires, friction, gratitude.</p>
    </div>
    <div class="grid gap-5 md:grid-cols-2">
      <ui-glass-card title="Template guidé">
        <form class="space-y-3" (ngSubmit)="submit()">
          <textarea class="w-full rounded-2xl bg-white/5 p-3 text-sm" rows="3" [(ngModel)]="wins" name="wins" placeholder="Victoires"></textarea>
          <textarea class="w-full rounded-2xl bg-white/5 p-3 text-sm" rows="3" [(ngModel)]="struggles" name="struggles" placeholder="Freins"></textarea>
          <textarea class="w-full rounded-2xl bg-white/5 p-3 text-sm" rows="2" [(ngModel)]="gratitude" name="gratitude" placeholder="Gratitude"></textarea>
          <label class="block text-xs text-mute">Humeur {{ mood }}</label>
          <input type="range" min="1" max="10" [(ngModel)]="mood" name="mood" class="w-full" />
          <label class="block text-xs text-mute">Énergie {{ energy }}</label>
          <input type="range" min="1" max="10" [(ngModel)]="energy" name="energy" class="w-full" />
          <input class="w-full rounded-full bg-white/5 px-4 py-2 text-sm" [(ngModel)]="tags" name="tags" placeholder="tags, séparés par des virgules" />
          <button class="accent-grad glow w-full rounded-full py-3 text-sm font-semibold" type="submit">Valider le récap</button>
        </form>
      </ui-glass-card>
      <ui-glass-card title="Historique">
        @for (entry of history(); track entry.id) {
          <div class="mb-3 border-b border-white/5 pb-3">
            <p class="text-xs text-mute">{{ entry.dayDate }} · mood {{ entry.mood }} · énergie {{ entry.energy }}</p>
            <p class="text-sm">{{ entry.wins }}</p>
          </div>
        }
      </ui-glass-card>
    </div>
  `
})
export class JournalPage implements OnInit {
  wins = '';
  struggles = '';
  gratitude = '';
  mood = 6;
  energy = 6;
  tags = '';
  history = signal<JournalEntry[]>([]);

  constructor(
    private readonly api: ApiService,
    private readonly router: Router
  ) {}

  ngOnInit(): void {
    this.api.journalHistory().subscribe({
      next: (rows) => this.history.set(rows),
      error: () => this.history.set([])
    });
  }

  submit(): void {
    this.api
      .submitJournal({
        dayDate: new Date().toISOString().slice(0, 10),
        wins: this.wins,
        struggles: this.struggles,
        gratitude: this.gratitude,
        mood: this.mood,
        energy: this.energy,
        tags: this.tags
      })
      .subscribe(() => void this.router.navigateByUrl('/streak'));
  }
}
