import { Component, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ApiService, DayBundle, SubTask, TaskItem } from '../../core/api';
import { GlassCardComponent } from '../../shared/ui/glass-card';
import { burst } from '../../shared/animations/gsap';

@Component({
  selector: 'app-planning',
  standalone: true,
  imports: [FormsModule, GlassCardComponent],
  template: `
    <div class="py-8">
      <h1 class="hero-title text-center">Agenda</h1>
      <p class="mt-2 text-center text-mute">Blocs horaires et todo imbriquée.</p>
      <div class="mt-6 flex justify-center gap-3">
        <input class="rounded-full border border-white/10 bg-black/40 px-4 py-2 text-sm" type="date" [(ngModel)]="date" (change)="reload()" />
        <button class="rounded-full border border-white/15 px-4 py-2 text-sm" (click)="close()">Clôturer la journée</button>
      </div>
    </div>

    <div class="grid gap-5 md:grid-cols-2">
      <ui-glass-card title="Blocs horaires">
        <form class="mb-4 flex flex-wrap gap-2" (ngSubmit)="addBlock()">
          <input class="rounded-full bg-white/5 px-3 py-2 text-sm" name="bt" [(ngModel)]="blockTitle" placeholder="Titre" />
          <input class="rounded-full bg-white/5 px-3 py-2 text-sm" type="time" name="bs" [(ngModel)]="blockStart" />
          <input class="rounded-full bg-white/5 px-3 py-2 text-sm" type="time" name="be" [(ngModel)]="blockEnd" />
          <button class="accent-grad rounded-full px-4 py-2 text-xs font-semibold" type="submit">Ajouter</button>
        </form>
        @for (block of bundle()?.blocks ?? []; track block.id) {
          <div
            class="mb-2 rounded-2xl border border-white/10 p-3"
            draggable="true"
            (dragstart)="draggingBlock = block.id">
            <p class="text-xs text-mute">{{ block.startTime }} – {{ block.endTime }}</p>
            <p class="font-medium">{{ block.title }}</p>
            @for (task of tasksFor(block.id); track task.id) {
              <label class="mt-2 flex items-center gap-2 text-sm">
                <input type="checkbox" [checked]="task.status === 'DONE'" (change)="complete($event, task)" />
                {{ task.title }}
                @if (task.postponedCount) {
                  <span class="text-xs text-ember">×{{ task.postponedCount }}</span>
                }
              </label>
            }
          </div>
        }
      </ui-glass-card>

      <ui-glass-card title="Inbox / sous-tâches">
        <form class="mb-4 flex gap-2" (ngSubmit)="addTask()">
          <input class="flex-1 rounded-full bg-white/5 px-3 py-2 text-sm" name="tt" [(ngModel)]="taskTitle" placeholder="Nouvelle tâche" />
          <button class="accent-grad rounded-full px-4 py-2 text-xs font-semibold" type="submit">Créer</button>
        </form>
        @for (task of unassigned(); track task.id) {
          <div class="mb-3 rounded-2xl border border-dashed border-white/10 p-3" (drop)="dropOnInbox($event)" (dragover)="$event.preventDefault()">
            <label class="flex items-center gap-2">
              <input type="checkbox" [checked]="task.status === 'DONE'" (change)="complete($event, task)" />
              <span>{{ task.title }}</span>
            </label>
            <form class="mt-2 flex gap-2" (ngSubmit)="addSub(task.id)">
              <input class="flex-1 rounded-full bg-white/5 px-3 py-1 text-xs" [ngModelOptions]="{standalone: true}" [(ngModel)]="subDraft[task.id]" placeholder="Sous-tâche" />
              <button class="text-xs text-ember" type="submit">+</button>
            </form>
            @for (sub of subs()[task.id]; track sub.id) {
              <label class="mt-1 flex items-center gap-2 text-xs text-mute">
                <input type="checkbox" [checked]="sub.done" (change)="toggleSub(sub)" />
                {{ sub.title }}
              </label>
            }
          </div>
        }
      </ui-glass-card>
    </div>
  `
})
export class PlanningPage implements OnInit {
  date = new Date().toISOString().slice(0, 10);
  bundle = signal<DayBundle | null>(null);
  subs = signal<Record<string, SubTask[]>>({});
  blockTitle = 'Focus';
  blockStart = '09:00';
  blockEnd = '11:00';
  taskTitle = '';
  subDraft: Record<string, string> = {};
  draggingBlock: string | null = null;

  constructor(private readonly api: ApiService) {}

  ngOnInit(): void {
    this.reload();
  }

  reload(): void {
    this.api.day(this.date).subscribe((bundle) => {
      this.bundle.set(bundle);
      bundle.tasks.forEach((task) =>
        this.api.subtasks(task.id).subscribe((list) =>
          this.subs.update((m) => ({ ...m, [task.id]: list }))
        )
      );
    });
  }

  tasksFor(blockId: string): TaskItem[] {
    return (this.bundle()?.tasks ?? []).filter((t) => t.timeBlockId === blockId);
  }

  unassigned(): TaskItem[] {
    return (this.bundle()?.tasks ?? []).filter((t) => !t.timeBlockId);
  }

  addBlock(): void {
    const id = this.bundle()?.day.id;
    if (!id) {
      return;
    }
    this.api.createBlock(id, this.blockTitle, this.blockStart, this.blockEnd).subscribe(() => this.reload());
  }

  addTask(): void {
    const id = this.bundle()?.day.id;
    if (!id || !this.taskTitle) {
      return;
    }
    this.api.createTask(id, this.taskTitle).subscribe(() => {
      this.taskTitle = '';
      this.reload();
    });
  }

  addSub(taskId: string): void {
    const title = this.subDraft[taskId];
    if (!title) {
      return;
    }
    this.api.addSubtask(taskId, title).subscribe(() => {
      this.subDraft[taskId] = '';
      this.reload();
    });
  }

  complete(event: Event, task: TaskItem): void {
    if (task.status === 'DONE') {
      return;
    }
    const target = event.target as HTMLInputElement;
    burst(event instanceof MouseEvent ? event.clientX : 0, event instanceof MouseEvent ? event.clientY : 80);
    if (target.checked) {
      this.api.completeTask(task.id).subscribe(() => this.reload());
    }
  }

  toggleSub(sub: SubTask): void {
    this.api.toggleSubtask(sub.id, !sub.done).subscribe(() => this.reload());
  }

  close(): void {
    const id = this.bundle()?.day.id;
    if (!id) {
      return;
    }
    this.api.closeDay(id).subscribe(() => this.reload());
  }

  dropOnInbox(event: DragEvent): void {
    event.preventDefault();
    this.draggingBlock = null;
  }
}
