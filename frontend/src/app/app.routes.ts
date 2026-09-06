import { Routes } from '@angular/router';
import { authGuard } from './core/auth.guard';

export const routes: Routes = [
  {
    path: 'login',
    loadComponent: () => import('./features/auth/login.page').then((m) => m.LoginPage)
  },
  {
    path: '',
    loadComponent: () => import('./layout/shell').then((m) => m.ShellComponent),
    canActivate: [authGuard],
    children: [
      {
        path: '',
        pathMatch: 'full',
        loadComponent: () => import('./features/dashboard/dashboard.page').then((m) => m.DashboardPage)
      },
      {
        path: 'planning',
        loadComponent: () => import('./features/planning/planning.page').then((m) => m.PlanningPage)
      },
      {
        path: 'journal',
        loadComponent: () => import('./features/journal/journal.page').then((m) => m.JournalPage)
      },
      {
        path: 'streak',
        loadComponent: () => import('./features/streak/streak.page').then((m) => m.StreakPage)
      },
      {
        path: 'goals',
        loadComponent: () => import('./features/goals/goals.page').then((m) => m.GoalsPage)
      }
    ]
  },
  { path: '**', redirectTo: '' }
];
