import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class ApiService {
  constructor(private readonly http: HttpClient) {}

  dashboard(period: string) {
    return this.http.get<DashboardDto>('/api/stats/dashboard', { params: { period } });
  }

  day(date: string) {
    return this.http.get<DayBundle>('/api/planning/days', { params: new HttpParams().set('date', date) });
  }

  createBlock(dayId: string, title: string, startTime: string, endTime: string) {
    return this.http.post<TimeBlock>('/api/planning/days/' + dayId + '/blocks', { title, startTime, endTime });
  }

  createTask(dayId: string, title: string, timeBlockId?: string) {
    return this.http.post<TaskItem>('/api/planning/days/' + dayId + '/tasks', { title, timeBlockId });
  }

  completeTask(taskId: string) {
    return this.http.post<TaskItem>('/api/planning/tasks/' + taskId + '/complete', {});
  }

  closeDay(dayId: string) {
    return this.http.post('/api/planning/days/' + dayId + '/close', {});
  }

  subtasks(taskId: string) {
    return this.http.get<SubTask[]>('/api/planning/tasks/' + taskId + '/subtasks');
  }

  addSubtask(taskId: string, title: string) {
    return this.http.post<SubTask>('/api/planning/tasks/' + taskId + '/subtasks', { title, sortOrder: 0 });
  }

  toggleSubtask(id: string, done: boolean) {
    return this.http.patch<SubTask>('/api/planning/subtasks/' + id, { done });
  }

  submitJournal(body: JournalPayload) {
    return this.http.post('/api/journal', body);
  }

  journalHistory() {
    return this.http.get<JournalEntry[]>('/api/journal');
  }

  streak() {
    return this.http.get<StreakDto>('/api/streak/current');
  }

  streakRules() {
    return this.http.get<StreakRules>('/api/streak/rules');
  }

  updateRules(offWeekdays: string, jokersPerMonth: number) {
    return this.http.put<StreakRules>('/api/streak/rules', { offWeekdays, jokersPerMonth });
  }

  goals() {
    return this.http.get<GoalDto[]>('/api/goals');
  }

  createGoal(title: string) {
    return this.http.post<GoalDto>('/api/goals', { title });
  }

  milestones(goalId: string) {
    return this.http.get<MilestoneDto[]>('/api/goals/' + goalId + '/milestones');
  }

  addMilestone(goalId: string, title: string) {
    return this.http.post<MilestoneDto>('/api/goals/' + goalId + '/milestones', { title });
  }

  toggleMilestone(id: string, done: boolean) {
    return this.http.patch<MilestoneDto>('/api/goals/milestones/' + id, { done });
  }

  habits() {
    return this.http.get<HabitDto[]>('/api/goals/habits');
  }

  createHabit(title: string) {
    return this.http.post<HabitDto>('/api/goals/habits', { title });
  }

  tickHabit(id: string) {
    return this.http.post<HabitDto>('/api/goals/habits/' + id + '/tick', {});
  }

  notifications() {
    return this.http.get<NotificationDto[]>('/api/notifications');
  }
}

export interface DashboardDto {
  from: string;
  to: string;
  planned: number;
  done: number;
  unfinished: number;
  postponed: number;
  missedJournals: number;
  procrastinationScore: number;
  completionRate: number;
  heatmap: HeatCell[];
}

export interface HeatCell {
  dayDate: string;
  planned: number;
  done: number;
  unfinished: number;
  postponed: number;
  missedJournals: number;
  mood: number;
  energy: number;
}

export interface DayBundle {
  day: { id: string; date: string; closed: boolean };
  blocks: TimeBlock[];
  tasks: TaskItem[];
}

export interface TimeBlock {
  id: string;
  title: string;
  startTime: string;
  endTime: string;
}

export interface TaskItem {
  id: string;
  title: string;
  status: string;
  timeBlockId?: string;
  postponedCount: number;
}

export interface SubTask {
  id: string;
  title: string;
  done: boolean;
}

export interface JournalPayload {
  dayDate: string;
  wins: string;
  struggles: string;
  gratitude: string;
  mood: number;
  energy: number;
  tags: string;
}

export interface JournalEntry extends JournalPayload {
  id: string;
}

export interface StreakDto {
  currentLength: number;
  longestLength: number;
  status: string;
  lastQualifiedDate?: string;
}

export interface StreakRules {
  offWeekdays: string;
  jokersPerMonth: number;
}

export interface GoalDto {
  id: string;
  title: string;
  progressPercent: number;
}

export interface MilestoneDto {
  id: string;
  title: string;
  done: boolean;
}

export interface HabitDto {
  id: string;
  title: string;
  currentStreak: number;
}

export interface NotificationDto {
  id: string;
  title: string;
  body: string;
}
