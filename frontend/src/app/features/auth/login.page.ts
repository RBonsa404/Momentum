import { Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { HaloBackgroundComponent } from '../../shared/ui/halo-background';
import { AuthStore } from '../../core/auth.store';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, HaloBackgroundComponent],
  template: `
    <ui-halo-background />
    <div class="flex min-h-screen items-center justify-center px-4">
      <form class="glass glow w-full max-w-md p-10 text-center" (ngSubmit)="submit()">
        <p class="mb-2 text-xs tracking-[0.35em] text-mute">MOMENTUM</p>
        <h1 class="hero-title mb-8">Entre dans ta journée.</h1>
        <input
          class="mb-3 w-full rounded-full border border-white/10 bg-white/5 px-5 py-3 text-sm outline-none"
          [(ngModel)]="email"
          name="email"
          type="email"
          placeholder="Email" />
        <input
          class="mb-6 w-full rounded-full border border-white/10 bg-white/5 px-5 py-3 text-sm outline-none"
          [(ngModel)]="password"
          name="password"
          type="password"
          placeholder="Mot de passe" />
        @if (error()) {
          <p class="mb-4 text-sm text-ember">{{ error() }}</p>
        }
        <button class="accent-grad glow w-full rounded-full py-3 text-sm font-semibold text-white" type="submit">
          Continuer
        </button>
      </form>
    </div>
  `
})
export class LoginPage {
  email = 'you@momentum.local';
  password = 'ChangeMeNow!';
  readonly error = signal('');

  constructor(
    private readonly auth: AuthStore,
    private readonly router: Router
  ) {}

  submit(): void {
    this.error.set('');
    this.auth.login(this.email, this.password).subscribe({
      next: () => void this.router.navigateByUrl('/'),
      error: () => this.error.set('Identifiants invalides ou compte verrouillé.')
    });
  }
}
