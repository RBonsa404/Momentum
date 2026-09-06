import { Component, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ApiService, GoalDto, HabitDto, MilestoneDto } from '../../core/api';
import { GlassCardComponent } from '../../shared/ui/glass-card';

@Component({
  selector: 'app-goals',
  standalone: true,
  imports: [FormsModule, GlassCardComponent],
  template: `
    <div class="py-8 text-center">
      <h1 class="hero-title">Objectifs SMART</h1>
    </div>
    <div class="grid gap-5 md:grid-cols-2">
      <ui-glass-card title="Objectifs">
        <form class="mb-4 flex gap-2" (ngSubmit)="addGoal()">
          <input class="flex-1 rounded-full bg-white/5 px-4 py-2 text-sm" [(ngModel)]="goalTitle" name="gt" placeholder="Nouvel objectif" />
          <button class="accent-grad rounded-full px-4 py-2 text-xs font-semibold" type="submit">Créer</button>
        </form>
        @for (goal of goals(); track goal.id) {
          <div class="mb-4">
            <div class="mb-1 flex justify-between text-sm">
              <span>{{ goal.title }}</span>
              <span class="text-ember">{{ goal.progressPercent }}%</span>
            </div>
            <div class="h-2 overflow-hidden rounded-full bg-white/10">
              <div class="h-full accent-grad" [style.width.%]="goal.progressPercent"></div>
            </div>
            <form class="mt-2 flex gap-2" (ngSubmit)="addMilestone(goal.id)">
              <input class="flex-1 rounded-full bg-white/5 px-3 py-1 text-xs" [ngModelOptions]="{standalone: true}" [(ngModel)]="msDraft[goal.id]" placeholder="Jalon" />
              <button class="text-xs text-ember" type="submit">+</button>
            </form>
            @for (ms of (milestones()[goal.id] ?? []); track ms.id) {
              <label class="mt-1 flex items-center gap-2 text-xs text-mute">
                <input type="checkbox" [checked]="ms.done" (change)="toggle(goal.id, ms)" />
                {{ ms.title }}
              </label>
            }
          </div>
        }
      </ui-glass-card>
      <ui-glass-card title="Habitudes">
        <form class="mb-4 flex gap-2" (ngSubmit)="addHabit()">
          <input class="flex-1 rounded-full bg-white/5 px-4 py-2 text-sm" [(ngModel)]="habitTitle" name="ht" placeholder="Habitude" />
          <button class="accent-grad rounded-full px-4 py-2 text-xs font-semibold" type="submit">Créer</button>
        </form>
        @for (habit of habits(); track habit.id) {
          <div class="mb-2 flex items-center justify-between rounded-2xl border border-white/10 px-4 py-3">
            <div>
              <p>{{ habit.title }}</p>
              <p class="text-xs text-mute">streak {{ habit.currentStreak }}</p>
            </div>
            <button class="rounded-full bg-ember px-3 py-1 text-xs" type="button" (click)="tick(habit.id)">+1</button>
          </div>
        }
      </ui-glass-card>
    </div>
  `
})
export class GoalsPage implements OnInit {
  goals = signal<GoalDto[]>([]);
  habits = signal<HabitDto[]>([]);
  milestones = signal<Record<string, MilestoneDto[]>>({});
  goalTitle = '';
  habitTitle = '';
  msDraft: Record<string, string> = {};

  constructor(private readonly api: ApiService) {}

  ngOnInit(): void {
    this.refresh();
  }

  refresh(): void {
    this.api.goals().subscribe({
      next: (goals) => {
        this.goals.set(goals);
        goals.forEach((g) =>
          this.api.milestones(g.id).subscribe((ms) => this.milestones.update((m) => ({ ...m, [g.id]: ms })))
        );
      },
      error: () => this.goals.set([])
    });
    this.api.habits().subscribe({ next: (h) => this.habits.set(h), error: () => this.habits.set([]) });
  }

  addGoal(): void {
    if (!this.goalTitle) {
      return;
    }
    this.api.createGoal(this.goalTitle).subscribe(() => {
      this.goalTitle = '';
      this.refresh();
    });
  }

  addMilestone(goalId: string): void {
    const title = this.msDraft[goalId];
    if (!title) {
      return;
    }
    this.api.addMilestone(goalId, title).subscribe(() => {
      this.msDraft[goalId] = '';
      this.refresh();
    });
  }

  toggle(goalId: string, ms: MilestoneDto): void {
    this.api.toggleMilestone(ms.id, !ms.done).subscribe(() => this.refresh());
  }

  addHabit(): void {
    if (!this.habitTitle) {
      return;
    }
    this.api.createHabit(this.habitTitle).subscribe(() => {
      this.habitTitle = '';
      this.refresh();
    });
  }

  tick(id: string): void {
    this.api.tickHabit(id).subscribe(() => this.refresh());
  }
}
